package seedu.address.email;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.message.Message;

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
    void composeEmail(Message message);

    /* view Email Draft */
    Message getEmailDraft();

    /* View Email Send Status */
    String getEmailStatus();

    /* send Email Draft to all users */
    void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException;

}
