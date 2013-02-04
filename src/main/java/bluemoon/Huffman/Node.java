package bluemoon.Huffman;

/**
 * 
 * @author Anssi Kinnunen
 * 
 *         Luokka solmun esittämistä varten Huffman-puussa.
 * 
 */
public class Node implements Comparable<Node> {

    private Node parent;
    private Node leftChild;
    private Node rightChild;
    private int weight;
    private String letter;
    private boolean isLeafNode;

    public Node(int weight, String letter) {
	isLeafNode = true;
	this.weight = weight;
	this.letter = letter;
    }

    public Node(int weight) {
	isLeafNode = false;
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

    public String getLetter() {
	return letter;
    }

    public boolean isLeaf() {
	return isLeafNode;
    }

    public void setLeaf(boolean value) {
	isLeafNode = value;
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