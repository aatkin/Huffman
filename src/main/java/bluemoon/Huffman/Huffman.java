package bluemoon.Huffman;

import java.io.*;

/**
 * Turun yliopiston Tietorakenteet ja algoritmit 2 -kurssin harjoitustyö, 28.2.2013.
 * Huffman-koodaus ja siihen oleellisesti liittyvät tietorakenteet. 
 * 
 * @author Anssi Kinnunen
 * @version 0.31415926535
 */
public class Huffman {
    /**
     * @param args args[0] == enc (enkoodaa) TAI dec (dekoodaa)
     * 		   args[1] == polku tiedostoon, josta luetaan
     * 		   args[2] == debug-vipu
     */
    public static void main(String[] args) {
	if (args.length < 2) {
	    System.out
		    .println("Ohje: Määritä joko enkoodaus (enc) tai dekoodaus (dec), polku tiedostoon ja vaihtoehtoinen debug-vipu\n"
			    + "[enc/dec] [polku] [debug]");
	    System.exit(0);
	}
	Enkooderi enkooderi;
	Dekooderi dekooderi;
	boolean debug = false;
	if (args.length > 2 && args[2].toLowerCase().equals("debug")) {
	    debug = true;
	}
	File inputFile = new File(args[1]);
	long timeBefore = System.currentTimeMillis();

	// Enkoodaus, where the magic happens
	if (args[0].toLowerCase().equals("enc")) {
	    enkooderi = new Enkooderi(debug);
	    enkooderi.enkoodaa(inputFile);
	} else if (args[0].toLowerCase().equals("dec")) {
	    dekooderi = new Dekooderi(debug);
	    dekooderi.dekoodaa(inputFile);
	} else {
	    System.out.println("Virhe: Annettu koodausparametri ei ole validi");
	    System.exit(0);
	}
	if (debug) {
	    System.out
		    .println((System.currentTimeMillis() - timeBefore) + "ms");
	}
    }
}