package beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import utils.DatabaseAccess;
import pojos.Item;
import pojos.User;
import utils.Sorting;

/**
 *
 * @author Blake
 */
@Named(value = "tableBean")
@ManagedBean
@SessionScoped
public class TableBean implements Serializable {
    
    private String name;
    private BigDecimal price;
    private String description;
    private String tag;
    
    private String searchTag;
    private int sortStatus;
    
    private List<Item> items = new ArrayList<>();
    
    ////////////////////////
    /// TABLE OPERATIONS ///
    ////////////////////////
    
    public void save() {
        for (Item item : items) item.setEditable(false);
    }    
    
    public void add() {
        items.add(new Item(name, price, description, tag));
        
        name = "";
        price = BigDecimal.ZERO;
        description = "";
        tag = "";
    }
    
    public void delete(Item tbd) {
        items.remove(tbd);
    }
    
    public void deleteAll() {
        items.clear();
    }
    
    public BigDecimal sum() {
        double sum = 0.00;
        for (Item item : items) sum += item.getPrice().doubleValue();

        return BigDecimal.valueOf(sum).setScale(2, RoundingMode.HALF_UP);
    }
    
    public void sortById() {
        if (sortStatus != 0) {
        Sorting.sortItemsById(items);
        sortStatus = 0;
        } else Collections.reverse(items);
    }
    
    public void sortByName() {
        if (sortStatus != 1) {
            Sorting.sortItemsByName(items);
            sortStatus = 1;
        } else Collections.reverse(items);
    }
    
    public void sortByPrice() {
        if (sortStatus != 2) {
            Sorting.sortItemsByPrice(items);
            sortStatus = 2;
        } else Collections.reverse(items);
    }
    
    public void sortByTag() {
        if (sortStatus != 3) {
            Sorting.sortItemsByTag(items);
            sortStatus = 3;
        } else Collections.reverse(items);
    }
    
    ///////////////////////////
    /// DATABASE OPERATIONS ///
    ///////////////////////////
    
    public void pushTable() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        User user = DatabaseAccess.getUser(params.get("user"));
        
        System.out.println("pushTable - username = " + user.getUsername());
        
        List<Item> push = new ArrayList<>();
        
        for (Item item : items) {
            if (DatabaseAccess.itemExists(item)) {
                DatabaseAccess.updateItem(item);
            } else {
                System.out.println(item.getName() + " - new item");
                push.add(item);
            }
        }
        
        DatabaseAccess.pushTable(push, user);

        items.clear();
    }
    
    public void loadAllItems() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        User user = DatabaseAccess.getUser(params.get("user"));
        
        items.clear();
        
        List<Item> ar = DatabaseAccess.loadAllItems(user);
        for (Item i : ar) items.add(i);
    }
    
    public void searchItemsUsingTag() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        User user = DatabaseAccess.getUser(params.get("user"));
        
        List<Item> ar = DatabaseAccess.loadItemsUsingTag(searchTag, user);
        
        for (Item i : ar) items.add(i);
        
        searchTag = "";
    }
    
    public void expungeItems() {
        for (Item i : items) if (DatabaseAccess.itemExists(i)) DatabaseAccess.deleteItem(i);
        items.clear();
    }
    
    public void expungeAll() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        User user = DatabaseAccess.getUser(params.get("user"));
        
        DatabaseAccess.deleteAllItems(user);
        System.out.println("Database nuke successful... You monster.");
        
        items.clear();
    }
    
    /// GETTERS AND SETTERS
    
    public List<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
    }
    
}
