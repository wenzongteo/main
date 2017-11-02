package seedu.address.email;

import javax.mail.AuthenticationFailedException;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.email.message.ReadOnlyMessageDraft;

//@@author awarenessxz
/**
 * The API of Email component
 */
public interface Email {

    /* Login to send Email */
    void loginEmail(String [] loginDetails) throws EmailLoginInvalidException;

    /* Checks if user is Log in */
    boolean isUserLogin();

    /* Create Email Draft with all details */
    void composeEmail(MessageDraft message);

    /* view Email Draft */
    ReadOnlyMessageDraft getEmailDraft();

    /* View Email Send Status */
    String getEmailStatus();

    /* Clear Email Draft content */
    void clearEmailDraft();

    /* send Email Draft to all users */
    void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException;

}
