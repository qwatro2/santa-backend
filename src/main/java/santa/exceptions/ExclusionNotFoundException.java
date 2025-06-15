package santa.exceptions;

import java.util.UUID;

public class ExclusionNotFoundException extends RuntimeException {
    public ExclusionNotFoundException(UUID exclusionId) {
        super(String.format("Exclusion with ID %s not found", exclusionId));
    }
}
