package bluemoon.Huffman;

import java.util.*;

/**
 * Class for building a Huffman-tree used in Huffman-coding. Class includes methods for converting 
 * a String-based message through auxiliary methods into Huffman-tree, creating code lists from 
 * the Huffman-tree and creating both String- and byte[] encoded messages.
 * 
 * @author Anssi Kinnunen
 */
public class TreeBuilder {

    /**
     * Attaches according weight to each symbol in a SortedMap< String, Integer >. Weight is the symbol's frequency 
     * in the given message.
     * 
     * @throws IllegalArgumentException Message has to be longer than 1
     * @param message String, which contains the message to be encoded.
     * @return A weighted list of the type SortedMap< String, Integer >.
     */
    public static SortedMap<String, Integer> createWeightedList(String message) {
	if (message.length() < 2) {
	    throw new IllegalArgumentException(
		    "Message has to be longer than 1");
	}
	String[] symbols = message.split("");
	SortedMap<String, Integer> weightedList = new TreeMap<String, Integer>();
	// Iterate through the message: starting column is 1, because split("") leaves first column empty
	int symbolWeight = 0;
	for (int i = 1; i < symbols.length; i++) {
	    // If list already contains the symbol, update it's weight
	    if (weightedList.containsKey(symbols[i])) {
		symbolWeight = weightedList.get(symbols[i]) + 1;
		weightedList.put(symbols[i], symbolWeight);
	    }
	    // Else create new symbol with weight of 1
	    else {
		symbolWeight = 1;
		weightedList.put(symbols[i], symbolWeight);
	    }
	}
	return weightedList;
    }

    /**
     * Creates a new node list of type ArrayList< Node > from a weighted list. The node list 
     * contains an unordered group of nodes without links to their parents and children.
     * 
     * @throws IllegalArgumentException Message has to contain more than one different symbol
     * @param weightedList 
     * @return Node list of type ArrayList< Node >.
     */
    public static ArrayList<Node> createNodeList(
	    SortedMap<String, Integer> weightedList) {
	if (weightedList.size() < 2) {
	    throw new IllegalArgumentException(
		    "Message has to contain more than one different symbol");
	}
	ArrayList<Node> nodes = new ArrayList<Node>();
	for (Map.Entry<String, Integer> entry : weightedList.entrySet()) {
	    Node current = new Node(entry.getValue(), entry.getKey());
	    nodes.add(current);
	}
	return nodes;
    }

    /**
     * Builds the Huffman-tree. The algorithm iterates the given node list, sorting the list 
     * by it's nodes' weight into an ascending order in the beginning of each iteration. During 
     * each iteration, the first two nodes are linked to an empty parent node which has no symbol, 
     * but which has the combined weight of it's children. Child nodes are removed from the node list and
     * the parent node is added back to the nodes list. Nodes, which contain an symbol (also called leaf nodes), 
     * are also added to the Huffman-tree ArrayList< Node >, which this function returns. Finally the 
     * root node is added to the beginning of the Huffman-tree.
     * 
     * @param nodes ArrayList< Node >, which contains >= 2 nodes.
     * @return An ordered ArrayList< Node >, which contains the Huffman-tree's root node and all leaf nodes.
     */
    public static ArrayList<Node> createHuffmanTree(ArrayList<Node> nodes) {
	ArrayList<Node> huffmanTree = new ArrayList<Node>();
	huffmanTree.add(new Node(0));

	// Orders the node list into ascending order for each iteration
	while (nodes.size() > 1) {
	    Collections.sort(nodes);
	    Node newParentNode = new Node(0);

	    // Set the node to be it's parent's left child node and add the node to Huffman-tree,
	    // if the node contains an symbol
	    Node leftChild = nodes.get(0);
	    leftChild.setParent(newParentNode);
	    newParentNode.setLeftChild(leftChild);
	    if (leftChild.getSymbol() != null) {
		huffmanTree.add(leftChild);
	    }

	    // Set the node to be it's parent's right child node and add the node to Huffman-tree,
	    // if the node contains an symbol
	    Node rightChild = nodes.get(1);
	    rightChild.setParent(newParentNode);
	    newParentNode.setRightChild(rightChild);
	    if (rightChild.getSymbol() != null) {
		huffmanTree.add(rightChild);
	    }

	    // Set the parent node's weight to be the sum of it's children's weight
	    newParentNode.setWeight(leftChild.getWeight()
		    + rightChild.getWeight());
	    nodes.remove(0);
	    nodes.remove(0);
	    // If this is the last iteration, add the root node to be the first element in Huffman-tree,
	    // else add the parent node to the node list
	    if (nodes.size() == 0) {
		huffmanTree.add(0, newParentNode);
	    } else {
		// HOX! Changing the entry-position for newParentNode in nodes can bring some interesting results in 
		// code tables. Changing it doesn't affect encoding size in any way, only the way code tables are generated.
		// This is most probably due to the way Collections.sort() behaves with current data structure.
		nodes.add(0, newParentNode);
	    }
	} // endof while
	return huffmanTree;
    } // endof returnHuffTree

