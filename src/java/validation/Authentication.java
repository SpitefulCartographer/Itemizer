package validation;

import utils.DatabaseAccess;

/**
 *
 * @author Blake
 */
public class Authentication {
    
    public static boolean authenticateSignup(String username, String password, 
            String confirmPassword, String email) {
        if (!password.equals(confirmPassword)) {
            System.out.println("passwords don't match");
            return false;
        } else if (DatabaseAccess.usernameExists(username)) {
            System.out.println("username already exist");
            return false;
        } else {
            System.out.println("Sign up is good to go!");
            return true;
        }
    }
    
    public static boolean authenticateLogin(String username, String password) {
        return DatabaseAccess.passwordMatch(username, password);
    }
    
}
