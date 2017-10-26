package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.email.message.MessageDraft;

/**
 * Indicates when the email draft changed
 */
public class EmailDraftChangedEvent extends BaseEvent {

    public final MessageDraft message;

    public EmailDraftChangedEvent(MessageDraft message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
