package main;

import extraction.KnowledgeGraphBuilder;
import extraction.KnowledgeGraphConfiguration;
import extraction.StringToXml;
import extraction.WikiPageExtractor;
import org.w3c.dom.Document;

public class Test {
    public static void main(String[] args) {
        System.out.println("Fetch test...");

        KnowledgeGraphBuilder.retrieveAndStoreWikipageXmlSource("Ethan Hunt");
        KnowledgeGraphBuilder.runExtractionFramework();
        KnowledgeGraphBuilder.decompressExtractedData();
    }
}
