package seedu.address.testutil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import seedu.address.commons.util.FileUtil;

//@@author wenzongteo
public class ImageInit {
    ImageInit() {
    }

    /**
     * Check if data/images and data/edited folder exist. If they do not exist, create them
     *
     * @throws IOException if encounter error in creating the folders.
     */
    public static void checkDirectories() {
        try {
            String editedFolder = "data/edited";
            String imagesFolder = "data/images";

            FileUtil.createDirs(new File(editedFolder));
            FileUtil.createDirs(new File(imagesFolder));
        } catch (IOException ioe) {
            throw new AssertionError("Error Creating File!");
        } catch (Exception e) {
            throw new AssertionError("No such error should exist!");
        }
    }

    /**
     *  Initialize photos of all possible contacts in PersonBuilder to ensure the photo is available before the start
     *  of a test.
     *
     *  @throws IOException if file does not exist.
     */
    public static void initPictures() {
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
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/cornelia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/werner@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/lydia@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/bob@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
           throw new AssertionError("Initialization of images failed!");
        } catch (Exception e) {
            throw new AssertionError("No such error possible");
        }
    }

    /**
     * Initialize alice's photo during the test.
     */
    public static void initAlice() {
        try {
            Files.copy(Paths.get("default.jpeg"), Paths.get("data/images/alice@example.com.jpg"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new AssertionError("Initialization of Alice's image failed");
        } catch (Exception e) {
            throw new AssertionError("No such error possible");
        }
    }

    /**
     * Delete all files inside data/edited folder before deleting the data/edited folder.
     */
    public static void deleteFiles() {
        File toBeDeletedFolder = new File("data/edited");
        File[] toBeDeletedImages = toBeDeletedFolder.listFiles();
        if (toBeDeletedImages != null) {
            for (File f : toBeDeletedImages) {
                f.delete();
            }
        }
        toBeDeletedFolder.delete();
    }
}
