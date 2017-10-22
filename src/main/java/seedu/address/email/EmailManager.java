package seedu.address.email;

import java.util.ArrayList;
import java.util.logging.Logger;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.email.message.Message;

/*
 * Handles how email are sent out of the application.
 */
public class EmailManager extends ComponentManager implements Email {
    private static final Logger logger = LogsCenter.getLogger(EmailManager.class);

    private Message message;
    private String [] loginDetails;

    public EmailManager() {
        this.message = new Message();
        this.loginDetails = new String[0];
    }

    @Override
    public void composeEmail(Message message) {
        this.message = message;
    }

    @Override
    public Message getEmailDraft() {
        return this.message;
    }

    public void sendEmail() {

        if (message.containsContent()) {
            //throw exception that user needs to enter message and subject
            System.out.println("Exception thrown for empty messsage or subject");
        }
        if (isUserLogin()) {
            //throw exception that user needs to enter login details
            System.out.println("Exception trown, user is not login");
        }

        //extract email recipients email
        extractEmailFromContacts();

        //send out details
    }

    @Override
    public void loginEmail(String [] loginDetails) {
        this.loginDetails = loginDetails;
    }

    public boolean isUserLogin() {
        if(this.loginDetails.length != 2) {
            return false;
        } else {
            return true;
        }
    }

    private void extractEmailFromContacts() {
        System.out.println(this.message.getRecipientsEmails().size());
        for(seedu.address.model.person.Email p : this.message.getRecipientsEmails()) {
            System.out.println(p);
        }
    }
}
