package dorm.ui.controller;

import dorm.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for the Owner Dashboard view.
 * Extends AdminController with staff management capabilities.
 */
public class OwnerController extends AdminController {
    
    // Staff Management
    @FXML private TableView<User> staffTable;
    @FXML private TableColumn<User, String> staffNameColumn;
    @FXML private TableColumn<User, String> staffUsernameColumn;
    @FXML private TableColumn<User, String> staffRoleColumn;
    @FXML private TextField staffNameField;
    @FXML private TextField staffUsernameField;
    @FXML private PasswordField staffPasswordField;
    
    @Override
    public void initialize() {
        if (service == null || user == null) return;
        
        welcomeLabel.setText("Owner: " + user.getDisplayName());
        
        setupFilters();
        setupApplicationTable();
        setupStaffTable();
        setupAnnouncements();
        setupMessages();
        setupSearch();
        refresh();
    }
    
    private void setupStaffTable() {
        staffNameColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getDisplayName()));
        
        staffUsernameColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getUsername()));
        
        staffRoleColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getRole().name()));
    }
    
    @Override
    protected void refresh() {
        super.refresh();
        refreshStaffTable();
    }
    
    private void refreshStaffTable() {
        if (staffTable != null) {
            staffTable.setItems(FXCollections.observableArrayList(
                service.getUsers().stream()
                    .filter(u -> u.getRole() == Role.ADMIN || u.getRole() == Role.OWNER)
                    .collect(Collectors.toList())
            ));
        }
    }
    
    @FXML
    private void onAddStaff() {
        String name = staffNameField.getText().trim();
        String username = staffUsernameField.getText().trim();
        String password = staffPasswordField.getText();
        
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("All fields are required", Alert.AlertType.WARNING);
            return;
        }
        
        // Check if username is available
        if (!service.isUsernameAvailable(username)) {
            showAlert("Username is already taken", Alert.AlertType.WARNING);
            return;
        }
        
        User newAdmin = new User(
            UUID.randomUUID().toString(),
            username,
            password,
            Role.ADMIN,
            name
        );
        
        service.addUser(newAdmin);
        
        staffNameField.clear();
        staffUsernameField.clear();
        staffPasswordField.clear();
        
        refreshStaffTable();
        showAlert("Admin added successfully", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void onRemoveStaff() {
        User selected = staffTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("Select a user first", Alert.AlertType.WARNING);
            return;
        }
        
        if (selected.getRole() == Role.OWNER) {
            showAlert("Cannot remove owner", Alert.AlertType.WARNING);
            return;
        }
        
        if (selected.getId().equals(user.getId())) {
            showAlert("Cannot remove yourself", Alert.AlertType.WARNING);
            return;
        }
        
        service.removeUser(selected);
        refreshStaffTable();
        showAlert("User removed", Alert.AlertType.INFORMATION);
    }
}
