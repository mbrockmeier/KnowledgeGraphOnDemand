package extraction;

import java.io.IOException;

public class Extraction_Process {
    public static void extractToTtl(){

        try {
            Process p = Runtime.getRuntime().exec("cmd /c \"cd "+KnowledgeGraphConfiguration.getExtractionFrameworkDir()+"/dump"+" && mvn scala:run -Dlauncher=extraction -DaddArgs=extraction.infoboxs.properties\"");

            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
