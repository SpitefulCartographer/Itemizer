package utils;

import validation.InputValidation;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pojos.Item;
import pojos.User;

/**
 *
 * @author Blake
 */
public class DatabaseAccess {
    
    private static final String DATABASE = "itemizer";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER_PATH = "com.mysql.jdbc.Driver";
    private static final String IP_ADDRESS = "jdbc:mysql://localhost:3306/" + DATABASE;
    
    private static Connection connection() {
        try {
            Class.forName(DRIVER_PATH).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            System.err.println("Unable to load database driver!");
            System.err.println("Details: " + ex);
            return null;
        }
        
        try {
            Connection con = DriverManager.getConnection(IP_ADDRESS, USERNAME, PASSWORD);
            con.setReadOnly(false);
            System.out.println("Connection successful.");
            return con;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }

    ///////////////////
    /// Table Stuff ///
    ///////////////////
    
    public static void pushTable(List<Item> ar, User user) {
        try (Connection con = connection(); 
                Statement stmt = con.createStatement();) {
            if (ar.isEmpty()) { System.out.println("Table is empty. No items pushed."); return; }
            
            // Build query from ArrayList ar
            String s = "INSERT INTO items VALUES";
            
            for (Item i : ar) {
                String name = InputValidation.processForSQL(i.getName());
                String desc = InputValidation.processForSQL(i.getDescription());
                String tag = InputValidation.processForSQL(i.getTag());
                
                s += "(NULL, '" + name + "', '" + i.getPrice() + "', '" + desc + "', '" + tag + "', " + user.getId() + "), ";
            }
            
            s = s.substring(0, s.length() - 2); // Trim end of string (", ") when finished accessing ar
            
            int rows = stmt.executeUpdate(s);
            
            System.out.println(s);
            
            System.out.println("Table insert successful. " + rows + " rows inserted.");
        } catch (SQLException ex) {
            System.out.println("Table insert failed.");
            System.out.println("Details: " + ex);
        }
    }
    
    public static void updateItem(Item item) {
        try (Connection con = connection(); 
                Statement stmt = con.createStatement();) {
            String name = InputValidation.processForSQL(item.getName());
            BigDecimal price = item.getPrice();
            String desc = InputValidation.processForSQL(item.getDescription());
            String tag = InputValidation.processForSQL(item.getTag());
            int id = item.getId();
            
            stmt.executeUpdate("UPDATE items SET name ='" + name + "', price = '" + price + "', description = '" + desc +
                    "', tag = '" + tag + "' WHERE item_id = " + id);
            
            System.out.println("Item update (id: " + item.getId() + ") successful.");
        } catch (SQLException ex) {
            System.out.println("Item update failed.");
            System.out.println("Details: " + ex);
        }
    }
    
    public static ArrayList<Item> loadAllItems(User user) {
        try (Connection con = connection(); 
                Statement stmt = con.createStatement(); 
                ResultSet rs = stmt.executeQuery("SELECT * FROM items WHERE user_id = '" + user.getId() + "'");) {
            ArrayList<Item> ar = new ArrayList();
            
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setDescription(rs.getString("description"));
                item.setTag(rs.getString("tag"));
                ar.add(item);
            }
            
            System.out.println("Retrieved " + rs.getRow() + " tuples.");
            return ar;
        } catch (SQLException ex) {
            System.out.println("Table select failed.");
            System.out.println("Details: " + ex);
            return null;
        }
    }
    
