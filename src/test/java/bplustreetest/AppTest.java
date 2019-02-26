package bplustreetest;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AppTest {

    @Test
    public void testFind1() {
        final int size = 5;
        Node rootNode = new Node(size, Arrays.asList("El Said", "Mozart"));
        {
            Node leafNode = new Node(size, Arrays.asList("Brandt", "Califieri", "Crick", "Einstein"));
            rootNode.addChild(0, leafNode);
        }
        {
            Node leafNode = new Node(size, Arrays.asList("El Said", "Gold", "Katz", "Kim"));
            rootNode.addChild(1, leafNode);
        }
        {
            Node leafNode = new Node(size, Arrays.asList("Mozart","Singh", "Srinivasan", "Wu"));
            rootNode.addChild(2, leafNode);
        }

        NodeFinder nodeFinder = new NodeFinder();
        NodeResult gold = nodeFinder.find("Gold", rootNode);

        if (gold != null) {
            System.out.println(gold);
        }
    }

    @Test
    public void testFind2() {
        final int nodeSize = 4;
        Node rootNode = new Node(nodeSize, Arrays.asList("Mozart"));

        AtomicInteger atomicInteger = new AtomicInteger();

        // leaves
        Node leafNode1 = new Node(nodeSize, Arrays.asList("Brandt", "Califieri","Crick"), Arrays.asList(
                atomicInteger.incrementAndGet(),
                atomicInteger.incrementAndGet()
        ));

        Node leafNode2 = new Node(nodeSize, Arrays.asList("Einstein", "El Said"), Arrays.asList(
                atomicInteger.incrementAndGet(),
                atomicInteger.incrementAndGet()
        ));

        Node leafNode3 = new Node(nodeSize, Arrays.asList("Gold", "Katz", "Kim"), Arrays.asList(
                atomicInteger.incrementAndGet(),
                atomicInteger.incrementAndGet(),
                atomicInteger.incrementAndGet()
        ));

        Node leafNode4 = new Node(nodeSize, Arrays.asList("Mozart", "Singh"), Arrays.asList(
                atomicInteger.incrementAndGet(),
                atomicInteger.incrementAndGet()
        ));

        Node leafNode5 = new Node(nodeSize, Arrays.asList("Srinivasan", "Wu"), Arrays.asList(
                atomicInteger.incrementAndGet(),
                atomicInteger.incrementAndGet()
        ));

        // intermediate nodes
        Node intermediateNode1 = new Node(nodeSize, Arrays.asList("Einstein", "Gold"));
        intermediateNode1.addChild(0, leafNode1);
        intermediateNode1.addChild(1, leafNode2);
        intermediateNode1.addChild(2, leafNode3);

        Node intermediateNode2 = new Node(nodeSize, Arrays.asList("Srinivasan"));
        intermediateNode2.addChild(0, leafNode4);
        intermediateNode2.addChild(1, leafNode5);

        rootNode.addChild(0, intermediateNode1);
        rootNode.addChild(1, intermediateNode2);

        NodeFinder nodeFinder = new NodeFinder();

        {
            NodeResult gold = nodeFinder.find("Wu", rootNode);
            Assert.assertNotNull(gold);
            System.out.println(gold);
            System.out.println(gold.getNode().getPointers()[gold.getIndex()]);
        }
        {
            NodeResult gold = nodeFinder.find("Gold", rootNode);
            Assert.assertNotNull(gold);
            System.out.println(gold);
            System.out.println(gold.getNode().getPointers()[gold.getIndex()]);
        }
        {
            NodeResult gold = nodeFinder.find("Hoge", rootNode);
            Assert.assertNull(gold);
        }
        {
            NodeResult gold = nodeFinder.find(null, rootNode);
            Assert.assertNull(gold);
        }
    }

    public AppTest() {
        super();
    }

    @Test
    public void testInsertRoot() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<String> nodeBuilder = new NodeBuilder<>(5);
        nodeBuilder.insert("TEST", atomicInteger.incrementAndGet());
        Node<String> rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new String[] {"TEST", null, null, null, null}, rootNode.getKeys());
        assertArrayEquals(new Integer[] {1, null, null, null, null, null}, rootNode.getPointers());
    }

    @Test
    public void testInsert2Nodes() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<String> nodeBuilder = new NodeBuilder<>(5);
        nodeBuilder.insert("A", atomicInteger.incrementAndGet());
        nodeBuilder.insert("B", atomicInteger.incrementAndGet());

        Node<String> rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new String[] {"A", "B", null, null, null}, rootNode.getKeys());
        assertArrayEquals(new Integer[] {1, 2, null, null, null, null}, rootNode.getPointers());
    }

    @Test
    public void testInsert3Nodes() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<String> nodeBuilder = new NodeBuilder<>(4);
        nodeBuilder
            .insert("A", atomicInteger.incrementAndGet())
            .insert("B", atomicInteger.incrementAndGet())
            .insert("C", atomicInteger.incrementAndGet());

        Node<String> rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new String[] {"A", "B", "C", null}, rootNode.getKeys());
        assertArrayEquals(new Integer[] {1, 2, 3, null, null}, rootNode.getPointers());
    }

    @Test
    public void testInsert4Nodes() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<String> nodeBuilder = new NodeBuilder<>(5);
        nodeBuilder
            .insert("A", atomicInteger.incrementAndGet())
            .insert("B", atomicInteger.incrementAndGet())
            .insert("C", atomicInteger.incrementAndGet())
            .insert("D", atomicInteger.incrementAndGet());

        Node<String> rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new String[] {"A", "B", "C", "D", null}, rootNode.getKeys());
        assertArrayEquals(new Integer[] {1, 2, 3, 4, null, null}, rootNode.getPointers());
    }

    @Test
    public void testInsert5Nodes() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<String> nodeBuilder = new NodeBuilder<>(5);
        nodeBuilder
                .insert("A", atomicInteger.incrementAndGet())
                .insert("B", atomicInteger.incrementAndGet())
                .insert("C", atomicInteger.incrementAndGet())
                .insert("D", atomicInteger.incrementAndGet())
                .insert("E", atomicInteger.incrementAndGet());

        Node<String> rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new String[] {"C", null, null, null, null}, rootNode.getKeys());

        Node<String> child1 = (Node<String>)rootNode.getPointers()[0];
        assertArrayEquals(new String[] {"A", "B", null, null, null}, child1.getKeys());
        assertArrayEquals(new Integer[] {1, 2, null, null, null, null}, child1.getPointers());

        Node<String> child2 = (Node<String>)rootNode.getPointers()[1];
        assertArrayEquals(new String[] {"C", "D", "E", null, null}, child2.getKeys());
        assertArrayEquals(new Integer[] {3, 4, 5, null, null, null}, child2.getPointers());
    }

    @Test
    public void testInsert6Nodes() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<Integer> nodeBuilder = new NodeBuilder<>(3);
        nodeBuilder
                .insert(1, atomicInteger.incrementAndGet())
                .insert(2, atomicInteger.incrementAndGet())
                .insert(3, atomicInteger.incrementAndGet());

        Node<Integer> rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new Integer[] {2, null, null}, rootNode.getKeys());

        Node<Integer> child1 = (Node<Integer>)rootNode.getPointers()[0];
        assertArrayEquals(new Integer[] {1, null, null}, child1.getKeys());
        assertArrayEquals(new Integer[] {1, null, null, null}, child1.getPointers());

        Node<Integer> child2 = (Node<Integer>)rootNode.getPointers()[1];
        assertArrayEquals(new Integer[] {2, 3, null}, child2.getKeys());
        assertArrayEquals(new Integer[] {2, 3, null, null}, child2.getPointers());
    }

    @Test
    public void testInsert7Nodes() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<Integer> nodeBuilder = new NodeBuilder<>(3);
        nodeBuilder
                .insert(1, atomicInteger.incrementAndGet())
                .insert(2, atomicInteger.incrementAndGet())
                .insert(3, atomicInteger.incrementAndGet())
                .insert(4, atomicInteger.incrementAndGet());

        Node rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new Integer[] {2, 3, null}, rootNode.getKeys());

        Node<Integer> child1 = (Node<Integer>)rootNode.getPointers()[0];
        assertArrayEquals(new Integer[] {1, null, null}, child1.getKeys());
        assertArrayEquals(new Integer[] {1, null, null, null}, child1.getPointers());

        Node<Integer> child2 = (Node<Integer>)rootNode.getPointers()[1];
        assertArrayEquals(new Integer[] {2, null, null}, child2.getKeys());
        assertArrayEquals(new Integer[] {2, null, null, null}, child2.getPointers());

        Node<Integer> child3 = (Node<Integer>)rootNode.getPointers()[2];
        assertArrayEquals(new Integer[] {3, 4, null}, child3.getKeys());
        assertArrayEquals(new Integer[] {3, 4, null, null}, child3.getPointers());
    }

    @Test
    public void testInsert8Nodes() {
        AtomicInteger atomicInteger = new AtomicInteger();

        NodeBuilder<Integer> nodeBuilder = new NodeBuilder<>(3);
        nodeBuilder
                .insert(1, atomicInteger.incrementAndGet())
                .insert(2, atomicInteger.incrementAndGet())
                .insert(3, atomicInteger.incrementAndGet())
                .insert(4, atomicInteger.incrementAndGet())
                .insert(5, atomicInteger.incrementAndGet());

        Node<Integer> rootNode = nodeBuilder.getRootNode();

        assertArrayEquals(new Integer[] {3, null, null}, rootNode.getKeys());

        Node<Integer> child1 = (Node<Integer>)rootNode.getPointers()[0];
        assertArrayEquals(new Integer[] {2, null, null}, child1.getKeys());
        {
            Node<Integer>[] childChild1 = child1.getPointers();
            assertArrayEquals(new Integer[] {1, null, null}, childChild1[0].getKeys());
            assertArrayEquals(new Integer[] {1, null, null, null}, childChild1[0].getPointers());
            assertArrayEquals(new Integer[] {2, null, null}, childChild1[1].getKeys());
            assertArrayEquals(new Integer[] {2, null, null, null}, childChild1[1].getPointers());
        }

        Node<Integer> child2 = (Node<Integer>)rootNode.getPointers()[1];
        assertArrayEquals(new Integer[] {4, null, null}, child2.getKeys());
        {
            Object[] childChild1 = child2.getPointers();
            Node childchildchild1 = (Node) childChild1[0];
            assertArrayEquals(new Integer[] {3, null, null}, childchildchild1.getKeys());
            assertArrayEquals(new Integer[] {3, null, null, null}, childchildchild1.getPointers());
            childchildchild1 = (Node) childChild1[1];
            assertArrayEquals(new Integer[] {4, 5, null}, childchildchild1.getKeys());
            assertArrayEquals(new Integer[] {4, 5, null, null}, childchildchild1.getPointers());
        }
    }
}
