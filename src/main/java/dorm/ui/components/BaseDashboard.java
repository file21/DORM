package dorm.ui.components;

import dorm.model.User;
import dorm.service.DatabaseDormService;
import dorm.ui.LoginViewDb;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Base dashboard with shared functionality for Admin and Owner.
 * Uses composition to share common tabs (Applications, Announcements, Messages, Search).
 * Demonstrates OCP - can be extended for different roles without modification.
 * Reduces duplication between Admin and Owner dashboards.
 */
public class BaseDashboard {
    
    protected final DatabaseDormService service;
    protected final User user;
    protected final Stage stage;
    protected final BorderPane root;
    protected final TabPane tabs;
    
    // Shared tabs
    protected final ApplicationsTab applicationsTab;
    protected final AnnouncementsTab announcementsTab;
    protected final MessagesTab messagesTab;
    protected final SearchTab searchTab;
    
    /**
     * Create base dashboard
     * @param service Database service
     * @param user Current user (admin or owner)
     * @param stage JavaFX stage
     * @param roleLabel Label to show in header (e.g., "Admin" or "Owner")
     */
    public BaseDashboard(DatabaseDormService service, User user, Stage stage, String roleLabel) {
        this.service = service;
        this.user = user;
        this.stage = stage;
        this.root = new BorderPane();
        this.tabs = new TabPane();
        
        // Initialize shared tabs
        this.applicationsTab = new ApplicationsTab(service, user.getUsername());
        this.announcementsTab = new AnnouncementsTab(service, user.getDisplayName(), true);
        this.messagesTab = new MessagesTab(service, user.getUsername());
        this.searchTab = new SearchTab(service);
        
        // Configure callbacks
        configureCallbacks();
        
        // Build UI
        buildHeader(roleLabel);
        buildTabs();
        
        // Initial refresh
        refresh();
    }
    
    private void configureCallbacks() {
        // Alert callbacks
        applicationsTab.setAlertCallback(this::showAlert);
        announcementsTab.setAlertCallback(this::showAlert);
        messagesTab.setAlertCallback(this::showAlert);
        searchTab.setAlertCallback(this::showAlert);
        
        // Window supplier for file dialogs
        applicationsTab.setWindowSupplier(() -> root.getScene().getWindow());
        
        // Refresh callback for search save
        searchTab.setOnSaveCallback(this::refresh);
    }
    
    private void buildHeader(String roleLabel) {
        Label headerLabel = new Label(roleLabel + ": " + user.getDisplayName());
        headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refresh());
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> logout());
        
        HBox header = new HBox(20, headerLabel, refreshButton, logoutButton);
        header.setPadding(new Insets(10));
        root.setTop(header);
    }
    
    /**
     * Build tabs - can be overridden to add role-specific tabs
     */
    protected void buildTabs() {
        tabs.getTabs().add(applicationsTab.getTab());
        tabs.getTabs().add(announcementsTab.getTab());
        tabs.getTabs().add(messagesTab.getTab());
        tabs.getTabs().add(searchTab.getTab());
        
        root.setCenter(tabs);
    }
    
    /**
     * Get the root node
     */
    public Parent getRoot() {
        return root;
    }
    
    /**
     * Get the tab pane (for adding additional tabs in subclasses)
     */
    protected TabPane getTabPane() {
        return tabs;
    }
    
    /**
     * Refresh all tabs
     */
    public void refresh() {
        applicationsTab.refresh();
        announcementsTab.refresh();
        messagesTab.refresh();
    }
    
    /**
     * Logout and return to login screen
     */
    protected void logout() {
        LoginViewDb loginView = new LoginViewDb(service, stage);
        Scene scene = new Scene(loginView.getRoot(), 1200, 700);
        stage.setScene(scene);
    }
    
    /**
     * Show an alert dialog
     */
    protected void showAlert(String message) {
        AlertHelper.showInfo(message);
    }
    
    /**
     * Show an error alert
     */
    protected void showError(String message) {
        AlertHelper.showError(message);
    }
}
