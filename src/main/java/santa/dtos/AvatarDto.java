package santa.dtos;

import org.springframework.core.io.Resource;

public record AvatarDto(Resource resource, String extension) {
}
