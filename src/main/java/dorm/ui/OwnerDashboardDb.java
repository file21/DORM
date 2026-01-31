package dorm.ui;

import dorm.model.User;
import dorm.service.DatabaseDormService;
import dorm.ui.components.BaseDashboard;
import dorm.ui.components.StaffTab;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Owner Dashboard - extends BaseDashboard functionality with Staff management.
 * Owner has all admin capabilities plus: Staff tab for managing admin accounts.
 * 
 * Demonstrates:
 * - Composition over inheritance (uses BaseDashboard + adds StaffTab)
 * - OCP - extends functionality without modifying base
 * - SRP - dashboard is now focused and small
 * - DRY - shared code in BaseDashboard/components
 */
public class OwnerDashboardDb {
    
    private final BaseDashboard baseDashboard;
    private final StaffTab staffTab;
    
    /**
     * Create owner dashboard
     * @param service Database service
     * @param owner Current owner user
     * @param stage JavaFX stage
     */
    public OwnerDashboardDb(DatabaseDormService service, User owner, Stage stage) {
        // Create base dashboard with core tabs
        this.baseDashboard = new BaseDashboard(service, owner, stage, "Owner");
        
        // Add owner-specific staff tab
        this.staffTab = new StaffTab(service);
        staffTab.setAlertCallback(this::showAlert);
        staffTab.setOnChangeCallback(this::refresh);
        
        // Insert staff tab after applications (position 1)
        baseDashboard.getTabPane().getTabs().add(1, staffTab.getTab());
        
        // Initial refresh of staff tab
        staffTab.refresh();
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
        staffTab.refresh();
    }
    
    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
