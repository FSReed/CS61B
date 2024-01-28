package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private TreeNode root;
    private int size = 0;


    private class TreeNode {
        K key;
        V value;
        TreeNode left;
        TreeNode right;

        /** Create a TreeNode with no children. */
        TreeNode(K k, V v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }

        /** Helper Function For get() */
        V get(K k) {
            if (k.equals(key)) {
                return value;
            }
            if (k.compareTo(key) < 0) {
                return searchLeft(k);
            } else {
                return searchRight(k);
            }
        }

        /** Two Helpers for the Helper */
        V searchLeft(K k) {
            if (left == null) {
                return null;
            } else {
                return left.get(k);
            }
        }

        V searchRight(K k) {
            if (right == null) {
                return null;
            } else {
                return right.get(k);
            }
        }
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K k) {
        return get(k) != null;
    }

    @Override
    public V get(K k) {
        if (root == null) {
            return null;
        } else {
            return root.get(k);
        }

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    public void printInOrder() {

    }
}
