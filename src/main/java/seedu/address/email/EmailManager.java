package seedu.address.email;

import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.ReadOnlyPerson;

/*
 * Handles how email are sent out of the application.
 */
public class EmailManager extends ComponentManager implements Email {
    private static final Logger logger = LogsCenter.getLogger(EmailManager.class);

    private SortedList<ReadOnlyPerson> recipients;
    private String message;
    private String subject;
    private String [] loginDetails;

    public EmailManager() {
        this.recipients = new SortedList<ReadOnlyPerson>(FXCollections.emptyObservableList());
    }

    public void sendEmail(String message, String subject, String [] loginDetails, SortedList<ReadOnlyPerson> recipients) {
        //is message empty?
            //yes --> draft message
        //is user logged in?
            //no --> get log in crudential and login
        //is command send or draft?
            //send --> send email out.


        this.recipients = recipients;
        this.message = message;
        this.subject = subject;
        this.loginDetails = loginDetails;
    }
}
