package bluemoon.Huffman;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Class for Huffman-encoding a message. Heavily relies on TreeBuilder-class to do it's bidding. Includes 
 * methods for building the Huffman-tree, building binary encoded message and reading from and writing 
 * to file.
 * 
 * @author Anssi Kinnunen
 */
public class Encoder {

    private static int encodedMsgLength = 0;
    private static SortedMap<String, Integer> weightedList;

    public Encoder() {
    }

    /**
     * Encodes the contents of a given file. Optionally prints debug-messages.
     */
    public void encode(File file, boolean debug) {
	// 1. Read the message to be encoded from a  file and replace all "-symbols, since they can't be dealt with yet
	String message = readMsgFromFile(file).replaceAll("\"", "");

	// 2. Build the Huffman-tree from the received message
	ArrayList<Node> huffmanTree = buildHuffmanTree(message, debug);

	// 3. Create code list from the Huffman-tree, encode the message to string-binary  
	// and again to true binary, and write to file
	byte[] binaryEncodedMsg = buildBinaryEncodedMsg(huffmanTree, message,
		debug);
	writeBinaryToFile(binaryEncodedMsg);

	// 4. Create a lookUp-table for the decoder
	createLookUpTable(weightedList, debug);
    }

    /**
     * Builds Huffman-tree from the given message.
     * 
     * @param message Message to be encoded as a String.
     * @param debug If set to true, function will print out the weighted list.
     * @return Huffman-tree as ArrayList<Node>.
     */
    public ArrayList<Node> buildHuffmanTree(String message, boolean debug) {
	weightedList = TreeBuilder.createWeightedList(message);
	if (debug) {
	    for (Map.Entry<String, Integer> entry : weightedList.entrySet()) {
		System.out.println("Symbol " + entry.getKey() + " : weight "
			+ entry.getValue());
	    }
	}
	ArrayList<Node> nodes = TreeBuilder.createNodeList(weightedList);
	ArrayList<Node> huffmanTree = TreeBuilder.createHuffmanTree(nodes);
	return huffmanTree;
    }

    /**
     * Builds true binary encoded message into Byte[].
     * 
     * @param huffmanTree Huffman-tree as ArrayList<Node>.
     * @param message Message to be encoded as String.
     * @param debug If set to true, function will print out code list.
     * @return Binary encoded message as Byte[].
     */
    public byte[] buildBinaryEncodedMsg(ArrayList<Node> huffmanTree,
	    String message, boolean debug) {
	SortedMap<String, String> codeList = TreeBuilder
		.createCodeList(huffmanTree);
	String stringEncodedMsg = TreeBuilder.createStringEncodedMsg(codeList,
		message);
	encodedMsgLength = stringEncodedMsg.length();
	if (debug) {
	    for (Map.Entry<String, String> entry : codeList.entrySet()) {
		System.out.println("Symbol " + entry.getKey()
			+ " : binarycode " + entry.getValue());
	    }
	}
	byte[] binaryEncodedMsg = TreeBuilder.createBinaryEncodedMsg(codeList,
		stringEncodedMsg);
	return binaryEncodedMsg;
    }

    /**
     * Writes binary encoded message into file. File name syntax is [dd_MM_yyyy] + [_encoded.txt].
     * 
     * @param binaryEncodedMsg Binary encoded message of type Byte[], which will be written into file.
     */
    public void writeBinaryToFile(byte[] binaryEncodedMsg) {
	try {
	    Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat("dd_MM_yyyy");
	    File outputFile = new File(ft.format(dNow) + "_encoded.txt");
	    if (!outputFile.exists()) {
		outputFile.createNewFile();
	    }
	    OutputStream oStream = new FileOutputStream(outputFile);
	    oStream.write(binaryEncodedMsg);
	    oStream.flush();
	    oStream.close();
	    if (encodedMsgLength != 0) {
		System.out.println("Wrote " + encodedMsgLength
			+ " bits (approx. " + encodedMsgLength / 8
			+ " bytes) to file " + outputFile.getAbsolutePath());
	    }
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
    }

    /**
     * Creates a lookUp-file into testiLookup.txt-file, which the decoder can use to easily reconstruct 
     * the Huffman-tree.
     * 
     * BRP == pair separator (eg. symbol 'a' and related weight 10 would be aBRP10, as in "a 'BRP' 10")
     * BRL == pairs separator (eg. multiple pairs separated by BRL would be aBPR10BRLcBRP5 etc.)
     * 
     * @param weightedList Weighted list as SortedMap<String, Integer>.
     */
    public void createLookUpTable(SortedMap<String, Integer> weightedList,
	    boolean debug) {
	StringBuilder lookUpTableContents = new StringBuilder();
	for (Map.Entry<String, Integer> entry : weightedList.entrySet()) {
	    if (!(entry.getKey() == weightedList.lastKey())) {
		lookUpTableContents.append(entry.getKey() + "BRP"
			+ entry.getValue() + "BRL");
	    } else {
		lookUpTableContents.append(entry.getKey() + "BRP"
			+ entry.getValue());
	    }
	}
	File lookUpFile = new File("testiLookUp.txt");
	writeToFile(lookUpFile, lookUpTableContents.toString() + "\r\n"
		+ encodedMsgLength);
	if (debug) {
	    System.out.println("Created lookUp-table in file "
		    + lookUpFile.getAbsolutePath());
	}
    }

    /**
     * Reads in the message from destined file.
     * 
     * @param file Input file.
     * @return Contents of the file as a single String.
     */
    public String readMsgFromFile(File file) {
	if (!file.exists()) {
	    throw new IllegalArgumentException(
		    "File path is illegal or the file doesn't exist");
	}
	BufferedReader bufReader;
	String message = "";
	try {
	    bufReader = new BufferedReader(new FileReader(file));
	    StringBuilder sbBuilder = new StringBuilder();
	    String auxLine = bufReader.readLine();
	    while (auxLine != null) {
		sbBuilder.append(auxLine);
		auxLine = bufReader.readLine();
	    }
	    message = sbBuilder.toString();
	    bufReader.close();

	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	return message;
    }

    /**
     * Writes a given string to destined file. Creates a new file, if the file doesn't exist. Also
     * overwrites the contents in any case.
     * 
     * @param file File, which to write to.
     * @param message String, which will be written to the file.
     */
    public static void writeToFile(File file, String message) {
	if (!file.exists()) {
	    try {
		file.createNewFile();
	    } catch (IOException e) {
		System.err.println("Error creating the file");
	    }
	}
	FileWriter flWriter;
	BufferedWriter bufWriter;
	try {
	    flWriter = new FileWriter(file);
	    bufWriter = new BufferedWriter(flWriter);
	    bufWriter.write(message);
	    bufWriter.close();
	    flWriter.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}