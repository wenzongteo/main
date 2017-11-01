package seedu.address.model.person;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;

//@@author wenzongteo
/**
 * Represents a Person's display picture in the address book
 * Guarantees: immutable; is valid as declared in (@Link #isValidImage(String))
 */
public class Photo {
    public static final String MESSAGE_PHOTO_CONSTRAINTS =
            "Person's photo should be in jpeg and preferred to be of 340px x 453px dimension";
    public static final String MESSAGE_PHOTO_NOT_FOUND = "Error! Photo does not exist!";

    /**
     * Can contain multiple words but must end with .jpg or .jpeg
     */
    public static final String PHOTO_VALIDATION_REGEX = "([^\\s]+[\\s\\w]*(\\.(?i)(jpg|jpeg|))$)";

    public final String value;
    private String hash;
    /**
     * Validates given photo.
     * @throws IllegalValueException if given photo string is invalid or file is not found.
     */
    public Photo(String photo) throws IllegalValueException {
        photo = photo.trim();

        if (!isValidPhoto(photo)) {
            throw new IllegalValueException(MESSAGE_PHOTO_CONSTRAINTS);
        } else {
            File image = new File(photo);
            if (!FileUtil.isFileExists(image)) {
                throw new IllegalValueException(MESSAGE_PHOTO_NOT_FOUND);
            } else {
                this.value = photo;
                try {
                    this.hash = generateHash(image);
                } catch (NoSuchAlgorithmException | IOException e) {
                    throw new AssertionError("Impossible to reach here");
                }
            }
        }
    }

    /**
     * Photo entered by the app that does not require validation as Image should already exist.
     * @param photo path to the image stored.
     * @param num used for overloading constructor.
     */
    public Photo(String photo, int num) {
        this.value = photo;
        MessageDigest hashing;
        try {
            File image = new File(photo);
            this.hash = generateHash(image);
        } catch (NoSuchAlgorithmException nsa) {
            throw new AssertionError("Algorithm should exist");
        } catch (IOException ioe) {
            throw new AssertionError("Image should already exist");
        }
    }

    /**
     *  @return the generated hash of the image.
     *  @throws IOException if the file does not exist.
     *  @throws NoSuchAlgorithmException if the algorithm does not exist.
     */
    public String generateHash(File photo) throws IOException, NoSuchAlgorithmException {
        MessageDigest hashing = MessageDigest.getInstance("MD5");
        return new String(hashing.digest(Files.readAllBytes(photo.toPath())));
    }

    /**
     * @return true if a given string is a valid person photo.
     */
    public static boolean isValidPhoto(String test) {
        return test.matches(PHOTO_VALIDATION_REGEX);
    }

    /**
     * @return the hash of the current image.
     */
    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Photo // instanceof handles nulls
                && this.value.equals(((Photo) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
