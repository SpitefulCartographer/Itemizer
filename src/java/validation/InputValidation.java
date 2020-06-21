package validation;

/**
 *
 * @author Blake
 */
public class InputValidation {

    /**
     * Process a String to make them SQL-safe by adding a backslash escape char before any apostrophes.
     * If the String doesn't contain any apostrophes, it returns the original String.
     *
     * @param s - String to be processed
     * @return processed String
     */
    public static String processForSQL(String s) {
        if (!(s.contains("'"))) return s;
        
        String newString = s;
        int n = newString.length() - 1;
        for (int i = 0; i < n; i++) {
            if (newString.charAt(i) == '\'') {
                String half1 = newString.substring(0, i);
                String half2 = newString.substring(i);
                
                half1 += "\\";
                
                newString = half1.concat(half2);
                i++;
                n++;
            }
        }
        return newString;
    }

}
