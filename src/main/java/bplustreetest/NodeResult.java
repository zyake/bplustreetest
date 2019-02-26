package bplustreetest;

public class NodeResult<T> {

    private final Node<T> node;

    private final int index;

    public NodeResult(Node<T> node, int index) {
        this.node = node;
        this.index = index;
    }

    public Node<T> getNode() {
        return node;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("node id=%d, index=%d, value=%s", node.getId(), index, node.getKeys()[index]);
    }
}
