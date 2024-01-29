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

        /** Helper Function for put() */
        boolean insert(K k, V v) {
            if (k.equals(key)) {
                value = v;
                return false;
            } else if (k.compareTo(key) < 0) {
                return insertLeft(k, v);
            } else {
                return insertRight(k, v);
            }
        }

        /** Two Helpers for the helper function insert */
        boolean insertLeft(K k, V v) {
            if (left == null) {
                left = new TreeNode(k, v);
                return true;
            } else {
                return left.insert(k, v);
            }
        }

        boolean insertRight(K k, V v) {
            if (right == null) {
                right = new TreeNode(k, v);
                return true;
            } else {
                return right.insert(k, v);
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
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new TreeNode(key, value);
        } else {
            boolean newNode = root.insert(key, value);
            if (newNode) {
                size += 1;
            }
        }
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
