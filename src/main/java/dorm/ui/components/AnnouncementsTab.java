package dorm.ui.components;

import dorm.service.DatabaseDormService;
import javafx.scene.control.Tab;

import java.util.function.Consumer;

/**
 * Announcements management tab - shared between Admin and Owner dashboards.
 * Wraps AnnouncementPane in a Tab for easy dashboard integration.
 * Demonstrates SRP and reduces dashboard size.
 */
public class AnnouncementsTab {
    
    private final Tab tab;
    private final AnnouncementPane announcementPane;
    
    /**
     * Create announcements tab
     * @param service Database service
     * @param createdBy Name to use as author when creating announcements
     * @param editable Whether this tab allows creating/editing
     */
    public AnnouncementsTab(DatabaseDormService service, String createdBy, boolean editable) {
        this.tab = new Tab("Announcements");
        this.announcementPane = new AnnouncementPane(service, createdBy, editable);
        
        tab.setClosable(false);
        tab.setContent(announcementPane);
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
        announcementPane.setAlertCallback(callback);
    }
    
    /**
     * Refresh the tab data
     */
    public void refresh() {
        announcementPane.refresh();
    }
}
