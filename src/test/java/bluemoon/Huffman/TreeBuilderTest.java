package bluemoon.Huffman;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class TreeBuilderTest {

    String[] s;

    @Before
    public void setUp() {
	s = "aaaassssdd".split("");
    }

    @Test
    public void testNonEmptyWeight() {
	Map<String, Integer> testMap = TreeBuilder.returnWeightedList(s);

	assertFalse(testMap.isEmpty());
    }

    @Test
    public void testWeights() {
	Map<String, Integer> testMap = TreeBuilder.returnWeightedList(s);
	int probabilityOfA = testMap.get("a");
	int probabilityOfD = testMap.get("d");

	assertEquals(4, probabilityOfA);
	assertEquals(2, probabilityOfD);
	assertNotSame(3, probabilityOfA);
    }

    @Test
    public void testReturnNodes() {
	SortedMap<String, Integer> testMap = TreeBuilder.returnWeightedList(s);
	ArrayList<Node> nodes = TreeBuilder.returnNodes(testMap);

	// test nodes.get(0) = Node(2, "d")
	assertTrue(nodes.get(0).getWeight() == 2);

	// nodes.get(1) = Node(4, "a")
	assertTrue(nodes.get(1).getLetter().equals("a"));
	assertTrue(nodes.get(1).getWeight() == 4);

	// nodes.get(2) = Node(2, "s")
	assertTrue(nodes.get(2).getLetter().equals("s"));
	assertTrue(nodes.get(2).getWeight() == 4);

	assertTrue(nodes.size() == 3);
    }
}