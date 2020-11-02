package main;

import extraction.KnowledgeGraphConfiguration;
import extraction.StringToXml;
import extraction.WikiPageExtractor;
import org.w3c.dom.Document;

public class Test {
    public static void main(String[] args) {
        System.out.println("Fetch test...");
        WikiPageExtractor wikiPageExtractor = new WikiPageExtractor();

        String source = wikiPageExtractor.retrieveWikiPageByTitle("Star Wars");
        System.out.println(source);


        Document d = StringToXml.toXmlDocument(source);

        StringToXml.saveXML(d);

        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String extractionFrameworkDir = KnowledgeGraphConfiguration.getExtractionFrameworkDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
    }
}
