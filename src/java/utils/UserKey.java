/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

public class UserKey {

    private String userName;
    private String password;
    final byte[] key;

    public UserKey(String userName, String password) {
        this.userName = userName;
        this.password = password;
        key = DESUtil.desKeyRandom(this.userName, this.password);
    }

    public byte[] getKey() {
        return key;
    }

    public String keyToString() {
        String stringKey = "";
        for (byte b : this.key) {
            stringKey += Byte.toString(b) + " ";
        }

        return stringKey.trim();   //eliminate last whitespace       
    }

    public byte[] StringToKey(String stringKey) {

        String s = stringKey.trim();
        String[] arrayOfKeys = s.split("[ ]+");

        byte[] keyFromString = new byte[arrayOfKeys.length];

        for (int i = 0; i < arrayOfKeys.length; ++i) {
            keyFromString[i] = Byte.valueOf(arrayOfKeys[i]);
        }
        return keyFromString;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString() {
        String s = "User{" + "userName=" + userName + ", password=" + password + ", " + "key={";
        for (int i = 0; i < key.length; ++i) {
            s += Byte.toString(this.key[i]) + " ";
        }
        s += "}";
        return s;
    }

    public static void main(String[] args) {
        UserKey u = new UserKey("blakebenoit", "default");

        System.out.println(u);
        System.out.println(u.keyToString());
        byte[] ar = u.StringToKey(u.keyToString());

        for (byte b : ar) {
            System.out.print(b + " ");
        }

        String encryptedPassword = DESUtil.encrypt(u.getPassword(), ar);

        System.out.println("encrypted password: " + encryptedPassword);
        String decrypedPassword = DESUtil.decrypt(encryptedPassword, ar);
        System.out.println("decrypted password: " + decrypedPassword);

    }

}