    /**
     * Creates code list for the leaf nodes of Huffman-tree. The algorithm iterates the
     * tree for each node by traversing from the leaf node to root node, where being left child means 
     * 0 (zero) and right child means 1 (one), since it's a binary tree. Codes are saved to a SortedMap < String, String >.
     *
     * @param huffmanTree Huffman-tree of the type ArrayList< Node >.
     * @return SortedMap A complete code list of type SortedMap < String, String > for the Huffman-tree.
     */
    public static SortedMap<String, String> createCodeList(
	    ArrayList<Node> huffmanTree) {
	SortedMap<String, String> codeList = new TreeMap<String, String>();
	for (Node node : huffmanTree) {
	    // Ignore the root node
	    if (node.getParent() != null) {
		String code = "";
		Node auxNode = node;
		while (auxNode.getParent() != null) {
		    if (auxNode == auxNode.getParent().getLeftChild()) {
			code += "0";
		    } else if (auxNode == auxNode.getParent().getRightChild()) {
			code += "1";
		    }
		    auxNode = auxNode.getParent();
		}
		// Received binary code is in backward order, so we need to reverse it
		codeList.put(node.getSymbol(), new StringBuffer(code).reverse()
			.toString());
	    }
	}
	return codeList;
    }

    /**
     * Encodes the message into binary-looking String (eg. "1001010"). This can be used for simple 
     * tree traversal and also decoding.
     * 
     * @param codeList Code list of type SortedMap< String, String >.
     * @param message The message we want to encode in a String.
     * @return Encoded string that looks like binary.
     */
    public static String createStringEncodedMsg(
	    SortedMap<String, String> codeList, String message) {
	String[] symbols = message.split("");
	StringBuilder encoded = new StringBuilder();
	for (String symbol : symbols) {
	    if (codeList.containsKey(symbol)) {
		// Get the binary value binded to the symbol
		encoded.append(codeList.get(symbol));
	    }
	}
	return encoded.toString();
    }

    /**
     * Encodes the message into true binary using Byte[]. The byte array's size is determined by the 
     * "binary-string's" length. We then iterate the byte array, shifting the bits in each byte according to 
     * the binary-string to determine if the bit is 0 or 1. 
     * 
     * @param codeList Code list of type SortedMap< String, String >.
     * @param stringEncodedMsg String, which contains message encoded into "string-binary" (eg. "1001010").
     * @return Message encoded into true binary of type Byte[].
     */
    public static byte[] createBinaryEncodedMsg(
	    SortedMap<String, String> codeList, String stringEncodedMsg) {
	// Byte[] length is always ceiled, so that no information is lost
	int bytesLength = (int) Math.ceil(stringEncodedMsg.length() / 8.0);
	byte[] binaryEncoded = new byte[bytesLength];
	int msgIndex = 0;
	for (int j = 0; j < binaryEncoded.length; j++) {
	    for (int i = 0; i < 8; i++) {
		// If this is the last byte and we've reached the end of message,
		// we'll shift bits in the byte (7 - i) times to the left and end the loop
		if (msgIndex == stringEncodedMsg.length()) {
		    binaryEncoded[j] <<= (7 - i);
		    break;
		}
		// We're at the end of the byte and we face a zero in the message, move forward in the message
		else if (stringEncodedMsg.charAt(msgIndex) == '0' && i == 7) {
		    msgIndex++;
		    continue;
		}
		// We're at the end of the byte and we face a one in the message
		else if (stringEncodedMsg.charAt(msgIndex) == '1' && i == 7) {
		    binaryEncoded[j] |= 1;
		}
		// We're somewhere in the byte and we face a one
		else if (stringEncodedMsg.charAt(msgIndex) == '1') {
		    binaryEncoded[j] |= 1;
		    binaryEncoded[j] <<= 1;
		}
		// We're somewhere in the byte and we face a zero
		else {
		    binaryEncoded[j] <<= 1;
		}
		// We've handled the current bit, time to move forward in the message
		msgIndex++;
	    } // endof for-loop for current byte
	} // endof for-loop for message
	return binaryEncoded;
    }
}