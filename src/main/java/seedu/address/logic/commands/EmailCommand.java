package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Compose an email draft or send the draft out using gmail account
 **/
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String COMMAND_ALIAS = "em";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Emails all contacts in the last displayed list\n"
            + "Parameters: email\n"
            + "Examples: email";

    public static final String MESSAGE_SUCCESS = "Email have been  %1$s";
    public static final String MESSAGE_LOGIN_INVALID = "You must log in with a gmail email account before you can send "
            + "an email.\n"
            + "Command: email el/<username@gmail.com>:<password>";
    public static final String MESSAGE_EMPTY_INVALID = "You must fill in the message and subject before you can send "
            + "an email.\n"
            + "Command: email em/<messages> es/<subjects>";
    public static final String MESSAGE_RECIPIENT_INVALID = "You must have at least 1 recipients in your contacts "
            + "display list before you can send an email.\n"
            + "Command: use the list or find command";
    public static final String MESSAGE_AUTHENTICATION_FAIL = "You are unable to log in to your gmail account. Please "
            + "check the following:\n"
            + "1) You have entered the correct email address and password.\n"
            + "2) You have enabled 'Allow less secure app' to sign in to your gmail account settings";
    public static final String MESSAGE_FAIL_UNKNOWN = "Unexpected error have occurred...Please try again later";

    private final MessageDraft message;
    private final String [] loginDetails;
    private final boolean send;

    public EmailCommand(String message, String subject, String [] loginDetails, boolean send) {
        this.message = new MessageDraft(message, subject);
        this.send = send;
        this.loginDetails = loginDetails;
    }

    /**
     * Extract Email from last display person {@code lastshownList} into an InternetAddresss[] for sending email
     *
     * @params: last shown display person list
     * @return: list of internet email address
     **/
    private InternetAddress[] extractEmailFromContacts(List<ReadOnlyPerson> lastShownList) throws AddressException {
        InternetAddress [] recipientsEmail = new InternetAddress[lastShownList.size()];
        try {
            for (int i = 0; i < lastShownList.size(); i++) {
                recipientsEmail[i] = new InternetAddress(lastShownList.get(i).getEmailAddress().value);
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
        } catch (AddressException e) {
            assert false : "The target email cannot be missing or be wrong format";
        }

        try {
            //Set up Email Details
            model.loginEmail(loginDetails);
            model.sendEmail(message, send);
            return new CommandResult(String.format(MESSAGE_SUCCESS, model.getEmailStatus()));
        } catch (EmailLoginInvalidException e) {
            throw new CommandException(MESSAGE_LOGIN_INVALID);
        } catch (EmailMessageEmptyException e) {
            throw new CommandException(MESSAGE_EMPTY_INVALID);
        } catch (EmailRecipientsEmptyException e) {
            throw new CommandException(MESSAGE_RECIPIENT_INVALID);
        } catch (AuthenticationFailedException e) {
            throw new CommandException(MESSAGE_AUTHENTICATION_FAIL);
        } catch (RuntimeException e) {
            throw new CommandException(MESSAGE_FAIL_UNKNOWN);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && ((EmailCommand) other).message.equals(this.message)
                && ((EmailCommand) other).loginDetailsEquals(this.loginDetails)
                && ((EmailCommand) other).send == this.send);
    }

    /**
     * For validating if the loginDetails are equal (Testing)
     * 
     * @params: loginDetails to compare with
     * @return true if loginDetails are equal
     **/
    private boolean loginDetailsEquals(String [] other) {
        if (this.loginDetails.length == other.length) {
            for (int i = 0; i < this.loginDetails.length; i++) {
                if (this.loginDetails[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
