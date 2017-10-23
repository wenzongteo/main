package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHOTO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.io.IOException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.Photo;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters (Only email parameter is compulsory): \n"
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_BIRTHDATE + "BIRTHDATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_BIRTHDATE + "25/12 "
            + PREFIX_PHOTO + "/data/photo.jpg "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String MESSAGE_FILE_ERROR = "File is not found!";

    private final Person toAdd;
    private Photo originalPhoto;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyPerson}
     */
    public AddCommand(ReadOnlyPerson person) {
        toAdd = new Person(person);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
        requireNonNull(model);
        try {
            originalPhoto = toAdd.getPhoto();
            String intendedPhotoPath = "data/images/" + toAdd.getEmail().toString() + ".jpg";

            toAdd.setPhoto(new Photo(intendedPhotoPath, 0));
            model.addPerson(toAdd);
            model.addImage(toAdd.getEmail(), originalPhoto);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicatePersonException e) {
            toAdd.setPhoto(originalPhoto);
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (IOException e) {
            toAdd.setPhoto(originalPhoto);
            throw new CommandException(MESSAGE_FILE_ERROR);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
