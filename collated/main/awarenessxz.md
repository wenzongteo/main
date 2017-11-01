# awarenessxz
###### \java\seedu\address\commons\events\ui\EmailDraftChangedEvent.java
``` java
/**
 * Indicates when the email draft changed
 */
public class EmailDraftChangedEvent extends BaseEvent {

    public final ReadOnlyMessageDraft message;

    public EmailDraftChangedEvent(ReadOnlyMessageDraft message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\address\commons\util\StringUtil.java
``` java
    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, and a full word match is not required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == true //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsNonFullWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        for (String wordInSentence: wordsInPreppedSentence) {
            if (wordInSentence.toLowerCase().contains(preppedWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
```
###### \java\seedu\address\email\Email.java
``` java
/**
 * The API of Email component
 */
public interface Email {

    /* Login to send Email */
    void loginEmail(String [] loginDetails) throws EmailLoginInvalidException;

    /* Checks if user is Log in */
    boolean isUserLogin();

    /* Create Email Draft with all details */
    void composeEmail(MessageDraft message);

    /* view Email Draft */
    ReadOnlyMessageDraft getEmailDraft();

    /* View Email Send Status */
    String getEmailStatus();

    /* send Email Draft to all users */
    void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException;

}
```
###### \java\seedu\address\email\EmailCompose.java
``` java
/**
 * Holds a list of messageDraft
 * Handles how messageDraft are created and edited
 */
public class EmailCompose {
    private DraftList emailDraftsList;

    public EmailCompose() {
        emailDraftsList = new DraftList();
    }

    /**
     * compose an Email or edit the current one
     * @param message
     */
    public void composeEmail(MessageDraft message) {
        emailDraftsList.composeEmail(message);
    }

    public ReadOnlyMessageDraft getMessage() {
        return emailDraftsList.getMessage(0);
    }

    public void resetData() {
        emailDraftsList = new DraftList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCompose // instanceof handles nulls
                && this.emailDraftsList.equals(((EmailCompose) other).emailDraftsList));
    }
}
```
###### \java\seedu\address\email\EmailLogin.java
``` java
/**
 * Handles how user logs into email
 */
public class EmailLogin {
    private static final Pattern GMAIL_FORMAT = Pattern.compile("^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(@gmail.com)$");

    private String [] loginDetails;

    public EmailLogin() {
        this.loginDetails = new String[0];
    }

    /**
     * Saves user login details
     * @param loginDetails
     */
    public void loginEmail(String [] loginDetails) throws EmailLoginInvalidException {
        //replace login details and ignore if login details is omitted.
        if (loginDetails.length != 0 && loginDetails.length == 2) {
            if (wrongUserEmailFormat(loginDetails)) {
                throw new EmailLoginInvalidException();
            } else {
                this.loginDetails = loginDetails;
            }
        }
    }

    /**
     * Checks if user's login details have been stored
     * @return boolean
     */
    public boolean isUserLogin() {
        if (this.loginDetails.length != 2) {
            //The loginDetails empty
            return false;
        } else {
            return true;
        }
    }

    /**
     * Verifies if the user is using a gmail account
     * @return true if gmail account, false for everything else
     */
    private boolean wrongUserEmailFormat(String [] loginDetails) {
        if (loginDetails.length == 2) {
            final Matcher matcher = GMAIL_FORMAT.matcher(loginDetails[0].trim());
            if (!matcher.matches()) {
                return true;
            }
        }
        return false;
    }


    public String getEmailLogin() {
        if (this.loginDetails.length == 2) {
            return this.loginDetails[0];
        } else {
            return "";
        }
    }

    public String getPassword() {
        if (this.loginDetails.length == 2) {
            return this.loginDetails[1];
        } else {
            return "";
        }
    }

    public void resetData() {
        this.loginDetails = new String[0];
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailLogin // instanceof handles nulls
                && this.loginDetailsEquals(((EmailLogin) other).loginDetails));
    }

    /**
     * Verifies that the loginDetails are equal
     * @param other
     * @return
     */
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
```
###### \java\seedu\address\email\EmailManager.java
``` java
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
    public ReadOnlyMessageDraft getEmailDraft() {
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
```
###### \java\seedu\address\email\EmailSend.java
``` java
/**
 * Handles how email is send via JavaAPI
 */
public class EmailSend {
    private Properties props;

    public EmailSend() {
        prepEmailProperties();
    }

    /** Prepare Email Default Properties **/
    private void prepEmailProperties() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");
    }

    /**
     * Handles sending email process
     * @throws EmailLoginInvalidException
     * @throws EmailMessageEmptyException
     * @throws EmailRecipientsEmptyException
     * @throws AuthenticationFailedException
     */
    public void sendEmail(EmailCompose emailCompose, EmailLogin emailLogin) throws EmailLoginInvalidException,
            EmailMessageEmptyException, EmailRecipientsEmptyException, AuthenticationFailedException {

        //Step 1. Verify that the email draft consists of message and subject
        if (!emailCompose.getMessage().containsContent()) {
            //throw exception that user needs to enter message and subject to send email
            throw new EmailMessageEmptyException();
        }

        //Step 2. Verify that the user is logged in with a gmail account
        if (!emailLogin.isUserLogin()) {
            //throw exception that user needs to enter gmail login details to send email
            throw new EmailLoginInvalidException();
        }

        //Step 3. Verify that Recipient's list is not empty
        if (emailCompose.getMessage().getRecipientsEmails().length <= 0) {
            throw new EmailRecipientsEmptyException();
        }

        //Step 4. sending Email out using JavaMail API
        sendingEmail(emailLogin.getEmailLogin(), emailLogin.getPassword(), emailCompose.getMessage());
    }

    /** Send email out using JavaMail API **/
    private void sendingEmail(String login, String pass, ReadOnlyMessageDraft message)
            throws AuthenticationFailedException {
        final String username = login;
        final String password = pass;

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
}
```
###### \java\seedu\address\email\exceptions\EmailLoginInvalidException.java
``` java
/**
 * Signals that the email is unable to be send due to invalid login details
 */
public class EmailLoginInvalidException extends Exception {
}
```
###### \java\seedu\address\email\exceptions\EmailMessageEmptyException.java
``` java
/**
 * Signals that the email is unable to be send without message or subject
 */
public class EmailMessageEmptyException extends Exception {
}
```
###### \java\seedu\address\email\exceptions\EmailRecipientsEmptyException.java
``` java
/**
 * Signals that the email is unable to be send without recipients
 */
public class EmailRecipientsEmptyException extends Exception {
}
```
###### \java\seedu\address\email\message\DraftList.java
``` java
/**
 * Contains a list of Email Message Drafts
 */
public class DraftList {
    private MessageDraft [] messages = new MessageDraft[1];

    public DraftList() {
        messages[0] = new MessageDraft();
    }

    /**
     * Compose a new email or edit the current one
     * @param newMessage
     */
    public void composeEmail(MessageDraft newMessage) {
        MessageDraft message = messages[0];
        if (newMessage.getSubject().isEmpty()) {
            newMessage.setSubject(message.getSubject());
        }
        if (newMessage.getMessage().isEmpty()) {
            newMessage.setMessage(message.getMessage());
        }
        messages[0] = newMessage;
    }

    public ReadOnlyMessageDraft getMessage(int i) {
        return messages[i];
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DraftList // instanceof handles nulls
                && this.draftListEquals(((DraftList) other).messages));
    }

    /**
     * Verifies if the draftlist matches
     * @param other
     * @return true if all message matches
     */
    private boolean draftListEquals(MessageDraft [] other) {
        if (other.length == this.messages.length) {
            for (int i = 0; i < this.messages.length; i++) {
                if (!this.messages[i].equals(other[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
```
###### \java\seedu\address\email\message\MessageDraft.java
``` java
/**
 * Email Message Object for storing Email Drafts Contents
 **/
public class MessageDraft implements ReadOnlyMessageDraft {

    private String message;
    private String subject;
    private InternetAddress [] recipientsEmail;

    public MessageDraft() {
        this.message = "";
        this.subject = "";
        this.recipientsEmail = new InternetAddress[0];
    }

    public MessageDraft(String message, String subject) {
        this.message = message;
        this.subject = subject;
        this.recipientsEmail = new InternetAddress[0];
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public InternetAddress[] getRecipientsEmails() {
        return this.recipientsEmail;
    }

    public void setRecipientsEmail(InternetAddress[] recipientsEmail) {
        this.recipientsEmail = new InternetAddress[recipientsEmail.length];
        System.arraycopy(recipientsEmail, 0, this.recipientsEmail, 0, recipientsEmail.length);
    }

    @Override
    public String getRecipientsEmailtoString() {
        String receipients = "";
        for (int i = 0; i < recipientsEmail.length; i++) {
            receipients += recipientsEmail[i].getAddress();
            if (i != recipientsEmail.length - 1) {
                receipients += ", ";
            }
        }
        return receipients;
    }

    @Override
    public boolean containsContent() {
        if (this.message.isEmpty() || this.subject.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyMessageDraft // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyMessageDraft) other));
    }

    @Override
    public boolean recipientsEquals(InternetAddress [] other) {
        if (other.length == this.recipientsEmail.length) {
            for (int i = 0; i < recipientsEmail.length; i++) {
                if (other[i] != this.recipientsEmail[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
```
###### \java\seedu\address\email\message\ReadOnlyMessageDraft.java
``` java
/**
 * A read-only immutable interface for a email message in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyMessageDraft {
    String getMessage();
    String getSubject();
    InternetAddress [] getRecipientsEmails();
    String getRecipientsEmailtoString();

    boolean containsContent();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyMessageDraft other) {

        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getMessage().equals(this.getMessage()) // state checks here onwards
                && other.getSubject().equals(this.getSubject())
                && other.recipientsEquals(this.getRecipientsEmails()));
    }

    boolean recipientsEquals(InternetAddress [] other);
}
```
###### \java\seedu\address\logic\commands\EmailCommand.java
``` java
/**
 * Compose an email draft or send the draft out using gmail account
 **/
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String COMMAND_ALIAS = "em";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Emails all contacts in the last displayed list\n"
            + "Parameters:\n"
            + "email et/[send|draft|compose] em/MESSAGE es/SUBJECT el/user@gmail.com:password\n"
            + "Examples:\n"
            + "1) email em/what is your message?\n"
            + "2) email es/new subject\n"
            + "3) email el/adam@gmail.com:password\n"
            + "4) email et/send\n"
            + "5) email em/message es/subject el/adam@gamil.com:password et/send";

    public static final String MESSAGE_SUCCESS = "Email have been  %1$s";
    public static final String MESSAGE_LOGIN_INVALID = "You must log in with a gmail email account before you can send "
            + "an email.\n"
            + "Command: email el/<username@gmail.com>:<password>";
    public static final String MESSAGE_EMPTY_INVALID = "You must fill in the message and subject before you can send "
            + "an email.\n"
            + "Command: email em/<messages> es/<subjects>";
    public static final String MESSAGE_RECIPIENT_INVALID = "You must have at least 1 recipients in your contacts "
            + "display list before you can send an email.\n"
            + "Command: use the list or find command";
    public static final String MESSAGE_AUTHENTICATION_FAIL = "You are unable to log in to your gmail account. Please "
            + "check the following:\n"
            + "1) You have entered the correct email address and password.\n"
            + "2) You have enabled 'Allow less secure app' to sign in to your gmail account settings";
    public static final String MESSAGE_FAIL_UNKNOWN = "Unexpected error have occurred...Please try again later";

    private final MessageDraft message;
    private final String [] loginDetails;
    private final boolean send;

    public EmailCommand(String message, String subject, String [] loginDetails, boolean send) {
        this.message = new MessageDraft(message, subject);
        this.send = send;
        this.loginDetails = loginDetails;
    }

    /**
     * Extract Email from last display person {@code lastshownList} into an InternetAddresss[] for sending email
     *
     * @params: last shown display person list
     * @return: list of internet email address
     **/
    private InternetAddress[] extractEmailFromContacts(List<ReadOnlyPerson> lastShownList) throws AddressException {
        InternetAddress [] recipientsEmail = new InternetAddress[lastShownList.size()];
        try {
            for (int i = 0; i < lastShownList.size(); i++) {
                recipientsEmail[i] = new InternetAddress(lastShownList.get(i).getEmailAddress().value);
            }
        } catch (AddressException e) {
            throw new AddressException();
        }
        return recipientsEmail;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        //Update recipient list based on last displayed list
        try {
            List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
            InternetAddress [] recipientsEmail = extractEmailFromContacts(lastShownList);
            message.setRecipientsEmail(recipientsEmail);
        } catch (AddressException e) {
            assert false : "The target email cannot be missing or be wrong format";
        }

        try {
            //Set up Email Details
            model.loginEmail(loginDetails);
            model.sendEmail(message, send);
            return new CommandResult(String.format(MESSAGE_SUCCESS, model.getEmailStatus()));
        } catch (EmailLoginInvalidException e) {
            throw new CommandException(MESSAGE_LOGIN_INVALID);
        } catch (EmailMessageEmptyException e) {
            throw new CommandException(MESSAGE_EMPTY_INVALID);
        } catch (EmailRecipientsEmptyException e) {
            throw new CommandException(MESSAGE_RECIPIENT_INVALID);
        } catch (AuthenticationFailedException e) {
            throw new CommandException(MESSAGE_AUTHENTICATION_FAIL);
        } catch (RuntimeException e) {
            throw new CommandException(MESSAGE_FAIL_UNKNOWN);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && ((EmailCommand) other).message.equals(this.message)
                && ((EmailCommand) other).loginDetailsEquals(this.loginDetails)
                && ((EmailCommand) other).send == this.send);
    }

    /**
     * For validating if the loginDetails are equal (Testing)
     *
     * @param other to compare with
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
```
###### \java\seedu\address\logic\parser\EmailCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EmailCommand object
 */
public class EmailCommandParser implements Parser<EmailCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EmailCommand
     * and returns an EmailCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMAIL_MESSAGE, PREFIX_EMAIL_SUBJECT, PREFIX_EMAIL_LOGIN,
                        PREFIX_EMAIL_TASK);

        boolean send = false;
        String message = "";
        String subject = "";
        String login = "";
        String task = "";
        String [] loginDetails = new String[0];

        try {

            if (argMultimap.getValue(PREFIX_EMAIL_MESSAGE).isPresent()) {
                message = ParserUtil.parseEmailMessage(argMultimap.getValue(PREFIX_EMAIL_MESSAGE)).trim();
                if (message.isEmpty()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
                }
            }
            if (argMultimap.getValue(PREFIX_EMAIL_SUBJECT).isPresent()) {
                subject = ParserUtil.parseEmailMessage(argMultimap.getValue(PREFIX_EMAIL_SUBJECT)).trim();
                if (subject.isEmpty()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
                }
            }
            if (argMultimap.getValue(PREFIX_EMAIL_LOGIN).isPresent()) {
                login = ParserUtil.parseLoginDetails(argMultimap.getValue(PREFIX_EMAIL_LOGIN)).trim();
                loginDetails = login.split(":");
                if (loginDetails.length != 2) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
                }
            }

            // checks what is the email task, to send or create draft
            if (argMultimap.getValue(PREFIX_EMAIL_TASK).isPresent()) {
                task = ParserUtil.parseEmailTask(argMultimap.getValue(PREFIX_EMAIL_TASK)).trim();
                if (!task.isEmpty() && task.equalsIgnoreCase("send")) {
                    send = true;
                }
            }

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        return new EmailCommand(message, subject, loginDetails, send);
    }
}
```
###### \java\seedu\address\logic\parser\FindCommandParser.java
``` java
    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG, PREFIX_SORT);

        String trimmedArgsName = "";
        String trimmedArgsTag = "";
        int sortOrder = 0;
        String [] nameKeywords = new String[0];
        String [] tagKeywords = new String[0];

        try {
            if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                trimmedArgsName = ParserUtil.parseKeywords(argMultimap.getValue(PREFIX_NAME)).get().trim();
                nameKeywords = trimmedArgsName.split("\\s+");
                if (trimmedArgsName.isEmpty()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
                }
            }
            if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
                trimmedArgsTag = ParserUtil.parseKeywords(argMultimap.getValue(PREFIX_TAG)).get().trim();
                tagKeywords = trimmedArgsTag.split("\\s+");
                if (trimmedArgsTag.isEmpty()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
                }
            }

            if (argMultimap.getValue(PREFIX_SORT).isPresent()) {
                sortOrder = ParserUtil.parseSortOrder(argMultimap.getValue(PREFIX_SORT));
                if (sortOrder < 0) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
                }
            }

            if (trimmedArgsName.isEmpty() && trimmedArgsTag.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords),
                Arrays.asList(tagKeywords)), sortOrder);
    }

}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses {@code String sort} into an {@code int} and returns it.
     * @throws IllegalValueException if the specified sort is invalid
     */
    public static int parseSortOrder(Optional<String> sort) throws IllegalValueException {
        requireNonNull(sort);
        int sortOrder = 0;
        if (sort.isPresent()) {
            switch(sort.get().trim()) {
            case "name":
                sortOrder = 0;
                break;
            case "tag":
                sortOrder = 1;
                break;
            case "email":
                sortOrder = 2;
                break;
            case "address":
                sortOrder = 3;
                break;
            default:
                sortOrder = -1;
                break;
            }
        } else {
            sortOrder = -1;
        }
        return sortOrder;
    }

```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> message} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseEmailMessage(Optional<String> message) throws IllegalValueException {
        requireNonNull(message);
        return message.isPresent() ? message.get() : "";
    }

```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> subject} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseEmailSubject(Optional<String> subject) throws IllegalValueException {
        requireNonNull(subject);
        return subject.isPresent() ? subject.get() : "";
    }

```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> login} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseLoginDetails(Optional<String> loginDetails) throws IllegalValueException {
        requireNonNull(loginDetails);
        return loginDetails.isPresent() ? loginDetails.get() : "";
    }

```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> login} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseEmailTask(Optional<String> task) throws IllegalValueException {
        requireNonNull(task);
        return task.isPresent() ? task.get() : "";
    }
}
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public Email getEmailManager() {
        return email;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException, IOException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(ReadOnlyPerson person) throws DuplicatePersonException, IOException {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized String addImage(EmailAddress email, Photo photo) throws IOException {
        String folder = "data/images/";
        String fileExt = ".jpg";

        File imageFolder = new File(folder);

        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        } else {

        }

        String destination = folder + email.toString() + fileExt;
        Path sourcePath = Paths.get(photo.toString());
        Path destPath = Paths.get(destination);

        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);

        return folder + email.toString() + fileExt;
    }

    @Override
    public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException, IOException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    @Override
    public void deleteTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException, IOException {
        for (int i = 0; i < addressBook.getPersonList().size(); i++) {
            ReadOnlyPerson orginalPerson = addressBook.getPersonList().get(i);

            Person person = new Person(orginalPerson);
            Set<Tag> tags = person.getTags();

            tags.remove(tag);
            person.setTags(tags);

            addressBook.updatePerson(orginalPerson, person);

        }
    }


    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyPerson} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(sortedPersonsList);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    /**
     * @param: int
     * 0 = sort by name ascending
     * 1 = sort by tags ascending
     * 2 = sort by email ascending
     * 3 = sort by address ascending
     * Returns a sorted unmodifable view of the list {@code ReadOnlyPerson} backed by the internal list of
     * {@code addressBook}
     */
    public void sortFilteredPersons(int sortOrder) {

        //sort by name by default
        Comparator<ReadOnlyPerson> sort = new Comparator<ReadOnlyPerson>() {
            @Override
            public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                return o1.getName().fullName.toUpperCase().compareTo(o2.getName().fullName.toUpperCase());
            }
        };

        if (sortOrder == 1) {
            //sort by tags
            sort = new Comparator<ReadOnlyPerson>() {
                @Override
                public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                    TreeSet<Tag> o1SortedTags = new TreeSet<Tag>(o1.getTags());
                    TreeSet<Tag> o2SortedTags = new TreeSet<Tag>(o2.getTags());

                    if (o1SortedTags.size() == 0) {
                        return 1;
                    } else if (o2SortedTags.size() == 0) {
                        return -1;
                    } else {
                        return o1SortedTags.first().tagName.compareTo(o2SortedTags.first().tagName);
                    }
                }
            };
        } else if (sortOrder == 2) {
            //sort by emails
            sort = new Comparator<ReadOnlyPerson>() {
                @Override
                public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                    return o1.getEmailAddress().value.toUpperCase().compareTo(o2.getEmailAddress().value.toUpperCase());
                }
            };
        } else if (sortOrder == 3) {
            //sort by address
            sort = new Comparator<ReadOnlyPerson>() {
                @Override
                public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                    return o1.getAddress().value.toUpperCase().compareTo(o2.getAddress().value.toUpperCase());
                }
            };
        }

        sortedPersonsList.setComparator(sort);
    }

    @Override
    public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void loginEmail(String [] loginDetails) throws EmailLoginInvalidException {
        email.loginEmail(loginDetails);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void sendEmail(MessageDraft message, boolean send) throws EmailLoginInvalidException,
            EmailMessageEmptyException, EmailRecipientsEmptyException, AuthenticationFailedException {
        email.composeEmail(message);

        if (send) {
            email.sendEmail();
        }
        raise(new EmailDraftChangedEvent(email.getEmailDraft()));
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public String getEmailStatus() {
        return email.getEmailStatus();
    }

    @Override
    public boolean equals(Object obj) {

        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && sortedPersonsList.equals(other.sortedPersonsList)
                && email.equals(other.email);
    }

}
```
###### \java\seedu\address\model\person\NameContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code ReadOnlyPerson}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> namekeywords;
    private final List<String> tagkeywords;

    public NameContainsKeywordsPredicate(List<String> namekeywords, List<String> tagkeywords) {
        this.namekeywords = namekeywords;
        this.tagkeywords = tagkeywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {

        if (!namekeywords.isEmpty() && !tagkeywords.isEmpty()) {
            return namekeywords.stream()
                    .anyMatch(keyword -> StringUtil.containsNonFullWordIgnoreCase(person.getName().fullName, keyword)
                            && person.containsTags(tagkeywords));
        } else if (!namekeywords.isEmpty()) {
            return namekeywords.stream()
                    .anyMatch(keyword -> StringUtil.containsNonFullWordIgnoreCase(person.getName().fullName, keyword));
        } else if (!tagkeywords.isEmpty()) {
            return person.containsTags(tagkeywords);
        } else {
            //should not occur at all.
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && this.namekeywords.equals(((NameContainsKeywordsPredicate) other).namekeywords)
                && this.tagkeywords.equals(((NameContainsKeywordsPredicate) other).tagkeywords)); // state check
    }

}
```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Check if this person have the given tags in argument tag set
     */
    @Override
    public boolean containsTags(List<String> tags) {
        for (Tag t : this.tags.get().toSet()) {
            boolean found = tags.stream().anyMatch(tag ->
                    StringUtil.containsNonFullWordIgnoreCase(t.tagName, tag));
            if (found) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyPerson // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyPerson) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, emailAddress, address, photo, tags, birthdate);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * Sort the list in lexicographically name order.
     */
    private void sortInternalList() {
        Comparator<Person> sort = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().fullName.toUpperCase().compareTo(o2.getName().fullName.toUpperCase());
            }
        };
        sortedInternalList.setComparator(sort);
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.sortedInternalList.equals(((UniquePersonList) other).sortedInternalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
```
###### \java\seedu\address\model\tag\Tag.java
``` java
    @Override
    public int compareTo(Tag o) {
        return this.tagName.compareTo(o.tagName);
    }

}
```
###### \java\seedu\address\ui\LeftDisplayPanel.java
``` java
/**
 * Tab Panel containing personListPanel and EmailDraftPanel
 */
public class LeftDisplayPanel extends UiPart<Region> {
    private static final String FXML = "LeftDisplayPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(LeftDisplayPanel.class);

    //Independent UI parts residing in this UI container
    private PersonListPanel personListPanel;
    private MessageDisplay messageDisplay;

    private boolean toggle;

    @FXML
    private TabPane leftDisplayPanel;

    @FXML
    private Tab personListTab;

    @FXML
    private Tab emailDraftTab;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane messageDraftPanelPlaceholder;

    public LeftDisplayPanel(ObservableList<ReadOnlyPerson> personList) {
        super(FXML);

        personListPanel = new PersonListPanel(personList);
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        messageDisplay = new MessageDisplay();
        messageDraftPanelPlaceholder.getChildren().add(messageDisplay.getRoot());

        toggle = true;
    }

    /**
     * Toggle Tabs
     */
    public void toggleTabs() {
        if  (toggle) {
            leftDisplayPanel.getSelectionModel().select(emailDraftTab);
        } else {
            leftDisplayPanel.getSelectionModel().select(personListTab);
        }
        toggle = !toggle;
    }

    /**
     * Scrolls one page down
     */
    public void scrollDown() {
        personListPanel.scrollDown();
    }

    /**
     * Scrolls one page up
     */
    public void scrollUp() {
        personListPanel.scrollUp();
    }

    public PersonListPanel getPersonListPanel() {
        return this.personListPanel;
    }

}
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Calls UI to toggle Tabs
     */
    @FXML
    private void handleToggleTabs() {
        leftDisplayPanel.toggleTabs();
    }

```
###### \java\seedu\address\ui\MessageDisplay.java
``` java
/**
 * A ui for the display of the current email draft
 */
public class MessageDisplay extends UiPart<Region> {
    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "MessageDisplay.fxml";

    private final StringProperty messageDisplay = new SimpleStringProperty("");
    private final StringProperty recipientsDisplay = new SimpleStringProperty("");
    private final StringProperty subjectDisplay = new SimpleStringProperty("");

    @FXML
    private TextArea messageArea;
    @FXML
    private TextArea recipientsArea;
    @FXML
    private TextArea subjectArea;


    public MessageDisplay() {
        super(FXML);

        messageArea.setWrapText(true);
        recipientsArea.setWrapText(true);
        subjectArea.setWrapText(true);

        messageArea.textProperty().bind(messageDisplay);
        recipientsArea.textProperty().bind(recipientsDisplay);
        subjectArea.textProperty().bind(subjectDisplay);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleEmailDraftChangedEvent(EmailDraftChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageDisplay.setValue(event.message.getMessage());
                recipientsDisplay.setValue(event.message.getRecipientsEmailtoString());
                subjectDisplay.setValue(event.message.getSubject());
            }
        });
    }
}
```
###### \resources\view\LeftDisplayPanel.fxml
``` fxml

