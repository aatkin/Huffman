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
     * algoritmin mukaisesti. lisätään alkiot, jotka ovat lehtisolmuja,
     * tree-listaan.
     */
    public static ArrayList<Node> returnHuffTree(ArrayList<Node> nodes) {

	ArrayList<Node> tree = new ArrayList<Node>();
	tree.add(new Node(0));

	while (nodes.size() > 2) {
	    Collections.sort(nodes);
	    Node newParentNode = new Node(0);

	    nodes.get(0).setParent(newParentNode);
	    newParentNode.setLeftChild(nodes.get(0));
	    if (nodes.get(0).getLetter() != null) {
		tree.add(nodes.get(0));
	    }

	    nodes.get(1).setParent(newParentNode);
	    newParentNode.setRightChild(nodes.get(1));
	    if (nodes.get(1).getLetter() != null) {
		tree.add(nodes.get(1));
	    }

	    newParentNode.setWeight(nodes.get(0).getWeight() + nodes.get(1).getWeight());

	    nodes.remove(0);
	    nodes.remove(0);
	    nodes.add(0, newParentNode);
	}

	// jos alkion kirjain ei ole null ja se on viimeinen tai toiseksi
	// viimeinen alkio nodes-listassa, lisätään se tree-listaan
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
	tree.get(0).setWeight(nodes.get(0).getWeight() + nodes.get(1).getWeight());

	return tree;
    } // endof returnHuffTree

    /**
     * @throws IllegalArgumentException
     * 
     *             tekee painotetuista map-arvoista Node-alkiot ja lisää ne
     *             sortattuun arraylistiin
     */
    public static ArrayList<Node> returnNodes(Map<String, Integer> wList) {

	if (wList.size() == 1) {
	    throw new IllegalArgumentException("String must contain two or more different letters");
	}

	ArrayList<Node> nodes = new ArrayList<Node>();

	for (Map.Entry<String, Integer> entry : wList.entrySet()) {
	    nodes.add(new Node(entry.getValue(), entry.getKey()));
	}
	return nodes;
    }

    /**
     * Palauttaa koodilistat huffman-puun lehtisolmuille. Iteroidaan valmista
     * listaa alkio (listassa jokainen alkio vastaa lehtisolmua) kerrallaan, ja
     * käydään jokaisen kohdalla läpi puuta lehtisolmusta juureen, kunnes
     * saadaan oikea koodi, ja tallennetaan se TreeMap-rakenteeseen. Tässä on
     * toistaiseksi koodille käytössä String-esitys.
     */
    public static SortedMap<String, String> returnCodeList(ArrayList<Node> huffTree) {

	SortedMap<String, String> codeList = new TreeMap<String, String>();

	for (Node n : huffTree) {
	    if (n.getParent() != null) {
		String code = "";
		Node temp = n;
		while (temp.getParent() != null) {
		    if (temp == temp.getParent().getLeftChild()) {
			code += "0";
		    } else if (temp == temp.getParent().getRightChild()) {
			code += "1";
		    }
		    temp = temp.getParent();
		}
		// saatu koodaus on päinvastaisessa järjestyksessä, koska se
		// muodostettiin lehtisolmusta juureen,
		// joten se täytyy kääntää
		codeList.put(n.getLetter(), new StringBuffer(code).reverse().toString());
	    }
	}
	return codeList;
    }

    /**
     * Palauttaa enkoodatun viestin String-muodossa.
     */
    public static String returnEncodedMsg(SortedMap<String, String> codeList, String[] msg) {

	StringBuilder encoded = new StringBuilder();

	for (String s : msg) {
	    if (codeList.containsKey(s)) {
		encoded.append(codeList.get(s));
	    }
	}
	return encoded.toString();
    }

    /**
     * Palauttaa enkoodatun viestin tavulistana. Olettaa, että encodedMsg on
     * "bittimerkkijono" (ks. funktio returnEncodedMsg())
     */
    public static byte[] returnBinaryEncodedMsg(SortedMap<String, String> codeList, String encodedMsg) {

	int byteslength = (int) Math.ceil(encodedMsg.length() / 8.0);
	byte[] encoded = new byte[byteslength];
	int msgIndex = 0;

	for (int j = 0; j < encoded.length; j++) {
	    for (int i = 0; i < 8; i++) {
		// jos päästään viimeiseen tavuun ja bitit loppuvat kesken,
		// shiftataan bittejä tavussa
		// vasemmalle 7 - i kertaa ja lopetetaan looppi
		if (msgIndex == encodedMsg.length()) {
		    encoded[j] <<= (7 - i);
		    break;
		}
		// ollaan tavun lopussa ja kohdataan nolla
		else if (encodedMsg.charAt(msgIndex) == '0' && i == 7) {
		    msgIndex++;
		    continue;
		}
		// ollaan tavun lopussa ja kohdataan ykkönen
		else if (encodedMsg.charAt(msgIndex) == '1' && i == 7) {
		    encoded[j] |= 1;
		}
		// ollaan jossain päin tavua ja kohdataan ykkönen
		else if (encodedMsg.charAt(msgIndex) == '1') {
		    encoded[j] |= 1;
		    encoded[j] <<= 1;
		}
		// ollaan jossain päin tavua ja kohdataan nolla
		else {
		    encoded[j] <<= 1;
		}
		// on saatu bitin käsittely loppuun, siirrytään merkkijonossa
		// eteenpäin
		msgIndex++;
	    }
	    // System.out.println("\n####DEBUG####\n");
	    // String msg = "";
	    // for (int h = 7; h >= 0; h--) {
	    // if (((encoded[j] >> h) & 1) == 1) {
	    // msg += "1";
	    // } else {
	    // msg += "0";
	    // }
	    // }
	    // System.out.println("Current binary: " + encoded[j] + " : " +
	    // msg);
	}
	return encoded;
    }
}