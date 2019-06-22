package controller.resource_loader;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*This class loads resources from the globalConfig.properties file*/

public class ResourceLoader {
    private static final Logger LOG = Logger.getLogger(ResourceLoader.class.getSimpleName());
    private static Map<Object, Object> properties;

    {
        properties = new HashMap<>();
        loadResources();
        LOG.info("Resourced loaded.");
    }

    public static Map<Object, Object> getProperties() {
        return properties;
    }

    private void loadResources() {
        InputStream fis = getClass().getClassLoader().getResourceAsStream("globalConfig.properties");
        Properties prop = new Properties();
        try {
            prop.load(fis);
        } catch (IOException e) {
            LOG.error("Error on loading resources.");
            e.printStackTrace();
        }
        prop.forEach((k, v) -> properties.put(k, v));
    }
}
