package bluemoon.Huffman;

import java.util.*;

public class Huffman {

    public static void main(String[] args) {

	String[] testString = "söikö sika sanasi, vai mennäänkö metsään".split("");

	for (String s : testString) {
	    System.out.println(s);
	}

	SortedMap<String, Integer> wList = TreeBuilder.returnWeightedList(testString);

	for (Map.Entry<String, Integer> entry : wList.entrySet()) {
	    System.out.println("Letter " + entry.getKey() + " : weight " + entry.getValue());
	}
    }
}