package dorm.ui.components;

import dorm.model.Role;
import dorm.model.User;
import dorm.service.DatabaseDormService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Staff management tab - Owner only.
 * Handles adding/removing admin users.
 * Demonstrates SRP and reduces dashboard size.
 */
public class StaffTab {
    
    private final DatabaseDormService service;
    private final Tab tab;
    private final TableView<User> staffTable;
    
    private Consumer<String> alertCallback;
    private Runnable onChangeCallback;
    
    /**
     * Create staff management tab
     * @param service Database service
     */
    public StaffTab(DatabaseDormService service) {
        this.service = service;
        this.tab = new Tab("Staff");
        this.staffTable = new TableView<>();
        
        buildUI();
    }
    
    private void buildUI() {
        tab.setClosable(false);
        
        // Table columns
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDisplayName()));
        nameCol.setPrefWidth(150);
        
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        usernameCol.setPrefWidth(120);
        
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRole().name()));
        roleCol.setPrefWidth(100);
        
        staffTable.getColumns().addAll(nameCol, usernameCol, roleCol);
        staffTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Form for adding new admin
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button addButton = new Button("Add Admin");
        Button removeButton = new Button("Remove");
        
        addButton.setOnAction(event -> handleAdd(nameField, usernameField, passwordField));
        removeButton.setOnAction(event -> handleRemove());
        
        HBox form = new HBox(10, nameField, usernameField, passwordField, addButton, removeButton);
        form.setPadding(new Insets(10));
        
        VBox wrapper = new VBox(10, staffTable, form);
        wrapper.setPadding(new Insets(10));
        tab.setContent(wrapper);
    }
    
    /**
     * Get the tab node
     */
    public Tab getTab() {
        return tab;
    }
    
    /**
     * Set callback for showing alerts
     */
    public void setAlertCallback(Consumer<String> callback) {
        this.alertCallback = callback;
    }
    
    /**
     * Set callback for when staff changes (for refresh)
     */
    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
    }
    
    /**
     * Refresh the staff table
     */
    public void refresh() {
        try {
            staffTable.setItems(FXCollections.observableArrayList(
                service.getUsers().stream()
                    .filter(u -> u.getRole() == Role.ADMIN || u.getRole() == Role.OWNER)
                    .collect(Collectors.toList())
            ));
        } catch (Exception e) {
            showAlert("Failed to load staff: " + e.getMessage());
        }
    }
    
    private void handleAdd(TextField nameField, TextField usernameField, PasswordField passwordField) {
        if (nameField.getText().isBlank() || usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
            showAlert("All fields required");
            return;
        }
        
        try {
            User newAdmin = new User(
                UUID.randomUUID().toString(),
                usernameField.getText().trim(),
                passwordField.getText().trim(),
                Role.ADMIN,
                nameField.getText().trim()
            );
            service.addUser(newAdmin);
            
            nameField.clear();
            usernameField.clear();
            passwordField.clear();
            
            notifyChange();
            showAlert("Admin added successfully");
        } catch (Exception e) {
            showAlert("Failed to add admin: " + e.getMessage());
        }
    }
    
    private void handleRemove() {
        User selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a user first");
            return;
        }
        if (selected.getRole() == Role.OWNER) {
            showAlert("Cannot remove owner");
            return;
        }
        
        try {
            service.removeUser(selected);
            notifyChange();
            showAlert("User removed");
        } catch (Exception e) {
            showAlert("Failed to remove user: " + e.getMessage());
        }
    }
    
    private void showAlert(String message) {
        if (alertCallback != null) {
            alertCallback.accept(message);
        }
    }
    
    private void notifyChange() {
        refresh();
        if (onChangeCallback != null) {
            onChangeCallback.run();
        }
    }
}
