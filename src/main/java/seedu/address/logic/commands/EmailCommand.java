package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Email;
import seedu.address.model.person.ReadOnlyPerson;

public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String COMMAND_ALIAS = "em";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Emails all contacts in the last displayed list\n"
            + "Parameters: email\n"
            + "Examples: email";

    public static final String MESSAGE_SUCCESS = "Email have been  %1$s";
    public static final String MESSAGE_LOGIN_INVALID = "You must log in with a gmail email account before you can send an email.\n"
            + "Command: email el/<username@gmail.com>:<password>";
    public static final String MESSAGE_EMPTY_INVALID = "You must fill in the message and subject before you can send an email.\n"
            + "Command: email em/<messages> es/<subjects>";

    private final MessageDraft message;
    private final String [] loginDetails;
    private final boolean send;

    public EmailCommand(String message, String subject, String [] loginDetails, boolean send) {
        this.message = new MessageDraft(message, subject);
        this.send = send;
        this.loginDetails = loginDetails;
    }

    private InternetAddress[] extractEmailFromContacts(List<ReadOnlyPerson> lastShownList) throws AddressException {
        InternetAddress [] recipientsEmail = new InternetAddress[lastShownList.size()];
        try {
            for (int i = 0; i < lastShownList.size(); i++) {
                recipientsEmail[i] = new InternetAddress(lastShownList.get(i).getEmail().value);
            }
        } catch (AddressException e) {
            throw new AddressException();
        }
        return recipientsEmail;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        //Update recipient list based on last displayed list
        try {
            List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
            InternetAddress [] recipientsEmail = extractEmailFromContacts(lastShownList);
            message.setRecipientsEmail(recipientsEmail);
        } catch(AddressException e) {
            assert false : "The target email cannot be missing or be wrong format";
        }

        try {
            //Set up Email Details
            model.loginEmail(loginDetails);
            model.sendEmail(message, send);
            return new CommandResult(String.format(MESSAGE_SUCCESS, model.getEmailStatus()));
        } catch(EmailLoginInvalidException e) {
            throw new CommandException(MESSAGE_LOGIN_INVALID);
        } catch (EmailMessageEmptyException e) {
            throw new CommandException(MESSAGE_EMPTY_INVALID);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand); // instanceof handles nulls
    }
}
