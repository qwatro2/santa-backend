package santa.dtos;

import java.time.Instant;

public record CreateGroupRequest(String name, Instant exchangeDate, Integer costMin, Integer costMax) {
}
