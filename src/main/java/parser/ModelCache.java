package parser;

import org.apache.jena.rdf.model.Model;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Malte Brockmeier
 */
public class ModelCache {
    LinkedList<String> modelKeys;
    HashMap<String, ModelCacheEntry> modelCache;
    int cacheLimit = 10;

    public ModelCache() {
        this.modelKeys = new LinkedList<>();
        this.modelCache = new HashMap<>();
    }

    public ModelCacheEntry storeModelInCache(String key, Model model, double extractionDuration) {
        if (modelKeys.size() == cacheLimit) {
            modelCache.remove(modelKeys.pollFirst());
        }

        ModelCacheEntry modelCacheEntry = new ModelCacheEntry(model, extractionDuration);

        modelKeys.remove(key);

        modelKeys.addLast(key);
        modelCache.put(key, modelCacheEntry);
        logCacheStatus();
        return modelCacheEntry;
    }

    public boolean containsModel(String key) {
        return modelCache.containsKey(key);
    }

    public ModelCacheEntry retrieveModelFromCache(String key) {
        ModelCacheEntry modelCacheEntry = modelCache.get(key);

        //move requested element to end of the queue
        modelKeys.remove(key);
        modelKeys.addLast(key);

        return modelCacheEntry;
    }

    private void logCacheStatus() {
        Logger.info("Current cache size " + modelCache.size());
        StringBuilder keyQueue = new StringBuilder();
        for(String key : modelKeys) {
            keyQueue.append(key).append(", ");
        }
        Logger.info("Key queue: " + keyQueue.toString());
    }
}
