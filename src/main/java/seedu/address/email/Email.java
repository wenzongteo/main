package seedu.address.email;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;

/**
 * The API of Email component
 */
public interface Email {

    /* Login to send Email */
    void loginEmail(String [] loginDetails);

    /* Logout of Email */
    //void logoutEmail();

    /* Checks if user is Log in */
    boolean isUserLogin();

    /* Create Email Draft with all details */
    void composeEmail(MessageDraft message);

    /* view Email Draft */
    MessageDraft getEmailDraft();

    /* View Email Send Status */
    String getEmailStatus();

    /* send Email Draft to all users */
    void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException, EmailRecipientsEmptyException;

}
