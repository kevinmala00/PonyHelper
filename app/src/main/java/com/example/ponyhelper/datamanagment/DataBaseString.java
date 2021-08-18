package com.example.ponyhelper.datamanagment;

public class DataBaseString {
    public static final String DBNAME="PONYHELPER";

    public static final String CREATION_ACCOUNT = "CREATE TABLE IF NOT EXISTS ACCOUNT(\n" +
            "username TEXT NOT NULL\n" +
            ", email TEXT NOT NULL\n" +
            ", nome TEXT NOT NULL\n" +
            ", cognome TEXT NOT NULL\n" +
            ", password TEXT NOT NULL\n" +
            ", codice_accesso INTEGER NOT NULL\n" +
            ", PRIMARY KEY(username)\n" +
            ", UNIQUE (email)\n" +
            ");";

    public static final String CREATION_ENTRATE = "CREATE TABLE IF NOT EXISTS ENTRATE(\n" +
            "data TEXT NOT NULL DEFAULT CURRENT_DATE\n" +
            ", ora_inizio TEXT NOT NULL\n" +
            ", ora_fine TEXT NOT NULL\n" +
            ", ore_tot TEXT NOT NULL\n" +
            ", entrate REAL NOT NULL\n" +
            ", mancia REAL NOT NULL DEFAULT 0\n" +
            ", km_percorsi INTEGER NOT NULL DEFAULT 0\n" +
            ", altro TEXT \n" +
            ", PRIMARY KEY (data)\n" +
            ");";

    public static final String CREATION_PRODOTTO = "CREATE TABLE IF NOT EXISTS PRODOTTO(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo REAL NOT NULL\n" +
            ", PRIMARY KEY(nome)\n" +
            ");";

    public static final String CREATION_INGREDIENTE = "CREATE TABLE IF NOT EXISTS INGREDIENTE(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome)\n" +
            ",CHECK(costo_aggiunta>=0)\n" +
            ",CHECK(costo_eliminazione<=0)\n" +
            ");";

    public static final String CREATION_TIPOLOGIA = "CREATE TABLE IF NOT EXISTS TIPOLOGIA(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome)\n" +
            ",CHECK(costo_aggiunta>=0)\n" +
            ",CHECK(costo_eliminazione<=0)\n" +
            ");";

    public static final String CREATION_IMPASTO = "CREATE TABLE IF NOT EXISTS IMPASTO(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome)\n" +
            ",CHECK(costo_aggiunta>=0)\n" +
            ",CHECK(costo_eliminazione<=0)\n" +
            ");";
    public static final String CREATION_PRODOTTO_INGREDIENTE ="CREATE TABLE IF NOT EXISTS PRODOTTO_INGREDIENTE(\n" +
            "nome_prodotto TEXT NOT NULL\n" +
            ", nome_ingrediente TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_ingrediente)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ",FOREIGN KEY (nome_ingrediente) REFERENCES INGREDIENTE(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";
    public static final String CREATION_PRODOTTO_TIPOLOGIA = "CREATE TABLE IF NOT EXISTS PRODOTTO_TIPOLOGIA(\n" +
            "nome_prodotto TEXT NOT NULL\n" +
            ", nome_tipologia TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_tipologia)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ",FOREIGN KEY (nome_tipologia) REFERENCES TIPOLOGIA(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";
    public static final String CREATION_PRODOTTO_IMPASTO = "CREATE TABLE IF NOT EXISTS PRODOTTO_IMPASTO(\n" +
            "nome_prodotto TEXT NOT NULL\n" +
            ", nome_impasto TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_impasto)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ",FOREIGN KEY (nome_impasto) REFERENCES IMPASTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";
    public static final String CREATION_DESTINAZIONE = "CREATE TABLE IF NOT EXISTS DESTINAZIONE(\n" +
            "codice_dest INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            ", data_modifica TEXT NOT NULL DEFAULT CURRENT_DATE\n" +
            ", ora_modifica TEXT NOT NULL DEFAULT CURRENT_TIME\n" +
            ", indirizzo TEXT NOT NULL\n" +
            ", citta TEXT NOT NULL\n" +
            ", cap TEXT NULL\n" +
            ", provincia TEXT NOT NULL\n" +
            ", latitudine REAL NOT NULL\n" +
            ", longitudine REAL NOT NULL\n" +
            ", note  TEXT NULL\n" +
            ", foto BLOB NULL\n" +
            ", UNIQUE (indirizzo, citta, provincia)\n" +
            ");";

    public static final String CREATION_TURNI = "CREATE TABLE IF NOT EXISTS TURNI(\n" +
            "data TEXT PRIMARY KEY\n"+
            ", ora_inizio TEXT NOT NULL\n"+
            ", ora_fine TEXT NOT NULL\n"+
            ");";

    public static  final String SELECT_ALL_ACCOUNT = "SELECT * FROM ACCOUNT";

    static String UPGRADE_SCRIPT="";


}
