package seedu.address.email;

import javafx.collections.transformation.SortedList;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * The API of Email component
 */
public interface Email {
    /* Login to send Email */
    //void loginEmail();

    /* Logout of Email */
    //void logoutEmail();

    /* Checks if user is Log in */
    //void isUserLogin();

    /* create Email Draft */
    //void createEmailDraft(String message);

    /* view Email Draft */
    //void getEmailDraft();

    /* send Email Draft to all users */
    void sendEmail(boolean send);

    /* Craft Email with all details */
    void craftEmail(String message, String subject, String [] loginDetails, SortedList<ReadOnlyPerson> recipients);

}
