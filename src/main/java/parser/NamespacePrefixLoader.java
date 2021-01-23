package parser;

import extraction.KnowledgeGraphConfiguration;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class NamespacePrefixLoader {
    private static final String COMMA_DELIMITER = ",";

    public static HashMap<String, String> getNsPrefixes() {
        HashMap<String, String> nsPrefixes = new HashMap<>();

        String prefixesFile = KnowledgeGraphConfiguration.getPrefixesFile();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(prefixesFile))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                if (values[0] != null && values[1] != null) {
                    nsPrefixes.put(values[0], values[1]);
                    Logger.info("Found prefix " + values[0] + " for namespace URI " + values[1]);
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            Logger.warn("File containing the namespace prefixes was not found.");
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return nsPrefixes;
    }
}
