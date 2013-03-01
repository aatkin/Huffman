package bluemoon.Huffman;

/**
 * Luokka solmun esittämiseksi Huffman-puussa varten. Sisältää metodit solmun 
 * linkitysten (isäsolmu sekä vasen ja oikea lapsisolmu), ja solmun 
 * frekvenssin sekä solmun sisältämän merkin muokkaamiseksi.
 * 
 * @author Anssi Kinnunen
 */
public class Solmu implements Comparable<Solmu> {

    private Solmu vanhempi;
    private Solmu vasenLapsi;
    private Solmu oikeaLapsi;
    private int frekvenssi;
    private String merkki;

    public Solmu(int frekvenssi, String merkki) {
	this.frekvenssi = frekvenssi;
	this.merkki = merkki;
    }

    public Solmu(int frekvenssi) {
	this.frekvenssi = frekvenssi;
    }

    public Solmu annaVanhempi() {
	return vanhempi;
    }

    public void asetaVanhempi(Solmu vanhempi) {
	this.vanhempi = vanhempi;
    }

    public Solmu annaVasenLapsi() {
	return vasenLapsi;
    }

    public void asetaVasenLapsi(Solmu vasenLapsi) {
	this.vasenLapsi = vasenLapsi;
    }

    public Solmu annaOikeaLapsi() {
	return oikeaLapsi;
    }

    public void asetaOikeaLapsi(Solmu oikeaLapsi) {
	this.oikeaLapsi = oikeaLapsi;
    }

    public int annaFrekvenssi() {
	return frekvenssi;
    }

    public void asetaFrekvenssi(int frekvenssi) {
	this.frekvenssi = frekvenssi;
    }

    public String annaMerkki() {
	return merkki;
    }

    public int compareTo(Solmu toinen) {
	if (toinen.annaFrekvenssi() > this.annaFrekvenssi()) {
	    return -1;
	} else if (toinen.annaFrekvenssi() < this.annaFrekvenssi()) {
	    return 1;
	}
	return 0;
    }
}