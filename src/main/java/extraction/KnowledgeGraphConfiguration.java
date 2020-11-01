package extraction;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

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

        }
    }

    private KnowledgeGraphConfiguration() { }

    public static String getExtractionFrameworkDir() {
        String extractionFrameworkDir = properties.getProperty("extractionFrameworkDir");

        return extractionFrameworkDir;
    }

    public static String getLanguage() {
        String lang = properties.getProperty("lang");

        return lang;
    }

    public static String getExtractionFrameworkBaseDir() {
        String extractionFrameworkBaseDir = properties.getProperty("extractionFrameworkBaseDir");

        return extractionFrameworkBaseDir;
    }
}
