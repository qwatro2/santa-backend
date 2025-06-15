package santa.dtos;

import santa.entities.Exclusion;

import java.util.UUID;

public record ExclusionDto(UUID exclusionId, UserDto giver, UserDto receiver, GroupDto group) {
    public static ExclusionDto fromEntity(Exclusion exclusion) {
        return new ExclusionDto(
                exclusion.getId(),
                UserDto.fromEntity(exclusion.getGiver().getUser()),
                UserDto.fromEntity(exclusion.getReceiver().getUser()),
                GroupDto.fromEntity(exclusion.getGroup())
        );
    }
}
