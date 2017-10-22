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

    public void craftEmail(String message, String subject, String [] loginDetails, SortedList<ReadOnlyPerson> recipients) {
        this.loginDetails = loginDetails;
        this.message = message;
        this.recipients = recipients;
        this.subject = subject;
    }

    public void sendEmail() {
        if(message.isEmpty() || subject.isEmpty()) {
            //throw exception that user needs to enter message and subject
            System.out.println("Exception thrown");
        }
        if(loginDetails.length != 2) {
            //throw exception that user needs to enter login details
            System.out.println("Exception trown");
        }

        //extract email recipients email
        extractEmailFromContacts();

        //send out details
    }

    private void extractEmailFromContacts() {
        for(ReadOnlyPerson p : recipients) {
            System.out.println(p.getEmail());
        }
    }
}
