package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final SortedList<Person> sortedInternalList = new SortedList<Person>(internalList);
    private final ObservableList<ReadOnlyPerson> mappedList = EasyBind.map(sortedInternalList, (person) -> person);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(ReadOnlyPerson toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicatePersonException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(ReadOnlyPerson toAdd) throws DuplicatePersonException, IOException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }

        Person person = new Person(toAdd);

        String intendedPhotoPath = "data/images/" + toAdd.getEmailAddress().toString() + ".jpg";

        createCurrentPhoto(toAdd.getPhoto().toString(), toAdd.getEmailAddress().toString());
        person = updatePhoto(person, intendedPhotoPath);

        internalList.add(new Person(person));
        sortInternalList();
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if the replacement is equivalent to another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    public void setPerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException, IOException {
        requireNonNull(editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.equals(editedPerson) && internalList.contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        Person person = new Person(editedPerson);
        String intendedPhotoPath = "data/images/" + editedPerson.getEmailAddress().toString() + ".jpg";
        boolean deleteFile = false;

        if (target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && !target.getPhoto().equals(editedPerson.getPhoto())) { //Only Photo changed.

            createBackUpPhoto(intendedPhotoPath, editedPerson.getEmailAddress().toString());
            createCurrentPhoto(editedPerson.getPhoto().toString(), editedPerson.getEmailAddress().toString());

            person = updatePhoto(person, intendedPhotoPath);

        } else if (!target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && target.getPhoto().equals(editedPerson.getPhoto())) { //only email changed.
            createBackUpPhoto(target.getPhoto().toString(), target.getEmailAddress().toString());
            createCurrentPhoto(editedPerson.getPhoto().toString(), editedPerson.getEmailAddress().toString());

            person = updatePhoto(person, intendedPhotoPath);
            deleteFile = true;

        } else if (!target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && !target.getPhoto().equals(editedPerson.getPhoto())) { //Both changed.

            createBackUpPhoto(target.getPhoto().toString(), target.getEmailAddress().toString());
            createCurrentPhoto(editedPerson.getPhoto().toString(), editedPerson.getEmailAddress().toString());

            person = updatePhoto(person, intendedPhotoPath);
            deleteFile = true;

        } else if (target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && target.getPhoto().equals(editedPerson.getPhoto())) { //No special update
        } else {
            throw new AssertionError("Shouldn't be here");
        }

        internalList.set(index, new Person(person));
        sortInternalList();

        if (deleteFile == true) {
            deleteExistingPhoto(target.getPhoto().toString());
        }
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public boolean remove(ReadOnlyPerson toRemove) throws PersonNotFoundException, IOException {
        requireNonNull(toRemove);
        sortInternalList();
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new PersonNotFoundException();
        }

        createBackUpPhoto(toRemove.getPhoto().toString(), toRemove.getEmailAddress().toString());
        deleteExistingPhoto(toRemove.getPhoto().toString());
        return personFoundAndDeleted;
    }

    public void setPersons(UniquePersonList replacement) {
        this.internalList.setAll(replacement.internalList);
        sortInternalList();
    }

    public void setPersons(List<? extends ReadOnlyPerson> persons) throws DuplicatePersonException, IOException {
        final UniquePersonList replacement = new UniquePersonList();
        for (final ReadOnlyPerson person : persons) {
            File image = new File(person.getPhoto().toString());
            File toBeCopied = new File("data/edited/" + person.getEmailAddress().toString() + ".jpg");

            if (!FileUtil.isFileExists(image)) {
                if (!FileUtil.isFileExists(toBeCopied)) {
                    throw new AssertionError("image should exist!");
                } else {
                    createCurrentPhoto(toBeCopied.toString(), person.getPhoto().toString());
                }
            } else {
                //Compare Hash.
                try {
                    String hashValue = calculateHash(person.getPhoto().toString());
                    if (!hashValue.equals(person.getPhoto().getHash())) { //Not equal, go take the old image
                        createCurrentPhoto(toBeCopied.toString(), person.getPhoto().toString());
                    } else {
                        //Equal, do nothing.
                    }
                } catch (NoSuchAlgorithmException nsa) {
                    throw new AssertionError("Impossible, algorithm should exist");
                }
            }
            replacement.add(new Person(person));
        }
        setPersons(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyPerson> asObservableList() {
        sortInternalList();
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    //@@author wenzongteo
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
     */
    public Person updatePhoto(Person person, String srcPath) {
        person.setPhoto(new Photo(srcPath, 0));
        return person;
    }

    //@@author awarenessxz
    /**
     * Sort the list in lexicographically name order.
     */
    private void sortInternalList() {
        Comparator<Person> sort = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().fullName.toUpperCase().compareTo(o2.getName().fullName.toUpperCase());
            }
        };
        sortedInternalList.setComparator(sort);
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.sortedInternalList.equals(((UniquePersonList) other).sortedInternalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
