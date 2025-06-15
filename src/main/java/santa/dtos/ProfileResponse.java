package santa.dtos;

import santa.entities.User;

public record ProfileResponse(
        Long id,
        String email,
        String name
) {
    public static ProfileResponse fromEntity(User user) {
        return new ProfileResponse(user.getId(), user.getEmail(), user.getDisplayName());
    }
}
