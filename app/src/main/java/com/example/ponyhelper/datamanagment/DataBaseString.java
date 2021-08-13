package com.example.ponyhelper.datamanagment;

public class DataBaseString {
    public static final String DBNAME="PONYHELPER";

    public static final String CREATION_SCRIPT = "CREATE TABLE IF NOT EXISTS PONYHELPER.ACCOUNT(\n" +
            "username TEXT NOT NULL\n" +
            ", email TEXT NOT NULL\n" +
            ", nome TEXT NOT NULL\n" +
            ", cognome TEXT NOT NULL\n" +
            ", password TEXT NOT NULL\n" +
            ", codice_accesso INTEGER NOT NULL DEFAULT 0\n" +
            ", data_nascita TEXT NOT NULL\n" +
            ", PRIMARY KEY(username)\n" +
            ", UNIQUE (email)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.ENTRATE(\n" +
            "data TEXT NOT NULL DEFAULT CURRENT_DATE\n" +
            ", ora_inizio TEXT NOT NULL\n" +
            ", ora_fine TEXT NOT NULL\n" +
            ", ore_tot TEXT NOT NULL\n" +
            ", entrate REAL NOT NULL\n" +
            ", mancia REAL NOT NULL DEFAULT 0\n" +
            ", km_percorsi INTEGER NOT NULL DEFAULT 0\n" +
            ", altro TEXT \n" +
            ", PRIMARY KEY (data)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.PRODOTTO(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo REAL NOT NULL\n" +
            ", PRIMARY KEY(nome)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.INGREDIENTE(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome)\n" +
            ",CHECK(costo_aggiunta>=0)\n" +
            ",CHECK(costo_eliminazione<=0)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.TIPOLOGIA(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome)\n" +
            ",CHECK(costo_aggiunta>=0)\n" +
            ",CHECK(costo_eliminazione<=0)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.IMPASTO(\n" +
            "nome TEXT NOT NULL\n" +
            ", costo_aggiunta REAL NOT NULL DEFAULT 0\n" +
            ", costo_eliminazione REAL NOT NULL DEFAULT 0\n" +
            ", PRIMARY KEY(nome)\n" +
            ",CHECK(costo_aggiunta>=0)\n" +
            ",CHECK(costo_eliminazione<=0)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.PRODOTTO_INGREDIENTE(\n" +
            "nome_prodotto TEXT NOT NULL\n" +
            ", nome_ingrediente TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_ingrediente)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ",FOREIGN KEY (nome_ingrediente) REFERENCES INGREDIENTE(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.PRODOTTO_TIPOLOGIA(\n" +
            "nome_prodotto TEXT NOT NULL\n" +
            ", nome_tipologia TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_ingrediente)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ",FOREIGN KEY (nome_tipologia) REFERENCES TIPOLOGIA(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.PRODOTTO_IMPASTO(\n" +
            "nome_prodotto TEXT NOT NULL\n" +
            ", nome_impasto TEXT NOT NULL\n" +
            ", PRIMARY KEY (nome_prodotto, nome_ingrediente)\n" +
            ", FOREIGN KEY (nome_prodotto) REFERENCES PRODOTTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ",FOREIGN KEY (nome_impasto) REFERENCES IMPASTO(nome) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS PONYHELPER.DESTINAZIONE(\n" +
            "codice_dest INTEGER AUTOINCREMENT NOT NULL\n" +
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
            ", PRIMARY KEY(codice_dest)\n" +
            ", UNIQUE (indirizzo, citta, provincia)\n" +
            ", CHECK(indirizzo LIKE '%,[0-9]%')\n" +
            ");";

    static String UPGRADE_SCRIPT="";


}
