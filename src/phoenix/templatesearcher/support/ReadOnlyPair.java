package phoenix.templatesearcher.support;

public class ReadOnlyPair<K, V> {
    protected final K key;
    protected final V value;

    public ReadOnlyPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
