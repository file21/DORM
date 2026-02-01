package dorm.dao;

/**
 * Custom exception for data access operations.
 * Provides user-friendly error handling instead of crashing with RuntimeException.
 * Demonstrates proper exception handling (OOP best practice).
 */
public class DataAccessException extends RuntimeException {
    
    private final String operation;
    private final String resource;
    
    /**
     * Create a new DataAccessException
     * @param message User-friendly error message
     * @param cause The underlying exception
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        this.operation = "unknown";
        this.resource = "unknown";
    }
    
    /**
     * Create a detailed DataAccessException
     * @param operation The operation that failed (e.g., "read", "write", "delete")
     * @param resource The resource involved (e.g., "students.csv")
     * @param cause The underlying exception
     */
    public DataAccessException(String operation, String resource, Throwable cause) {
        super(buildMessage(operation, resource, cause), cause);
        this.operation = operation;
        this.resource = resource;
    }
    
    private static String buildMessage(String operation, String resource, Throwable cause) {
        return String.format("Failed to %s %s: %s", operation, resource, 
                cause != null ? cause.getMessage() : "unknown error");
    }
    
    public String getOperation() {
        return operation;
    }
    
    public String getResource() {
        return resource;
    }
    
    /**
     * Get a user-friendly message suitable for display in UI
     */
    public String getUserFriendlyMessage() {
        return String.format("Unable to %s data. Please try again or contact support.", operation);
    }
}
