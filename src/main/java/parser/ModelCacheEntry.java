package parser;

import org.apache.jena.rdf.model.Model;

import java.util.Date;

/**
 * @author Malte Brockmeier
 */
public class ModelCacheEntry {
    private Model model;
    private Date date;
    private double extractionDuration;

    /**
     *
     * @param model the Apache Jena model
     * @param extractionDuration the time it took to extract the model using the extraction-framework
     */
    public ModelCacheEntry(Model model, double extractionDuration) {
        this.model = model;
        this.date = new Date();
        this.extractionDuration = extractionDuration;
    }

    /**
     * get the apache jena model
     * @return the Apache Jena Model
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * get the extraction timestamp
     * @return the extraction timestamp
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * get the extraction duration
     * @return the extraction duration
     */
    public double getExtractionDuration() {
        return this.extractionDuration;
    }
}
