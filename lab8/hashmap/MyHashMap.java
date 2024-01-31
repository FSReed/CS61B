package hashmap;

import java.util.Collection;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author FSReed
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    private int nodeNumber;
    private double maxLoad;

    /** Constructors */
    public MyHashMap() {
        this.buckets = null;
        this.size = 0;
        this.nodeNumber = 0;
        this.maxLoad = Double.POSITIVE_INFINITY;
    }

    public MyHashMap(int initialSize) {
        this.buckets = (Collection<Node>[]) new Collection[initialSize];
        this.size = initialSize;
        this.nodeNumber = 0;
        this.maxLoad = Double.POSITIVE_INFINITY;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.buckets = (Collection<Node>[]) new Collection[initialSize];
        this.size = initialSize;
        this.nodeNumber = 0;
        this.maxLoad = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return null;
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return (Collection<Node>[]) new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    public void put(K key, V value) {
        int position = getBucket(key);
        for (Node n: buckets[position]) {
            if (n.key.equals(key)) {
                n.value = value;
                return;
            }
        }
        Node temp = new Node(key, value);
        buckets[position].add(temp);
        this.nodeNumber += 1;
    }

    public V get(K key) {
        int position = getBucket(key);
        for (Node n: buckets[position]) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return this.nodeNumber;
    }

    /** Helper function for getting the right bucket */
    private int getBucket(K key) {
        int hashing = key.hashCode();
        return Math.floorMod(hashing, this.size);
    }
}
