package extraction;


import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;

/**
 * @author Yawen Liu
 * StringToXml: used to convert string to xml document
 */

public class StringToXml {

    /**
     * transforms the given XML string into a XML Document Object
     * @param str XML string
     * @return the XML string as a XML Document Object
     */
    public static Document toXmlDocument(String str){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(str)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves the specified Document Object to a xml file
     * @param document the XML Document Object
     * @param file where .xml file will be stored
     */
    public static void saveXML(Document document, File file) {
        try {
            Transformer tf= TransformerFactory.newInstance().newTransformer();
            tf.transform(new DOMSource(document), new StreamResult(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}