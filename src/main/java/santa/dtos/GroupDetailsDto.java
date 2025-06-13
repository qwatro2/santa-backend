package santa.dtos;

import santa.entities.Participant;
import santa.entities.SantaGroup;
import santa.entities.User;

import java.util.List;

public record GroupDetailsDto(
        GroupDto groupInfo,
        UserDetails organizerInfo,
        ListParticipantUsers participantsInfo
) {
    public record UserDetails(Long id, String name, AvatarLink avatarLink) {
        public record AvatarLink(String method, String link) {
        }

        public static UserDetails fromEntity(User user) {
            return new UserDetails(
                    user.getId(),
                    user.getDisplayName(),
                    new AvatarLink("GET", String.format("/api/avatars?userId=%d", user.getId()))
            );
        }
    }

    public record ListParticipantUsers(List<UserDetails> userDetails, int size) {
    }

    public static GroupDetailsDto fromEntity(SantaGroup group) {
        GroupDto groupInfo = GroupDto.fromEntity(group);
        UserDetails organizerInfo = UserDetails.fromEntity(group.getOrganizer());
        List<UserDetails> userDetails = group.getParticipants().stream()
                .map(Participant::getUser)
                .map(UserDetails::fromEntity)
                .toList();
        ListParticipantUsers participantsInfo = new ListParticipantUsers(userDetails, userDetails.size());
        return new GroupDetailsDto(groupInfo, organizerInfo, participantsInfo);
    }
}
