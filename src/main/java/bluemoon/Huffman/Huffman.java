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

	long timeBefore = System.currentTimeMillis();

	String fileName = args[0];
	if (fileName == null) {
	    throw new IllegalArgumentException("File path must be provided");
	}
	// 1. Annetaan haluttu teksti Stringinä, ja tehdään siitä
	// String[]-taulukko split()-funktiolla
	String text = getInputStringFromFile(new File(fileName));
	String[] testString = text.split("");

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

	// System.out.println("\nString: " + text + "\nEncoded: " + encoded);
	System.out.println("wrote " + encoded.length() + " bits");

	long timeAfter = System.currentTimeMillis() - timeBefore;
	System.out.println(timeAfter + " ms");
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

    public static String getInputStringFromFile(File file) {
	BufferedReader bReader;
	String text = "";
	try {
	    bReader = new BufferedReader(new FileReader(file));
	    StringBuilder sBuilder = new StringBuilder();
	    String line = bReader.readLine();
	    while (line != null) {
		sBuilder.append(line);
		line = bReader.readLine();
	    }
	    text = sBuilder.toString();
	    bReader.close();

	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	return text;
    }
}