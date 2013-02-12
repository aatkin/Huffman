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

	// 1. Annetaan haluttu teksti Stringinä, ja tehdään siitä
	// String[]-taulukko split()-funktiolla
	String sana = "abbbbbbbbbbabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
	String[] testString = sana.split("");

	// 2. Muodostetaan edellisestä taulukosta TreeMap, joka on painotettu
	// lista
	SortedMap<String, Integer> wList = TreeBuilder.returnWeightedList(testString);

	for (Map.Entry<String, Integer> entry : wList.entrySet()) {
	    System.out.println("Letter " + entry.getKey() + " : weight " + entry.getValue());
	}

	// 3. Muodostetaan painotetusta listasta solmut, ja konstruoidaan niistä
	// edelleen Huffman-puu
	ArrayList<Node> nodes = TreeBuilder.returnNodes(wList);
	ArrayList<Node> huffTree = TreeBuilder.returnHuffTree(nodes);

	// 4. Muodostetaan Huffman-puusta koodilista
	SortedMap<String, String> codeList = TreeBuilder.returnCodeList(huffTree);

	// 5. Enkoodataan viesti String-muotoiseksi
	String encoded = TreeBuilder.returnEncodedMsg(codeList, testString);

	for (Map.Entry<String, String> entry : codeList.entrySet()) {
	    System.out.println("Letter " + entry.getKey() + " : binary code " + entry.getValue());
	}

	// 6. Enkoodataan viesti tavuiksi, lähetetään tiedostovirtaan ja
	// kirjoitetaan levylle
	byte[] binaryEncoded = TreeBuilder.returnBinaryEncodedMsg(codeList, encoded);
	File file = new File("huffman.txt");
	outputBinaryToFile(file, binaryEncoded);

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

    public static void outputBinaryToFile(File file, byte[] binaryEncoded) {
	try {
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    OutputStream oStream = new FileOutputStream(file);
	    oStream.write(binaryEncoded);
	    oStream.flush();
	    oStream.close();
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
    }
}