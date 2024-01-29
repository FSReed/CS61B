package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

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
        TreeNode get(K k) {
            if (k.equals(key)) {
                return this;
            }
            if (k.compareTo(key) < 0) {
                return searchLeft(k);
            } else {
                return searchRight(k);
            }
        }

        /** Two Helpers for the Helper */
        TreeNode searchLeft(K k) {
            if (left == null) {
                return null;
            } else {
                return left.get(k);
            }
        }

        TreeNode searchRight(K k) {
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
        if (root == null) {
            return false;
        }
        return root.get(k) != null;
    }

    @Override
    public V get(K k) {
        if (root == null) {
            return null;
        } else {
            return root.get(k).value;
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
            size += 1;
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
        return new BSTIter();
    }

    private class BSTIter implements Iterator<K> {

        /** Create a stack, and do the first time push */
        BSTIter() {
            current = root;
            stackForNodes = new Stack<>();
            pushTillMin(current);
        }

        @Override
        public boolean hasNext() {
            return !stackForNodes.isEmpty();
        }

        @Override
        public K next() {
            current = stackForNodes.pop();
            pushTillMin(current.right);
            return current.key;
        }

        /** Push till the minimum key goes to the top of the stack */
        private void pushTillMin(TreeNode N) {
            while (N != null) {
                stackForNodes.push(N);
                N = N.left;
            }
        }

        private Stack<TreeNode> stackForNodes;
        private TreeNode current;
    }

    public void printInOrder() {
    }
}
