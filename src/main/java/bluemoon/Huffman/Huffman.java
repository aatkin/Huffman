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

	Node iterableNode = huffTree.get(0);
	String searchable = "e";
	String binary = "";
	while (!iterableNode.isLeaf()) {
	    if (iterableNode.getLeftChild() != null) {
		iterableNode = iterableNode.getLeftChild();
		binary += "0";
	    } else {
		iterableNode = iterableNode.getRightChild();
		binary += "1";
	    }
	}
	System.out.println("Node " + iterableNode.getLetter() + ": " + binary);
    }
}