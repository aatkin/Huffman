package bluemoon.Huffman;

import java.util.*;

/**
 * Luokka Huffman-puun rakentamista varten. Luokassa on metodit
 * Stringissä esiintyvien kirjainten painojen (frekvenssi) laskemiselle, 
 * näiden painojen muuttamista Nodeiksi (Solmu) ja Nodejen isä-lapsi-suhteiden 
 * muokkaamista Huffman-puuksi varten.
 * 
 * @author Anssi Kinnunen
 */
public class PuunRakentaja {
    /**
     * Liitetään String-avaimeen arvoksi kokonaisluku frekvenssi, eli kuinka monta 
     * kertaa avain esiintyy parametrina annetussa Stringissä. SortedMap:iin ei tule
     * duplikaattiavaimia.
     * 
     * @throws IllegalArgumentException Stringin pituus on pienempi kuin 2.
     * @param merkit String, jossa on enkoodattava viesti.
     * @return Tyyppiä SortedMap oleva frekvenssilista.
     */
    public static SortedMap<String, Integer> frekvenssilistaksi(
	    String merkkijono) {
	if (merkkijono.length() < 2) {
	    throw new IllegalArgumentException(
		    "Merkkijonon täytyy olla suurempi kuin 1");
	}
	String[] merkit = merkkijono.split("");
	SortedMap<String, Integer> frekvenssilista = new TreeMap<String, Integer>();
	int kirjainFrekvenssi = 0;
	for (int i = 1; i < merkit.length; i++) {
	    if (frekvenssilista.containsKey(merkit[i])) {
		kirjainFrekvenssi = frekvenssilista.get(merkit[i]) + 1;
		frekvenssilista.put(merkit[i], kirjainFrekvenssi);
	    } else {
		kirjainFrekvenssi = 1;
		frekvenssilista.put(merkit[i], kirjainFrekvenssi);
	    }
	}
	return frekvenssilista;
    }

    /**
     * Muodostaa parametrina annetusta frekvenssitaulusta ArrayListin,
     * joka sisältää linkittämättömiä solmuja.
     * 
     * @throws IllegalArgumentException Stringissä alle kahta erilaista merkkiä.
     * @param frekvenssilista
     * @return Solmuja sisältävä ArrayList, josta voidaan rakentaa Huffman-puu.
     */
    public static ArrayList<Solmu> muodostaSolmut(
	    SortedMap<String, Integer> frekvenssilista) {
	if (frekvenssilista.size() < 2) {
	    throw new IllegalArgumentException(
		    "Merkkijonossa täytyy olla vähintään kahta erilaista merkkiä");
	}
	ArrayList<Solmu> solmut = new ArrayList<Solmu>();
	for (Map.Entry<String, Integer> entry : frekvenssilista.entrySet()) {
	    solmut.add(new Solmu(entry.getValue(), entry.getKey()));
	}
	return solmut;
    }

    /**
     * Muodostetaan Huffman-puu. Algoritmi ottaa solmut parametrina annetusta ArrayLististä
     * ja muodostaa niistä Huffman-puun dokumentoinnissa esitetyn algoritmin mukaisesti.
     * 
     * @param solmut ArrayList, jossa on solmualkioita n >= 2 kpl.
     * @return Järjestetty ArrayList, jossa on Huffman-puun juurisolmu sekä kaikki lehtisolmut linkkeineen.
     */
    public static ArrayList<Solmu> muodostaHuffmanPuu(ArrayList<Solmu> solmut) {
	ArrayList<Solmu> huffmanPuu = new ArrayList<Solmu>();
	huffmanPuu.add(new Solmu(0));

	// Järjestää solmut-taulun aina joka iteraation aluksi,
	// jotta solmut ovat kasvavassa järjestyksessä
	while (solmut.size() > 2) {
	    Collections.sort(solmut);
	    Solmu newParentNode = new Solmu(0);

	    // Asetetaan solmu isänsä vasemmaksi lapsisolmuksi ja 
	    // lisätään huffmanPuu:hun, mikäli solmu on lehtisolmu
	    solmut.get(0).asetaVanhempi(newParentNode);
	    newParentNode.asetaVasenLapsi(solmut.get(0));
	    if (solmut.get(0).annaMerkki() != null) {
		huffmanPuu.add(solmut.get(0));
	    }

	    // Asetetaan solmu isänsä oikeaksi lapsisolmuksi ja 
	    // lisätään huffmanPuu:hun, mikäli solmu on lehtisolmu
	    solmut.get(1).asetaVanhempi(newParentNode);
	    newParentNode.asetaOikeaLapsi(solmut.get(1));
	    if (solmut.get(1).annaMerkki() != null) {
		huffmanPuu.add(solmut.get(1));
	    }

	    // Asetetaan isän painoksi sen lapsien painojen summa
	    newParentNode.asetaFrekvenssi(solmut.get(0).annaFrekvenssi()
		    + solmut.get(1).annaFrekvenssi());
	    solmut.remove(0);
	    solmut.remove(0);
	    solmut.add(0, newParentNode);
	} // endof while

	Collections.sort(solmut);

	// Jos solmun sisältämä merkki ei ole null ja solmu on viimeinen tai toiseksi
	// viimeinen solmut-taulussa, lisätään solmu huffmanPuu:hun
	if (solmut.get(0).annaMerkki() != null) {
	    huffmanPuu.add(solmut.get(0));
	}
	if (solmut.get(1).annaMerkki() != null) {
	    huffmanPuu.add(solmut.get(1));
	}

	// Asetetaan jäljelle jääneet solmut juurisolmun lapsiksi, ja 
	// asetetaan juurisolmulle koko puun paino.
	solmut.get(0).asetaVanhempi(huffmanPuu.get(0));
	huffmanPuu.get(0).asetaVasenLapsi(solmut.get(0));
	solmut.get(1).asetaVanhempi(huffmanPuu.get(0));
	huffmanPuu.get(0).asetaOikeaLapsi(solmut.get(1));
	huffmanPuu.get(0)
		.asetaFrekvenssi(
			solmut.get(0).annaFrekvenssi()
				+ solmut.get(1).annaFrekvenssi());
	return huffmanPuu;
    } // endof returnHuffTree

