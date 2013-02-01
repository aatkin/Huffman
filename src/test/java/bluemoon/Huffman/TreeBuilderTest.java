package bluemoon.Huffman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {

    String[] s;

    @Before
    public void setUp() {
	s = "aaaassssfucker".split("");
    }

    @Test
    public void testEmptyWeight() {
	String[] st = { "" };
	Map<String, Integer> testMap = TreeBuilder.returnWeightedList(st);
	assertTrue(testMap.isEmpty());
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
	int probabilityOfU = testMap.get("u");
	assertEquals(4, probabilityOfA);
	assertEquals(1, probabilityOfU);
    }

}