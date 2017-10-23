package seedu.address.email.message;

import javax.mail.internet.InternetAddress;

public class MessageDraft implements ReadOnlyMessageDraft {

    private String message;
    private String subject;
    private InternetAddress [] recipientsEmail;

    public MessageDraft() {
        this.message = "";
        this.subject = "";
        this.recipientsEmail = new InternetAddress[0];
    }

    public MessageDraft(String message, String subject) {
        this.message = message;
        this.subject = subject;
        this.recipientsEmail = new InternetAddress[0];
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
    public InternetAddress[] getRecipientsEmails() {
        return this.recipientsEmail;
    }

    public void setRecipientsEmail(InternetAddress[] recipientsEmail) {
        this.recipientsEmail = new InternetAddress[recipientsEmail.length];
        System.arraycopy(recipientsEmail,0,this.recipientsEmail,0, recipientsEmail.length);
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
                || (other instanceof ReadOnlyMessageDraft // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyMessageDraft) other));
    }

    @Override
    public boolean recipientsEquals(InternetAddress [] other) {
        if (other.length == this.recipientsEmail.length) {
            for (int i = 0; i < recipientsEmail.length; i++) {
                if(other[i] != this.recipientsEmail[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
