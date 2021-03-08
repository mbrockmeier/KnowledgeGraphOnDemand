package extraction;

import org.json.JSONObject;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Malte Brockmeier
 */
public class KnowledgeGraphConfiguration {
    private static Properties properties;
    private static BufferedInputStream bufferedInputStream;

    static {
        try {
            properties = new Properties();
            bufferedInputStream = new BufferedInputStream(new FileInputStream("kgod.properties"));
            properties.load(bufferedInputStream);
            bufferedInputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private KnowledgeGraphConfiguration() { }

    public static String getExtractionFrameworkDir() {
        return properties.getProperty("extractionFrameworkDir");
    }

    public static String getLanguage() {
        return properties.getProperty("lang");
    }

    public static String getExtractionFrameworkBaseDir() {
        return properties.getProperty("extractionFrameworkBaseDir");
    }

    public static String getPrefixesFile() {
        return properties.getProperty("prefixesFile");
    }

    public static int getBacklinksCount() {
        try {
            return Integer.parseInt(properties.getProperty("backlinksCount"));
        } catch(Exception ex) {
            return 10;
        }
    }

    public static void setExtractionFrameworkDir(String extractionFrameworkDir) {
        properties.setProperty("extractionFrameworkDir", extractionFrameworkDir);
        storeProperties();
    }

    public static void setExtractionFrameworkBaseDir(String extractionFrameworkBaseDir) {
        properties.setProperty("extractionFrameworkBaseDir", extractionFrameworkBaseDir);
        storeProperties();
    }

    public static void setPrefixesFile(String prefixesFile) {
        properties.setProperty("prefixesFile", prefixesFile);
        storeProperties();
    }

    public static void setLanguage(String language) {
        properties.setProperty("lang", language);
        storeProperties();
    }

    public static void setBacklinksCount(int backlinksCount) {
        properties.setProperty("backlinksCount", Integer.toString(backlinksCount));
        storeProperties();
    }

    public static void updateProperty(String key, String value) {
        properties.setProperty(key, value);
        storeProperties();
    }

    public static void updateProperties(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            properties.setProperty(key, String.valueOf(jsonObject.get(key)));
        }

        storeProperties();
    }

    public static JSONObject getProperties() {
        JSONObject jsonProperties = new JSONObject();
        for (Map.Entry<Object, Object> property : properties.entrySet()) {
            jsonProperties.put(property.getKey().toString(), property.getValue().toString());
        }
        return jsonProperties;
    }

    private static void storeProperties() {
        try {
            properties.store(new FileOutputStream("kgod.properties"), null);
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
