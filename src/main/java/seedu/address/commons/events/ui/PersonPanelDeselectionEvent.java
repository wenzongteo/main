package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.PersonCard;

//@@author ritchielq-reuse
/**
 * Represents a Deselection in the Person List Panel
 */
public class PersonPanelDeselectionEvent extends BaseEvent {

    public PersonPanelDeselectionEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
