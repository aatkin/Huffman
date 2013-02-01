package bluemoon.Huffman;

import java.util.Map;
import java.util.TreeMap;

public class TreeBuilder {

    public static Map<String, Integer> returnWeightedList(String[] s) {

	Map<String, Integer> wList = new TreeMap<String, Integer>();
	int probability = 0;

	for (int i = 0; i < s.length - 1; i++) {
	    if (wList.containsKey(s[i])) {
		probability = wList.get(s[i]) + 1;
		wList.put(s[i], probability);
	    } else {
		probability = 1;
		wList.put(s[i], probability);
	    }
	}

	return wList;
    }
}