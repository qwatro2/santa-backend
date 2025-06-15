package santa.dtos;

import santa.entities.Participant;
import santa.entities.SantaGroup;

import java.util.List;

public record GroupDetailsDto(
        GroupDto groupInfo,
        UserDto organizerInfo,
        ListParticipantUsers participantsInfo
) {
    public record ListParticipantUsers(List<UserDto> userDetails, int size) {
    }

    public static GroupDetailsDto fromEntity(SantaGroup group) {
        GroupDto groupInfo = GroupDto.fromEntity(group);
        UserDto organizerInfo = UserDto.fromEntity(group.getOrganizer());
        List<UserDto> userDetails = group.getParticipants().stream()
                .map(Participant::getUser)
                .map(UserDto::fromEntity)
                .toList();
        ListParticipantUsers participantsInfo = new ListParticipantUsers(userDetails, userDetails.size());
        return new GroupDetailsDto(groupInfo, organizerInfo, participantsInfo);
    }
}
