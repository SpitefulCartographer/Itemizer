package utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import pojos.Item;
import pojos.User;

/**
 *
 * @author Blake
 */
public class Sorting {

    public static void sortItemsById(List<Item> ar) {
        
        for (int i = 1; i < ar.size(); i++) {
            Item key = ar.get(i);
            int j = i;
            while ((j > 0) && (ar.get(j - 1).compareTo(key) > 0)) {
                ar.set(j, ar.get(j - 1));
                j--;
            }
            ar.set(j, key);
        }
    }
    
    public static void sortItemsByName(List<Item> ar) {
        for (int i = 1; i < ar.size(); i++) {
            Item key = ar.get(i);
            int j = i;
            while ((j > 0) && (ar.get(j - 1).getName().compareTo(key.getName()) > 0)) {
                ar.set(j, ar.get(j - 1));
                j--;
            }
            ar.set(j, key);
        }
    }
    
    public static void sortItemsByPrice(List<Item> ar) {
        for (int i = 1; i < ar.size(); i++) {
            Item key = ar.get(i);
            int j = i;
            while ((j > 0) && (ar.get(j - 1).getPrice().compareTo(key.getPrice()) < 0)) {
                ar.set(j, ar.get(j - 1));
                j--;
            }
            ar.set(j, key);
        }
    }
    
    public static void sortItemsByTag(List<Item> ar) {
        for (int i = 1; i < ar.size(); i++) {
            Item key = ar.get(i);
            int j = i;
            while ((j > 0) && (ar.get(j - 1).getTag().compareTo(key.getTag()) > 0)) {
                ar.set(j, ar.get(j - 1));
                j--;
            }
            ar.set(j, key);
        }
    }

}
