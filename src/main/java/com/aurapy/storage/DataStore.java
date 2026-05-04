package com.aurapy.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory data store for AuraPay
 */
public class DataStore {
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    /**
     * Store value
     * @param key Key
     * @param value Value
     */
    public void put(String key, Object value) {
        store.put(key, value);
    }

    /**
     * Retrieve value
     * @param key Key
     * @return Value or null
     */
    public Object get(String key) {
        return store.get(key);
    }

    /**
     * Remove value
     * @param key Key
     * @return Removed value or null
     */
    public Object remove(String key) {
        return store.remove(key);
    }

    /**
     * Check if key exists
     * @param key Key
     * @return True if exists
     */
    public boolean containsKey(String key) {
        return store.containsKey(key);
    }

    /**
     * Clear all data
     */
    public void clear() {
        store.clear();
    }

    /**
     * Get store size
     * @return Number of entries
     */
    public int size() {
        return store.size();
    }
}
