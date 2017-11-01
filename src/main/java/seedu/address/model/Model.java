package seedu.address.model;

import java.io.IOException;
import java.util.function.Predicate;

import javax.mail.AuthenticationFailedException;

import javafx.collections.ObservableList;
import seedu.address.email.Email;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.model.person.EmailAddress;
import seedu.address.model.person.Photo;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<ReadOnlyPerson> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /** Returns the email Manager Component */
    Email getEmailManager();

    /** Deletes the given person. */
    void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException, IOException;

    /** Adds the given person */
    void addPerson(ReadOnlyPerson person) throws DuplicatePersonException, IOException;

    /** Copy a person's contact into a fixed location */
    String addImage(EmailAddress email, Photo photo) throws IOException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson) throws DuplicatePersonException,
            PersonNotFoundException, IOException;

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<ReadOnlyPerson> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered person list sorted by upcoming birthdays*/
    ObservableList<ReadOnlyPerson> getFilteredPersonListBirthdate();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate);

    /**
     * Updates the sort format of the filtered person list to sort by the given {@code sortOrder}.
     */
    void sortFilteredPersons(int sortOrder);

    /** delete tag from all person with the tag **/
    void deleteTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException, IOException;

    /** send email based on last displayed person list **/
    void sendEmail(MessageDraft message, boolean send) throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException;

    /** set login credentials for sending emails **/
    void loginEmail(String [] loginDetails) throws EmailLoginInvalidException;

    /** get Email Sent status **/
    String getEmailStatus();

}
