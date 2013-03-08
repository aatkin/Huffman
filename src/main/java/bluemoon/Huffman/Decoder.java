package bluemoon.Huffman;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Class for decoding a Huffman-encoded message. Relies heavily on TreeBuilder-class for rebuilding 
 * the Huffman-tree, and also on Encoder-class to create a lookUp-table for decoding purposes. 
 * 
 * @author Anssi Kinnunen
 */
public class Decoder {

    private static int encodedMsgLength = 0;

    public Decoder() {

    }

    /**
     * Decodes the contents of given (binary encoded) file. Utilizes a lookUp-table provided by 
     * Encoder-class. 
     * 
     * @param lookUpFile LookUp-table for decoder.
     */
    public void decode(File lookUpFile, boolean debug) {
	String rawLookUpMsg = readFromLookupFile();
	String parsedLookUpMsg = parseLookupMsg(rawLookUpMsg, "BRL", "BRP");
	ArrayList<Node> huffmanTree = rebuildHuffmanTree(parsedLookUpMsg);
	String decodedMsg = decodeMessage(huffmanTree);
	if (debug) {
	    System.out.println("Decoded message: " + decodedMsg);
	}
	Date dateNow = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
	File tiedosto = new File(dateFormat.format(dateNow) + "_decoded.txt");
	writeToFile(tiedosto, decodedMsg);
    }

    /**
     * Reads data from a pre-determined lookUp-table and returns it as a String.
     * 
     * @return Raw data from the lookUp-table as a String.
     */
    public String readFromLookupFile() {
	File lookUpFile = new File("testiLookUp.txt");
	if (!lookUpFile.exists()) {
	    System.err.println("Error: testiLookUp.txt doesn't exist");
	    System.exit(0);
	}
	BufferedReader bufReader;
	String line = "";
	try {
	    bufReader = new BufferedReader(new FileReader(lookUpFile));
	    line = bufReader.readLine();
	    encodedMsgLength = Integer.parseInt(bufReader.readLine());
	    bufReader.close();
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	return line;
    }

    /**
     * Iterates through the lookUp-table and creates new string, which contains the original message 
     * in random order (the string has same amount of symbols, but they aren't in order).
     * 
     * @param lookUpMsg Parseable message from the lookUp-table as String.
     * @param msgRegex Regex string for splitting lookUp-message into symbol[pairRegex]number -strings.
     * @param pairRegex Regex string for splitting symbol[pairRegex]number-strings into symbols and numbers.
     * @return Parsed lookUp-message, which contains the original message unordered as String.
     */
    public String parseLookupMsg(String lookUpMsg, String msgRegex,
	    String pairRegex) {
	StringBuilder unsortedMsg = new StringBuilder();
	String[] pairs = lookUpMsg.split(msgRegex);
	int amountOfSymbols = 0;
	for (String weightedPair : pairs) {
	    amountOfSymbols = Integer
		    .parseInt(weightedPair.split(pairRegex)[1]);
	    String symbol = weightedPair.split(pairRegex)[0];
	    for (int i = 0; i < amountOfSymbols; i++) {
		unsortedMsg.append(symbol);
	    }
	}
	return unsortedMsg.toString();
    }

    /**
     * Rebuilds the Huffman-tree from the given message.
     * 
     * @param message Message as a String.
     * @return Huffman-tree as ArrayList<Node>.
     */
    public ArrayList<Node> rebuildHuffmanTree(String message) {
	SortedMap<String, Integer> weightedList = TreeBuilder
		.createWeightedList(message);
	ArrayList<Node> nodes = TreeBuilder.createNodeList(weightedList);
	ArrayList<Node> huffmanTree = TreeBuilder.createHuffmanTree(nodes);

	return huffmanTree;
    }

    /**
     * Decodes the encoded message. Reads the binary data written by encoder and decodes it into String 
     * by traversing the Huffman-tree as the original algorithm suggests. This type of decoding, however, is
     * painfully slow and is practically unusable for any text files over 30KB in size.
     * 
     * @param huffmanTree Huffman-tree as ArrayList<Node>.
     * @return Decoded message as a String.
     */
    public String decodeMessage(ArrayList<Node> huffmanTree) {
	Date dateNow = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
	File file = new File(dateFormat.format(dateNow) + "_encoded.txt");
	byte[] binaryEncodedMsg = readBinaryFromFile(file);
	StringBuilder stringEncodedMsg = new StringBuilder();

	// Reads binary input and creates string encoded message accordingly, adding 
	// "1" or "0" to the string
	// binaryEncodedMsg.length
	int auxCalculator = encodedMsgLength;
	for (int i = 0; i < binaryEncodedMsg.length; i++) {
	    for (int j = 7; j >= 0; j--) {
		if (auxCalculator == 0) {
		    break;
		} else if (((binaryEncodedMsg[i] >> j) & 1) == 1) {
		    stringEncodedMsg.append("1");
		} else {
		    stringEncodedMsg.append("0");
		}
		auxCalculator -= 1;
	    }
	}
	// Bit-by-bit-decoding method, "by the book". Painfully slow method for anything useful.
	StringBuilder decodedMsg = new StringBuilder();
	Node currentNode = huffmanTree.get(0);
	for (int i = 0; i < stringEncodedMsg.toString().length(); i++) {
	    if (stringEncodedMsg.charAt(i) == '1') {
		currentNode = currentNode.getRightChild();
	    } else {
		currentNode = currentNode.getLeftChild();
	    }
	    if (currentNode.getSymbol() != null) {
		decodedMsg.append(currentNode.getSymbol());
		currentNode = huffmanTree.get(0);
	    }
	}
	return decodedMsg.toString();
    }

    /**
     * Reads binary data from the given file.
     * 
     * @param file File to read from.
     * @return Binary data as Byte[].
     */
    public byte[] readBinaryFromFile(File file) {
	FileInputStream inStream;
	byte[] binaryMsg = null;
	try {
	    inStream = new FileInputStream(file);
	    int numberOfBytes = inStream.available();
	    binaryMsg = new byte[numberOfBytes];
	    inStream.read(binaryMsg);
	    inStream.close();
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	return binaryMsg;
    }

    /**
     * Writes given content as String to given file.
     * 
     * @param file File to write to.
     * @param message Content of type String which to write.
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