package santa.dtos;

import java.util.List;

public record ListGroupsDto(List<GroupDto> groups, int size) {
}
