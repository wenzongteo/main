package seedu.address.email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.email.exceptions.EmailLoginInvalidException;

public class EmailLoginTest {
    private EmailLogin emailLogin = new EmailLogin();

    @Test
    public void isUserLogin() {
        //Methods only ensures there are 2 string inputs,
        //Verification of input is down in parser and method wrongUserEmailFormat

        try {
            //login without any values --> return false
            emailLogin.loginEmail(new String[0]);
            assertFalse(emailLogin.isUserLogin());

            //login with only 1 value --> return false
            emailLogin = new EmailLogin();
            String[] loginDetails1 = {"adam@gmail.com"};
            emailLogin.loginEmail(loginDetails1);
            assertFalse(emailLogin.isUserLogin());

            //login with 2 values --> return true
            emailLogin = new EmailLogin();
            String[] loginDetails2 = {"adam@gmail.com", "password"};
            emailLogin.loginEmail(loginDetails2);
            assertTrue(emailLogin.isUserLogin());

            //login with gmail --> return true
            emailLogin = new EmailLogin();
            String[] loginDetails3 = {"adam@gmail.com", "password"};
            emailLogin.loginEmail(loginDetails3);
            assertTrue(emailLogin.isUserLogin());

            //empty --> returns false
            emailLogin = new EmailLogin();
            emailLogin.loginEmail(new String[0]);
            assertFalse(emailLogin.isUserLogin());

        } catch (EmailLoginInvalidException e) {
            assert false : "Should not hit this case";
        }

        try {
            //login with non gmail --> return false
            emailLogin = new EmailLogin();
            String[] loginDetails4 = {"adam@yahoo.com", "password"};
            emailLogin.loginEmail(loginDetails4);
            assertFalse(emailLogin.isUserLogin());

        } catch (EmailLoginInvalidException e) {
            assert true: "user is unable to login with non gmail account";
        }
    }

    @Test
    public void equals() {
        try {
            //Set up expected EmailCompose
            EmailLogin standardEmailLogin = new EmailLogin();
            String[] loginDetails = {"adam@gmail.com", "password"};
            standardEmailLogin.loginEmail(loginDetails);

            //same values --> returns true
            EmailLogin emailLogin = new EmailLogin();
            emailLogin.loginEmail(loginDetails);
            assertTrue(standardEmailLogin.equals(emailLogin));

            //same object --> returns true
            assertTrue(standardEmailLogin.equals(standardEmailLogin));

            //null --> returns false
            assertFalse(standardEmailLogin.equals(null));

            //different type --> return false
            assertFalse(standardEmailLogin.equals(5));

            //different message --> return false
            emailLogin = new EmailLogin();
            String [] loginDetails2 = {"alex@gmail.com", "password"};
            emailLogin.loginEmail(loginDetails2);
            assertFalse(standardEmailLogin.equals(emailLogin));

        } catch (EmailLoginInvalidException e) {
            assert false : "should not hit this case";
        }
    }
}
