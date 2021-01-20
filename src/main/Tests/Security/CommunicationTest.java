package Security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 * Testy vztahující se k tříde Communication
 */
public class CommunicationTest {

    /**
     * Test na vytvoření klůčů a jejich vlastností
     */
    @org.junit.Test
    public void createKeys() {
        try {
            Communication.createKeys("RSA","AES", 2048,128);
            assertEquals(2048,Communication.getAsymKeySize());
            assertEquals("RSA",Communication.getAsymAlgorithm());
            assertEquals("AES", Communication.getSymAlgorithm());
            assertEquals(128,Communication.getSymKeySize());

        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    /**
     * Test na zašifrovaní/dešifrování textu
     */
    @org.junit.Test
    public void stringEncryptDecrypth() {
        try {

            Communication.createKeys("RSA","AES", 2048,128);
            String text = "Tomáš má malé péro a všichni to vědí a máma se za něj stydí. Xd /// - xd $ß";
            String encryptetText = Communication.stringEncrypt(text);



            String decryptedMessage = Communication.stringDecrypt(encryptetText);
            assertEquals(text, decryptedMessage);

        }catch (Exception e){
            e.printStackTrace();
            fail("nepovedl se test");
        }
    }

    /**
     * Test na Zašifrování a dešifrovaní AES klíče
     */
    @org.junit.Test
    public void symKeyEncryptDecrypth() {
        try {
            Communication.createKeys("RSA","AES", 2048,128);

            KeyGenerator kg = KeyGenerator.getInstance(Communication.getSymAlgorithm());
            kg.init(Communication.getSymKeySize());
            SecretKey secKey = kg.generateKey();

            String encryptedKey = Base64.getEncoder().encodeToString(Communication.encryptAESKey(Communication.getPublicKey(),secKey));
            System.out.println(encryptedKey);
            String decryptedKey = Base64.getEncoder().encodeToString(Communication.decryptAsymKey(Base64.getDecoder().decode(encryptedKey)));
            System.out.println(decryptedKey);


            assertEquals(Base64.getEncoder().encodeToString(secKey.getEncoded()),decryptedKey);
        }catch (Exception e){
            e.printStackTrace();
            fail("Nepovedl se test šifrování sym klíče");
        }


    }
}