package guitests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxToolkit;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.PersonListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import javafx.stage.Stage;
import seedu.address.TestApp;
import seedu.address.commons.core.EventsCenter;
import seedu.address.model.AddressBook;
import seedu.address.testutil.TypicalPersons;

/**
 * A GUI Test class for AddressBook.
 */
public abstract class AddressBookGuiTest {

    /* The TestName Rule makes the current test name available inside test methods */
    @Rule
    public TestName name = new TestName();

    protected GuiRobot guiRobot = new GuiRobot();

    protected Stage stage;

    protected MainWindowHandle mainWindowHandle;

    @BeforeClass
    public static void setupOnce() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() throws Exception {
        String folder = "data/images/";

        File imageFolder = new File(folder);

        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        } else {

        }

        try {
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/johnd@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/heinz@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/anna@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/stefan@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/hans@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e ) {
            throw new AssertionError("Impossible");
        }

        FxToolkit.setupStage((stage) -> {
            this.stage = stage;
        });
        FxToolkit.setupApplication(() -> new TestApp(this::getInitialData, getDataFileLocation()));
        FxToolkit.showStage();

        mainWindowHandle = new MainWindowHandle(stage);
        mainWindowHandle.focus();
    }

    /**
     * Override this in child classes to set the initial local data.
     * Return null to use the data in the file specified in {@link #getDataFileLocation()}
     */
    protected AddressBook getInitialData() {
        return TypicalPersons.getTypicalAddressBook();
    }

    protected CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    protected PersonListPanelHandle getPersonListPanel() {
        return mainWindowHandle.getPersonListPanel();
    }

    protected MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    protected BrowserPanelHandle getBrowserPanel() {
        return mainWindowHandle.getBrowserPanel();
    }

    protected StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    protected ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Runs {@code command} in the application's {@code CommandBox}.
     * @return true if the command was executed successfully.
     */
    protected boolean runCommand(String command) {
        return mainWindowHandle.getCommandBox().run(command);
    }

    /**
     * Override this in child classes to set the data file location.
     */
    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    @After
    public void cleanup() throws Exception {
        EventsCenter.clearSubscribers();
        FxToolkit.cleanupStages();
    }

}
