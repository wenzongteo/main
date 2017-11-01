# wenzongteo
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @BeforeClass
    public static void setup() {
        String imageFilePath = "data/images/";
        File imageFolder = new File(imageFilePath);

        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        } else {

        }

        try {
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/amy@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/bob@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new AssertionError("Nope");
        }
    }

```
###### \java\seedu\address\model\person\PhotoTest.java
``` java
public class PhotoTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void isValidPhoto() {
        // invalid photos
        assertFalse(Photo.isValidPhoto("")); // empty string
        assertFalse(Photo.isValidPhoto(" ")); // spaces only
        assertFalse(Photo.isValidPhoto("91")); // digits only
        assertFalse(Photo.isValidPhoto("Photo")); // alphabet only
        assertFalse(Photo.isValidPhoto("9011p041")); // alphabets within digits
        assertFalse(Photo.isValidPhoto("9312 1534")); // spaces between digits
        assertFalse(Photo.isValidPhoto("abc abc")); // spaces between words
        assertFalse(Photo.isValidPhoto("data/default")); //No file type.
        assertFalse(Photo.isValidPhoto("data/default.mp3")); //Invalid file type
        assertFalse(Photo.isValidPhoto(".jpeg")); //No file name

        // valid Photo
        assertTrue(Photo.isValidPhoto("docs/images/default.jpeg")); // end with .jpeg file extension
        assertTrue(Photo.isValidPhoto("docs/images/ritchie.jpg")); // end with .jpg file extension
        assertTrue(Photo.isValidPhoto("docs/images/yl_coder.jpg")); // file name with special character _
        assertTrue(Photo.isValidPhoto("docs/images/m133225.jpg")); // file name with digits
        assertTrue(Photo.isValidPhoto("docs/images/prof damith.jpg")); //file name with space in between
    }

    @Test
    public void photoDoesNotExist() {
        try {
            new Photo("doesnotexist.jpg");
        } catch (IllegalValueException ioe) {
            assertEquals("Error! Photo does not exist!", ioe.getMessage());
        }
        try {
            new Photo("doesnotexist.jpg", 0);
        } catch (AssertionError ae) {
            assertEquals("Image should already exist", ae.getMessage());
        }
    }

    @Test
    public void photoFailCheck() {
        try {
            new Photo("wrong format");
        } catch (IllegalValueException ive) {
            assertEquals("Person's photo should be in jpeg and preferred to be of 340px x 453px dimension",
                    ive.getMessage());
        }
    }

    @Test
    public void photoHashCode() {
        try {
            Photo photo1 = new Photo("default.jpeg", 0);
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
            Photo photo1 = new Photo("data/images/default.jpeg");
            Photo photo2 = new Photo("docs/images/wz.jpg", 0);

            //Compare hash of the same photo --> return true
            assertTrue(photo1.getHash().equals(photo1.getHash()));

            //Compare hash of different photos --> return false
            assertFalse(photo1.getHash().equals(photo2.getHash()));

        } catch (IllegalValueException ive) {
            assert false : "Not possible";
        }
    }

    @Test
    public void compareString() {
        try {
            Photo photo1 = new Photo("data/images/default.jpeg");
            Photo photo2 = new Photo("docs/images/wz.jpg", 0);

            //Compare String value of the same photo --> return true
            assertTrue(photo1.toString().equals(photo1.toString()));

            //Compare String value of different photos --> return false
            assertFalse(photo1.toString().equals(photo2.toString()));
        } catch (IllegalValueException ive) {
            assert false : "Not possible";
        }
    }

    @Test
    public void equals() {
        try {
            Photo photo1 = new Photo("data/images/default.jpeg");
            Photo photo2 = new Photo("docs/images/wz.jpg", 0);

            //Compare String value of the same photo --> return true
            assertTrue(photo1.equals(photo1));

            //Compare String value of different photos --> return false
            assertFalse(photo1.equals(photo2));
        } catch (IllegalValueException ive) {
            assert false : "Not possible";
        }
    }
}
```
###### \java\seedu\address\TestApp.java
``` java
    /**
     * Check if the necessary folder and images to ensure the test is successful exist.
     */
    private void initImages() {
        String imageFilePath = "data/images/";
        File imageFolder = new File(imageFilePath);

        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        } else {

        }

        try {
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/johnd@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/heinz@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/cornelia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/werner@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/lydia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/anna@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/bob@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/amy@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/stefan@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/hans@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AssertionError("Impossible");
        }
    }
}
```
###### \java\seedu\address\testutil\TypicalPersons.java
``` java
    /**
     *  Initialize all photos to ensure the photo is available before certain tests.
     */
    public static void initializePictures() {
        try {
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/johnd@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/heinz@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/anna@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/stefan@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/hans@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/amy@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/johnd@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/heinz@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/cornelia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/werner@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/lydia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/anna@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Preprocess failed");
        }
    }
}
```
###### \java\systemtests\EditCommandSystemTest.java
``` java
    @BeforeClass
    public static void setup() throws Exception {
        String imageFilePath = "data/images/";
        File imageFolder = new File(imageFilePath);

        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        } else {

        }

        try {
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/johnd@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/heinz@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/cornelia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/werner@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/lydia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/anna@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/stefan@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/hans@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/amy@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/cornelia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/werner@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/lydia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AssertionError("Impossible");
        }
    }

```
