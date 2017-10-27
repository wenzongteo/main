package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class PhotoTest {

    @Test
    public void isValidPhoto() {
        // invalid photos
        assertFalse(Photo.isValidPhoto("")); // empty string
        assertFalse(Photo.isValidPhoto(" ")); // spaces only
        assertFalse(Photo.isValidPhoto("91")); // digits only
        assertFalse(Photo.isValidPhoto("Photo")); // alphabet only
        assertFalse(Photo.isValidPhoto("9011p041")); // alphabets within digits
        assertFalse(Photo.isValidPhoto("9312 1534")); // spaces within digits
        assertFalse(Photo.isValidPhoto("124293842033123")); // long Photo numbers
        assertFalse(Photo.isValidPhoto("12429384")); // Exactly 8 numbers but does not start with 6,8 or 9
        assertFalse(Photo.isValidPhoto("data/default")); //No file type.
        assertFalse(Photo.isValidPhoto("data/default.mp3")); //Invalid file type
        assertFalse(Photo.isValidPhoto(".jpeg")); //No file name

        // valid Photo
        assertTrue(Photo.isValidPhoto("docs/images/default.jpeg")); // end with .jpeg file extension
        assertTrue(Photo.isValidPhoto("docs/images/yl_coder.jpg")); // end with .jpg file extension
        assertTrue(Photo.isValidPhoto("docs/images/yl_coder.jpg")); // file name with special character _
        assertTrue(Photo.isValidPhoto("docs/images/prof damith.jpg")); //file name with space in between
    }

    @Test
    public void photoHashCode() {
        try {
            Photo photo1 = new Photo("default.jpeg",0);
            Photo photo2 = new Photo("data/images/alice@example.com.jpg");

            //hashcode matches for same email --> return true
            assertTrue(photo1.hashCode() == photo1.hashCode());

            //hashcode don't match for different email --> return false
            assertFalse(photo1.hashCode() == photo2.hashCode());

        } catch (IllegalValueException e) {
            assert false : "shouldn't hit this case at all";
        }
    }

    @Test
    public void compareHash() {
        try {
            Photo photo1 = new Photo("default.jpeg",0);
            Photo photo2 = new Photo("docs/images/wz.jpg");

            //Compare hash of the same photo --> return true
            assertTrue(photo1.getHash().equals(photo1.getHash()));

            //Compare hash of different photos --> return false
            assertFalse(photo1.getHash().equals(photo2.getHash()));

        } catch (IllegalValueException e) {
            assert false : "Not possible";
        }
    }
}
