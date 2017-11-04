package seedu.address.email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.email.exceptions.EmailLoginInvalidException;

//@@author awarenessxz
/**
 * Handles how user logs into email
 */
public class EmailLogin {
    private static final Pattern GMAIL_FORMAT = Pattern.compile("^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(@gmail.com)$");

    private String [] loginDetails;

    public EmailLogin() {
        this.loginDetails = new String[0];
    }

    /**
     * Saves user login details
     * @param loginDetails
     */
    public void loginEmail(String [] loginDetails) throws EmailLoginInvalidException {
        //replace login details and ignore if login details is omitted.
        if (loginDetails.length != 0 && loginDetails.length == 2) {
            if (wrongUserEmailFormat(loginDetails)) {
                throw new EmailLoginInvalidException();
            } else {
                this.loginDetails = loginDetails;
            }
        }
    }

    /**
     * Checks if user's login details have been stored
     * @return boolean
     */
    public boolean isUserLogin() {
        if (this.loginDetails.length != 2) {
            //The loginDetails empty
            return false;
        } else {
            return true;
        }
    }

    /**
     * Verifies if the user is using a gmail account
     * @return true if gmail account, false for everything else
     */
    private boolean wrongUserEmailFormat(String [] loginDetails) {
        if (loginDetails.length == 2) {
            final Matcher matcher = GMAIL_FORMAT.matcher(loginDetails[0].trim());
            if (!matcher.matches()) {
                return true;
            }
        }
        return false;
    }


    public String getEmailLogin() {
        if (this.loginDetails.length == 2) {
            return this.loginDetails[0];
        } else {
            return "";
        }
    }

    public String getPassword() {
        if (this.loginDetails.length == 2) {
            return this.loginDetails[1];
        } else {
            return "";
        }
    }

    public void resetData() {
        this.loginDetails = new String[0];
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailLogin // instanceof handles nulls
                && this.loginDetailsEquals(((EmailLogin) other).loginDetails));
    }

    /**
     * Verifies that the loginDetails are equal
     * @param other
     * @return
     */
    private boolean loginDetailsEquals(String [] other) {
        if (this.loginDetails.length == other.length) {
            for (int i = 0; i < this.loginDetails.length; i++) {
                if (this.loginDetails[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
