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

//@@author awarenessxz
/**
 * Tab Panel containing personListPanel, EmailDraftPanel, and BirthdateTab
 */
public class LeftDisplayPanel extends UiPart<Region> {
    private static final String FXML = "LeftDisplayPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(LeftDisplayPanel.class);

    //Independent UI parts residing in this UI container
    private PersonListPanel personListPanel;
    private PersonListBirthdatePanel birthdayListPanel;
    private MessageDisplay messageDisplay;
    private boolean toggle;
    private boolean toggle2;

    @FXML
    private TabPane leftDisplayPanel;

    @FXML
    private Tab personListTab;

    @FXML
    private Tab emailDraftTab;

    @FXML
    private Tab birthdateTab;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane messageDraftPanelPlaceholder;

    @FXML
    private StackPane birthdatePanelPlaceholder;

    public LeftDisplayPanel(ObservableList<ReadOnlyPerson> personList,
                            ObservableList<ReadOnlyPerson> personListBirthdate) {
        super(FXML);

        personListPanel = new PersonListPanel(personList);
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        birthdayListPanel = new PersonListBirthdatePanel(personListBirthdate);
        birthdatePanelPlaceholder.getChildren().add(birthdayListPanel.getRoot());

        messageDisplay = new MessageDisplay();
        messageDraftPanelPlaceholder.getChildren().add(messageDisplay.getRoot());

        toggle = true;
        toggle2 = true;
    }

    /**
     * Toggle Tabs
     */
    public void toggleTabs() {
        if  (toggle && toggle2) {
            leftDisplayPanel.getSelectionModel().select(emailDraftTab);
            toggle2 = !toggle2;
        }
        else if (toggle && !toggle2) {
            leftDisplayPanel.getSelectionModel().select(birthdateTab);
            toggle2 = !toggle2;
            toggle = false;
        }
        else {
            leftDisplayPanel.getSelectionModel().select(personListTab);
            toggle = true;
        }
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
