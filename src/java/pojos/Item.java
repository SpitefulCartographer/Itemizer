package pojos;

import java.math.BigDecimal;

/**
 * 
 * @author Blake
 */
public class Item implements Comparable, Cloneable {
    
    private int id = -1;
    private String name;
    private BigDecimal price;
    private String description;
    private String tag;
    private boolean editable = false;

    public Item(String name, BigDecimal price, String description, String tag) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.tag = tag;
    }

    public Item(int id, String name, BigDecimal price, String description, String tag) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.tag = tag;
    }
    
    public Item() {};

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(double price) { this.price = BigDecimal.valueOf(price); }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    
    public boolean isEditable() { return editable; }
    public void setEditable(boolean editable) { this.editable = editable; }

    @Override
    public String toString() {
        return "[Item: " + id + "] - " + name + "; " + price.toString() + "; '" + description + "'; " + tag; 
    }
     
    @Override
    public int compareTo(Object t) {
        Item i = (Item)t;
        return this.id - i.id;
    }

}
