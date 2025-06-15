package santa.dtos;

import java.util.List;

public record ListExclusionDto(int size, List<ExclusionDto> list) {
}
