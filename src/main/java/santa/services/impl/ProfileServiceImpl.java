package santa.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import santa.dtos.ChangePasswordRequest;
import santa.dtos.ProfileResponse;
import santa.entities.User;
import santa.exceptions.UserNotFoundException;
import santa.exceptions.WrongPasswordException;
import santa.repositories.UserRepository;
import santa.services.ProfileService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return ProfileResponse.fromEntity(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Long sourceUserId) {
        User user = userRepository.findById(sourceUserId)
                .orElseThrow(() -> new UserNotFoundException(sourceUserId));
        if (!Objects.equals(user.getPasswordHash(), passwordEncoder.encode(request.oldPassword()))) {
            throw new WrongPasswordException(sourceUserId);
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
