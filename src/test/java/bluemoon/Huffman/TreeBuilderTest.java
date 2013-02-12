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
    public void testBinaryMode() {
	String[] testString = "aaabbbccddeeegghhjjjffgghhh".split("");

	SortedMap<String, Integer> wList = TreeBuilder.returnWeightedList(testString);
	ArrayList<Node> nodes = TreeBuilder.returnNodes(wList);
	ArrayList<Node> huffTree = TreeBuilder.returnHuffTree(nodes);
	SortedMap<String, String> codeList = TreeBuilder.returnCodeList(huffTree);
	String encoded = TreeBuilder.returnEncodedMsg(codeList, testString);

	byte[] binaryStuff = TreeBuilder.returnBinaryEncodedMsg(codeList, encoded);

	assertEquals(2, binaryStuff[0] & 0b10);
    }
}