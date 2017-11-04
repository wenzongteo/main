package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author ritchielq
/**
 * Indicates a request to reselect person
 */
public class ReselectEvent extends BaseEvent {


    public ReselectEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
