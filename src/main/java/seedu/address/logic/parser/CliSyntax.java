package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_BIRTHDATE = new Prefix("b/");
    public static final Prefix PREFIX_SORT = new Prefix("s/");
    public static final Prefix PREFIX_PHOTO = new Prefix("dp/");
    public static final Prefix PREFIX_EMAIL_MESSAGE = new Prefix("em/");
    public static final Prefix PREFIX_EMAIL_SUBJECT = new Prefix("es/");
    public static final Prefix PREFIX_EMAIL_LOGIN = new Prefix("el/");
    public static final Prefix PREFIX_EMAIL_SEND = new Prefix("esend/");

}
