package Security;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testy vztahující se na třidu Password
 */
public class PasswordsTest {

    /**
     * Test na hashování
     */
    @Test
    public void isExpectedPassword() {
        Passwords psswd = new Passwords();


        byte[] salt = psswd.getNextSalt();
        String text = "Lorem Ipsum";
        byte[] expectedHash = psswd.hash(text.toCharArray(), salt);

        assertTrue(psswd.isExpectedPassword(text.toCharArray(), salt, expectedHash));
    }
}