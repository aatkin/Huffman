package bluemoon.Huffman;

import java.util.*;

/**
 * 
 * @author Anssi Kinnunen
 * 
 *         Pääohjelma Huffman-puun käyttämistä varten. Tässä on sitten joskus
 *         koko ohjelman toiminta, enkoodausta ja dekoodausta myöten.
 * 
 */
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

	SortedMap<String, Integer> testMap = TreeBuilder.returnWeightedList(testString);
	ArrayList<Node> nodes = TreeBuilder.returnNodes(testMap);
	ArrayList<Node> huffTree = TreeBuilder.returnHuffTree(nodes);

    }
}