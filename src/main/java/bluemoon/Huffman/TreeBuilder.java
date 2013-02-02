package bluemoon.Huffman;

import java.util.*;

/**
 * 
 * @author Anssi
 * 
 *         Luokka Huffman-puun rakentamista varten. Luokassa on metodit
 *         merkkijonossa esiintyvien kirjainten painojen (todennäköisyys
 *         esiintyä tekstissä) laskemiselle, näiden painojen muuttamista
 *         Nodeiksi ja Nodejen isä-lapsi-suhteiden muokkaamista Huffman-puuksi
 *         varten.
 */
public class TreeBuilder {

    /**
     * tässä väännetään string-taulukosta (jossa mielivaltaisia alkioita n-kpl)
     * treemap, missä alkioon liitetään sen todennäköisyys, siis kuinka monta
     * kertaa se esiintyy kyseisessä taulukossa. for-loop alkaa i = 1:stä, koska
     * jostain syystä String.split()-funkkari tekee taulun ensimmäisestä
     * alkiosta tyhjän. .
     */
    public static SortedMap<String, Integer> returnWeightedList(String[] letters) {

	SortedMap<String, Integer> wList = new TreeMap<String, Integer>();
	int letterProbability = 0;

	for (int i = 1; i < letters.length; i++) {
	    if (wList.containsKey(letters[i])) {
		letterProbability = wList.get(letters[i]) + 1;
		wList.put(letters[i], letterProbability);
	    } else {
		letterProbability = 1;
		wList.put(letters[i], letterProbability);
	    }
	}

	return wList;
    }

    /**
     * rakennetaan huffman-puu: ensimmäinen alkio on tyhjä parent-alkio. ottaa
     * alkiot parametrina annetusta nodes-listasta (joka on järjestetty
     * pienimmästä esiintymämäärästä suurimpaan) ja lisää ne huffman-puuksi
     * algoritmin mukaisesti. lehtisolmussa on aina jokin ei-null alkio, jonka
     * lapset ovat null-alkioita. muissa solmuissa voi olla ei-null alkio, mutta
     * niillä ei välttämättä ole alkiota.
     */
    public static ArrayList<Node> returnHuffTree(ArrayList<Node> nodes) {

	ArrayList<Node> tree = new ArrayList<Node>();

	/*
	 * Iteroidaan nodes-listaa niin kauan, että jäljellä on vain 2 alkiota:
	 * puun vasemman puolen juuri ja puun oikean puolen juuri. Kustakin
	 * juuresta polveutuu sitten enemmän alkioita.
	 */
	while (nodes.size() >= 3) {
	    Node newParent = new Node(0);

	    if (nodes.get(0).getWeight() > (nodes.get(1).getWeight() + nodes.get(2).getWeight())) {

		Node leftChild = nodes.get(1);
		leftChild.setParent(newParent);
		newParent.setLeftChild(leftChild);
		leftChild.setLeaf(true);

		Node rightChild = nodes.get(2);
		rightChild.setParent(newParent);
		newParent.setRightChild(rightChild);
		rightChild.setLeaf(true);

		nodes.remove(leftChild);
		nodes.remove(rightChild);

		newParent.setWeight(leftChild.getWeight() + rightChild.getWeight());

		nodes.add(1, newParent);

	    } else {

		Node leftChild = nodes.get(0);
		leftChild.setParent(newParent);
		newParent.setLeftChild(leftChild);
		leftChild.setLeaf(true);

		Node rightChild = nodes.get(1);
		rightChild.setParent(newParent);
		newParent.setRightChild(rightChild);
		rightChild.setLeaf(true);

		nodes.remove(leftChild);
		nodes.remove(rightChild);

		newParent.setWeight(leftChild.getWeight() + rightChild.getWeight());

		nodes.add(0, newParent);
	    } // endof if-else
	} // endof while

	Node parentNode = new Node(0);

	/*
	 * Lisätään puun ylimpänä olevat (lue: vasemman ja oikean puolen juuret)
	 * alkiot arraylistiin ja määritetään tyhjä alkio koko puun juureksi.
	 */
	tree.add(parentNode);
	tree.add(nodes.get(0));
	tree.add(nodes.get(1));

	parentNode.setLeftChild(tree.get(1));
	tree.get(1).setParent(parentNode);

	parentNode.setRightChild(tree.get(2));
	tree.get(2).setParent(parentNode);

	return tree;
    } // endof returnHuffTree

    /**
     * tekee painotetuista map-arvoista Node-alkiot ja lisää ne sortattuun
     * arraylistiin
     */
    public static ArrayList<Node> returnNodes(Map<String, Integer> wList) {

	ArrayList<Node> nodes = new ArrayList<Node>();

	for (Map.Entry<String, Integer> entry : wList.entrySet()) {
	    nodes.add(new Node(entry.getValue(), entry.getKey()));
	}

	Collections.sort(nodes);
	return nodes;
    }
}