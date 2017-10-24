package seedu.address.email;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    private String emailStatus;
    private Properties props;

    public EmailManager() {
        logger.fine("Initializing Email Component");

        this.emailLogin = new EmailLogin();
        this.emailCompose = new EmailCompose();
        this.emailStatus = "";
        prepEmail();
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

        //Step 1. Verify that the email draft consists of message and subject
        if (!emailCompose.getMessage().containsContent()) {
            //throw exception that user needs to enter message and subject to send email
            throw new EmailMessageEmptyException();
        }
        //Step 2. Verify that the user have logged in.
        //Step 3. Verify that the user have logged in with a gmail account
        if (!isUserLogin()) {
            //throw exception that user needs to enter gmail login details to send email
            throw new EmailLoginInvalidException();
        }
        //Step 4. Verify that Recipient's list is not empty
        if (emailCompose.getMessage().getRecipientsEmails().length <= 0) {
            throw new EmailRecipientsEmptyException();
        }

        //Step 5. sending Email out using JavaMail API
        sendingEmail();

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

    /** Prepare Email Default Properties **/
    private void prepEmail() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");
    }

    /** Send email out using JavaMail API **/
    private void sendingEmail() throws AuthenticationFailedException {
        final String username = emailLogin.getEmailLogin();
        final String password = emailLogin.getPassword();

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message newMessage = new MimeMessage(session);
            newMessage.setFrom(new InternetAddress(username));
            newMessage.setRecipients(Message.RecipientType.TO, emailCompose.getMessage().getRecipientsEmails());
            newMessage.setSubject(emailCompose.getMessage().getSubject());
            newMessage.setText(emailCompose.getMessage().getMessage());

            Transport.send(newMessage);
        } catch (AuthenticationFailedException e) {
            throw new AuthenticationFailedException();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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
