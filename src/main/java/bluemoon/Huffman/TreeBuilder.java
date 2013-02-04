package bluemoon.Huffman;

import java.util.*;

/**
 * 
 * @author Anssi Kinnunen
 * 
 *         Luokka Huffman-puun rakentamista varten. Luokassa on metodit
 *         merkkijonossa esiintyvien kirjainten painojen (todennäköisyys
 *         esiintyä tekstissä) laskemiselle, näiden painojen muuttamista
 *         Nodeiksi ja Nodejen isä-lapsi-suhteiden muokkaamista Huffman-puuksi
 *         varten.
 */
public class TreeBuilder {

    /**
     * @throws IllegalArgumentException
     * 
     *             tässä väännetään string-taulukosta (jossa mielivaltaisia
     *             alkioita n-kpl) treemap, missä alkioon liitetään sen
     *             todennäköisyys, siis kuinka monta kertaa se esiintyy
     *             kyseisessä taulukossa. for-loop alkaa i = 1:stä, koska
     *             jostain syystä String.split()-funkkari tekee taulun
     *             ensimmäisestä alkiosta tyhjän. .
     */
    public static SortedMap<String, Integer> returnWeightedList(String[] letters) {

	if (letters.length < 3) {
	    throw new IllegalArgumentException("String must be longer than 1");
	}

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
	tree.add(new Node(0));

	while (nodes.size() > 2) {
	    Collections.sort(nodes);
	    Node tempP = new Node(0);

	    nodes.get(0).setParent(tempP);
	    tempP.setLeftChild(nodes.get(0));
	    if (nodes.get(0).getLetter() != null) {
		tree.add(nodes.get(0));
	    }

	    nodes.get(1).setParent(tempP);
	    tempP.setRightChild(nodes.get(1));
	    if (nodes.get(1).getLetter() != null) {
		tree.add(nodes.get(1));
	    }

	    tempP.setWeight(nodes.get(0).getWeight() + nodes.get(1).getWeight());

	    nodes.remove(0);
	    nodes.remove(0);
	    nodes.add(0, tempP);
	}

	// jos alkion kirjain ei ole null ja se on viimeinen tai toiseksi
	// viimeinen alkio nodes-listassa, lisätään se huffman-puuhun
	if (nodes.get(0).getLetter() != null) {
	    tree.add(nodes.get(0));
	}
	if (nodes.get(1).getLetter() != null) {
	    tree.add(nodes.get(1));
	}

	nodes.get(0).setParent(tree.get(0));
	tree.get(0).setLeftChild(nodes.get(0));

	nodes.get(1).setParent(tree.get(0));
	tree.get(0).setRightChild(nodes.get(1));

	return tree;
    } // endof returnHuffTree

    /**
     * tekee painotetuista map-arvoista Node-alkiot ja lisää ne sortattuun
     * arraylistiin
     */
    public static ArrayList<Node> returnNodes(Map<String, Integer> wList) {

	if (wList.size() == 1) {
	    throw new IllegalArgumentException("String must contain more than one different letters");
	}

	ArrayList<Node> nodes = new ArrayList<Node>();

	for (Map.Entry<String, Integer> entry : wList.entrySet()) {
	    nodes.add(new Node(entry.getValue(), entry.getKey()));
	}

	Collections.sort(nodes);
	return nodes;
    }
}