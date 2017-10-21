package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String COMMAND_ALIAS = "em";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Emails all contacts in the last displayed list\n"
            + "Parameters: email\n"
            + "Examples: email";

    public static final String MESSAGE_SUCCESS = "Email have been drafted";

    private final boolean send;
    private final String message;
    private final String subject;
    private final String [] loginDetails;

    public EmailCommand(String message, String subject, String [] loginDetails, boolean send) {
        this.loginDetails = loginDetails;
        this.message = message;
        this.send = send;
        this.subject = subject;
    }

    @Override
    public CommandResult execute() {
        requireNonNull(model);
        model.sendEmail(message, subject, loginDetails, send);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand); // instanceof handles nulls
    }
}