    /**
     * Palauttaa koodilistat Huffman-puun lehtisolmuille. Iteroi valmista
     * ArrayListiä solmu kerrallaan (listassa jokainen alkio vastaa lehtisolmua) ja
     * käy jokaisen solmun kohdalla puuta läpi kyseisestä solmusta juureen, kunnes
     * saadaan oikea koodi. Koodit tallennetaan SortedMap-rakenteeseen.
     *
     * @param huffmanPuu ArrayList-muotoinen Huffman-puu.
     * @return SortedMap koodilista, jossa jokaista Stringiä vastaa jokin String-muotoinen binääriluku.
     */
    public static SortedMap<String, String> muodostaKoodilista(
	    ArrayList<Solmu> huffmanPuu) {
	SortedMap<String, String> koodilista = new TreeMap<String, String>();
	for (Solmu solmu : huffmanPuu) {
	    if (solmu.annaVanhempi() != null) {
		String koodi = "";
		Solmu apusolmu = solmu;
		while (apusolmu.annaVanhempi() != null) {
		    if (apusolmu == apusolmu.annaVanhempi().annaVasenLapsi()) {
			koodi += "0";
		    } else if (apusolmu == apusolmu.annaVanhempi()
			    .annaOikeaLapsi()) {
			koodi += "1";
		    }
		    apusolmu = apusolmu.annaVanhempi();
		}
		// Saatu koodaus on päinvastaisessa järjestyksessä, joten se täytyy kääntää
		koodilista.put(solmu.annaMerkki(), new StringBuffer(koodi)
			.reverse().toString());
	    }
	}
	return koodilista;
    }

    /**
     * Palauttaa enkoodatun viestin Stringinä.
     * 
     * @param koodilista SortedMap-muotoinen koodilista.
     * @param merkkijono Alkuperäinen viesti String-muotoisena.
     * @return Binääriksi enkoodattu viesti String-muotoisena.
     */
    public static String muodostaEnkoodattuViesti(
	    SortedMap<String, String> koodilista, String merkit) {
	String[] merkkijono = merkit.split("");
	StringBuilder enkoodattu = new StringBuilder();
	for (String merkki : merkkijono) {
	    if (koodilista.containsKey(merkki)) {
		enkoodattu.append(koodilista.get(merkki));
	    }
	}
	return enkoodattu.toString();
    }

    /**
     * Palauttaa enkoodatun viestin Byte[]:nä.
     * 
     * @param koodilista SortedMap-muotoinen koodilista.
     * @param enkoodattuViesti String-muotoinen, binäärienkoodattu viesti (ks. funktio muodostaEnkoodattuViesti() ylempänä).
     * @return Byte[]:ksi enkoodattu viesti.
     */
    public static byte[] muodostaBinaariEnkoodaus(
	    SortedMap<String, String> koodilista, String enkoodattuViesti) {
	int tavujenMaara = (int) Math.ceil(enkoodattuViesti.length() / 8.0);
	byte[] enkoodattu = new byte[tavujenMaara];
	int viestinIndeksi = 0;
	for (int j = 0; j < enkoodattu.length; j++) {
	    for (int i = 0; i < 8; i++) {
		// Jos päästään viimeiseen tavuun ja bitit loppuvat kesken,
		// siirretään bittejä tavussa vasemmalle (7 - i) kertaa ja lopetetaan silmukka
		if (viestinIndeksi == enkoodattuViesti.length()) {
		    enkoodattu[j] <<= (7 - i);
		    break;
		}
		// Ollaan tavun lopussa ja kohdataan nolla, siirrytään viestissä eteenpäin
		else if (enkoodattuViesti.charAt(viestinIndeksi) == '0'
			&& i == 7) {
		    viestinIndeksi++;
		    continue;
		}
		// Ollaan tavun lopussa ja kohdataan ykkönen
		else if (enkoodattuViesti.charAt(viestinIndeksi) == '1'
			&& i == 7) {
		    enkoodattu[j] |= 1;
		}
		// Ollaan jossain päin tavua ja kohdataan ykkönen
		else if (enkoodattuViesti.charAt(viestinIndeksi) == '1') {
		    enkoodattu[j] |= 1;
		    enkoodattu[j] <<= 1;
		}
		// Ollaan jossain päin tavua ja kohdataan nolla
		else {
		    enkoodattu[j] <<= 1;
		}
		// On saatu bitin käsittely loppuun, siirrytään viestissä eteenpäin
		viestinIndeksi++;
	    }
	}
	return enkoodattu;
    }
}