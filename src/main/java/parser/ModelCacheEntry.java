package parser;

import org.apache.jena.rdf.model.Model;

import java.util.Date;

public class ModelCacheEntry {
    private Model model;
    private Date date;
    private double extractionDuration;

    public ModelCacheEntry(Model model, double extractionDuration) {
        this.model = model;
        this.date = new Date();
        this.extractionDuration = extractionDuration;
    }

    public Model getModel() {
        return this.model;
    }

    public Date getDate() {
        return this.date;
    }

    public double getExtractionDuration() {
        return this.extractionDuration;
    }
}
