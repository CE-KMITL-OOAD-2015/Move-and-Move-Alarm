package movealarm.kmitl.net;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * Created by oat90 on 2/11/2558.
 */


public class Crypto {
   private final String ALGORITHM = "AES"; //type of algorithm
    private final String KEYVAL = "1Hbfh667adfDEJ78"; //master key for ker generation
    public static Crypto instance = null;

    public String encryption(String value)
    {
      try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(value.getBytes());
            String encryptedData = new String(Base64.getEncoder().encode(encVal));
            return encryptedData;
      }
      catch (Exception e) {
          return "There is a problem with encryption";
      }
    }

    public String decryption(String encryptedData)
    {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedData =  Base64.getDecoder().decode(encryptedData);
            byte[] decVal = cipher.doFinal(decodedData);
            String decryptedData = new String(decVal);
            return decryptedData;
        }
        catch (Exception e) {
            return "There is a problem with decryption";
        }
    }

    private Key generateKey()
    {
        Key key = new SecretKeySpec(KEYVAL.getBytes(),ALGORITHM);
        return key;
    }

    public static Crypto getInstance()
    {
        if(instance == null) {
            instance = new Crypto();
        }
        return instance;
    }
}
