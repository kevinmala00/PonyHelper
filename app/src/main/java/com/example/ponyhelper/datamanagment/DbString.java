package com.example.ponyhelper.datamanagment;

public class DbString {
    /**
     * stringa nome del database
     */
    public static final String DBNAME="PONYHELPER";

    /**
     * strigna di creazione della tabella account
     */
    public static final String CREATION_ACCOUNT = "CREATE TABLE IF NOT EXISTS ACCOUNT(\n" +
            "username TEXT NOT NULL\n" +
            ", email TEXT NOT NULL\n" +
            ", nome TEXT NOT NULL\n" +
            ", cognome TEXT NOT NULL\n" +
            ", password TEXT NOT NULL\n" +
            ", codice_accesso INTEGER NOT NULL\n" +
            ", attivo INTEGER NOT NULL" +
            ", PRIMARY KEY(username)\n" +
            ", UNIQUE (email)\n" +
            ");";

    /**
     * strigna di creazione della tabella entrate
     */
    public static final String CREATION_ENTRATE = "CREATE TABLE IF NOT EXISTS ENTRATE(\n" +
            "username TEXT NOT NULL\n" +
            ", data INTEGER NOT NULL DEFAULT CURRENT_DATE\n" +
            ", ora_inizio TEXT NOT NULL\n" +
            ", ora_fine TEXT NOT NULL\n" +
            ", ore_tot TEXT NOT NULL\n" +
            ", entrate REAL NOT NULL\n" +
            ", mancia REAL NOT NULL DEFAULT 0\n" +
            ", km_percorsi INTEGER NOT NULL DEFAULT 0\n" +
            ", altro TEXT \n" +
            ", PRIMARY KEY (data, username)\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";

    /**
     * strigna di creazione della tabella prodotto
     */
    public static final String CREATION_PRODOTTO = "CREATE TABLE IF NOT EXISTS PRODOTTO(\n" +
            "username TEXT NOT NULL\n" +
            ", nome TEXT NOT NULL\n" +
            ", costo REAL NOT NULL\n" +
            ", PRIMARY KEY(nome, username)\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";

    /**
     * strigna di creazione della tabella ingrediente
     */
    public static final String CREATION_INGREDIENTE = "CREATE TABLE IF NOT EXISTS INGREDIENTE(\n" +
            "username TEXT NOT NULL\n" +
            ", nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome, username)\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", CHECK(costo_aggiunta>=0)\n" +
            ", CHECK(costo_eliminazione<=0)\n" +
            ");";

    /**
     * strigna di creazione della tabella tipologia
     */
    public static final String CREATION_TIPOLOGIA = "CREATE TABLE IF NOT EXISTS TIPOLOGIA(\n" +
            "username TEXT NOT NULL\n" +
            ", nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome, username)\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", CHECK(costo_aggiunta>=0)\n" +
            ", CHECK(costo_eliminazione<=0)\n" +
            ");";

    /**
     * strigna di creazione della tabella impasto
     */
    public static final String CREATION_IMPASTO = "CREATE TABLE IF NOT EXISTS IMPASTO(\n" +
            "username TEXT NOT NULL\n" +
            ", nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome, username)\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", CHECK(costo_aggiunta>=0)\n" +
            ", CHECK(costo_eliminazione<=0)\n" +
            ");";

