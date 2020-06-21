package pojos;

/**
 *
 * @author Blake
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private int status;
    
    public User(String username, String password, String email, int status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
    }
    
    public User() {};

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    
}
