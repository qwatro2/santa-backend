package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import santa.SantaConfig;
import santa.dtos.AvatarDto;
import santa.entities.Avatar;
import santa.entities.User;
import santa.exceptions.AvatarNotFoundException;
import santa.exceptions.AvatarReadingFailedException;
import santa.exceptions.AvatarSavingFailedException;
import santa.exceptions.UserNotFoundException;
import santa.repositories.AvatarRepository;
import santa.repositories.UserRepository;
import santa.services.AvatarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {
    private final SantaConfig santaConfig;
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    @Override
    public void uploadAvatar(MultipartFile file, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Optional<Avatar> existing = avatarRepository.findByUser_Id(userId);
        existing.ifPresent(old -> {
            try {
                Files.deleteIfExists(Path.of(avatarPath(old.getId(), old.getExtension())));
            } catch (IOException ignored) {
            }
            avatarRepository.delete(old);
        });

        UUID avatarId = UUID.randomUUID();
        String extension = Optional.ofNullable(file.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.') + 1))
                .orElse("bin");

        Path targetLocation = Path.of(avatarPath(avatarId, extension));
        try {
            Files.createDirectories(targetLocation.getParent());
            file.transferTo(targetLocation);
        } catch (IOException ex) {
            throw new AvatarSavingFailedException(userId);
        }

        Avatar avatar = new Avatar();
        avatar.setId(avatarId);
        avatar.setExtension(extension);
        avatar.setUser(user);
        avatarRepository.save(avatar);

        user.setAvatar(avatar);
        userRepository.save(user);
    }

    @Override
    public AvatarDto getAvatarByUserId(Long userId) {
        Avatar avatar = avatarRepository.findByUser_Id(userId)
                .orElseThrow(() -> new AvatarNotFoundException(userId));
        Path path = Paths.get(avatarPath(avatar.getId(), avatar.getExtension()));

        byte[] data;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new AvatarReadingFailedException(userId);
        }
        return new AvatarDto(new ByteArrayResource(data), avatar.getExtension());
    }

    private String avatarPath(UUID id, String extension) {
        return String.format("%s/%s.%s", santaConfig.avatars().storagePath(), id, extension);
    }
}
