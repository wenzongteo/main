package seedu.address.email;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.message.Message;

/*
 * Handles how email are sent out of the application.
 */
public class EmailManager extends ComponentManager implements Email {
    private static final Logger logger = LogsCenter.getLogger(EmailManager.class);
    private static final Pattern GMAIL_FORMAT = Pattern.compile("^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(@gmail.com)$");

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
        if(message.getSubject().isEmpty()) {
            message.setSubject(this.message.getSubject());
        }
        if(message.getMessage().isEmpty()) {
            message.setMessage(this.message.getMessage());
        }
        this.message = message;
        this.emailStatus = "drafted";
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
    public void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException {

        if (!message.containsContent()) {
            //throw exception that user needs to enter message and subject to send email
            throw new EmailMessageEmptyException();
        }
        verifyUserEmailFormat();
        if (!isUserLogin()) {
            //throw exception that user needs to enter login details to send email
            throw new EmailLoginInvalidException();
        }


        //set up email Object

        //send out details

        //reset the email draft after email have been sent
        this.emailStatus = "sent";
        this.message = new Message();
    }

    @Override
    public void loginEmail(String [] loginDetails) {
        //replace login details and ignore if login details is omitted.
        if (loginDetails.length != 0 && loginDetails.length == 2) {
            //command entered with login prefix
            this.loginDetails = loginDetails;
        }
    }

    public boolean isUserLogin() {
        if(this.loginDetails.length != 2) {
            //The loginDetails empty
            return false;
        } else {
            return true;
        }
    }

    /** Verify if the user is using a gmail account **/
    private void verifyUserEmailFormat() throws EmailLoginInvalidException {
        if(this.loginDetails.length == 2) {
            final Matcher matcher = GMAIL_FORMAT.matcher(this.loginDetails[0].trim());
            if (!matcher.matches()) {
                throw new EmailLoginInvalidException();
            }
        }
    }

}
