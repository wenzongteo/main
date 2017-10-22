package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthdate;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.NusModules;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Photo;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPerson {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String photo;
    @XmlElement(required = true)
    private String birthdate;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    @XmlElement(name = "nusModule")
    private List<XmlAdaptedNusModule> nusModules = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPerson() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(ReadOnlyPerson source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        photo = source.getPhoto().value;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
        birthdate = source.getBirthdate().value;
        nusModules = new ArrayList<>();
        if (source.getNusModules() != null) {
            for (Map.Entry<String, HashMap<String, String>> module : source.getNusModules().value.entrySet()) {
                nusModules.add(new XmlAdaptedNusModule(module));
            }
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }
        final Name name = new Name(this.name);
        final Phone phone = new Phone(this.phone);
        final Email email = new Email(this.email);
        final Address address = new Address(this.address);
        final Photo photo = new Photo(this.photo);
        final Set<Tag> tags = new HashSet<>(personTags);
        final Birthdate birthdate = new Birthdate(this.birthdate);

        NusModules personNusModules = new NusModules();
        if (!nusModules.isEmpty()) {
            for (XmlAdaptedNusModule nusModule : nusModules) {
                for (Map.Entry<String, HashMap<String, String>> mod : nusModule.toNusModulesModelEntry().entrySet()) {
                    personNusModules = personNusModules.addModule(mod.getKey(), mod.getValue());
                }
            }
        }

        return new Person(name, phone, email, address, photo, tags, birthdate, personNusModules);



    }
}
