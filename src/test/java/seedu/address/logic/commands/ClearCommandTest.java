package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.address.email.EmailManager;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.ImageInit;


public class ClearCommandTest {
    @Before
    public void setUp() {
        ImageInit.checkDirectories();
        ImageInit.initPictures();
    }

    @After
    public void recovery() {
        ImageInit.deleteEditedFiles();
        ImageInit.deleteImagesFiles();
    }

    @Test
    public void execute_emptyAddressBook_success() throws IOException {
        Model model = new ModelManager();
        assertCommandSuccess(prepareCommand(model), model, ClearCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() throws IOException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new EmailManager());
        assertCommandSuccess(prepareCommand(model), model, ClearCommand.MESSAGE_SUCCESS, model);
    }

    /**
     * Generates a new {@code ClearCommand} which upon execution, clears the contents in {@code model}.
     */
    private ClearCommand prepareCommand(Model model) {
        ClearCommand command = new ClearCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
