package com.example.ponyhelper.body;

public class Indirizzo {
    int cap;
    String via;
    String civico;
    String provincia;
    String citta;

    /**
     * constructor della classe indirizzo, istanzia un nuovo oggetto indirizzo con
     * attribuendo come valori degli attributi i corrispettivi parametri passati
     *
     * @param via       devo contenere l'indirizzo
     * @param cap       deve contenere il codice di avviamento postale delle città
     * @param citta     deve contenere la città
     * @param civico    deve comtenere il numero civico legato all'inidrizzo
     * @param provincia deve contenere la provincia di cui fa parte la città
     */
    public Indirizzo(String via, String civico, String citta, String provincia, int cap) {
        super();
        this.cap = cap;
        this.via = via;
        this.provincia = provincia;
        this.civico = civico;
        this.citta = citta;
    }

    /**
     * constructor della classe indirizzo, istanzia un nuovo oggetto di tipo indirizzo vuoto
     */
    public Indirizzo(){
        super();
    }

    /**
     * getter dell'attributo cap
     *
     * @return ritorna il cap di un'istanza di un oggetto indirizzo
     */
    public int getCap() {
        return cap;
    }

    /**
     * setter dell'attributo cap
     *
     * @param cap contiene il cap di un indirizzo, con cui verrà settata l'istanza di un oggetto indirizzo
     */
    public void setCap(int cap) {
        this.cap = cap;
    }

    /**
     * getter dell'attributo via
     *
     * @return ritorna la via di un'istanza di un oggetto indirizzo
     */
    public String getVia() {
        return via;
    }

    /**
     * setter dell'attributo via
     *
     * @param via contiene la via di un indirizzo, con cui verrà settata l'istanza di un oggetto indirizzo
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * getter dell'attributo provincia
     *
     * @return ritorna la provincia di un'istanza di un oggetto indirizzo
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * setter dell'attributo provincia
     *
     * @param provincia contiene la provincia di un indirizzo, con cui verrà settata l'istanza di un oggetto indirizzo
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * getter dell'attributo città
     *
     * @return ritorna la città di un'istanza di un oggetto indirizzo
     */
    public String getCitta() {
        return citta;
    }

    /**
     * setter dell'attributo città
     *
     * @param citta contiene la città di un indirizzo, con cui verrà settata l'istanza di un oggetto indirizzo
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * toString della classe indirizzo
     *
     * @return ritorna la conversione in stringa delle istanze degli attributi dell'oggetto
     */
    @Override
    public String toString() {
        return "com.example.ponyhelper.appclass.Indirizzo{" +
                "cap=" + cap +
                ", via='" + via + '\'' +
                ", civico='" + civico + '\'' +
                ", città='" + citta + '\'' +
                ", provincia='" + provincia + '\'' +
                '}';
    }

    /**
     * metodo toStringFullAddress
     * @return ritorna una stringa contenente l'dirizzo completo ovvero via e numero civico
     */
    public String toStringFullAddress(){
        return via + ", " + civico;
    }
}
