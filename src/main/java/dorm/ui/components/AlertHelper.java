package dorm.ui.components;

import dorm.dao.DataAccessException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class for showing consistent alerts across the application.
 * Provides specialized methods for different error types.
 * Ensures I/O failures are user-visible (not just logged).
 */
public final class AlertHelper {
    
    private AlertHelper() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Show an information alert
     */
    public static void showInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show a success alert
     */
    public static void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show a warning alert
     */
    public static void showWarning(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show an error alert
     */
    public static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show a data access error alert with user-friendly message
     */
    public static void showDataError(DataAccessException e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Data Error");
        alert.setHeaderText("Unable to access data");
        alert.setContentText(e.getUserFriendlyMessage() + 
            "\n\nDetails: " + e.getMessage());
        alert.showAndWait();
    }
    
    /**
     * Show a data access error with custom context
     */
    public static void showDataError(String context, Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Data Error");
        alert.setHeaderText("Error: " + context);
        
        String message;
        if (e instanceof DataAccessException) {
            message = ((DataAccessException) e).getUserFriendlyMessage();
        } else {
            message = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
        }
        
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show validation error
     */
    public static void showValidation(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please correct the following:");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
