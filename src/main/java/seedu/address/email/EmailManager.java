package seedu.address.email;

import java.awt.*;
import java.util.Collections;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.ReadOnlyPerson;

/*
 * Handles how email are sent out of the application.
 */
public class EmailManager extends ComponentManager implements Email {
    private static final Logger logger = LogsCenter.getLogger(EmailManager.class);

    private final FilteredList<ReadOnlyPerson> recipients;
    private String message;
    private boolean isLogin;

    public EmailManager() {
        this.recipients = new FilteredList<ReadOnlyPerson>(FXCollections.emptyObservableList());
    }

    public void sendEmail() {

    }
}
