package main;

import extraction.ExtractionManager;
import extraction.WikiPageExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) {
        WikiPageExtractor wikiPageExtractor = new WikiPageExtractor();

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        System.out.println("== KnowledgeGraphOnDemand ==");
        System.out.print("Please input page to retrieve: ");
        try {
            String page = bufferedReader.readLine();
            String source = wikiPageExtractor.retrieveWikiPageByTitle(page);
            new ExtractionManager().storePageForExtraction("data.xml", source);
            System.out.println(source);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
