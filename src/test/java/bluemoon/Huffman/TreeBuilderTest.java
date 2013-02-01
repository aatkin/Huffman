package bluemoon.Huffman;

import static org.junit.Assert.assertEquals;

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
    public void testWeights() {
	Map<String, Integer> testMap = TreeBuilder.returnWeightedList(s);
	int probabilityOfA = testMap.get("a");
	int probabilityOfU = testMap.get("u");
	assertEquals(4, probabilityOfA);
	assertEquals(1, probabilityOfU);
    }

}