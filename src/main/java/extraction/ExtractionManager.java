package extraction;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExtractionManager {
    public void storePageForExtraction(String fileName, String source) {

        try {
            Files.createFile(Paths.get(fileName));
        } catch (IOException ioException) {

        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName),StandardCharsets.UTF_8)) {
            writer.write(source);
        } catch (FileNotFoundException fileNotFoundException) {

        } catch (IOException ioException) {

        }
    }
}
