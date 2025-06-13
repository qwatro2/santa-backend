package santa.exceptions;

public class AssignmentsGenerationFailedException extends RuntimeException {
    public AssignmentsGenerationFailedException(int attempts) {
        super(String.format("Failed to generate valid assignment after %s attempts", attempts));
    }
}