    /**
     * strigna di creazione della tabella prodotto ingrediente
     * rappresenta gli ingredienti cjhe compongono un determinato prodotto
     */
    public static final String CREATION_PRODOTTO_INGREDIENTE ="CREATE TABLE IF NOT EXISTS PRODOTTO_INGREDIENTE(\n" +
            "username TEXT NOT NULL\n" +
            ", nome_prodotto TEXT NOT NULL\n" +
            ", nome_ingrediente TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_ingrediente, username)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", FOREIGN KEY (nome_ingrediente) REFERENCES INGREDIENTE(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";

    /**
     * strigna di creazione della tabella prodotto tipologia
     * rappresenta di che tipologia è fatto un determinato prodotto
     */
    public static final String CREATION_PRODOTTO_TIPOLOGIA = "CREATE TABLE IF NOT EXISTS PRODOTTO_TIPOLOGIA(\n" +
            "username TEXT NOT NULL\n" +
            ", nome_prodotto TEXT NOT NULL\n" +
            ", nome_tipologia TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_tipologia, username)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", FOREIGN KEY (nome_tipologia) REFERENCES TIPOLOGIA(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";

    /**
     * strigna di creazione della tabella prodotto impasto
     * rappresenta di che impasto è fatto un determinato prodotto
     */
    public static final String CREATION_PRODOTTO_IMPASTO = "CREATE TABLE IF NOT EXISTS PRODOTTO_IMPASTO(\n" +
            "username TEXT NOT NULL\n" +
            ", nome_prodotto TEXT NOT NULL\n" +
            ", nome_impasto TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_impasto, username)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", FOREIGN KEY (nome_impasto) REFERENCES IMPASTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";

    /**
     * strigna di creazione della tabella destinazione
     */
    public static final String CREATION_DESTINAZIONE = "CREATE TABLE IF NOT EXISTS DESTINAZIONE(\n" +
            "username TEXT NOT NULL\n" +
            ", codice_dest INTEGER NOT NULL\n" +
            ", data_modifica INTEGER DEFAULT CURRENT_DATE\n" +
            ", ora_modifica TEXT DEFAULT CURRENT_TIME\n" +
            ", indirizzo TEXT NOT NULL\n" +
            ", citta TEXT NOT NULL\n" +
            ", cap TEXT NULL\n" +
            ", provincia TEXT NOT NULL\n" +
            ", latitudine REAL NOT NULL\n" +
            ", longitudine REAL NOT NULL\n" +
            ", note  TEXT NULL\n" +
            ", foto BLOB NULL\n" +
            ", PRIMARY KEY(username, codice_dest)" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ", UNIQUE (indirizzo, citta, provincia)\n" +
            ");";

    /**
     * stringa di creazione della tabella turni
     */
    public static final String CREATION_TURNI = "CREATE TABLE IF NOT EXISTS TURNI(\n" +
            "username TEXT NOT NULL\n" +
            ", data INTEGER NOT NULL\n" +
            ", ora_inizio TEXT NOT NULL\n" +
            ", ora_fine TEXT NOT NULL\n" +
            ", PRIMARY KEY(username, data)\n" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";

    /**
     * stringa di creazione della tabella costi
     */
    public static final String CREATION_COSTI_CONSUMI = "CREATE TABLE IF NOT EXISTS COSTI_CONSUMI(\n" +
            "username TEXT NOT NULL\n" +
            ", mese_anno TEXT NOT NULL" +
            ", costo_carburante REAL DEFAULT 0\n" +
            ", consumo_medio REAL DEFAULT 0\n" +
            ", PRIMARY KEY(username, mese_anno)" +
            ", FOREIGN KEY (username) REFERENCES ACCOUNT(username) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");";

    /**
     * stringa che oermettte di selezionare tutti i record relativi agli account
     */
    public static  final String SELECT_ALL_ACCOUNT = "SELECT * FROM ACCOUNT";

    /**
     * stringa che permette di selezionare tutte le entrate di un determinato mese e account,
     * ordinate per data in senso crescente
     */
    public static String selectAllEntrateMese="SELECT * FROM ENTRATE WHERE (username LIKE ? AND data BETWEEN ? AND ?) ORDER BY data ASC;";

    /**
     * seleziona una entrata a partire da una data passata
     */
    public static String selectEntrata="SELECT * FROM ENTRATE WHERE (username LIKE ? AND data = ?) LIMIT 1;";

    /**
     * seleziona i turni di una data settimana
     */
    public static String selectTurniSettimanali = "SELECT * FROM TURNI WHERE (username LIKE ? AND data BETWEEN ? AND ?);";

    /**
     * seleziona i costi e i consumi
     */
    public static String selectCostiMensili =  "SELECT * FROM COSTI_CONSUMI WHERE (username LIKE ? AND mese_anno LIKE ?) LIMIT 1;";

    /**
     * Seleziona l'account attivo
     */
    public static String selectActiveAccount = "SELECT * FROM ACCOUNT WHERE attivo = 1 LIMIT 1;";

    public static String selectAccount = "SELECT * FROM ACCOUNT WHERE username LIKE ? LIMIT 1;";

    public static String checkPresenzaTurno = "SELECT EXISTS(SELECT * FROM TURNI WHERE data LIKE ? AND username LIKE ?)";

    static String UPGRADE_SCRIPT="";


}
