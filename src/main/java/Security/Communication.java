/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;

import Network.Client.RunningClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Třída která se stará o šifrování/dešifrování komunikace
 * @author pix
 */
public class Communication {

    private static KeyPairGenerator kpg;
    private static KeyGenerator kg;
    private static KeyPair keys;
    private static int asymKeySize;
    private static int symKeySize;
    private static String asymAlgorithm;
    private static String symAlgorithm;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    private static SecretKey secKey;
    private static HashMap<RunningClient,PublicKey> clientPublicKeys;
    private static final Logger logger = LoggerFactory.getLogger(Communication.class);

    /**
     *  Metoda na vytvoření klíčů pomocí zadaných algoritmů
     * @param asymAlgorithm
     * @param symAlgorithm
     * @param asymKeySize
     * @param symKeySize
     * @throws NoSuchAlgorithmException
     */
    public static void createKeys(String asymAlgorithm,String symAlgorithm, int asymKeySize, int symKeySize) throws NoSuchAlgorithmException {
        Communication.asymKeySize = asymKeySize;
        Communication.symKeySize = symKeySize;
        Communication.asymAlgorithm = asymAlgorithm;
        Communication.symAlgorithm = symAlgorithm;

        // Generování asym klíčů
        kpg = KeyPairGenerator.getInstance(asymAlgorithm);
        kpg.initialize(asymKeySize);
        keys = kpg.genKeyPair();
        privateKey = keys.getPrivate();
        publicKey = keys.getPublic();


        //Generování sym klíče
        kg = KeyGenerator.getInstance(symAlgorithm);
        kg.init(symKeySize);
        secKey = kg.generateKey();
        logger.debug("Vygenerovaný AES: "+Base64.getEncoder().encodeToString(secKey.getEncoded()));
        clientPublicKeys = new HashMap<>();


    }

    /**
     * ašifrovává text na Byte64 string
     * @param message
     * @return
     * @throws Exception
     */
    public static String stringEncrypt(String message) throws Exception {
       byte[] encryptMessage = byteEncrypt(message.getBytes("UTF-8"));
       return  Base64.getEncoder().encodeToString(encryptMessage);
    }


    /**
     *  Zašifrování byte[] -> využívá stringEncrypt()
     * @param message
     * @return
     * @throws Exception
     */
    public static byte[] byteEncrypt(byte[] message) throws Exception {

        Cipher cipherSym = Cipher.getInstance(symAlgorithm+"/ECB/PKCS5PADDING");
        cipherSym.init(Cipher.ENCRYPT_MODE,secKey);

        return cipherSym.doFinal(message);
    }

    /**
     * Dešifrování Base64 stringu
     * @param encrypted
     * @return
     * @throws Exception
     */
  public static String stringDecrypt(String encrypted) throws Exception {
      byte[] decryptedMessage = byteDecrypt(Base64.getDecoder().decode(encrypted));
      return  new String(decryptedMessage,"UTF-8");
    }


    /**
     *  Dešifrovává byte[] -> používá stringDecrypt()
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static byte[] byteDecrypt(byte [] encrypted) throws Exception {

        if (privateKey != null){

            Cipher cipherSym = Cipher.getInstance(symAlgorithm+"/ECB/PKCS5PADDING");
            cipherSym.init(Cipher.DECRYPT_MODE,secKey);

            return cipherSym.doFinal(encrypted);
        }
        throw new Exception("Nemozno decryprovat zpravu.");

    }

    public static void setClientPublicKey(RunningClient rClient, byte[] serverPublicKey) throws Exception{
        clientPublicKeys.put(rClient, KeyFactory.getInstance(asymAlgorithm).generatePublic(new X509EncodedKeySpec(serverPublicKey)));
    }


    public static String getEncryptedAESKey(RunningClient rClient) throws Exception{
        return Base64.getEncoder().encodeToString(encryptAESKey(clientPublicKeys.get(rClient),secKey));

    }

    /**
     *  Zašifrovává AES klíč pomocí klientského veřejného klíče
     * @param clientPubKey
     * @param secKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptAESKey(PublicKey clientPubKey, SecretKey secKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance(asymAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE,clientPubKey);
        return  cipher.doFinal(secKey.getEncoded());
    }

    /**
     * Decryptuje AES klíč -> používano pouze v testech
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static byte[] decryptAsymKey(byte[] encrypted) throws Exception{
        Cipher cipher = Cipher.getInstance(asymAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return  cipher.doFinal(encrypted);
    }


    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static String getAsymAlgorithm() {
        return asymAlgorithm;
    }

    public static int getAsymKeySize() {
        return asymKeySize;
    }

    public static String getSymAlgorithm(){return symAlgorithm;}



    public static int getSymKeySize() {return symKeySize;}





}
