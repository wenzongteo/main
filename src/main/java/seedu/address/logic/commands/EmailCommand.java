package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.email.message.Message;
import seedu.address.model.person.Email;
import seedu.address.model.person.ReadOnlyPerson;

public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String COMMAND_ALIAS = "em";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Emails all contacts in the last displayed list\n"
            + "Parameters: email\n"
            + "Examples: email";

    public static final String MESSAGE_SUCCESS = "Email have been  %1$s";

    private final Message message;
    private final String [] loginDetails;
    private final boolean send;

    public EmailCommand(String message, String subject, String [] loginDetails, boolean send) {
        this.message = new Message(message, subject);
        this.send = send;
        this.loginDetails = loginDetails;
    }

    private ArrayList<Email> extractEmailFromContacts(List<ReadOnlyPerson> lastShownList) {
        ArrayList<Email> recipientsEmail = new ArrayList<Email>(lastShownList.size());
        for(ReadOnlyPerson p: lastShownList) {
            recipientsEmail.add(p.getEmail());
        }
        return recipientsEmail;
    }

    @Override
    public CommandResult execute() {
        requireNonNull(model);

        //Update recipient list based on last displayed list
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        ArrayList<Email> recipientsEmail = extractEmailFromContacts(lastShownList);
        message.setRecipientsEmail(recipientsEmail);

        //Set up Email Details
        model.loginEmail(loginDetails);
        model.sendEmail(message, send);

        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getEmailStatus()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand); // instanceof handles nulls
    }
}
