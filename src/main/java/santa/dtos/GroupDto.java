package santa.dtos;

import santa.entities.SantaGroup;

import java.time.Instant;
import java.util.UUID;

public record GroupDto(UUID groupId, String name, Instant exchangeDate) {
    public static GroupDto fromEntity(SantaGroup group) {
        return new GroupDto(group.getId(), group.getName(), group.getExchangeDate());
    }
}
