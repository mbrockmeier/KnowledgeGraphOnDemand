package extraction;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

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
}
