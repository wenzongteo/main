package guitests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

//@@author awarenessxz
/**
 * Test the Toggle and scrolling for LeftDisplayPanel
 */
public class leftDisplayTest extends AddressBookGuiTest {

    @Test
    public void toggleTabs() {
        //Change to Tab 2
        getLeftDisplayPanel().toggle(1);
        assertEquals(1, getLeftDisplayPanel().getSelectedTabIndex());

        //Change to Tab 3
        getLeftDisplayPanel().toggle(2);
        assertEquals(2, getLeftDisplayPanel().getSelectedTabIndex());

        //Change to Tab 1
        getLeftDisplayPanel().toggle(0);
        assertEquals(0, getLeftDisplayPanel().getSelectedTabIndex());
    }
}
