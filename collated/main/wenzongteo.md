# wenzongteo
###### \java\seedu\address\logic\parser\AddCommandParser.java
``` java
    /**
     * Check if the user has input any value for this variable before sending the input for further parsing.
     *
     * @param userInput Input entered by the user that is parsed by argMultimap.
     * @return the value of the input entered by the user or '-' if no input was entered.
     */
    private static Optional<String> checkInput(Optional<String> userInput) {
        return Optional.of(userInput.orElse("-"));
    }
}
```
###### \java\seedu\address\MainApp.java
``` java
    /**
     * Check if data/images and data/edited folders exist. If they do not exist, create them.
     */
    private void checkIfFilesExist() {
        String imagesFolderPath = "data/images";
        String editedPhotoPath = "data/edited";

        File imagesFolder = new File(imagesFolderPath);
        File editedFolder = new File(editedPhotoPath);

        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
            logger.info("Image storage location does not exist. Will be creating 'data/images' folder");
        } else {

        }

        if (!editedFolder.exists()) {
            editedFolder.mkdirs();
            logger.info("Temporary image storage does not exist. Will be creating 'data/edited' folder");
        } else {

        }
    }

    /**
     * Deletes all existing images in data/edited folder first before deleting the folder itself.
     */
    private void removeFiles() {
        File toBeDeletedFolder = new File("data/edited");
        File[] toBeDeletedImages = toBeDeletedFolder.listFiles();
        if (toBeDeletedImages != null) {
            for (File f : toBeDeletedImages) {
                f.delete();
            }
        }
        toBeDeletedFolder.delete();
    }

    /**
     * Copy the default.jpeg, which is used for the default image, into the /data/images/ folder.
     */
    private void checkDefaultImage() {
        try {
            InputStream is = this.getClass().getResourceAsStream("/images/default.jpeg");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            File targetFile = new File("data/images/default.jpeg");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);

        } catch (IOException ioe) {
            throw new AssertionError("Impossible");
        } catch (Exception e) {
            throw new AssertionError("No other exceptions possible");
        }
    }
}
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized String addImage(EmailAddress email, Photo photo) throws IOException {
        String folder = "data/images/";
        String fileExt = ".jpg";

        File imageFolder = new File(folder);

        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        } else {

        }

        String destination = folder + email.toString() + fileExt;
        Path sourcePath = Paths.get(photo.toString());
        Path destPath = Paths.get(destination);

        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);

        return folder + email.toString() + fileExt;
    }
```
###### \java\seedu\address\model\person\Photo.java
``` java
/**
 * Represents a Person's display picture in the address book
 * Guarantees: immutable; is valid as declared in (@Link #isValidImage(String))
 */
public class Photo {
    public static final String MESSAGE_PHOTO_CONSTRAINTS =
            "Person's photo should be in jpeg and preferred to be of 340px x 453px dimension";
    public static final String MESSAGE_PHOTO_NOT_FOUND = "Error! Photo does not exist!";
    public static final String MESSAGE_LINK_ERROR = "Error! URL given is invalid!";

    /**
     * Can contain multiple words but must end with .jpg or .jpeg
     */
    public static final String PHOTO_VALIDATION_REGEX = "([^\\s]+[\\s\\w]*(\\.(?i)(jpg|jpeg|))$)";
    public static final String URL_REGEX = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String DEFAULT_PHOTO = "data/images/default.jpeg";
    public static final String tempStorage = "data/temporary.jpg";

    public final String value;
    private final String hash;

    /**
     * Validates given photo.
     * @throws IllegalValueException if given photo string is invalid or file is not found.
     */
    public Photo(String photo) throws IllegalValueException {
        photo = photo.trim();

        if (photo.equals("-")) {
            photo = "data/images/default.jpeg";
        }

        if (!isValidPhoto(photo)) {
            throw new IllegalValueException(MESSAGE_PHOTO_CONSTRAINTS);
        } else {
            if (photo.matches(URL_REGEX)) {
                this.value = downloadFromInternet(photo);
            } else {
                File image = new File(photo);
                if (!FileUtil.isFileExists(image)) {
                    throw new IllegalValueException(MESSAGE_PHOTO_NOT_FOUND);
                } else {
                    this.value = photo;
                }
            }
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
        File image = new File(photo);
        this.value = photo;

        try {
            if (!FileUtil.isFileExists(image)) {
                Files.copy(Paths.get(DEFAULT_PHOTO), Paths.get(photo), StandardCopyOption.REPLACE_EXISTING);
            } else {
            }
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

    /**
     * Downloads photo from the link given onto the computer and store it locally.
     * @param photo URL link given by the user
     * @return the temporary storage location of the downloaded image.
     * @throws IllegalValueException if errors are faced when writing file onto local drive.
     */
    private String downloadFromInternet(String photo) throws IllegalValueException {
        try {
            URL url = new URL(photo);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(tempStorage);
            byte[] buffer = new byte[4096];

            int length = 0;

            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }

            is.close();
            os.close();

            return tempStorage;
        } catch (MalformedURLException mue) {
            throw new IllegalValueException(MESSAGE_LINK_ERROR);
        } catch (IOException ioe) {
            throw new AssertionError("Read / Write issue");
        } catch (Exception e) {
            throw new AssertionError("Impossible to reach here");
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
```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * Calculates the MD5 hash value of the person's display picture in {@code srcPath}.
     * returns the calculated hash in {@code hashValue}.
     *
     * @throws IOException if the srcPath cannot be found in the system.
     * @throws NoSuchAlgorithmException if the algorithm used for hashing is invalid.
     */
    public String calculateHash(String srcPath) throws IOException, NoSuchAlgorithmException {
        MessageDigest hashing;
        hashing = MessageDigest.getInstance("MD5");

        File existingImage = new File(srcPath);
        String hashValue = new String(hashing.digest(Files.readAllBytes(existingImage.toPath())));

        return hashValue;
    }

    /**
     * Creates a backup copy of the person's display picture in {@code srcPath} to {@code destPath}.
     *
     * @throws IOException if the srcPath cannot be found in the system.
     */
    public void createBackUpPhoto(String srcPath, String emailAddr) throws IOException {
        String destPath = "data/edited/" + emailAddr + ".jpg";
        Files.copy(Paths.get(srcPath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Creates a local copy of the person's display picture in {@code srcPath} to {@code destPath}.
     *
     * @throws IOException if the srcPath cannot be found in the system.
     */
    public void createCurrentPhoto(String srcPath, String emailAddr) throws IOException {
        String destPath = "data/images/" + emailAddr + ".jpg";
        Files.copy(Paths.get(srcPath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Deletes the existing copy of the person's display picture in {@code srcPath}.
     *
     * @throws IOException if the srcPath cannot be found in the system.
     */
    public void deleteExistingPhoto(String srcPath) throws IOException {
        Files.delete(Paths.get(srcPath));
    }

    /**
     * Update the photo of the person in (@code person) to reflect the new address of local file.
     *
     * @return the updated person
     */
    public Person updatePhoto(Person person, String srcPath) {
        person.setPhoto(new Photo(srcPath, 0));
        return person;
    }
```
###### \java\seedu\address\ui\PersonCard.java
``` java
    /**
     * Bind the path of a contact's display picture into an Image and set the image into the ImageView photo.
     */
    public void getPhoto() {
        try {
            StringExpression filePath = Bindings.convert(person.photoProperty());
            FileInputStream imageInputStream = new FileInputStream(filePath.getValue());
            Image image = new Image(imageInputStream, 100, 200, true, true);
            photo.setImage(image);
            imageInputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
```
###### \java\seedu\address\ui\PersonCardBirthday.java
``` java
    /**
     * Bind the path of a contact's display picture into an Image and set the image into the ImageView photo.
     */
    public void getPhoto() {
        try {
            StringExpression filePath = Bindings.convert(person.photoProperty());
            FileInputStream imageInputStream = new FileInputStream(filePath.getValue());
            Image image = new Image(imageInputStream, 100, 200, true, true);
            photo.setImage(image);
            imageInputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
```
