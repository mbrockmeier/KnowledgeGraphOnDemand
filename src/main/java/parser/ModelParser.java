package parser;

import extraction.KnowledgeGraphConfiguration;
import org.apache.jena.base.Sys;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModelParser {

    public Model model;

    public ModelParser(){
        model = ModelFactory.createDefaultModel();
    }

    /**
     * @author:Yawen Liu
     * read the TTL file to a model
     *
     */

    public void readRDF(){


        String baseDir = KnowledgeGraphConfiguration.getExtractionFrameworkBaseDir();
        String language = KnowledgeGraphConfiguration.getLanguage();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String folderPath = "/" + language + "wiki/" + currentDate + "/";
        String fileName = language + "wiki-" + currentDate + "-infobox-properties.ttl";
        String filePath = baseDir + folderPath + fileName;

        model.read(filePath);
    }
    /**
     * @author:Yawen Liu
     * parse the Model
     *
     */
    public void parseRDF(){
        StmtIterator iter = model.listStatements();
        while (iter.hasNext())
        {
            Statement stmt = iter.nextStatement();
            String subject = stmt.getSubject().toString();
            String predicate = stmt.getPredicate().toString();
            RDFNode object = stmt.getObject();

            System.out.print("subject" + subject+"\t");
            System.out.print(" predicate" + predicate+"\t");
            if (object instanceof Resource)
            {
                System.out.print(" object" + object);
            }
            else {
                System.out.print("object \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }

    }
}
