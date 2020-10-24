package extraction;


import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;

/**
 * @author Yawen Liu
 * StringToXml: used to convert string to xml document
 */

public class StringToXml {
    public static Document toXmlDocument(String str){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(str)));
            System.out.println();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveXML(Document document) {

        try{

            Transformer tf= TransformerFactory.newInstance().newTransformer();

            tf.transform(new DOMSource(document), new StreamResult("src/page.xml"));

        }catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}