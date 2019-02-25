package bplustreetest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Node<T> implements Cloneable {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final int id = COUNTER.incrementAndGet();

    private final Object[] poinsters;

    private final Comparable<T>[] keys;

    private Node <T> parent;

    public Node(int number, List<Comparable<T>> items) {
        if (items == null) {
            throw new RuntimeException();
        }
        this.poinsters = new Node[number + 1];
        this.keys = (Comparable<T>[]) Arrays.copyOf(items.toArray(), number);
    }

    public Node(int number, List<Comparable<T>> items, List leafPointers) {
        if (items == null) {
            throw new RuntimeException();
        }
        this.poinsters = Arrays.copyOf(leafPointers.toArray(), number + 1);
        this.keys = (Comparable<T>[]) Arrays.copyOf(items.toArray(), number);
        for (int i = 0 ; i < leafPointers.size() ; i ++) {
            Object o = leafPointers.get(i);
            if (o instanceof Node) {
                Node node = (Node)o;
                node.setParent(this);
            }
        }
    }

    public Node(Comparable<T>[] keys, Object[] poinsters, Node parent) {
        this.keys = keys;
        this.poinsters = poinsters;
        this.parent = parent;
    }

    public Node(int size) {
        this.keys = new Comparable[size];
        this.poinsters = new Object[size + 1];
    }

    public <T> T[] getPointers() {
        return (T[]) this.poinsters;
    }

    public Comparable<T>[] getKeys() {
        return this.keys;
    }

    public boolean isLeaf() {
        for (int i = 0 ; i < this.poinsters.length ; i ++ ) {
            if (poinsters[i] != null && poinsters[i] instanceof Node) {
                return false;
            }
        }
        return true;
    }

    public int findSmallestNumberLargerThanKey(Comparable<T> key) {
        if (key == null) {
            return -1;
        }
        if (keys[0] == null) {
            return -1;
        }
        int i;
        for (i = 0 ; keys[i] != null && key.compareTo((T) keys[i]) > 0 ; i ++) {
        }
        return i;
    }

    public int findInsertionTargetIndex(Comparable<T> value) {
        if (value == null) {
            return -1;
        }
        int i;
        for (i = 0 ; i < keys.length && keys[i] != null && keys[i].compareTo((T) value) < 0 ; i ++) {
        }
        if (i == keys.length) {
            return -1;
        }
        return i;
    }

    public Node getLastNonNullPointer() {
        if (poinsters[0] == null) {
            return null;
        }
        Node node = (Node) poinsters[0];
        for (int i = 0 ; poinsters[i] != null ; i ++) {
            node = (Node) poinsters[i];
        }
        return node;
    }

    public int getLastKeyIndex() {
        if (keys[0] == null) {
            return -1;
        }
        int i;
        for (i = 0; i < keys.length && keys[i] != null ; i ++) {
        }
        return i;
    }

    public int hasKey(Comparable<T> key) {
        if(key == null) {
            return -1;
        }
        if (keys[0] == null) {
            return -1;
        }
        for (int i = 0 ; i < keys.length && keys[i] != null && i < keys.length ; i ++) {
            if (key.compareTo((T) keys[i]) == 0) {
                return i;
            }
        }
        return -1;
    }

    public void reset() {
        for (int i = 0 ; i < poinsters.length ; i++ ) {
            poinsters[i] = null;
        }
        for (int i = 0 ; i < keys.length ; i ++) {
            keys[i] = null;
        }
        parent = null;
    }

    public int getId() {
        return id;
    }

    protected Object clone() throws CloneNotSupportedException {
        return new Node(keys.clone(), poinsters.clone(), parent);
    }

    public Comparable<T> getSmallestKey() {
        return keys[0];
    }

    public void addChild(int index, Node childNode) {
        poinsters[index] = childNode;
        childNode.parent = this;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public boolean hasEmptyPointerSpace() {
        int i;
        for ( i = 0 ; i < keys.length && keys[i] != null ; i ++) {
        }
        return i + 1 != keys.length;
    }

    public int findInsertionTargetPointer(Node targetNode) {
        for (int i = 0 ; i < poinsters.length; i ++) {
            if (poinsters[i] == targetNode) {
                return i;
            }
        }
        return -1;
    }
}
