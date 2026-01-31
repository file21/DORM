package dorm.ui.components;

import dorm.service.DatabaseDormService;
import javafx.scene.control.Tab;

import java.util.function.Consumer;

/**
 * Messages management tab - shared between Admin and Owner dashboards.
 * Wraps MessagePane in a Tab for easy dashboard integration.
 * Demonstrates SRP and reduces dashboard size.
 */
public class MessagesTab {
    
    private final Tab tab;
    private final MessagePane messagePane;
    
    /**
     * Create messages tab
     * @param service Database service
     * @param currentUsername Username of current admin/owner
     */
    public MessagesTab(DatabaseDormService service, String currentUsername) {
        this.tab = new Tab("Messages");
        this.messagePane = new MessagePane(service, currentUsername);
        
        tab.setClosable(false);
        tab.setContent(messagePane);
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
        messagePane.setAlertCallback(callback);
    }
    
    /**
     * Refresh the tab data
     */
    public void refresh() {
        messagePane.refresh();
    }
}
