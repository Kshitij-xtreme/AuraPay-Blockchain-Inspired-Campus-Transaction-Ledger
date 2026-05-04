package com.aurapy.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles data storage and persistence for AuraPay
 */
public class StorageManager {
    private static final Logger logger = LoggerFactory.getLogger(StorageManager.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private final String dataDirectory;

    public StorageManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        createDirectoryIfNotExists();
    }

    /**
     * Create data directory if it doesn't exist
     */
    private void createDirectoryIfNotExists() {
        try {
            Files.createDirectories(Paths.get(dataDirectory));
            logger.info("Data directory ensured at: {}", dataDirectory);
        } catch (IOException e) {
            logger.error("Error creating data directory", e);
        }
    }

    /**
     * Save object to JSON file
     * @param data Data to save
     * @param filename Filename to save to
     */
    public void saveData(Object data, String filename) {
        try {
            String filePath = dataDirectory + File.separator + filename;
            String json = gson.toJson(data);
            Files.write(Paths.get(filePath), json.getBytes());
            logger.info("Data saved to: {}", filePath);
        } catch (IOException e) {
            logger.error("Error saving data to {}", filename, e);
        }
    }

    /**
     * Load object from JSON file
     * @param filename Filename to load from
     * @param classType Class type to deserialize to
     * @param <T> Generic type
     * @return Loaded object or null if not found
     */
    public <T> T loadData(String filename, Class<T> classType) {
        try {
            String filePath = dataDirectory + File.separator + filename;
            if (Files.exists(Paths.get(filePath))) {
                String json = new String(Files.readAllBytes(Paths.get(filePath)));
                return gson.fromJson(json, classType);
            }
        } catch (IOException e) {
            logger.error("Error loading data from {}", filename, e);
        }
        return null;
    }

    /**
     * Save object to serialized file
     * @param obj Object to save
     * @param filename Filename
     */
    public void serializeObject(Serializable obj, String filename) {
        try {
            String filePath = dataDirectory + File.separator + filename;
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
            fos.close();
            logger.info("Object serialized to: {}", filePath);
        } catch (IOException e) {
            logger.error("Error serializing object to {}", filename, e);
        }
    }

    /**
     * Load object from serialized file
     * @param filename Filename
     * @return Deserialized object or null
     */
    public Object deserializeObject(String filename) {
        try {
            String filePath = dataDirectory + File.separator + filename;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Object obj = ois.readObject();
                ois.close();
                fis.close();
                logger.info("Object deserialized from: {}", filePath);
                return obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error deserializing object from {}", filename, e);
        }
        return null;
    }

    /**
     * Delete file
     * @param filename Filename to delete
     * @return True if deleted
     */
    public boolean deleteFile(String filename) {
        try {
            String filePath = dataDirectory + File.separator + filename;
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("Error deleting file: {}", filename, e);
            return false;
        }
    }

    /**
     * Check if file exists
     * @param filename Filename to check
     * @return True if exists
     */
    public boolean fileExists(String filename) {
        String filePath = dataDirectory + File.separator + filename;
        return Files.exists(Paths.get(filePath));
    }
}
