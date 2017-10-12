package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Birthdate {

    public static final String MESSAGE_BIRTHDATE_CONSTRAINTS =
            "Person birthdate can take any values, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String BIRTHDATE_VALIDATION_REGEX = "[^\\s].*";

    public static final String BIRTHDATE_VALIDATION_REGEX2 = "([0]?[1-9]|[1|2][0-9]|[3][0|1])[./-]([0]?[1-9]|[1]" +
            "[0-2])[./-]([0-9]{4}|[0-9]{2})";        // dd/mm/yyyy or d/m/yy or d.m.yyyy with separators: . - /

    public static final String BIRTHDATE_VALIDATION_REGEX3 = "([0]?[1-9]|[1|2][0-9]|[3][0|1])[./-]([0]?[1-9]|[1][0-2])" +
            "[./-])";        // dd/mm or d/m or d.m with separators: . - /

    public final String value;

    /**
     * Validates given address.
     *
     * @throws IllegalValueException if given address string is invalid.
     */
    public Birthdate(String birthdate) throws IllegalValueException {
//        requireNonNull(birthdate);
        if (!isValidBirthdate(birthdate)) {
            throw new IllegalValueException(MESSAGE_BIRTHDATE_CONSTRAINTS);
        }
        this.value = birthdate;
    }

    /**
     * Returns true if a given string is a valid person email.
     */
    public static boolean isValidBirthdate(String test) {
        return ((test.matches(BIRTHDATE_VALIDATION_REGEX)) && (
                (test.matches(BIRTHDATE_VALIDATION_REGEX2)) ||
                (test.matches(BIRTHDATE_VALIDATION_REGEX3))));
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Birthdate // instanceof handles nulls
                && this.value.equals(((Birthdate) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
