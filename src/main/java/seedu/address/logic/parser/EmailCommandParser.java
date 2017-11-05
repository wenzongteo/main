package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_LOGIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_MESSAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_TASK;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.email.EmailTask;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author awarenessxz
/**
 * Parses input arguments and creates a new EmailCommand object
 */
public class EmailCommandParser implements Parser<EmailCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EmailCommand
     * and returns an EmailCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMAIL_MESSAGE, PREFIX_EMAIL_SUBJECT, PREFIX_EMAIL_LOGIN,
                        PREFIX_EMAIL_TASK);

        EmailTask task = new EmailTask();
        String message = "";
        String subject = "";
        String login = "";
        String [] loginDetails = new String[0];

        try {

            if (argMultimap.getValue(PREFIX_EMAIL_MESSAGE).isPresent()) {
                message = ParserUtil.parseEmailMessage(argMultimap.getValue(PREFIX_EMAIL_MESSAGE)).trim();
                if (message.isEmpty()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
                }
            }
            if (argMultimap.getValue(PREFIX_EMAIL_SUBJECT).isPresent()) {
                subject = ParserUtil.parseEmailMessage(argMultimap.getValue(PREFIX_EMAIL_SUBJECT)).trim();
                if (subject.isEmpty()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
                }
            }
            if (argMultimap.getValue(PREFIX_EMAIL_LOGIN).isPresent()) {
                login = ParserUtil.parseLoginDetails(argMultimap.getValue(PREFIX_EMAIL_LOGIN)).trim();
                loginDetails = login.split(":");
                if (loginDetails.length != 2) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
                }
            }

            /** checks what is the email task, to send or create draft **/
            if (argMultimap.getValue(PREFIX_EMAIL_TASK).isPresent()) {
                task.setTask(ParserUtil.parseEmailTask(argMultimap.getValue(PREFIX_EMAIL_TASK)).trim());
                if (!task.isValid()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
                }
            }

            /** checks if only "email" command is run **/
            if (message.isEmpty() && subject.isEmpty() && login.isEmpty() && !task.isValid()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        return new EmailCommand(message, subject, loginDetails, task);
    }
}
