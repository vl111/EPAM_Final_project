package controller;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
    private static final Logger LOG = Logger.getLogger(ResourceLoader.class.getSimpleName());

    private static final String resourceName = "src/main/resources/globalProperties.properties";
    private static Map<String, Object> properties;

    static {
        properties = new HashMap<>();
        loadResources();
        LOG.info("Resourced loaded.");
    }

    private static void loadResources() {
        InputStreamReader isReader = null;
        try {
            isReader = new InputStreamReader(
                    new FileInputStream(resourceName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try (BufferedReader br = new BufferedReader(isReader)) {
            while ((line = br.readLine()) != null) {
                String[] property = line.split(" = ");
                properties.put(property[0], property[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getProperties() {
        return properties;
    }
}
