package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "f";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names or tag contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: "
            + "[ " + PREFIX_NAME + "KEYWORD [MORE_KEYWORDS]... ] [ " + PREFIX_TAG + "KEYWORD [MORE_KEYWORDS]... ]\n"
            + "Examples:\n"
            + "1) " + COMMAND_WORD + " " + PREFIX_NAME + "alice bob charlie\n"
            + "2) " + COMMAND_WORD + " " + PREFIX_TAG + " tag1 tag2 tag3\n"
            + "3) " + COMMAND_WORD + " " + PREFIX_NAME + "alice bob charlie " + PREFIX_TAG + "tag1 tag2 tag3";

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