    public static ArrayList<Item> loadItemsUsingTag(String tag, User user) {
        try (Connection con = connection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM items WHERE tag = '" + tag + "' AND user_id = '" + 
                        user.getId() + "'");) {
            ArrayList<Item> ar = new ArrayList();
            
            while (rs.next()) {
                Item item = new Item();
                item.setName(rs.getString("name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setDescription(rs.getString("description"));
                item.setTag(rs.getString("tag"));
                ar.add(item);
            }
            
            System.out.println("Retrieved " + ar.size() + " matching rows from table.");
            return ar;
        } catch (SQLException ex) {
            System.out.println("Table select failed.");
            System.out.println("Details: " + ex);
            return null;
        }
    }
    
    public static void deleteItem(Item i) {
        try (Connection con = connection();
                Statement stmt = con.createStatement();) {
            stmt.executeUpdate("DELETE FROM items WHERE item_id = " + i.getId());
        } catch (SQLException ex) {
            System.out.println("Item delete failed.");
            System.out.println("Details: " + ex);
        }
    }
    
    public static void deleteAllItems(User user) {
        try (Connection con = connection();
                Statement stmt = con.createStatement();) {
            stmt.executeUpdate("DELETE FROM items WHERE user_id = " + user.getId());
        } catch (SQLException ex) {
            System.out.println("User-wide item delete failed.");
            System.out.println("Details: " + ex);
        }
    }
    
    public static void nukeDatabase() {
        try (Connection con = connection();
                Statement stmt = con.createStatement();) {
            stmt.executeUpdate("CALL nukeDatabase();");
        } catch (SQLException ex) {
            System.out.println("Database nuke failed. (probably a good thing too!)");
            System.out.println("Details: " + ex);
        }
    }
    
    public static boolean itemExists(Item item) {
        try (Connection con = connection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM items WHERE item_id = " + item.getId());) {
            
            if (rs.next()) {
                System.out.println("item exists. ID: " + rs.getInt("item_id"));
                return true;
            } else {
                System.out.println("item doesn't exist.");
                return false;
            }
            
        } catch (SQLException ex) {
            System.out.println("Item search failed.");
            System.out.println("Details: " + ex);
            return false;
        }
    }
    
    public static int getCurrentItemAutoincrementValue() {
        try (Connection con = connection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT GetItemsAutoIncrementValue() as value");) {
            
            if (rs.next()) {
                return rs.getInt("value");
            } else return 0;
            
        } catch (SQLException ex) {
            System.out.println("Something went wrong retreiving auto-increment value.");
            System.out.println("Details: " + ex);
            return 0;
        }
    }
    
    //////////////////////////////
    /// Login and Signup Stuff ///
    //////////////////////////////
    
    public static boolean usernameExists(String usernameToFind) {
        try (Connection con = connection(); 
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username = '" + usernameToFind + "'");) { 
            
            System.out.println("username - go");
            
            if(rs.next()) {
                System.out.println("username found!");
                return usernameToFind.equals(rs.getString("username"));
            }
            
            return false;
        } catch (SQLException ex) {
            System.out.println("User select failed.");
            System.out.println("Details: " + ex);
            return false;
        } 
    }
    
    public static boolean passwordMatch(String usernameToFind, String passwordToMatch) {
        try (Connection con = connection(); 
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + usernameToFind +
                        "' AND password = '" + passwordToMatch + "';")) {
            
            if(rs.next()) return passwordToMatch.equals(rs.getString("password"));
            
            System.out.println("nothing fucking here");
            return false;

        } catch (SQLException ex) {
            System.out.println("User select failed.");
            System.out.println("Details: " + ex);
            return false;
        }
    }
    
    public static void addUser(String username, String password, String email) {
         try (Connection con = connection(); 
                Statement stmt = con.createStatement();) {
             
            String s1 = InputValidation.processForSQL(username);
            String s2 = InputValidation.processForSQL(password);
            String s3 = InputValidation.processForSQL(email);
             
            stmt.executeUpdate("INSERT INTO users VALUES (NULL, '" + s1 + "', '" + s2 + "', '" + s3 +"', 0);");
            System.out.println("User insert successful.");
        } catch (SQLException ex) {
             System.out.println("User insert failed.");
             System.out.println("Details: " + ex);
        }
    }
    
    public static User getUser(String usernameToFind) {
        try (Connection con = connection(); 
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + usernameToFind + "'");) {
            
            User user = new User();
            
            if (rs.next()) {
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getInt("status"));
            }

            return user;
        } catch (SQLException ex) {
            System.out.println("User select failed.");
            System.out.println("Details: " + ex);
            return null;
        }
    }
    
}
