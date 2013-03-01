package bluemoon.Huffman;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Huffman-dekooderi -luokka. Hyödyntää enkooderin luomaa lookup-taulua rakentaakseen Huffman-puun uudelleen
 * ja siten muodostaakseen alkuperäisen viestin.
 * 
 * @author Anssi Kinnunen
 */
public class Dekooderi {

    private boolean debug = false;

    public Dekooderi(boolean debug) {
	this.debug = debug;
    }

    /**
     * Dekoodaus-luokan päämetodi. Toimii vähän kuin enkoodaus, mutta tekee vähän päinvastaisessa järjestyksessä.
     * 
     * @param lookUpTiedosto
     */
    public void dekoodaa(File lookUpTiedosto) {
	// Lukee lookup-taulun läpi, parsii sen sisällön ja luo sen pohjalta huffman-puun uudelleen
	String luettava = lueLookupTaulusta(lookUpTiedosto);
	String testattava = parsiLookupTaulu(luettava, "BRL", "BRP");
	ArrayList<Solmu> huffPuu = uudelleenRakennaHuffPuu(testattava);

	// Dekoodaa hienosti viestin. GJ!
	String dekoodattuViesti = dekoodaaViesti(huffPuu);
	if (debug) {
	    System.out.println("Dekoodattu viesti: " + dekoodattuViesti);
	}
	Date dNow = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("dd_MM_yyyy");
	File tiedosto = new File(ft.format(dNow) + "_decoded.txt");
	kirjoitaTiedostoon(tiedosto, dekoodattuViesti);
    }

    /**
     * Lukee enkooderin luomasta lookup-taulusta tiedot ja palauttaa ne.
     * 
     * @param tiedosto File, jossa on enkooderin luoma lookup-taulu.
     * @return String-muotoisen tekstin, jossa on parsittavaa dataa.
     */
    public String lueLookupTaulusta(File tiedosto) {
	BufferedReader lukija;
	StringBuilder sBbuilder = new StringBuilder();
	String apurivi = "";
	try {
	    lukija = new BufferedReader(new FileReader(tiedosto));
	    while ((apurivi = lukija.readLine()) != null) {
		sBbuilder.append(apurivi);
	    }
	    lukija.close();
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	return sBbuilder.toString();
    }

    /**
     * Käy läpi lookup-taulusta löytyvät avain-arvo-parit ja luo uuden Stringin, johon se lisää 
     * avaimia iteratiivisesti arvon osoittaman määrän.
     * 
     * @param rivi String-muotoinen lookup-taulu viesti.
     * @param lineRegex Parsittavaan tekstiin liittyvä stop-merkki(?), joiden kohdalta teksti pilkotaan.
     * @return String-muotoon parsittu viesti, jonka kirjaimet ovat toistaiseksi epäjärjestyksessä.
     */
    public String parsiLookupTaulu(String rivi, String lineRegex,
	    String pairRegex) {
	StringBuilder sBuilder = new StringBuilder();
	String[] riviSplit = rivi.split(lineRegex);
	int lukumaara = 0;
	for (String s : riviSplit) {
	    lukumaara = Integer.parseInt(s.split(pairRegex)[1]);
	    for (int i = 0; i < lukumaara; i++) {
		sBuilder.append(s.split(pairRegex)[0]);
	    }
	}
	return sBuilder.toString();
    }

    /**
     * Rakentaa huffman-puun uudelleen epäjärjestetyn viestin perusteella.
     * 
     * @param viesti
     * @return
     */
    public ArrayList<Solmu> uudelleenRakennaHuffPuu(String viesti) {
	SortedMap<String, Integer> frekvenssilista = PuunRakentaja
		.frekvenssilistaksi(viesti);
	ArrayList<Solmu> solmut = PuunRakentaja.muodostaSolmut(frekvenssilista);
	ArrayList<Solmu> huffPuu = PuunRakentaja.muodostaHuffmanPuu(solmut);

	return huffPuu;
    }

    /**
     * Dekoodaa viestin lukemalla binääridatan virrasta ja konvertoi sen Stringiksi.
     * 
     * @param huffPuu
     * @return
     */
    public String dekoodaaViesti(ArrayList<Solmu> huffPuu) {
	Date dNow = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("dd_MM_yyyy");
	File tiedosto = new File(ft.format(dNow) + "_encoded.txt");
	byte[] enkoodattu = lueBinaariTiedostosta(tiedosto);

	StringBuilder binaariTeksti = new StringBuilder();

	for (int i = 0; i < enkoodattu.length; i++) {
	    for (int j = 7; j >= 0; j--) {
		if (((enkoodattu[i] >> j) & 1) == 1) {
		    binaariTeksti.append("1");
		} else {
		    binaariTeksti.append("0");
		}
	    }
	}
	SortedMap<String, String> koodilista = PuunRakentaja
		.muodostaKoodilista(huffPuu);

	StringBuilder dekoodattu = new StringBuilder();
	StringBuilder apuBuilder = new StringBuilder();
	Solmu nykyinen = huffPuu.get(0);

	for (int i = 0; i < binaariTeksti.toString().length(); i++) {
	    if (binaariTeksti.charAt(i) == '1') {
		nykyinen = nykyinen.annaOikeaLapsi();
	    } else {
		nykyinen = nykyinen.annaVasenLapsi();
	    }
	    if (nykyinen.annaMerkki() != null) {
		dekoodattu.append(nykyinen.annaMerkki());
		nykyinen = huffPuu.get(0);
	    }
	}

	//	for (int i = 0; i < binaariTeksti.toString().length(); i++) {
	//	    apuBuilder.append(binaariTeksti.toString().charAt(i));
	//	    for (Map.Entry<String, String> entry : koodilista.entrySet()) {
	//		if (entry.getValue().equals(apuBuilder.toString())) {
	//		    dekoodattu.append(entry.getKey());
	//		    apuBuilder = new StringBuilder();
	//		    break;
	//		}
	//	    }
	//	}
	return dekoodattu.toString();
    }

    /**
     * Lukee binääridatan virrasta kivasti käytettäväksi.
     * 
     * @param tiedosto
     * @return
     */
    public byte[] lueBinaariTiedostosta(File tiedosto) {
	FileInputStream inStream;
	byte[] binaari = null;
	try {
	    inStream = new FileInputStream(tiedosto);
	    int numberBytes = inStream.available();
	    binaari = new byte[numberBytes];
	    inStream.read(binaari);
	    inStream.close();
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	return binaari;
    }

    public static void kirjoitaTiedostoon(File tiedosto, String kirjoitettava) {
	if (!tiedosto.exists()) {
	    try {
		tiedosto.createNewFile();
	    } catch (IOException e) {
		System.err.println("Virhe tiedostoa luotaessa");
	    }
	}
	FileWriter fw;
	BufferedWriter bw;
	try {
	    fw = new FileWriter(tiedosto);
	    bw = new BufferedWriter(fw);
	    bw.write(kirjoitettava);
	    bw.close();
	    fw.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}