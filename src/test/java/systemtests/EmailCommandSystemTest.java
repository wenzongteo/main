package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_LOGIN;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_SUBJECT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_LOGIN;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_LOGIN_LENGTH;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_SUBJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_JOHN_EMAILTESTER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_LOGIN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SUBJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_JOHN_EMAILTESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.mail.AuthenticationFailedException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.model.Model;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.testutil.PersonBuilder;

public class EmailCommandSystemTest extends AddressBookSystemTest {

    private static final String EMAIL_SUCCESSFULLY_DRAFTED = "drafted";
    private static final String EMAIL_COMMAND_SEND = " et/send";

    @BeforeClass
    public static void setup() throws Exception {
        String imageFilePath = "data/images/";
        File imageFolder = new File(imageFilePath);

        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        } else {

        }

        try {
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/johnd@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/heinz@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/anna@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/stefan@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/hans@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/amy@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/cornelia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/werner@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/lydia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/bob@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AssertionError("Impossible");
        }
    }

    @Test
    public void sendEmail() throws Exception {
        String command = "";
        MessageDraft message = new MessageDraft();
        String [] loginDetails = new String[0];

        /**
         * Case: Email Command to send email with invalid message --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + INVALID_EMAIL_MESSAGE + EMAIL_COMMAND_SEND;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));

        /**
         * Case: Email Command to send email with empty subject --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + INVALID_EMAIL_SUBJECT + EMAIL_COMMAND_SEND;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));

        /**
         * Case: Email Command to send email with empty message (no message and subject) --> invalid
         */
        command = EmailCommand.COMMAND_WORD + EMAIL_COMMAND_SEND;
        assertCommandFailure(command, EmailCommand.MESSAGE_EMPTY_INVALID);

        /**
         * Case: Email Command to send email with invalid login parameters --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + INVALID_EMAIL_LOGIN_LENGTH + EMAIL_COMMAND_SEND;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));

        /**
         * Case: Email Command to send email with a non gmail account --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + EMAIL_MESSAGE + EMAIL_SUBJECT + INVALID_EMAIL_LOGIN + EMAIL_COMMAND_SEND;
        assertCommandFailure(command, EmailCommand.MESSAGE_LOGIN_INVALID);

        /**
         * Case: Email Commmand to draft email without any parameters --> success
         **/
        command = EmailCommand.COMMAND_WORD;
        assertCommandSucess(command, message, loginDetails, false, EMAIL_SUCCESSFULLY_DRAFTED);

        /**
         * Case: Email Commmand to draft email with only message parameter --> success
         **/
        command = EmailCommand.COMMAND_WORD + EMAIL_MESSAGE;
        message = new MessageDraft(VALID_EMAIL_MESSAGE, "");
        assertCommandSucess(command, message, loginDetails, false, EMAIL_SUCCESSFULLY_DRAFTED);

        /**
         * Case: Email Commmand to draft email with empty message parameter --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + INVALID_EMAIL_MESSAGE;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));

        /**
         * Case: Email Commmand to draft email with only subject parameter --> success
         **/
        command = EmailCommand.COMMAND_WORD + EMAIL_SUBJECT;
        message = new MessageDraft("", VALID_EMAIL_SUBJECT);
        assertCommandSucess(command, message, loginDetails, false, EMAIL_SUCCESSFULLY_DRAFTED);

        /**
         * Case: Email Commmand to draft email with empty subject parameter --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + INVALID_EMAIL_SUBJECT;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));


        /**
         * Case: Email Commmand to draft email with login parameter --> success
         **/
        command = EmailCommand.COMMAND_WORD + EMAIL_LOGIN;
        message = new MessageDraft();
        loginDetails = VALID_EMAIL_LOGIN.split(":");
        assertCommandSucess(command, message, loginDetails, false, EMAIL_SUCCESSFULLY_DRAFTED);

        /**
         * Case: Email Commmand to draft email with invalid login parameter --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + INVALID_EMAIL_LOGIN_LENGTH;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));

        /**
         * Case: Email Command to draft email with a non gmail account --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + INVALID_EMAIL_LOGIN;
        assertCommandFailure(command, EmailCommand.MESSAGE_LOGIN_INVALID);

        /**
         * Case: Email Command to send email with an empty last display list --> invalid
         **/
        command = EmailCommand.COMMAND_WORD + EMAIL_MESSAGE + EMAIL_SUBJECT + EMAIL_LOGIN + EMAIL_COMMAND_SEND;
        assertCommandFailureEmptyList(command, EmailCommand.MESSAGE_RECIPIENT_INVALID);
    }

    private void setupModel(Model expectedModel) throws Exception {
        /**
         *  Set up Model such the list is empty
         */
        ReadOnlyPerson toAdd = new PersonBuilder().withName(VALID_NAME_JOHN_EMAILTESTER)
                .withEmailAddress(VALID_EMAIL_JOHN_EMAILTESTER).build();
        String findCommand = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + VALID_NAME_JOHN_EMAILTESTER;
        ModelHelper.setFilteredList(expectedModel, toAdd);
        executeCommand(findCommand);
    }

    /**
     * Executes the {@code EmailCommand} that sends or draft an email {@code message} and verifies the command box
     * displays the email status results of executing the {@code EmailCommand} and the model related components equal
     * to the current model with the send command composed with email message {@code message}. These verifications are
     * done by {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String,String,Model)}.<br>
     * Also verifies that the command box has the default style class, the status bar's sync status changes, the
     * browser url and selected card remains unchanged.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model).
     */
    private void assertCommandSucess(String command, MessageDraft message, String [] loginDetails,
                                     boolean send, String status) throws Exception {
        Model expectedModel = getModel();

        try {
            //Set up Email Details
            expectedModel.loginEmail(loginDetails);
            expectedModel.sendEmail(message, send);
        } catch (EmailLoginInvalidException e) {
            throw new IllegalArgumentException(EmailCommand.MESSAGE_LOGIN_INVALID);
        } catch (EmailMessageEmptyException e) {
            throw new IllegalArgumentException(EmailCommand.MESSAGE_EMPTY_INVALID);
        } catch (EmailRecipientsEmptyException e) {
            throw new IllegalArgumentException(EmailCommand.MESSAGE_RECIPIENT_INVALID);
        } catch (AuthenticationFailedException e) {
            throw new IllegalArgumentException(EmailCommand.MESSAGE_AUTHENTICATION_FAIL);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(EmailCommand.MESSAGE_FAIL_UNKNOWN);
        }
        String expectedResultMessage = String.format(EmailCommand.MESSAGE_SUCCESS, status);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Message, String [], boolean)} except
     * that the result display box displays {@code expectedResultMessage} and the model related components equal to
     * {@code expectedModel}.
     * @see EmailCommandSystemTest#
     * assertCommandSuccess(String, MessageDraft, String [], boolean)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) throws Exception {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, String)} except
     * that the displayed person list is empty.
     * @see EmailCommandSystemTest#
     * assertCommandSuccess(String, String)
     */
    private void assertCommandFailureEmptyList(String command, String expectedResultMessage) throws Exception {
        Model expectedModel = getModel();
        setupModel(expectedModel);

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
