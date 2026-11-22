package org.example.demo1;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocalizationService {

  public static Map<String, String> getLocalizedStrings(Locale locale) {
    Map<String, String> strings = new HashMap<>();

    try {
      ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
      for (String key : bundle.keySet()) {
        strings.put(key, bundle.getString(key));
      }
    } catch (MissingResourceException e) {
      System.err.println("⚠️ No resource bundle found for locale: " + locale);
    }

    return strings;
  }
}
