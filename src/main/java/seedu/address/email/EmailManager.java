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
    private String emailStatus;

    public EmailManager() {
        this.message = new Message();
        this.loginDetails = new String[0];
        this.emailStatus = "";
    }

    @Override
    public void composeEmail(Message message) {
        this.emailStatus = "drafted";
        this.message = message;
    }

    @Override
    public Message getEmailDraft() {
        return this.message;
    }

    @Override
    public String getEmailStatus() {
        return this.emailStatus;
    }

    @Override
    public void sendEmail() {

        if (!message.containsContent()) {
            //throw exception that user needs to enter message and subject
            System.out.println("Exception thrown for empty messsage or subject");
        }
        if (!isUserLogin()) {
            //throw exception that user needs to enter login details
            System.out.println("Exception trown, user is not login");
        }

        //send out details

        //reset the email draft after email have been sent
        this.emailStatus = "sent";
        this.message = new Message();
    }

    @Override
    public void loginEmail(String [] loginDetails) {
        if (loginDetails.length != 0 && loginDetails.length == 2) {
            //command entered with login prefix
            this.loginDetails = loginDetails;
        }
    }

    public boolean isUserLogin() {
        if(this.loginDetails.length != 2) {
            return false;
        } else {
            return true;
        }
    }

}
