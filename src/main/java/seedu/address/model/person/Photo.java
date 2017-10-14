package seedu.address.model.person;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's display picture in the address book
 * Guarantees: immutable; is valid as declared in (@Link #isValidImage(String))
 */
public class Photo {
    public static final String MESSAGE_PHOTO_CONSTRAINTS =
            "Person's photo should be in jpeg and be of 340px x 453px dimension";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String PHOTO_VALIDATION_REGEX = "[^\\s].*"; //Yet to update

    public final String value;

    /**
     * Validates given photo.
     *
     * @throws IllegalValueException if given photo string is invalid.
     */
    public Photo(String address) throws IllegalValueException {
        if (address == null) {
            address = "data/default.jpeg"; //Give a default profile picture
        } else {
            address = address.trim();
        }
        /*if (!isValidPhoto(address)) {
            throw new IllegalValueException(MESSAGE_PHOTO_CONSTRAINTS);
        }
        */
        this.value = address;
    }

    /**
     * Returns true if a given string is a valid person email.
     */
    public static boolean isValidPhoto(String test) {
        return test.matches(PHOTO_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Address // instanceof handles nulls
                && this.value.equals(((Address) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
