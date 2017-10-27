package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.ui.EmailDraftChangedEvent;
import seedu.address.email.Email;
import seedu.address.email.EmailManager;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.model.person.EmailAddress;
import seedu.address.model.person.Person;
import seedu.address.model.person.Photo;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Email email;
    private final AddressBook addressBook;
    private final FilteredList<ReadOnlyPerson> filteredPersons;
    private final SortedList<ReadOnlyPerson> sortedPersonsList;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs, Email email) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.email = email;
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        sortedPersonsList = new SortedList<ReadOnlyPerson>(filteredPersons);
        sortFilteredPersons(0);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new EmailManager());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public Email getEmailManager() {
        return email;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException, IOException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(ReadOnlyPerson person) throws DuplicatePersonException, IOException {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized String addImage(EmailAddress email, Photo photo) throws IOException {
        String folder = "data/images/";
        String fileExt = ".jpg";

        File imageFolder = new File(folder);

        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        } else {

        }

        String destination = folder + email.toString() + fileExt;
        Path sourcePath = Paths.get(photo.toString());
        Path destPath = Paths.get(destination);

        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);

        return folder + email.toString() + fileExt;
    }

    @Override
    public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException, IOException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    @Override
    public void deleteTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException, IOException {
        for (int i = 0; i < addressBook.getPersonList().size(); i++) {
            ReadOnlyPerson orginalPerson = addressBook.getPersonList().get(i);

            Person person = new Person(orginalPerson);
            Set<Tag> tags = person.getTags();

            tags.remove(tag);
            person.setTags(tags);

            addressBook.updatePerson(orginalPerson, person);

        }
    }


    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyPerson} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(sortedPersonsList);
    }

    /**
     * @param: int
     * 0 = sort by name ascending
     * 1 = sort by tags ascending
     * 2 = sort by email ascending
     * 3 = sort by address ascending
     * Returns a sorted unmodifable view of the list {@code ReadOnlyPerson} backed by the internal list of
     * {@code addressBook}
     */
    public void sortFilteredPersons(int sortOrder) {

        //sort by name by default
        Comparator<ReadOnlyPerson> sort = new Comparator<ReadOnlyPerson>() {
            @Override
            public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                return o1.getName().fullName.toUpperCase().compareTo(o2.getName().fullName.toUpperCase());
            }
        };

        if (sortOrder == 1) {
            //sort by tags
            sort = new Comparator<ReadOnlyPerson>() {
                @Override
                public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                    TreeSet<Tag> o1SortedTags = new TreeSet<Tag>(o1.getTags());
                    TreeSet<Tag> o2SortedTags = new TreeSet<Tag>(o2.getTags());

                    if (o1SortedTags.size() == 0) {
                        return 1;
                    } else if (o2SortedTags.size() == 0) {
                        return -1;
                    } else {
                        return o1SortedTags.first().tagName.compareTo(o2SortedTags.first().tagName);
                    }
                }
            };
        } else if (sortOrder == 2) {
            //sort by emails
            sort = new Comparator<ReadOnlyPerson>() {
                @Override
                public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                    return o1.getEmailAddress().value.toUpperCase().compareTo(o2.getEmailAddress().value.toUpperCase());
                }
            };
        } else if (sortOrder == 3) {
            //sort by address
            sort = new Comparator<ReadOnlyPerson>() {
                @Override
                public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                    return o1.getAddress().value.toUpperCase().compareTo(o2.getAddress().value.toUpperCase());
                }
            };
        }

        sortedPersonsList.setComparator(sort);
    }

    @Override
    public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void loginEmail(String [] loginDetails) throws EmailLoginInvalidException {
        email.loginEmail(loginDetails);
    }

    @Override
    public void sendEmail(MessageDraft message, boolean send) throws EmailLoginInvalidException,
            EmailMessageEmptyException, EmailRecipientsEmptyException, AuthenticationFailedException {
        email.composeEmail(message);

        if (send) {
            email.sendEmail();
        }
        raise(new EmailDraftChangedEvent(email.getEmailDraft()));
    }

    @Override
    public String getEmailStatus() {
        return email.getEmailStatus();
    }

    @Override
    public boolean equals(Object obj) {

        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && sortedPersonsList.equals(other.sortedPersonsList)
                && email.equals(other.email);
    }

}
