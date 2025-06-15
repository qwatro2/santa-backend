package santa.dtos;

import santa.entities.User;

public record UserDto(Long id, String name, AvatarLink avatarLink) {
    public record AvatarLink(String method, String link) {
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getDisplayName(),
                new AvatarLink("GET", String.format("/api/avatars?userId=%d", user.getId()))
        );
    }
}
