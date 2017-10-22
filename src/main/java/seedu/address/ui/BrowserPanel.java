package seedu.address.ui;

import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String NUSMODS_SEARCH_URL_PREFIX = "https://nusmods.com/timetable/";

    private static final String FXML = "BrowserPanel.fxml";

    private String semester;
    private String academicYear;


    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel(Config config) {
        super(FXML);

        setAcademicYearSemester(config);
        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    /**
     * Loads nusmods website base on person object
     * @param person
     */
    private void loadPersonPage(ReadOnlyPerson person) {
        // Loads website if nusModule is not null or empty
        if (person.getNusModules() != null && !person.getNusModules().value.isEmpty()) {
            loadPage(NUSMODS_SEARCH_URL_PREFIX + academicYear + "/sem" + semester + "?"
                    + person.getNusModules().toString());
        }
    }

    public String getSemester(){
        return semester;
    }

    public String getAcademicYear(){
        return academicYear;
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    private void setAcademicYearSemester(Config config) {
        academicYear = config.getAcademicYear();
        semester = config.getSemester();
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }
}
