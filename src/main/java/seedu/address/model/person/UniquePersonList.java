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

        Photo originalPhoto = toAdd.getPhoto();
        String intendedPhotoPath = "data/images/" + toAdd.getEmailAddress().toString() + ".jpg";
        Files.copy(Paths.get(originalPhoto.toString()), Paths.get(intendedPhotoPath),
                StandardCopyOption.REPLACE_EXISTING);
        person.setPhoto(new Photo(intendedPhotoPath, 0));

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

        Photo originalPhoto = target.getPhoto();
        Photo newPhoto = editedPerson.getPhoto();
        String intendedPhotoPath = "data/images/" + editedPerson.getEmailAddress().toString() + ".jpg";
        boolean deleteFile = false;

        if (target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && !target.getPhoto().equals(editedPerson.getPhoto())) { //Only Photo changed.

            person.setPhoto(new Photo(intendedPhotoPath, 0));
            Files.copy(Paths.get(intendedPhotoPath), Paths.get("data/edited/" +
                    editedPerson.getEmailAddress().toString() + ".jpg"),StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(newPhoto.toString()), Paths.get(intendedPhotoPath),
                    StandardCopyOption.REPLACE_EXISTING);

        } else if (!target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && target.getPhoto().equals(editedPerson.getPhoto())) { //only email changed.
            person.setPhoto(new Photo(intendedPhotoPath, 0));

            Files.copy(Paths.get(originalPhoto.toString()), Paths.get("data/edited/" +
                    target.getEmailAddress().toString() + ".jpg"), StandardCopyOption.REPLACE_EXISTING);

            Files.copy(Paths.get(newPhoto.toString()), Paths.get(intendedPhotoPath),
                    StandardCopyOption.REPLACE_EXISTING);
            deleteFile = true;

        } else if (!target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && !target.getPhoto().equals(editedPerson.getPhoto())) { //Both changed.

            Files.copy(Paths.get(originalPhoto.toString()), Paths.get("data/edited/" +
                    target.getEmailAddress().toString() + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
            person.setPhoto(new Photo(intendedPhotoPath, 0));
            Files.copy(Paths.get(newPhoto.toString()), Paths.get(intendedPhotoPath),
                    StandardCopyOption.REPLACE_EXISTING);
            deleteFile = true;

        } else if (target.getEmailAddress().equals(editedPerson.getEmailAddress())
                && target.getPhoto().equals(editedPerson.getPhoto())) { //No special update
        } else {
            throw new AssertionError("Shouldn't be here");
        }

        internalList.set(index, new Person(person));
        sortInternalList();
        if (deleteFile == true) {
            Files.delete(Paths.get(originalPhoto.toString()));
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
        Files.copy(Paths.get(toRemove.getPhoto().toString()), Paths.get("data/edited/" +
                toRemove.getEmailAddress().toString() + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(Paths.get(toRemove.getPhoto().toString()));
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
            if (!image.exists()) {
                File toBeCopied = new File("data/edited/" + person.getEmailAddress().toString() + ".jpg");
                if (!toBeCopied.exists()) {
                    throw new AssertionError("image should exist!");
                } else {
                    Files.copy(toBeCopied.toPath(), image.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                //Compare Hash.
                MessageDigest hashing;
                try {
                    hashing = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException nsa) {
                    throw new AssertionError("Impossible, algorithm should exist");
                }

                File existingImage = new File(person.getPhoto().toString());
                String hashValue = new String(hashing.digest(Files.readAllBytes(existingImage.toPath())));
;
                if (!hashValue.equals(person.getPhoto().getHash())) { //Not equal, go take the old image.
                    File toBeCopied = new File("data/edited/" + person.getEmailAddress().toString() + ".jpg");
                    Files.copy(toBeCopied.toPath(), existingImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else { //Equal, do nothing.
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
