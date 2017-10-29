package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthdate;
import seedu.address.model.person.EmailAddress;
import seedu.address.model.person.Name;
import seedu.address.model.person.NusModules;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Photo;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHOTO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

/**
 * Edits the details of an existing person in the address book.
 */
public class NusmodsCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "nusmods";
    public static final String COMMAND_ALIAS = "nm";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the last person listing.\n"
            + "Apart from tags, existing values will be overwritten by the input values.\n"
            + "Tags will be added if person does not have the tag and deleted otherwise.\n"
            + "You can remove all the person's tags by typing `t/` without specifying any tags after it.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL_ADDRESS + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PHOTO + "PHOTO] "
            + "[" + PREFIX_BIRTHDATE + "BIRTHDATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL_ADDRESS + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_INVALID_TYPE = "Type needs to be 'add', 'url', or 'delete'";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final NusmodsDescriptor nusmodsDescriptor;
    private Photo originalPhoto;

    /**
     * @param index of the person in the filtered person list to edit
     * @param nusmodsDescriptor details to edit the person with
     */
    public NusmodsCommand(Index index, NusmodsDescriptor nusmodsDescriptor) {
        requireNonNull(index);
        requireNonNull(nusmodsDescriptor);

        this.index = index;
        this.nusmodsDescriptor = new NusmodsDescriptor(nusmodsDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, nusmodsDescriptor);

        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        } catch (IOException ioe) {
            throw new AssertionError("The file must exist");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(ReadOnlyPerson personToEdit,
                                             NusmodsDescriptor nusmodsDescriptor) {
        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        EmailAddress updatedEmail = personToEdit.getEmailAddress();
        Address updatedAddress = personToEdit.getAddress();
        Photo updatedPhoto = personToEdit.getPhoto();
        Set<Tag> updatedTags = personToEdit.getTags();
        Birthdate updatedBirthdate = personToEdit.getBirthdate();
        NusModules updatedNusModules = processNusmodsDescriptor(personToEdit, nusmodsDescriptor);




        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedPhoto, updatedTags,
                updatedBirthdate, updatedNusModules);

    }

    /**
     *
     * @param personToEdit
     * @param nusmodsDescriptor
     * @return
     */
    private static NusModules processNusmodsDescriptor(ReadOnlyPerson personToEdit, NusmodsDescriptor nusmodsDescriptor) {
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NusmodsCommand)) {
            return false;
        }

        // state check
        NusmodsCommand e = (NusmodsCommand) other;
        return index.equals(e.index)
                && nusmodsDescriptor.equals(e.nusmodsDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class NusmodsDescriptor {
        private Name name;
        private Phone phone;
        private EmailAddress emailAddress;
        private Address address;
        private Photo photo;
        private Set<Tag> tags;
        private Birthdate birthdate;


        public NusmodsDescriptor() {}

        public NusmodsDescriptor(NusmodsDescriptor toCopy) {
            this.name = toCopy.name;
            this.phone = toCopy.phone;
            this.emailAddress = toCopy.emailAddress;
            this.address = toCopy.address;
            this.photo = toCopy.photo;
            this.tags = toCopy.tags;
            this.birthdate = toCopy.birthdate;
        }

        /**
         * Returns true if type if valid
         */
        public boolean isValidType() {
            if (this.type.equals("add") || this.type.equals("a")
                    || this.type.equals("delete") || this.type.equals("a")
                    || this.type.equals("url") || this.type.equals("a")) {
                return true;
            }
            return false;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmailAddress(EmailAddress emailAddress) {
            this.emailAddress = emailAddress;
        }

        public Optional<EmailAddress> getEmailAddress() {
            return Optional.ofNullable(emailAddress);
        }

        public void setPhoto(Photo photo) {
            this.photo = photo;
        }

        public Optional<Photo> getPhoto() {
            return Optional.ofNullable(photo);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public Optional<Birthdate> getBirthdate() {
            return Optional.ofNullable(birthdate);
        }

        public void setBirthdate(Birthdate birthdate) {
            this.birthdate = birthdate;
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }

        public Optional<Set<Tag>> getTags() {
            return Optional.ofNullable(tags);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmailAddress().equals(e.getEmailAddress())
                    && getAddress().equals(e.getAddress())
                    && getPhoto().equals(e.getPhoto())
                    && getTags().equals(e.getTags())
                    && getBirthdate().equals(e.getBirthdate());
        }
    }
}
