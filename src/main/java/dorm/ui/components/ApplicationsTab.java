package dorm.ui.components;

import dorm.model.DormApplication;
import dorm.service.DatabaseDormService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Applications management tab - shared between Admin and Owner dashboards.
 * Demonstrates SRP - handles only applications tab functionality.
 * Reduces duplication and dashboard size.
 */
public class ApplicationsTab {
    
    private final DatabaseDormService service;
    private final String currentUsername;
    private final Tab tab;
    private final TableView<DormApplication> applicationTable;
    private final Map<String, SimpleBooleanProperty> selectionMap;
    private final ApplicationFilterPane filterPane;
    private final ApplicationActionsPane actionsPane;
    
    private Consumer<String> alertCallback;
    private Supplier<Window> windowSupplier;
    
    /**
     * Create applications tab
     * @param service Database service
     * @param currentUsername Username of current admin/owner
     */
    public ApplicationsTab(DatabaseDormService service, String currentUsername) {
        this.service = service;
        this.currentUsername = currentUsername;
        this.tab = new Tab("Applications");
        this.applicationTable = new TableView<>();
        this.selectionMap = new HashMap<>();
        this.filterPane = new ApplicationFilterPane();
        this.actionsPane = new ApplicationActionsPane(service, currentUsername, applicationTable, selectionMap);
        
        buildUI();
    }
    
    private void buildUI() {
        tab.setClosable(false);
        
        // Build table columns
        ApplicationTableBuilder tableBuilder = new ApplicationTableBuilder(applicationTable, selectionMap);
        tableBuilder.buildAllColumns();
        
        // Configure filter pane
        filterPane.setOnFilterApplied(v -> applyFilters());
        
        // Configure actions pane
        actionsPane.setOnActionCompleted(this::refresh);
        
        // Layout
        VBox wrapper = new VBox(10, filterPane, applicationTable, actionsPane);
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
        actionsPane.setAlertCallback(callback);
    }
    
    /**
     * Set supplier for getting parent window
     */
    public void setWindowSupplier(Supplier<Window> supplier) {
        this.windowSupplier = supplier;
        actionsPane.setWindowSupplier(supplier);
    }
    
    /**
     * Apply filters and refresh table
     */
    public void applyFilters() {
        try {
            java.util.List<DormApplication> all = service.getApplications();
            java.util.List<DormApplication> filtered = filterPane.applyFilters(all);
            applicationTable.setItems(FXCollections.observableArrayList(filtered));
            
            // Show info if no applications found
            if (filtered.isEmpty() && all.isEmpty()) {
                // Data might have failed to load - show subtle warning
                // (CsvHelper logs the error, UI shows empty state)
            }
        } catch (Exception e) {
            // Use AlertHelper for consistent error display
            AlertHelper.showDataError("Loading applications", e);
        }
    }
    
    /**
     * Refresh the tab data
     */
    public void refresh() {
        applyFilters();
    }
    
    private void showAlert(String message) {
        if (alertCallback != null) {
            alertCallback.accept(message);
        }
    }
}
