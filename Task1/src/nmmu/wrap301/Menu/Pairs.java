package nmmu.wrap301.Menu;

import nmmu.wrap301.Menu.Pair;

import java.util.ArrayList;

/**
 * A collection of key value pairs, where a value can be looked up using a unique key. This
 * is a <b>really</b> simple version of a dictionary/hash table. If you want a more complete
 * implementation, check out the Map interface and HashMap class.
 *
 * @param <K> The type of the keys.
 * @param <V> The type of the values.
 */
public class Pairs<K, V> {
    private ArrayList<Pair<K, V>> pairs;

    /**
     * Create an empty collection of pairs.
     */
    public Pairs() {
        pairs = new ArrayList<>();
    }

    /**
     * Is there a pair with the given key?
     *
     * @param key The key to be searched for.
     * @return true if the a pair exists with the given key, false otherwise.
     */
    public boolean contains(K key) {
        boolean found = false;

        for (Pair<K, V> pair : pairs) {
            if (pair.getKey().equals(key))
                found = true;
        }

        return found;
    }

    /**
     * Adds a new pair to the pairs, if there is not already a pair with the given key, otherwise updates the value associated with the key.
     *
     * @param key   The key of the pair.
     * @param value The value associated with the key.
     */
    public void set(K key, V value) {
        if (!contains(key)) {
            // doesn't contain the key, so set a new pairing
            pairs.add(new Pair<K, V>(key, value));
        } else {
            // key exists, so update the value paired with it
            for (Pair<K, V> pair : pairs) {
                if (pair.getKey().equals(key))
                    pair.setValue(value);
            }
        }
    }

    /**
     * Retrieves the value associated with the given key. If no value is found, then the default value is returned.
     *
     * @param key          The key being searched for.
     * @param defaultValue The value returned if there is no key-value pair.
     * @return The value associated with the key, if it exists, otherwise the default value.
     */
    public V get(K key, V defaultValue) {
        V value = defaultValue;

        for (Pair<K, V> pair : pairs) {
            if (pair.getKey().equals(key))
                value = pair.getValue();
        }

        return value;
    }

    @Override
    public String toString() {
        return pairs.toString();
    }

    public int size()
    {
        return pairs.size();
    }

    public Pair<K,V> get(int index)
    {
        return pairs.get(index);
    }
}
