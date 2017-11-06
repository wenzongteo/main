package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHOTO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERID;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthdate;
import seedu.address.model.person.EmailAddress;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Photo;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.UserID;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException, IOException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL_ADDRESS,
                PREFIX_ADDRESS, PREFIX_PHOTO, PREFIX_BIRTHDATE, PREFIX_TAG, PREFIX_USERID);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_EMAIL_ADDRESS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(checkInput(argMultimap.getValue(PREFIX_NAME))).get();
            Phone phone = ParserUtil.parsePhone(checkInput(argMultimap.getValue(PREFIX_PHONE))).get();
            EmailAddress emailAddress = ParserUtil.parseEmail(checkInput(argMultimap.getValue(PREFIX_EMAIL_ADDRESS)))
                    .get();
            Address address = ParserUtil.parseAddress(checkInput(argMultimap.getValue(PREFIX_ADDRESS))).get();
            Photo photo = ParserUtil.parsePhoto(checkInput(argMultimap.getValue(PREFIX_PHOTO))).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            Birthdate birthdate = ParserUtil.parseBirthdate(checkInput(argMultimap.getValue(PREFIX_BIRTHDATE))).get();
            UserID id = ParserUtil.parseUserID(checkInput(argMultimap.getValue(PREFIX_USERID))).get();
            ReadOnlyPerson person = new Person(name, phone, emailAddress, address, photo, tagList, birthdate, id);

            return new AddCommand(person);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    //@@author wenzongteo
    /**
     * Check if the user has input any value for this variable before sending the input for further parsing.
     *
     * @param userInput Input entered by the user that is parsed by argMultimap.
     * @return the value of the input entered by the user or '-' if no input was entered.
     */
    private static Optional<String> checkInput(Optional<String> userInput) {
        return Optional.of(userInput.orElse("-"));
    }
}
