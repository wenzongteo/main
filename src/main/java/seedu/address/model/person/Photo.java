package seedu.address.model.person;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;

//@@author wenzongteo
/**
 * Represents a Person's display picture in the address book
 * Guarantees: immutable; is valid as declared in (@Link #isValidImage(String))
 */
public class Photo {
    public static final String MESSAGE_PHOTO_CONSTRAINTS =
            "Person's photo end in .jpeg or .jpg and preferred to be of 340px x 453px dimension";
    public static final String MESSAGE_PHOTO_NOT_FOUND = "Photo does not exist. If the photo is in the system, please "
            + "give the absolute path of the photo. If the photo is from the internet, please ensure that the url "
            + "starts with http or https and ends with .jpeg or .jpg";
    public static final String MESSAGE_IMPROPER_URL = "URL entered should lead to a valid .jpg or .jpeg image, "
            + "start with either http or https and end with .jpeg or .jpg";
    /**
     * Can contain multiple words but must end with .jpg or .jpeg
     */
    public static final String PHOTO_VALIDATION_REGEX = "([^\\s]+[\\s\\w]*(\\.(?i)(jpg|jpeg|))$)";

    /**
     * must start with http or https and must end with .jpeg or .jpg
     */
    public static final String URL_REGEX = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static final String DEFAULT_PHOTO = "data\\images\\default.jpeg";
    public static final String UNFILLED = "-";
    public static final String TEMP_STORAGE = "data/downloaded.jpg";
    public static final String HASHING_ALGO = "MD5";
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public final String value;
    private final String hash;

    /**
     * Validates given photo.
     * @throws IllegalValueException if given photo string is invalid or file is not found.
     */
    public Photo(String photo) throws IllegalValueException {
        photo = photo.trim();

        if (photo.equals(UNFILLED)) {
            photo = DEFAULT_PHOTO;
        }

        if (!isValidPhoto(photo)) {
            throw new IllegalValueException(MESSAGE_PHOTO_CONSTRAINTS);
        } else {
            this.value = checkPhotoAvailability(photo);
            File image = new File(this.value);
            try {
                this.hash = generateHash(image);
            } catch (NoSuchAlgorithmException | IOException e) {
                throw new AssertionError("Impossible to reach here");
            }
        }
    }

    /**
     * Photo entered by the app that does not require validation as Image should already exist.
     * If Photo entered by the app does not exist, default image will used instead.
     * @param photo path to the image stored.
     * @param num used for overloading constructor.
     */
    public Photo(String photo, int num) {
        photo = photo.trim();
        File image = new File(photo);
        try {
            if (!FileUtil.isFileExists(image)) {
                logger.info("Contact's photo does not exist, using default photo instead");
                copyDefaultPhoto(photo);
            }
            this.value = photo;
            this.hash = generateHash(image);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new AssertionError("Should not enter here");
        }
    }

    /**
     * Check if photo specified by the user is from the internet or local storage.
     * @param photo path of the photo inputted by the user.
     * @return photo path of the photo for the contact.
     * @throws IllegalValueException if photo does not exist in the system or system face read / write issues.
     */
    private String checkPhotoAvailability(String photo) throws IllegalValueException {
        if (isUrl(photo)) {
            return downloadFromInternet(photo);
        } else {
            return checkIfPhotoExist(photo);
        }
    }

    /**
     * Check if photo for a contact exist
     * @param photo file path of the photo in the system.
     * @return the file path of the photo in the system.
     * @throws IllegalValueException If the photo does not exist in the system.
     */
    private String checkIfPhotoExist (String photo) throws IllegalValueException {
        File image = new File(photo);
        if (!FileUtil.isFileExists(image)) {
            throw new IllegalValueException(MESSAGE_PHOTO_NOT_FOUND);
        }
        return photo;
    }

    /**
     * Assign the default photo for contact a contact if the contact's original photo cannot be found on the system.
     * @param photo File path of the contact's photo
     * @throws IOException If the default photo cannot be found.
     */
    private void copyDefaultPhoto (String photo) throws IOException {
        Files.copy(Paths.get(DEFAULT_PHOTO), Paths.get(photo), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     *  @return the generated MD5 hash of the image.
     *  @throws IOException if the file does not exist.
     *  @throws NoSuchAlgorithmException if the algorithm does not exist.
     */
    public String generateHash(File photo) throws IOException, NoSuchAlgorithmException {
        MessageDigest hashing = MessageDigest.getInstance(HASHING_ALGO);
        return new String(hashing.digest(Files.readAllBytes(photo.toPath())));
    }

    /**
     * @return true if a given string is a valid photo url.
     */
    public static boolean isUrl(String test) {
        return test.matches(URL_REGEX);
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

    /**
     * Downloads photo from the link given onto the computer and store it locally.
     * @param photo URL link given by the user
     * @return the temporary storage location of the downloaded image.
     * @throws IllegalValueException if errors are faced when writing file onto local drive.
     */
    private String downloadFromInternet(String photo) throws IllegalValueException {
        logger.info("Attempting to download photo from the internet");
        try {
            URL url = new URL(photo);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(TEMP_STORAGE);
            byte[] buffer = new byte[4096];

            int length = 0;

            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }

            is.close();
            os.close();
            logger.info("Download complete");
            return TEMP_STORAGE;
        } catch (MalformedURLException mue) {
            throw new IllegalValueException(MESSAGE_IMPROPER_URL);
        } catch (IOException ioe) {
            throw new AssertionError("Read / Write issue");
        }
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
