package bluemoon.Huffman;

import java.util.*;

public class TreeBuilder {

    /**
     * tässä väännetään string-taulukosta (jossa mielivaltaisia alkioita n-kpl)
     * treemap, missä alkioon liitetään sen todennäköisyys, siis kuinka monta
     * kertaa se esiintyy kyseisessä taulukossa. for-loop alkaa i = 1:stä, koska
     * jostain syystä String.split()-funkkari tekee taulun ensimmäisestä
     * alkiosta tyhjän.
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
     * tässä pitäisi tehdä huffman-puu. under construction
     */
    public static ArrayList<Node> returnHuffTree(ArrayList<Node> nodes) {

	ArrayList<Node> tree = new ArrayList<Node>();

	return null;
    }

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