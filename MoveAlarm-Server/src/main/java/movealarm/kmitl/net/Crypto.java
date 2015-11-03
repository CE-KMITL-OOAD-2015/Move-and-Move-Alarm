package movealarm.kmitl.net;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Created by oat90 on 2/11/2558.
 */


public class Crypto {
    private final String ALGORITHM = "AES";
    private final String KEYVAL = "1Hbfh667adfDEJ78";
    public static Crypto crypto = null;

    public String encryption(String value)
    {
      try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(value.getBytes());
            String encryptedData = new BASE64Encoder().encode(encVal);
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
            byte[] decodedData = new BASE64Decoder().decodeBuffer(encryptedData);
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
        if(crypto == null) {
            crypto = new Crypto();
        }
        return crypto;
    }
}
