package seedu.address.email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import seedu.address.email.message.MessageDraft;

public class EmailManagerTest {

    Email email = new EmailManager();

    @Test
    public void isUserLogin() {
        //Methods only ensures there are 2 string inputs,
        //Verification of input is down in parser and method wrongUserEmailFormat

        //login without any values --> return false
        email.loginEmail(new String [0]);
        assertFalse(email.isUserLogin());

        //login with only 1 value --> return false
        String [] loginDetails1 = {"adam@gmail.com"};
        email.loginEmail(loginDetails1);
        assertFalse(email.isUserLogin());

        //login with 2 values --> return true
        String [] loginDetails2 = {"adam@gmail.com", "password"};
        email.loginEmail(loginDetails2);
        assertTrue(email.isUserLogin());

    }

    @Test
    public void wrongUserEmailFormat() {
        //Method test whether user log in with gmail account

        //login with gmail --> return false
        String [] loginDetails1 = {"adam@gmail.com", "password"};
        email.loginEmail(loginDetails1);
        assertFalse(email.wrongUserEmailFormat());

        //login with non gmail --> return true
        String [] loginDetails2 = {"adam@yahoo.com", "password"};
        email.loginEmail(loginDetails2);
        assertTrue(email.wrongUserEmailFormat());

        //empty --> returns true
        email.loginEmail(new String[0]);
        assertTrue(email.wrongUserEmailFormat());

    }

    @Test
    public void equals() {
        //Set up expected Email
        Email standardEmail = new EmailManager();
        MessageDraft message = new MessageDraft("Hello", "subject");
        String [] loginDetails = {"adam@gmail.com", "password"};
        standardEmail.loginEmail(loginDetails);
        standardEmail.composeEmail(message);

        //same values --> returns true
        email = new EmailManager();
        email.composeEmail(message);
        email.loginEmail(loginDetails);
        assertTrue(standardEmail.equals(email));

        //same object --> returns true
        assertTrue(standardEmail.equals(standardEmail));

        //null --> returns false
        assertFalse(standardEmail.equals(null));

        //different type --> return false
        assertFalse(standardEmail.equals(5));

        //different message --> return false
        email = new EmailManager();
        email.composeEmail(new MessageDraft());
        email.loginEmail(loginDetails);
        assertFalse(standardEmail.equals(email));

        //different login --> return false
        email.composeEmail(message);
        String [] loginDetails2 = {"bernice@gmail.com", "password"};
        email.loginEmail(loginDetails2);
        assertFalse(standardEmail.equals(email));

    }
}
