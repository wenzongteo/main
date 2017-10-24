package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.email.Email;
import seedu.address.email.EmailManager;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {
    @Before
    public void setup() throws Exception {
        String imageFilePath = "data/images/";
        File imageFolder = new File(imageFilePath);

        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
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
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/amy@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AssertionError("Impossible");
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        ModelManager modelManager = new ModelManager();
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredPersonList().remove(0);
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();
        Email email = new EmailManager();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(addressBook, userPrefs, email);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs, email);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs, email)));

        // different filteredList -> returns false
        String[] keywordsname = ALICE.getName().fullName.split("\\s+");
        String[] keywordstag = new String[0];
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(
                Arrays.asList(keywordsname), Arrays.asList(keywordstag)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs, email)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookName("differentName");
        assertTrue(modelManager.equals(new ModelManager(addressBook, differentUserPrefs, email)));
    }
}
