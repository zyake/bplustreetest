package bplustreetest;

import java.util.Arrays;

public class NodeBuilder<T> {

    private final int size;

    private final NodeFinder nodeFinder = new NodeFinder();

    private Node<T> rootNode;

    public NodeBuilder(int size) {
        this.size = size;
    }

    public NodeBuilder<T> insert(Comparable<T> value, Object pointer) {
        Node<T> insertionNode;
        if (rootNode == null) {
            rootNode = new Node(size, Arrays.asList(value), Arrays.asList(pointer));
            return this;
        } else {
            insertionNode = nodeFinder.findInsertTarget(value, rootNode);
        }

        int lastKeyIndex = insertionNode.getLastKeyIndex();
        if (lastKeyIndex < size - 1) {
            insertInLeaf(insertionNode, value, pointer, lastKeyIndex);
        } else {
            Node<T> newNode = new Node(size);
            Node<T> copiedNode = cloneNode(insertionNode);
            insertInLeaf(copiedNode, value, pointer, lastKeyIndex);

            Node<T> parent = insertionNode.getParent();
            insertionNode.reset();

            copyRange(copiedNode,0, size/2, insertionNode);
            insertionNode.setParent(parent);

            Comparable newValue = copiedNode.getKeys()[size/2];

            copyRangeFrom(copiedNode, size/2, newNode);
            newNode.setParent(parent);

            insertInParent(insertionNode, newValue, newNode);
        }
        return this;
    }

    public Node getRootNode() {
        return rootNode;
    }

    private void insertInParent(Node<T> node, Comparable smallestKey, Node<T> newNode) {
        if (node == rootNode) {
            Node<T> newRootNode = new Node(size, Arrays.asList(smallestKey), Arrays.asList(node, newNode));
            rootNode = newRootNode;
            return;
        }

        Node<T> parent = node.getParent();
        if (parent.hasEmptyPointerSpace()) {
            insertNode(parent, smallestKey, node, newNode);
        } else {
            Node<T> copiedParent = cloneNode(parent);
            insertNode(copiedParent, smallestKey, node, newNode);
            parent.reset();
            Node<T> newParentNode = new Node(size);

            {
                Node sourceNode = copiedParent;
                Node destNode = parent;
                Object[] sourcePointers = sourceNode.getPointers();
                Comparable[] sourceKeys = sourceNode.getKeys();
                Object[] destPointers = destNode.getPointers();
                Comparable[] destKeys = destNode.getKeys();

                // end must consider the actual size of the node.
                for (int i = 0 ; i < (size + 1)/2 ; i ++) {
                    destPointers[i] = sourcePointers[i];
                }
                for (int i = 0 ; i < size/2 ; i ++) {
                    destKeys[i] = sourceKeys[i];
                }
            }
            Comparable newKey = copiedParent.getKeys()[size/2];
            {
                Node<T> sourceNode = copiedParent;
                Node<T> destNode = newParentNode;
                Object[] sourcePointers = sourceNode.getPointers();
                Comparable[] sourceKeys = sourceNode.getKeys();
                Object[] destPointers = destNode.getPointers();
                Comparable[] destKeys = destNode.getKeys();

                // end must consider the actual size of the node.
                for (int i = (size + 1)/2 ; i < size + 1 ; i ++) {
                    destPointers[i - (size + 1)/2] = sourcePointers[i];
                }
                for (int i = size/2 + 1 ; i < size ; i ++) {
                    destKeys[i - (size/2 + 1)] = sourceKeys[i];
                }
            }
            insertInParent(parent, newKey, newParentNode);
        }
    }

    private Node<T> cloneNode(Node<T> parent) {
        Node<T> copiedParent;
        try {
            copiedParent = (Node<T>) parent.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return copiedParent;
    }

    private void insertNode(Node<T> parent, Comparable smallestKey, Node<T> justBeforeNode, Node<T> newNode) {
        int insertionTargetPointerIndex = parent.findInsertionTargetPointer(justBeforeNode);

        Object[] pointers = parent.getPointers();
        int lastPointerIndex = pointers.length;
        for (int i = insertionTargetPointerIndex ; i < lastPointerIndex - 1 ; i ++) {
            pointers[i + 1] = pointers[i];
            if (pointers[i] instanceof Node) {
                ((Node<T>) pointers[i]).setParent(parent);
            }
        }
        pointers[insertionTargetPointerIndex + 1] = newNode;
        newNode.setParent(parent);

        Comparable[] keys = parent.getKeys();
        int lastKeyIndex = keys.length;
        for (int i = insertionTargetPointerIndex; i < lastKeyIndex - 1 ; i ++) {
            keys[i + 1] = keys[i];
        }
        keys[insertionTargetPointerIndex] = smallestKey;
    }

    private void copyRangeFrom(Node<T> sourceNode, int from, Node<T> destNode) {
        Object[] sourcePointers = sourceNode.getPointers();
        Comparable[] sourceKeys = sourceNode.getKeys();
        Object[] destPointers = destNode.getPointers();
        Comparable[] destKeys = destNode.getKeys();

        // end must consider the actual size of the node.
        for (int i = from ; i < size ; i ++) {
            destPointers[i - from] = sourcePointers[i];
        }
        for (int i = from ; i < size ; i ++) {
            destKeys[i - from] = sourceKeys[i];
        }
    }

    private void copyRange(Node<T> sourceNode, int from, int end, Node<T> destNode) {
        Object[] sourcePointers = sourceNode.getPointers();
        Comparable[] sourceKeys = sourceNode.getKeys();
        Object[] destPointers = destNode.getPointers();
        Comparable[] destKeys = destNode.getKeys();

        // end must consider the actual size of the node.
        for (int i = from ; i < end ; i ++) {
            destPointers[i] = sourcePointers[i];
        }
        for (int i = from ; i < end ; i ++) {
            destKeys[i] = sourceKeys[i];
        }
    }

    private <T> void insertInLeaf(Node<T> node, Comparable<T> value, Object pointer, int lastKeyIndex) {
        if (lastKeyIndex == -1) {
            node.getKeys()[0] = value;
            node.getPointers()[0] = pointer;
            return;
        }

        Comparable<T> leastKey = node.getKeys()[0];
        if (value.compareTo((T) leastKey) < -1) {
            Object leastPointer = node.getPointers()[0];
            node.getKeys()[0] = value;
            node.getPointers()[0] = pointer;
            node.getKeys()[1] = leastKey;
            node.getPointers()[1] = leastPointer;
        } else {
            int targetIndex = node.findInsertionTargetIndex(value);
            if (targetIndex == -1) {
                return;
            }

            Comparable[] keys = node.getKeys();
            Object[] pointers = node.getPointers();

            Comparable[] moveKeys = Arrays.copyOfRange(keys, targetIndex, keys.length - 1);
            Object[] movePointers = Arrays.copyOfRange(pointers, targetIndex, pointers.length - 1);

            keys[targetIndex] = value;
            pointers[targetIndex] = pointer;

            copyArray(keys, moveKeys);
            copyArray(pointers, movePointers);
        }
    }

    private void copyArray(Object[] keys, Object[] moveKeys) {
        if (keys == null) {
            return;
        }
        if (moveKeys == null) {
            return;
        }
        for (int i = 0; i < moveKeys.length && moveKeys[i] != null; i ++) {
            keys[i + moveKeys.length] = moveKeys[i];
        }
    }
}
