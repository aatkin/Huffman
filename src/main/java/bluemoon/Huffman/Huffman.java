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

	SortedMap<String, Integer> testMap = TreeBuilder.returnWeightedList(testString);
	ArrayList<Node> nodes = TreeBuilder.returnNodes(testMap);
	ArrayList<Node> huffTree = TreeBuilder.returnHuffTree(nodes);

	Node parent = huffTree.get(0).getRightChild();

	while (parent.getLeftChild() != null) {
	    System.out.println("Parent " + parent.getLetter() + ": weight " + parent.getWeight());
	    System.out.println("Left child " + parent.getLeftChild().getLetter() + ": weight " + parent.getLeftChild().getWeight());
	    System.out.println("Right child " + parent.getRightChild().getLetter() + ": weight " + parent.getRightChild().getWeight()
		    + "\n");
	    parent = parent.getLeftChild();
	}
    }
}