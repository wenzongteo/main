package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.EmailCommand;

//@@author awarenessxz
public class EmailCommandParserTest {
    private EmailCommandParser parser = new EmailCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String [] loginDetails = {"adam@gmail.com", "password"};

        //Empty Message
        assertParseFailure(parser, "email em/ ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EmailCommand.MESSAGE_USAGE));

        //Empty Subject
        assertParseFailure(parser, "email es/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EmailCommand.MESSAGE_USAGE));

        //Invalid Login Format
        assertParseFailure(parser, "email el/adam@gmail.com:password:testing",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
    }
}
