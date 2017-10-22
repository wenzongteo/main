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

    /* create Email Draft */
    //void createEmailDraft(String message);

    /* view Email Draft */
    Message getEmailDraft();

    /* send Email Draft to all users */
    void sendEmail();

    /* Craft Email with all details */
    void composeEmail(Message message);

}
