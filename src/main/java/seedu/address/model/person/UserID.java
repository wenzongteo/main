package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author hengyu95
/**
 * Represents a Person's Instagram account name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUserID(String)}
 */
public class UserID {

    public static final String MESSAGE_USERID_CONSTRAINTS = "ID should contain only letters and numbers.";

    public static final String USERNAME_VALIDATION_REGEX = "(^[a-zA-Z0-9_]*$)";

    public final String value;

    public UserID(String id) throws IllegalValueException {
        requireNonNull(id);
        String trimmedID = id.trim();

        if (!trimmedID.equals("-")) {
            if (!isValidUserID(trimmedID)) {
                throw new IllegalValueException(MESSAGE_USERID_CONSTRAINTS);
            }
        }

        this.value = trimmedID;
    }

    /**
     * Returns true if a given string is a valid person phone number.
     */

    public static boolean isValidUserID(String test) {
        return test.matches(USERNAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UserID // instanceof handles nulls
                && this.value.equals(((UserID) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}






