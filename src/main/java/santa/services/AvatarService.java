package santa.services;

import org.springframework.web.multipart.MultipartFile;
import santa.dtos.AvatarDto;

public interface AvatarService {
    void uploadAvatar(MultipartFile file, Long userId);

    AvatarDto getAvatarByUserId(Long userId);
}
