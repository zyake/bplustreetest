package bplustreetest;

public class NodeFinder {

    public <T> NodeResult find(Comparable<T> key, Node root) {
        if (key == null) {
            return null;
        }
        if (root == null) {
            return null;
        }

        Node currentNode = root;
        while (!currentNode.isLeaf()) {
            final int i = currentNode.findSmallestNumberLargerThanKey(key);
            if (i < 0) {
                Node nonNullPointer = currentNode.getLastNonNullPointer();
                currentNode = nonNullPointer;
            } else if(currentNode.getKeys()[i] != null && key.compareTo((T) currentNode.getKeys()[i]) == 0) {
                currentNode = (Node) currentNode.getPointers()[i + 1];
            } else {
                currentNode = (Node) currentNode.getPointers()[i];
            }
        }

        int i = currentNode.hasKey(key);
        if (i > -1) {
            return new NodeResult(currentNode, i);
        }

        return null;
    }

    public <T> Node findInsertTarget(Comparable<T> key, Node root) {
        if (key == null) {
            return null;
        }
        if (root == null) {
            return null;
        }

        Node currentNode = root;
        while (!currentNode.isLeaf()) {
            final int i = currentNode.findSmallestNumberLargerThanKey(key);
            if (i < 0) {
                Node nonNullPointer = currentNode.getLastNonNullPointer();
                currentNode = nonNullPointer;
            } else if(currentNode.getKeys()[i] != null && key.compareTo((T) currentNode.getKeys()[i]) == 0) {
                currentNode = (Node) currentNode.getPointers()[i + 1];
            } else {
                currentNode = (Node) currentNode.getPointers()[i];
            }
        }

        return currentNode;
    }
}
