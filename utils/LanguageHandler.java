package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageHandler {

    // Static Variables ------------------------------------------------------------------------------------------------
    private static Locale locale;
    private static ResourceBundle rb;

    // Static Methods --------------------------------------------------------------------------------------------------
    /**
     * Utilizes a resource bundle to provide the requested String in the correct language.
     * @param   stringIdentifier    The string identifier in the resource bundle.
     * @return                      The string in the requested language.
     */
    public static String getLocaleString(String stringIdentifier){
        return rb.getString(stringIdentifier);
    }

    /**
     * Initializes the Language Handler's variables with the default system locale. This is required before using the
     * getLocaleString method.
     */
    public static void setupLanguageHandler(){
        System.out.println("Setting up language handler.");
        locale = Locale.getDefault();
        System.out.println("Locale: " + locale);
        rb = ResourceBundle.getBundle("utils.rb", locale);
        System.out.println("Resource Bundle loaded: " + rb.getBaseBundleName());
    }

    /**
     * Initializes the Language Handler's variables with the Locale input. This is required before using the
     * getLocaleString method.
     * @param manualLocale  The locale to be assigned.
     */
    public static void setupLanguageHandler(Locale manualLocale){
        System.out.println("Setting up language handler.");
        locale = manualLocale;
        System.out.println("Locale: " + locale);
        rb = ResourceBundle.getBundle("utils.rb", locale);
        System.out.println("Resource Bundle loaded: " + rb.getBaseBundleName());
    }

    // GETTERS --------------------------------------------------------------------------------------------------------

    public static Locale getLocale() {
        return locale;
    }
}
