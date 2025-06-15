package santa.services;

import santa.dtos.ChangePasswordRequest;
import santa.dtos.ProfileResponse;

public interface ProfileService {
    ProfileResponse getProfile(Long userId);

    void changePassword(ChangePasswordRequest request, Long sourceUserId);
}
