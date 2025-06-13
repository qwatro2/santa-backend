package santa.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String token) {
        super(String.format("Refresh token %s not found", token));
    }
}
