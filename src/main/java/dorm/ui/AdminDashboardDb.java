package dorm.ui;

import dorm.model.User;
import dorm.service.DatabaseDormService;
import dorm.ui.components.BaseDashboard;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Admin Dashboard - uses BaseDashboard for core functionality.
 * Admin has: Applications, Announcements, Messages, Search tabs.
 * 
 * Demonstrates:
 * - Composition over inheritance (uses BaseDashboard)
 * - SRP - dashboard is now focused and small
 * - DRY - shared code in BaseDashboard/components
 */
public class AdminDashboardDb {
    
    private final BaseDashboard baseDashboard;
    
    /**
     * Create admin dashboard
     * @param service Database service
     * @param admin Current admin user
     * @param stage JavaFX stage
     */
    public AdminDashboardDb(DatabaseDormService service, User admin, Stage stage) {
        this.baseDashboard = new BaseDashboard(service, admin, stage, "Admin");
    }
    
    /**
     * Get the root node for the scene
     */
    public Parent getRoot() {
        return baseDashboard.getRoot();
    }
    
    /**
     * Refresh dashboard data
     */
    public void refresh() {
        baseDashboard.refresh();
    }
}
