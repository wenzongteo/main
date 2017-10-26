package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Tab Panel containing personListPanel and EmailDraftPanel
 */
public class LeftDisplayPanel extends UiPart<Region> {
    private static final String FXML = "LeftDisplayPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(LeftDisplayPanel.class);

    //Independent UI parts residing in this UI container
    private PersonListPanel personListPanel;
    private MessageDisplay messageDisplay;

    private boolean toggle;

    @FXML
    private TabPane leftDisplayPanel;

    @FXML
    private Tab personListTab;

    @FXML
    private Tab emailDraftTab;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane messageDraftPanelPlaceholder;

    public LeftDisplayPanel(ObservableList<ReadOnlyPerson> personList) {
        super(FXML);

        personListPanel = new PersonListPanel(personList);
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        messageDisplay = new MessageDisplay();
        messageDraftPanelPlaceholder.getChildren().add(messageDisplay.getRoot());

        toggle = true;
    }

    /**
     * Toggle Tabs
     */
    public void toggleTabs() {
        if  (toggle) {
            leftDisplayPanel.getSelectionModel().select(emailDraftTab);
        } else {
            leftDisplayPanel.getSelectionModel().select(personListTab);
        }
        toggle = !toggle;
    }

    /**
     * Scrolls one page down
     */
    public void scrollDown() {
        personListPanel.scrollDown();
    }

    /**
     * Scrolls one page up
     */
    public void scrollUp() {
        personListPanel.scrollUp();
    }

    public PersonListPanel getPersonListPanel() {
        return this.personListPanel;
    }

}
