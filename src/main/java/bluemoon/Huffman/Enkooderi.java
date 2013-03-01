package bluemoon.Huffman;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Huffman enkooderi -luokka.
 * 
 * @author Anssi Kinnunen
 */
public class Enkooderi {

    private boolean debug;
    private static int enkoodattuPituus = 0;
    private static SortedMap<String, Integer> frekvenssilista;

    /**
     * @param debug Jos true, näytetään ohjelman toiminnasta tarkempaa tietoa ajon aikana.
     */
    public Enkooderi(boolean debug) {
	this.debug = debug;
    }

    public boolean isDebug() {
	return debug;
    }

    public void setDebug(boolean debug) {
	this.debug = debug;
    }

    /**
     * Päämetodi Huffman-enkoodaamiseen.
     */
    public void enkoodaa(File tiedosto) {
	// 1. Annetaan enkoodattava viesti Filenä
	String viesti = lueTiedosto(tiedosto).replaceAll("\"", "");

	// 2. Konstruoidaan muodostetusta frekvenssilistasta Huffman-puu
	ArrayList<Solmu> huffPuu = rakennaHuffmanPuu(viesti, debug);

	// 3. Muodostetaan Huffman-puusta koodilista, enkoodataan viesti String-muotoiseksi 
	// sekä edelleen byteiksi, lähetetään tiedostovirtaan ja kirjoitetaan levylle
	byte[] binaarikoodi = rakennaBinaarikoodi(huffPuu, viesti, debug);
	kirjoitaBinaariTiedostoon(binaarikoodi);

	// 4. Tehdään dekooderia varten lookup-table
	teeLookUpTaulu(frekvenssilista);
    }

    /**
     * Rakentaa annetuista parametreista Huffman-puun PuunRakentaja-luokan metodeita käyttäen. 
     * Enkoodattava viesti muokataan ensin frekvenssitauluksi, jonka pohjalta luodaan solmuja sisältävä taulu, 
     * josta edelleen luodaan varsinainen Huffman-puu.
     * 
     * @param viesti Enkoodattava viesti Stringinä.
     * @param debug True, mikäli halutaan tulostaa täydellinen frekvenssilistaus.
     * @return Frekvenssilista SortedMap-muotoisena.
     */
    public ArrayList<Solmu> rakennaHuffmanPuu(String viesti, boolean debug) {
	frekvenssilista = PuunRakentaja.frekvenssilistaksi(viesti);
	if (debug) {
	    for (Map.Entry<String, Integer> entry : frekvenssilista.entrySet()) {
		System.out.println("Merkki " + entry.getKey()
			+ " : frekvenssi " + entry.getValue());
	    }
	}
	ArrayList<Solmu> solmut = PuunRakentaja.muodostaSolmut(frekvenssilista);
	ArrayList<Solmu> huffmanPuu = PuunRakentaja.muodostaHuffmanPuu(solmut);
	return huffmanPuu;
    }

    /**
     * Muodostaa annetuista parametreista binäärikoodauksen tavutauluun.
     * 
     * @param huffmanPuu ArrayList-muotoinen Huffman-puu.
     * @param viesti Enkoodattava viesti Stringinä.
     * @param debug True, mikäli halutaan tulostaa täydellinen koodilistaus.
     * @return Viestin binäärikoodaus Byte[]:ssä.
     */
    public byte[] rakennaBinaarikoodi(ArrayList<Solmu> huffmanPuu,
	    String viesti, boolean debug) {
	SortedMap<String, String> koodilista = PuunRakentaja
		.muodostaKoodilista(huffmanPuu);
	String enkoodattuViesti = PuunRakentaja.muodostaEnkoodattuViesti(
		koodilista, viesti);
	enkoodattuPituus = enkoodattuViesti.length();
	if (debug) {
	    for (Map.Entry<String, String> entry : koodilista.entrySet()) {
		System.out.println("Merkki " + entry.getKey()
			+ " : binäärikoodi " + entry.getValue());
	    }
	}
	byte[] binaarienkoodattu = PuunRakentaja.muodostaBinaariEnkoodaus(
		koodilista, enkoodattuViesti);
	return binaarienkoodattu;
    }

    /**
     * Kirjoittaa binäärikoodauksen huffman.txt-tiedostoon.
     * 
     * @param tiedosto Tiedosto, johon kirjoitetaan.
     * @param binaari Byte[], joka kirjoitetaan tiedostoon.
     */
    public void kirjoitaBinaariTiedostoon(byte[] binaari) {
	try {
	    Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat("dd_MM_yyyy");
	    File tiedosto = new File(ft.format(dNow) + "_encoded.txt");
	    if (!tiedosto.exists()) {
		tiedosto.createNewFile();
	    }
	    OutputStream oStream = new FileOutputStream(tiedosto);
	    oStream.write(binaari);
	    oStream.flush();
	    oStream.close();
	    System.out.println("Kirjoitettiin " + enkoodattuPituus
		    + " bittiä (noin " + enkoodattuPituus / 8
		    + " tavua) tiedostoon " + tiedosto.getAbsolutePath());
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
    }

    /**
     * Tekee lookUp-taulun testiLookUp.txt-tiedostoon, josta dekooderi voi helposti lukea merkki-frekvenssi-kombinaatiot 
     * ja muodostaa uuden Huffman-puun niiden perusteella. 
     * 
     * @param frekvenssilista SortedMap-lista, jossa on kaikki kirjaimet ja niiden frekvenssit.
     */
    public void teeLookUpTaulu(SortedMap<String, Integer> frekvenssilista) {
	StringBuilder sBs = new StringBuilder();
	for (Map.Entry<String, Integer> entry : frekvenssilista.entrySet()) {
	    if (!(entry.getKey() == frekvenssilista.lastKey())) {
		sBs.append(entry.getKey() + "BRP" + entry.getValue() + "BRL");
	    } else {
		sBs.append(entry.getKey() + "BRP" + entry.getValue());
	    }
	}
	File tiedosto = new File("testiLookUp.txt");
	kirjoitaTiedostoon(tiedosto, sBs.toString());
	System.out.println("Kirjoitettiin lookup-table tiedostoon "
		+ tiedosto.getAbsolutePath());
    }

    /**
     * Lukee viestin halutusta tiedostosta.
     * 
     * @param tiedosto Tiedosto, josta luetaan.
     * @return Tiedoston sisältö Stringinä.
     */
    public String lueTiedosto(File tiedosto) {
	if (!tiedosto.exists()) {
	    throw new IllegalArgumentException(
		    "Tiedostopolku on virheellinen tai tiedostoa ei ole olemassa");
	}
	BufferedReader lukija;
	String viesti = "";
	try {
	    lukija = new BufferedReader(new FileReader(tiedosto));
	    StringBuilder sBuilder = new StringBuilder();
	    String line = lukija.readLine();
	    while (line != null) {
		sBuilder.append(line);
		line = lukija.readLine();
	    }
	    viesti = sBuilder.toString();
	    lukija.close();

	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	return viesti;
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