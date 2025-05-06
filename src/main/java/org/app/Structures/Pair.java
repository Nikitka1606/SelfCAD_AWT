package org.app.Structures;

import java.util.List;

public class Pair<K, V> {
    public final K key;
    public final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V get() {
        return value;
    }

    static String getFirstValue(List<Pair<Integer, String>> entityData, int code) {
        for (Pair<Integer, String> pair : entityData) {
            if (pair.key == code) {
                return pair.value;
            }
        }
        return "0"; // по аналогии с getOrDefault
    }

    static String getFirstValue(List<Pair<Integer, String>> entityData, int code, int index) {
        if (entityData.size() > index - 1 && entityData.get(index).key == code)
            return entityData.get(index).value;
        else
            return "0"; // по аналогии с getOrDefault
    }
}
