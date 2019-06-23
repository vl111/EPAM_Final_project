package controller.resource_loader;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class Localization {

    private static ResourceBundle rb;

    public static String getLocalizedValue(String val) {
        if (rb == null)
            rb = ResourceBundle.getBundle("localization");
        try {
            return new String(rb.getString(val).getBytes("ISO-8859-1"),
                    "windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
