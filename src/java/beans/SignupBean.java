package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import validation.Authentication;
import utils.DatabaseAccess;
import validation.InputValidation;

/**
 *
 * @author Blake
 */
@Named(value = "signupBean")
@SessionScoped
public class SignupBean implements Serializable {
    
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    
    public String signup() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (Authentication.authenticateSignup(username, password, confirmPassword, email)) {
            DatabaseAccess.addUser(username, password, email);
            fc.addMessage(null, new FacesMessage("Sign up successful. Please sign in with your username and password."));
            return "welcome.xhtml?faces-redirect=true";
        } else {
            fc.addMessage(null, new FacesMessage("Sign in failed."));
            return "";
        }
    }
    
    // GETTERS AND SETTERS

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = InputValidation.processForSQL(username); }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = InputValidation.processForSQL(password); }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = InputValidation.processForSQL(confirmPassword); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
}
