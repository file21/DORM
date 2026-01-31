package dorm.ui.components;

import dorm.service.DatabaseDormService;
import javafx.scene.control.Tab;

import java.util.function.Consumer;

/**
 * Student search tab - shared between Admin and Owner dashboards.
 * Wraps StudentSearchPane in a Tab for easy dashboard integration.
 * Demonstrates SRP and reduces dashboard size.
 */
public class SearchTab {
    
    private final Tab tab;
    private final StudentSearchPane searchPane;
    
    /**
     * Create search tab
     * @param service Database service
     */
    public SearchTab(DatabaseDormService service) {
        this.tab = new Tab("Search");
        this.searchPane = new StudentSearchPane(service);
        
        tab.setClosable(false);
        tab.setContent(searchPane);
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
        searchPane.setAlertCallback(callback);
    }
    
    /**
     * Set callback for when save is completed
     */
    public void setOnSaveCallback(Runnable callback) {
        searchPane.setOnSaveCallback(callback);
    }
}
