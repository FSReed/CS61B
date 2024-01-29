package bstmap;

import java.util.HashSet;
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
        TreeNode parent;

        /** Create a TreeNode with no children. */
        TreeNode(K k, V v) {
            key = k;
            value = v;
            left = null;
            right = null;
            parent = null;
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
        private boolean insertLeft(K k, V v) {
            if (left == null) {
                left = new TreeNode(k, v);
                left.parent = this;
                return true;
            } else {
                return left.insert(k, v);
            }
        }

        private boolean insertRight(K k, V v) {
            if (right == null) {
                right = new TreeNode(k, v);
                right.parent = this;
                return true;
            } else {
                return right.insert(k, v);
            }
        }

        /** Helper Function for remove() */
        V remove() {
            if (left != null && right != null) {
                return twoChildrenDelete();
            } else {
                return simpleDelete();
            }
        }

        /** This is where most bugs appear */
        private V simpleDelete() {
            V result = this.value;
            TreeNode child = (this.left == null) ? this.right : this.left;
            if (this.parent == null) {
                root = child;
            } else if (key.compareTo(parent.key) < 0) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            if (child != null) {
                child.parent = parent;
            }
            return result;
        }

        private V twoChildrenDelete() {
            TreeNode leftMax = findMax(left);
            V result = this.value;
            K tempKey = leftMax.key;
            V tempValue = leftMax.value;
            leftMax.simpleDelete();
            this.key = tempKey;
            this.value = tempValue;
            return result;
        }

        TreeNode findMax(TreeNode T) {
            TreeNode pointer = T;
            while (pointer.right != null) {
                pointer = pointer.right;
            }
            return pointer;
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
        HashSet<K> result = new HashSet<>();
        for(K key: this) {
            result.add(key);
        }
        return result;
    }

    @Override
    public V remove(K key) {
        TreeNode target = root.get(key);
        if (target == null) {
            return null;
        }
        return target.remove();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public K parentTest(K key) {
        TreeNode result = root.get(key);
        if (result == null) {
            return null;
        } else {
            return result.parent.key;
        }
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

        private final Stack<TreeNode> stackForNodes;
        private TreeNode current;
    }

    public void printInOrder() {
        for(K key: this) {
            V value = get(key);
            System.out.print(key + " -> " + value + '\n');
        }
    }
}