<?import javafx.scene.control.Tab?>
<?import javafx.scene.control.TabPane?>
<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.VBox?>

<TabPane fx:id="leftDisplayPanel" minWidth="450" prefWidth="450" tabClosingPolicy="UNAVAILABLE" xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">
  <tabs>
    <Tab fx:id="personListTab" text="Contact List">
         <content>
            <VBox fx:id="personList" minWidth="450" prefWidth="450">
               <children>
                  <StackPane fx:id="personListPanelPlaceholder" prefHeight="150.0" prefWidth="200.0" VBox.vgrow="ALWAYS" />
               </children>
            </VBox>
         </content>
    </Tab>
    <Tab fx:id="emailDraftTab" text="Email Draft">
         <content>
            <StackPane fx:id="messageDraftPanelPlaceholder" prefHeight="150.0" prefWidth="200.0" VBox.vgrow="ALWAYS" />
         </content>
    </Tab>
  </tabs>
</TabPane>
```
###### \resources\view\MainWindow.fxml
``` fxml
      <MenuItem fx:id="shortcutMenuToggleTab" mnemonicParsing="false" onAction="#handleToggleTabs" text="Toggle Tabs" />
```
###### \resources\view\MessageDisplay.fxml
``` fxml
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.TextArea?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.text.Font?>

<AnchorPane minWidth="450.0" prefHeight="475.0" prefWidth="450" xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Label layoutX="14.0" layoutY="14.0" prefHeight="27.0" prefWidth="113.0" text="Subject:" styleClass="email-Display"/>
      <Label layoutX="14.0" layoutY="87.0" prefHeight="27.0" prefWidth="127.0" text="Recipients:" styleClass="email-Display"/>
      <Label layoutX="14.0" layoutY="190.0" prefHeight="27.0" prefWidth="103.0" text="Message:" styleClass="email-Display"/>
      <TextArea fx:id="subjectArea" editable="false" layoutX="13.0" layoutY="42.0" prefHeight="43.0" prefWidth="419.0" />
      <TextArea fx:id="recipientsArea" editable="false" layoutX="14.0" layoutY="114.0" prefHeight="74.0" prefWidth="419.0" />
      <TextArea fx:id="messageArea" editable="false" layoutX="14.0" layoutY="220.0" prefHeight="242.0" prefWidth="419.0" />
   </children>
</AnchorPane>
```
