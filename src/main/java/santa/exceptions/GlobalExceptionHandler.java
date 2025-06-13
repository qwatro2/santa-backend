package santa.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import santa.SantaConfig;
import santa.entities.ErrorResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final SantaConfig santaConfig;

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAssignmentNotFound(AssignmentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("ASSIGNMENT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(AssignmentsGenerationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAssignmentGenerationFailed(AssignmentsGenerationFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("ASSIGNMENT_GENERATION_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(AvatarNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAvatarNotFound(AvatarNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("AVATAR_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(AvatarReadingFailedException.class)
    public ResponseEntity<ErrorResponse> handleAvatarReadingFailed(AvatarReadingFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("AVATAR_READING_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(AvatarSavingFailedException.class)
    public ResponseEntity<ErrorResponse> handleAvatarSavingFailed(AvatarSavingFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("AVATAR_SAVING_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(DeleteGroupAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleDeleteGroupAccessDenied(DeleteGroupAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("DELETE_GROUP_ACCESS_DENIED", ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyUsed(EmailAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("EMAIL_ALREADY_USED", ex.getMessage()));
    }

    @ExceptionHandler(ExclusionAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleExclusionAlreadyExists(ExclusionAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("EXCLUSION_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(GetGroupDetailsAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleGetGroupDetailsAccessDenied(GetGroupDetailsAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("GET_GROUP_DETAILS_ACCESS_DENIED", ex.getMessage()));
    }

    @ExceptionHandler(GetJoinLinkAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleGetJoinLinkAccessDenied(GetJoinLinkAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("GET_JOIN_LINK_ACCESS_DENIED", ex.getMessage()));
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGroupNotFound(GroupNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("GROUP_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(IncorrectExclusionException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectExclusion(IncorrectExclusionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("INCORRECT_EXCLUSION", ex.getMessage()));
    }

    @ExceptionHandler(MarkDoneAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleMarkDoneAccessDenied(MarkDoneAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("MARK_DONE_ACCESS_DENIED", ex.getMessage()));
    }

    @ExceptionHandler(ParticipantAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleParticipantAlreadyExists(ParticipantAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("PARTICIPANT_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleParticipantNotFound(ParticipantNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("PARTICIPANT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenExpired(RefreshTokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("REFRESH_TOKEN_EXPIRED", ex.getMessage()));
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenNotFound(RefreshTokenNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("REFRESH_TOKEN_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(RemoveParticipantAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleRemoveParticipantAccessDenied(RemoveParticipantAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("REMOVE_PARTICIPANT_ACCESS_DENIED", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("USER_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", message));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("EXPIRED_JWT", "Jwt expired"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) throws Exception {
        if (!santaConfig.handleAllExceptions()) {
            throw ex;
        }
        log.atError().log(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
    }
}
