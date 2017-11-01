package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.NusmodsCommand.NusmodsDescriptor
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.person.NusModules;
import seedu.address.model.person.ReadOnlyPerson;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class NusmodsDescriptorBuilder {

    private NusmodsDescriptor descriptor;

    public NusmodsDescriptorBuilder() {
        descriptor = new NusmodsDescriptor();
    }

    public NusmodsDescriptorBuilder(NusmodsDescriptor descriptor) {
        this.descriptor = new NusmodsDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public NusmodsDescriptorBuilder(ReadOnlyPerson person) {
        descriptor = new NusmodsDescriptor();
        NusModules personNusModules = person.getNusModules();
        descriptor.ssetName(person.getName())
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public NusmodsDescriptorBuilder withName(String name) {
        try {
            ParserUtil.parseName(Optional.of(name)).ifPresent(descriptor::setName);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("name is expected to be unique.");
        }
        return this;
    }


    public NusmodsDescriptor build() {
        return descriptor;
    }
}
