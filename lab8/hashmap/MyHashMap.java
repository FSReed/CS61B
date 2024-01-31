package hashmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

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
    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        constructor(16);
        loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        constructor(initialSize);
        loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        constructor(initialSize);
        this.loadFactor = maxLoad;
    }

    /** Helper Function For Constructors */
    protected void constructor(int initialSize) {
        this.buckets = (Collection<Node>[]) new Collection[initialSize];
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = this.createBucket();
        }
        this.size = initialSize;
        this.loadFactor = Double.POSITIVE_INFINITY;
        this.nodeNumber = 0;
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
        return new ArrayList<>();
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
        Collection<Node>[] newTable = (Collection<Node>[]) new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            newTable[i] = createBucket();
        }
        return newTable;
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
        update();
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

    public void clear() {
        constructor(1);
    }

    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }
    /** Helper function for getting the right bucket */
    private int getBucket(K key) {
        int hashing = key.hashCode();
        return Math.floorMod(hashing, this.size);
    }

    @Override
    public V remove(K key) {
        int position = getBucket(key);
        for (Node n: buckets[position]) {
            if (n.key.equals(key)) {
                buckets[position].remove(n);
                return n.value;
            }
        }
        return null;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> newSet = new HashSet<>();
        for (K key: this) {
            newSet.add(key);
        }
        return newSet;
    }

    @Override
    public V remove(K key, V value) {
        int position = getBucket(key);
        for (Node n: buckets[position]) {
            if (n.key.equals(key) && n.value.equals(value)) {
                buckets[position].remove(n);
                return n.value;
            }
        }
        return null;
    }

    /** Helper function for resizing the table */
    private void update() {
        double rate = (this.nodeNumber) / ((double) this.size);
        if (rate <= this.loadFactor) {
            return;
        }
        int newSize = this.size * 2;
        Collection<Node>[] newBuckets = createTable(newSize);
        for (Collection<Node> bucket: this.buckets) {
            for (Node n: bucket) {
                int hashing = n.key.hashCode();
                int newPosition = Math.floorMod(hashing, newSize);
                newBuckets[newPosition].add(n);
            }
        }
        this.buckets = newBuckets;
        this.size = newSize;
    }

    /** Iterator class */
    private class MyHashMapIterator implements Iterator<K> {

        public MyHashMapIterator() {
            this.currentBucket = 0;
            this.iter = buckets[0].iterator();
            this.count = 0;
        }
        @Override
        public boolean hasNext() {
            return count < nodeNumber;
        }

        @Override
        public K next() {
            if (!iter.hasNext()) {
                findNewIterator();
            }
            count += 1;
            return iter.next().key;
        }

        /** Helper Function for finding the new bucket to iterate */
        private void findNewIterator() {
            while (!this.iter.hasNext()) {
                currentBucket += 1;
                this.iter = buckets[currentBucket].iterator();
            }
        }

        private int currentBucket;
        private Iterator<Node> iter;
        private int count;
    }
}
