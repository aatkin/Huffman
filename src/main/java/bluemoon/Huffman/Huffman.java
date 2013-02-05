package bluemoon.Huffman;

import java.io.*;
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

	// Annetaan haluttu teksti Stringinä, ja tehdään siitä String[]-taulukko
	// split()-funktiolla
	String sana = "This graph is also useful for verifying improvements. Before the red line gets slow, you can see a few big dips. These were periods of careful performance optimization, and this graph allowed me to remain objective about every performance improvement I introduced. It also kept me from becoming too focused on one metric to the detriment of another. If I optimized large backups, but accidentally made empty incremental backups slow, the graph would let me know. I've done development consulting on many projects since then, none of which had this type of analysis in place. Every time, I've wished that we were doing it, especially on the larger projects. I highly recommend the practice: it will keep performance regressions out of the hands of your users, and it will remove yet another source of stress from your development process.";
	String[] testString = sana.split("");

	// Muodostetaan edellisestä taulukosta TreeMap, joka on painotettu lista
	SortedMap<String, Integer> wList = TreeBuilder.returnWeightedList(testString);

	for (Map.Entry<String, Integer> entry : wList.entrySet()) {
	    System.out.println("Letter " + entry.getKey() + " : weight " + entry.getValue());
	}

	// Muodostetaan painotetusta listasta solmut, ja konstruoidaan niistä
	// edelleen Huffman-puu
	ArrayList<Node> nodes = TreeBuilder.returnNodes(wList);
	ArrayList<Node> huffTree = TreeBuilder.returnHuffTree(nodes);

	System.out.println("\nWeight of the parent node: " + huffTree.get(0).getWeight());
	System.out.println("Weight of the left child node: " + huffTree.get(0).getLeftChild().getWeight());
	System.out.println("Weight of the right child node: " + huffTree.get(0).getRightChild().getWeight() + "\n");

	// Muodostetaan Huffman-puusta koodilista
	SortedMap<String, String> codeList = TreeBuilder.returnCodeList(huffTree);

	// Enkoodataan viesti (tällä hetkellä String-muotoiseksi)
	String encoded = TreeBuilder.returnEncodedMsg(codeList, testString);

	for (Map.Entry<String, String> entry : codeList.entrySet()) {
	    System.out.println("Letter " + entry.getKey() + " : binary code " + entry.getValue());
	}

	System.out.println("\nString: " + sana + "\nEncoded: " + encoded);
	System.out.println(encoded.length() + " bits");

	byte[] utf8Bytes = null;
	try {
	    utf8Bytes = sana.getBytes("UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	System.out.println("For comparison, Java representation of the string in UTF-8 is " + utf8Bytes.length + " bytes (or around "
		+ utf8Bytes.length * 8 + " bits).");
    }
}