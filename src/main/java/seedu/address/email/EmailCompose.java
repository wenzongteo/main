package seedu.address.email;

import seedu.address.email.message.DraftList;
import seedu.address.email.message.MessageDraft;

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

    public MessageDraft getMessage() {
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
