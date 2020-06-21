package utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class DESUtil
{

    public static final byte[] desKey = new byte[]
      {
        21, 1, -110, 82, -32, -85, -128, -65
      };

    public static String encrypt(String data, byte[] desKey)
    {
        String encryptedData = null;
        try
          {
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);

            encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
          }
        catch (Exception e)
          {
            throw new RuntimeException(e);
          }
        return encryptedData;
    }



    public static String decrypt(String cryptData, byte[] desKey)
    {
        String decryptedData = null;
        try
          {

            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(cryptData)));
          }
        catch (Exception e)
          {

            throw new RuntimeException(e);
          }
        return decryptedData;
    }

    public static String decrypt(String cryptData)
    {
        return decrypt(cryptData, desKey);
    }

    public static byte[] desKeyRandom(String userName, String password)
    {
    byte [] key = new byte[8];
    
    int hcUserName = userName.hashCode();
    int hcPassword = password.hashCode();
    
    key[0] =  (byte) (hcUserName & 0x000000FF); 
    key[1] =  (byte) ( (hcUserName & 0x0000FF00) >> 8); 
    key[2] =  (byte) ( (hcUserName & 0x00FF0000) >> 16); 
    key[3] =  (byte) ( (hcUserName & 0xFF000000) >> 24); 
    key[4] =  (byte) (hcPassword & 0x000000FF); 
    key[5] =  (byte) ( (hcPassword & 0x0000FF00) >> 8); 
    key[6] =  (byte) ( (hcPassword & 0x00FF0000) >> 16); 
    key[7] =  (byte) ( (hcPassword & 0xFF000000) >> 24);  

    return key;
    }

    public static void main(String[] args)
    {
        byte[] desKey =
          {
            1, 2, 3, 4, 5, 6, 7, -8
          };
        String encryptedPassword = DESUtil.encrypt("mary", desKey);

        System.out.println("password encrypted: " + encryptedPassword);
        String decrypedPassword = DESUtil.decrypt(encryptedPassword, desKey);
        System.out.println("password Decrypted: " + decrypedPassword);
    }
}
