package seedu.address.email;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern GMAIL_FORMAT = Pattern.compile("^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(@gmail.com)$");

    private MessageDraft message;
    private String [] loginDetails;
    private String emailStatus;
    private Properties props;

    public EmailManager() {
        logger.fine("Initializing Email Component");

        this.message = new MessageDraft();
        this.loginDetails = new String[0];
        this.emailStatus = "";
        prepEmail();
    }

    @Override
    public void composeEmail(MessageDraft message) {
        if (message.getSubject().isEmpty()) {
            message.setSubject(this.message.getSubject());
        }
        if (message.getMessage().isEmpty()) {
            message.setMessage(this.message.getMessage());
        }
        this.message = message;
        this.emailStatus = "drafted";
    }

    @Override
    public MessageDraft getEmailDraft() {
        return this.message;
    }

    @Override
    public String getEmailStatus() {
        return this.emailStatus;
    }

    @Override
    public void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException {

        //Step 1. Verify that the email draft consists of message and subject
        if (!message.containsContent()) {
            //throw exception that user needs to enter message and subject to send email
            throw new EmailMessageEmptyException();
        }
        //Step 2. Verify that the user have logged in.
        //Step 3. Verify that the user have logged in with a gmail account
        if (!isUserLogin() || wrongUserEmailFormat()) {
            //throw exception that user needs to enter gmail login details to send email
            throw new EmailLoginInvalidException();
        }
        //Step 4. Verify that Recipient's list is not empty
        if (message.getRecipientsEmails().length <= 0) {
            throw new EmailRecipientsEmptyException();
        }

        //Step 5. sending Email out using JavaMail API
        sendingEmail();

        //reset the email draft after email have been sent
        this.emailStatus = "sent";
        resetData();
    }

    @Override
    public void loginEmail(String [] loginDetails) {
        //replace login details and ignore if login details is omitted.
        if (loginDetails.length != 0 && loginDetails.length == 2) {
            //command entered with login prefix
            this.loginDetails = loginDetails;
        }
    }

    /**
     * Checks if the email manager holds the username and password of user
     *
     * @return boolean
     **/
    public boolean isUserLogin() {
        if (this.loginDetails.length != 2) {
            //The loginDetails empty
            return false;
        } else {
            return true;
        }
    }

    /** Verify if the user is using a gmail account **/
    public boolean wrongUserEmailFormat() {
        if (this.loginDetails.length == 2) {
            final Matcher matcher = GMAIL_FORMAT.matcher(this.loginDetails[0].trim());
            if (!matcher.matches()) {
                return true;
            }
        }
        return false;
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
        final String username = loginDetails[0];
        final String password = loginDetails[1];

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message newMessage = new MimeMessage(session);
            newMessage.setFrom(new InternetAddress(username));
            newMessage.setRecipients(Message.RecipientType.TO, message.getRecipientsEmails());
            newMessage.setSubject(message.getSubject());
            newMessage.setText(message.getMessage());

            Transport.send(newMessage);
        } catch (AuthenticationFailedException e) {
            throw new AuthenticationFailedException();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /** reset Email Draft Data **/
    private void resetData() {
        this.message = new MessageDraft();
        this.loginDetails = new String[0];
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
        return message.equals(other.message)
                && this.loginDetailsEquals(other.loginDetails);
    }

    /**
     * For validating if the loginDetails are equal (Testing)
     *
     * @params: loginDetails to compare with
     * @return true if loginDetails are equal
     **/
    private boolean loginDetailsEquals(String [] other) {
        if (this.loginDetails.length == other.length) {
            for (int i = 0; i < this.loginDetails.length; i++) {
                if (this.loginDetails[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
