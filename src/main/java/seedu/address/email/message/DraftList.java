package seedu.address.email.message;

//@@author awarenessxz
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
        assert false; //should never reach this part of return statement
        return false;
    }
}
