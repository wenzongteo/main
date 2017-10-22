package seedu.address.email.message;

import java.util.ArrayList;

import seedu.address.model.person.Email;

public class Message implements ReadOnlyMessage {

    private String message;
    private String subject;
    private ArrayList<Email> recipientsEmail;

    public Message() {
        this.message = "";
        this.subject = "";
        this.recipientsEmail = new ArrayList<Email>();
    }

    public Message(String message, String subject) {
        this.message = message;
        this.subject = subject;
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
    public ArrayList<Email> getRecipientsEmails() {
        return this.recipientsEmail;
    }

    public void setRecipientsEmail(ArrayList<Email> recipientsEmail) {
        this.recipientsEmail = recipientsEmail;
    }

    @Override
    public boolean containsContent() {
        if(this.message.isEmpty() || this.subject.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyMessage // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyMessage) other));
    }
}
