package santa.dtos;

public record LoginResponse(String jwtToken, String refreshToken) {
}
