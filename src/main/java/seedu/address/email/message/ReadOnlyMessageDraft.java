package seedu.address.email.message;

import javax.mail.internet.InternetAddress;

/**
 * A read-only immutable interface for a email message in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyMessageDraft {
    String getMessage();
    String getSubject();
    InternetAddress [] getRecipientsEmails();

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
