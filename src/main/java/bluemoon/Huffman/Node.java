package bluemoon.Huffman;

/**
 * Class for representing binary tree nodes used in Huffman-coding. Nodes have information about 
 * their parent, left and right children, absolute weight and data (symbol they carry). Nodes can be 
 * compared to each other by their weight.
 * 
 * @author Anssi Kinnunen
 */
public class Node implements Comparable<Node> {

    private Node parent;
    private Node leftChild;
    private Node rightChild;
    private int weight;
    private String symbol;

    public Node(int weight, String symbol) {
	this.weight = weight;
	this.symbol = symbol;
    }

    public Node(int weight) {
	this.weight = weight;
    }

    public Node getParent() {
	return parent;
    }

    public void setParent(Node parent) {
	this.parent = parent;
    }

    public Node getLeftChild() {
	return leftChild;
    }

    public void setLeftChild(Node leftChild) {
	this.leftChild = leftChild;
    }

    public Node getRightChild() {
	return rightChild;
    }

    public void setRightChild(Node rightChild) {
	this.rightChild = rightChild;
    }

    public int getWeight() {
	return weight;
    }

    public void setWeight(int weight) {
	this.weight = weight;
    }

    public String getSymbol() {
	return symbol;
    }

    public int compareTo(Node other) {
	if (other.getWeight() > this.getWeight()) {
	    return -1;
	} else if (other.getWeight() < this.getWeight()) {
	    return 1;
	}
	return 0;
    }
}