package bluemoon.Huffman;

import java.io.*;

/**
 * 08.03.2013
 * Practical course work in the course Data structures and algorithms 2 in University of Turku.
 * Huffman coding in Java using strings. 
 * 
 * @author Anssi Kinnunen
 * @version 0.9
 */
public class Huffman {
    /**
     * @param args args[0] == enc (encode) or dec (decode)
     * 		   args[1] == path to file which contains message to encode or lookUp-table for decoder
     * 		   args[2] == debug-option
     */
    public static void main(String[] args) {
	long timeBefore = System.currentTimeMillis();
	validateParameters(args);
	boolean debug = false;
	if (args.length > 2 && args[2].toLowerCase().equals("debug")) {
	    debug = true;
	}
	File inputFile = new File(args[1]);
	if (args[0].toLowerCase().equals("enc")) {
	    Encoder encoder = new Encoder();
	    encoder.encode(inputFile, debug);
	} else {
	    Decoder decoder = new Decoder();
	    decoder.decode(inputFile, debug);
	}
	if (debug) {
	    System.out
		    .println((System.currentTimeMillis() - timeBefore) + "ms");
	}
    }

    public static void validateParameters(String[] args) {
	if (args.length < 2) {
	    System.out
		    .println("Instruction: Define either encoding (enc) or decoding (dec), path to the file to encode or path to the lookUp table -file for decoder and optional debug\n"
			    + "[enc/dec] [path] [debug]");
	    System.exit(0);
	} else if (args[0].toLowerCase().equals("enc")
		&& args[0].toLowerCase().equals("dec")) {
	    System.out.println(args[0]);
	    System.out
		    .println("Error: Given coding (encode or decode) parameter is invalid\nUse either enc or dec");
	    System.exit(0);
	}
    }
}