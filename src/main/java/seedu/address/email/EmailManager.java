package seedu.address.email;

import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;

/**
 * Handles how email are sent out of the application.
 **/
public class EmailManager extends ComponentManager implements Email {
    private static final Logger logger = LogsCenter.getLogger(EmailManager.class);

    private final EmailLogin emailLogin;
    private final EmailCompose emailCompose;
    private final EmailSend emailSend;

    private String emailStatus;

    public EmailManager() {
        logger.fine("Initializing Email Component");

        this.emailLogin = new EmailLogin();
        this.emailCompose = new EmailCompose();
        this.emailSend = new EmailSend();
        this.emailStatus = "";
    }

    @Override
    public void composeEmail(MessageDraft message) {
        emailCompose.composeEmail(message);
        this.emailStatus = "drafted";
    }

    @Override
    public MessageDraft getEmailDraft() {
        return emailCompose.getMessage();
    }

    @Override
    public String getEmailStatus() {
        return this.emailStatus;
    }

    @Override
    public void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException {

        emailSend.sendEmail(emailCompose, emailLogin);

        //reset the email draft after email have been sent
        this.emailStatus = "sent";
        resetData();
    }

    @Override
    public void loginEmail(String [] loginDetails) throws EmailLoginInvalidException {
        emailLogin.loginEmail(loginDetails);
    }

    /**
     * Checks if the email manager holds the username and password of user
     *
     * @return boolean
     **/
    public boolean isUserLogin() {
        return emailLogin.isUserLogin();
    }

    /** reset Email Draft Data **/
    private void resetData() {
        this.emailCompose.resetData();
        this.emailLogin.resetData();
    }

    @Override
    public boolean equals(Object obj) {

        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof EmailManager)) {
            return false;
        }

        // state check
        EmailManager other = (EmailManager) obj;
        return this.emailCompose.equals(((EmailManager) obj).emailCompose)
                && this.emailLogin.equals(((EmailManager) obj).emailLogin);
    }

}