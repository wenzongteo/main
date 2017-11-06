package seedu.address.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.LeftDisplayPanelHandle;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

//@@author awarenessxz
/**
 * Test the Left Display Panel Toggle Tabs for the UI
 */
public class LeftDisplayPanelTest extends GuiUnitTest {

    private LeftDisplayPanelHandle leftDisplayPanelHandle;
    private LeftDisplayPanel leftDisplayPanel;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        Logic logic = new LogicManager(model);

        leftDisplayPanel = new LeftDisplayPanel(
                logic.getFilteredPersonList(), logic.getFilteredPersonListBirthdate());
        uiPartRule.setUiPart(leftDisplayPanel);

        leftDisplayPanelHandle = new LeftDisplayPanelHandle(getChildNode(leftDisplayPanel.getRoot(),
                LeftDisplayPanelHandle.LEFT_DISPLAY_ID));
    }

    @Test
    public void display() {
        // default tab
        guiRobot.pauseForHuman();
        assertEquals(0, leftDisplayPanelHandle.getSelectedTabIndex());

        //Change to Tab 2
        leftDisplayPanelHandle.toggle(1);
        assertEquals(1, leftDisplayPanelHandle.getSelectedTabIndex());

        //Change to Tab 3
        leftDisplayPanelHandle.toggle(2);
        assertEquals(2, leftDisplayPanelHandle.getSelectedTabIndex());

        //Change to Tab 1
        leftDisplayPanelHandle.toggle(0);
        assertEquals(0, leftDisplayPanelHandle.getSelectedTabIndex());
    }
    //@@author

    //@@author hengyu95
    @Test
    public void toggleTabs() throws Exception {
        //Start from Tab 1
        assertEquals(0, leftDisplayPanelHandle.getSelectedTabIndex());

        //Toggling once goes to Tab 2
        leftDisplayPanel.toggleTabs();
        assertEquals(1, leftDisplayPanelHandle.getSelectedTabIndex());

        //Toggling once goes to Tab 3
        leftDisplayPanel.toggleTabs();
        assertEquals(2, leftDisplayPanelHandle.getSelectedTabIndex());

    }

}
