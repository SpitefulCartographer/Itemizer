package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import pojos.User;
import validation.Authentication;
import utils.DatabaseAccess;

/**
 *
 * @author Blake
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private boolean loginFormActive = true; // If false, the Signup form is active.
    
    private User user = null;
    
    private String username;
    private String password;
    
    public void swapForms() {
        loginFormActive = !loginFormActive;
    }

    public String login() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (Authentication.authenticateLogin(username, password)) {
            user = DatabaseAccess.getUser(username);
            if (user == null) {
                fc.addMessage(null, new FacesMessage("Username or password is incorrect."));
                return "";
            }
            System.out.println(user.getUsername() + " " + user.getPassword());
            System.out.println("Login successful!");
            return "itemizer.xhtml?faces-redirect=true";
        } else {
            System.out.println("Something went wrong.");
            fc.addMessage(null, new FacesMessage("Username or password is incorrect."));
            return "";
        }
        
    }
    
    // GETTERS AND SETTERS

    public boolean isLoginFormActive() { return loginFormActive; }

    public User getUser() { return user; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
}
