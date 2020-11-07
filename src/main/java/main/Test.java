package main;

import extraction.KnowledgeGraphBuilder;
import extraction.KnowledgeGraphConfiguration;
import extraction.StringToXml;
import extraction.WikiPageExtractor;
import org.w3c.dom.Document;

import extraction.WikiBacklinksExtractor;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println("Fetch test...");

        String queryWikiPage = "Ethan Hunt";

        KnowledgeGraphBuilder.retrieveAndStoreWikipageXmlSource(queryWikiPage);
        KnowledgeGraphBuilder.runExtractionFramework();
        KnowledgeGraphBuilder.decompressExtractedData();

        WikiBacklinksExtractor extractor = new WikiBacklinksExtractor();
        List<String> backlinks = extractor.getBackLinks(queryWikiPage);
        System.out.println("Backlinks: " + backlinks);
    }
}
