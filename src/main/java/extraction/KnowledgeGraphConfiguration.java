package extraction;

import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author Malte Brockmeier
 */
public class KnowledgeGraphConfiguration {
    private static Properties properties;
    private static Properties frameworkProperties;
    private static BufferedInputStream bufferedInputStream;

    static {
        try {
            properties = new Properties();
            frameworkProperties = new Properties();
            bufferedInputStream = new BufferedInputStream(new FileInputStream("kgod.properties"));
            properties.load(bufferedInputStream);
            bufferedInputStream.close();
            Logger.info("Loaded KGoD properties.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            frameworkProperties = new Properties();
            bufferedInputStream = new BufferedInputStream(new FileInputStream(getExtractionFrameworkDir() + "/extraction.kgod.properties"));
            frameworkProperties.load(bufferedInputStream);
            bufferedInputStream.close();
            Logger.info("Loaded extraction-framework properties.");
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
        } catch(Exception e) {
            return 10;
        }
    }

    public static boolean getIncludeBacklinks() {
        try {
            return Boolean.parseBoolean(properties.getProperty("includeBacklinks"));
        } catch(Exception e) {
            return true;
        }
    }

    public static int getCacheSize() {
        try {
            return Integer.parseInt(properties.getProperty("cacheSize"));
        } catch (Exception e) {
            return 10;
        }
    }

    public static int getServerPort() {
        try {
            return Integer.parseInt(properties.getProperty("serverPort"));
        } catch (Exception e) {
            return 8080;
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

    public static void setIncludeBacklinks(boolean includeBacklinks) {
        properties.setProperty("includeBacklinks", Boolean.toString(includeBacklinks));
        storeProperties();
    }

    public static void setCacheSize(int cacheSize) {
        properties.setProperty("cacheSize", Integer.toString(cacheSize));
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
            if (properties.containsKey(key)) {
                properties.setProperty(key, String.valueOf(jsonObject.get(key)));
                if (key.equals("lang")) {
                    frameworkProperties.setProperty("languages", String.valueOf(jsonObject.get(key)));
                }
            } else {
                if (key.equals("extractors")) {
                    setEnabledExtractors(String.valueOf(jsonObject.get(key)));
                }
            }
        }

        storeProperties();
    }

    public static JSONObject getProperties() {
        JSONObject jsonProperties = new JSONObject();
        for (Map.Entry<Object, Object> property : properties.entrySet()) {
            jsonProperties.put(property.getKey().toString(), property.getValue().toString());
        }
        jsonProperties.put("extractors", getEnabledExtractors());
        return jsonProperties;
    }

    private static String getEnabledExtractors() {
        String extractorsKey = "extractors." + getLanguage();
        return frameworkProperties.getProperty(extractorsKey);
    }

    private static void setEnabledExtractors(String enabledExtractors) {
        String extractorsKey = "extractors." + getLanguage();
        frameworkProperties.setProperty(extractorsKey, enabledExtractors);
    }

    private static void storeProperties() {
        try {
            properties.store(new FileOutputStream("kgod.properties"), null);
            frameworkProperties.store(new FileOutputStream(getExtractionFrameworkDir() + "/extraction.kgod.properties"), null);
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * @author Sunita Pateer
     */
    public static boolean getRetrieveExtract() {
        return Boolean.parseBoolean(properties.getProperty("retrieveExtract"));
    }

    public static void setRetrieveExtract(boolean retrieveExtract) {
        properties.setProperty("retrieveExtract", String.valueOf(retrieveExtract));
        storeProperties();
    }
}
