package seedu.address.email.message;

import java.util.ArrayList;

import seedu.address.model.person.Email;

/**
 * A read-only immutable interface for a email message in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyMessage {
    String getMessage();
    String getSubject();
    ArrayList<Email> getRecipientsEmails();

    boolean containsContent();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyMessage other) {

        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getMessage().equals(this.getMessage()) // state checks here onwards
                && other.getSubject().equals(this.getSubject())
                && other.getRecipientsEmails().equals(this.getRecipientsEmails()));
    }
}
