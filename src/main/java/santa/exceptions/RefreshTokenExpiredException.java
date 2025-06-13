package santa.exceptions;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() {
        super("Refresh token was expired. Please make a new login request");
    }
}
