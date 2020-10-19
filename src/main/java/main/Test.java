package main;

import extraction.WikiPageExtractor;

public class Test {
    public static void main(String[] args) {
        System.out.println("Fetch test...");
        WikiPageExtractor wikiPageExtractor = new WikiPageExtractor();

        String source = wikiPageExtractor.retrieveWikiPageByTitle("Star Wars");
        System.out.println(source);
    }
}
