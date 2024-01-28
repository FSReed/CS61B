package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K, V> implements Map61B<K, V> {

    private TreeNode root;
    private int size = 0;

    private class TreeNode {
        K key;
        V value;
        int size;
        TreeNode left;
        TreeNode right;

        /** Create a TreeNode with no children. */
        TreeNode(K k, V v) {
            key = k;
            value = v;
            size = 1;
            left = null;
            right = null;
        }

        void addLeft(TreeNode L) {
            this.size += L.size;
            this.left = L;
        }

        void addRight(TreeNode R) {
            this.size += R.size;
            this.right = R;
        }
    }
    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {

    }
}
