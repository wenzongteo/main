package guitests.guihandles;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

//@@author awarenessxz
/**
 * A handler for the {@code ResultDisplay} of the UI
 */
public class LeftDisplayPanelHandle extends NodeHandle<TabPane> {
    public static final String LEFT_DISPLAY_ID = "#leftDisplayPanel";

    public LeftDisplayPanelHandle(TabPane leftDisplayPanelNode) {
        super(leftDisplayPanelNode);
    }

    /**
     * Toggle the Tabs
     */
    public void toggle(int index) {
        click();
        guiRobot.interact(() -> getRootNode().getSelectionModel().select(index));
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the text in the result display.
     */
    public int getSelectedTabIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    public ObservableList<Tab> getTabs() {
        return getRootNode().getTabs();
    }
}
