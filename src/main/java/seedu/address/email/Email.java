package seedu.address.email;

import javafx.collections.transformation.SortedList;
import seedu.address.email.message.Message;
import seedu.address.model.person.ReadOnlyPerson;

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
    void sendEmail();

}
