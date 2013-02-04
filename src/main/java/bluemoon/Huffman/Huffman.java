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

	SortedMap<String, String> codeList = new TreeMap<String, String>();

	String sana = "aasdffdfgdfghhdfh1122354677";
	String[] testString = sana.split("");

	SortedMap<String, Integer> wList = TreeBuilder.returnWeightedList(testString);

	for (Map.Entry<String, Integer> entry : wList.entrySet()) {
	    System.out.println("Letter " + entry.getKey() + " : weight " + entry.getValue());
	}

	SortedMap<String, Integer> testMap = TreeBuilder.returnWeightedList(testString);
	ArrayList<Node> nodes = TreeBuilder.returnNodes(testMap);
	ArrayList<Node> huffTree = TreeBuilder.returnHuffTree(nodes);

	// fugly shit, tästä sitten joku metodi millä muodostetaan codetablet
	for (Node n : huffTree) {
	    if (n.getParent() != null) {
		String code = "";
		Node temp = n;
		Node tempscnd = n;
		while (temp.getParent() != null) {
		    if (temp == temp.getParent().getLeftChild()) {
			code += "0";
		    } else if (temp == temp.getParent().getRightChild()) {
			code += "1";
		    }
		    tempscnd = temp;
		    temp = temp.getParent();
		}
		codeList.put(n.getLetter(), new StringBuffer(code).reverse().toString());
	    }
	}

	for (Map.Entry<String, String> entry : codeList.entrySet()) {
	    System.out.println("Letter " + entry.getKey() + " : binary code " + entry.getValue());
	}
    }
}