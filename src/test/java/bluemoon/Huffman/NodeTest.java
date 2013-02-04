package bluemoon.Huffman;

import static org.junit.Assert.*;

import org.junit.*;

public class NodeTest {

    Node parent, lChild, rChild;

    @Before
    public void setUp() throws Exception {
	parent = new Node(0);
	lChild = new Node(1, "a");
	rChild = new Node(1, "b");
    }

    @Test
    public void testParentsNull() {
	assertTrue(parent.getParent() == null);
	assertTrue(lChild.getParent() == null);
	assertTrue(rChild.getParent() == null);
    }

    @Test
    public void testRightParents() {
	lChild.setParent(parent);
	rChild.setParent(parent);
	assertTrue(parent.getParent() == null);
	assertTrue(lChild.getParent() == parent);
	assertTrue(rChild.getParent() == parent);
    }

    @Test
    public void testTreeLike() {
	Node random = new Node(5, "s");
	lChild.setParent(parent);
	rChild.setParent(parent);
	rChild.setRightChild(random);
	random.setParent(rChild);
	assertTrue(random.getParent() == rChild);
	// assert that random[Parent] -> rChild[Parent] -> parent
	assertTrue(random.getParent().getParent() == parent);
    }

    @Test
    public void testTreeOfNodes() {
	Node parent = new Node(0);
	Node b = new Node(1, "b");
	Node c = new Node(1, "c");

	parent.setLeftChild(b);
	b.setParent(parent);

	assertTrue(b.getParent() == parent);

	b.setRightChild(c);
	c.setParent(b);

	assertTrue(c.getParent() == b);
    }
}