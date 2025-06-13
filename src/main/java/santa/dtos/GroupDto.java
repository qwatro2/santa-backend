package santa.dtos;

import santa.entities.SantaGroup;

import java.time.Instant;
import java.util.UUID;

public record GroupDto(UUID groupId, String name, Instant exchangeDate, Integer minCost, Integer maxCost) {
    public static GroupDto fromEntity(SantaGroup group) {
        return new GroupDto(
                group.getId(),
                group.getName(),
                group.getExchangeDate(),
                group.getCostMin(),
                group.getCostMax()
        );
    }
}
