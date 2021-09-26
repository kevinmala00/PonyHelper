package com.example.ponyhelper.datamanagment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ponyhelper.body.Costo;
import com.example.ponyhelper.body.Destinazione;
import com.example.ponyhelper.body.Entrata;
import com.example.ponyhelper.body.Indirizzo;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.body.Turno;
import com.example.ponyhelper.util.UtilClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * classe che permette la creazione e la gestione del database Sqlite
 */
public class DbHelper extends SQLiteOpenHelper {

    /**
     * costruttore della classe dbhelper
     * @param context   context nel momento della creazione.
     */
    public DbHelper(Context context){
      super(context, DbString.DBNAME, null, 1);
    }

    /**
     * Metodo chiamato quando il database viene creato la prima volta.
     * Esecuzione dello script di creazione delle tabelle.
     *
     * @param db il database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbString.CREATION_ACCOUNT);
        db.execSQL(DbString.CREATION_PRODOTTO);
        db.execSQL(DbString.CREATION_ENTRATE);
        db.execSQL(DbString.CREATION_DESTINAZIONE);
        db.execSQL(DbString.CREATION_TURNI);
        db.execSQL(DbString.CREATION_IMPASTO);
        db.execSQL(DbString.CREATION_TIPOLOGIA);
        db.execSQL(DbString.CREATION_INGREDIENTE);
        db.execSQL(DbString.CREATION_PRODOTTO_TIPOLOGIA);
        db.execSQL(DbString.CREATION_PRODOTTO_IMPASTO);
        db.execSQL(DbString.CREATION_PRODOTTO_INGREDIENTE);
        db.execSQL(DbString.CREATION_COSTI_CONSUMI);

    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //REGISTRAZIONE
    /**
     * Consente di controllare se è gia presente all'interno del database l'account con email o username che stanno cercando di inserire
     * @param username username con il quale ci si vuole registrare
     * @param email emil con la qulae ci si vuole registrare
     * @throws Exception genera un eccezione se username o email sono gia presenti
     */
    public void checkRegistrazione(String username, String email) throws Exception{
        SQLiteDatabase db= this.getReadableDatabase();
        db.execSQL(DbString.enableCaseSensitive);
        Cursor rs=db.rawQuery( DbString.checkPresenzaUsername, new String[]{ username });

        if(rs.getCount()>0){
            rs.moveToFirst();
            if(rs.getInt(0) == 1){
                rs.close();
                db.close();
                throw new Exception("Username già presente");
            }
        }
        rs=db.rawQuery( DbString.checkPresenzaEmail, new String[]{ email });
        if(rs.getCount()>0){
            rs.moveToFirst();
            if(rs.getInt(0) == 1){
                rs.close();
                db.close();
                throw new Exception("E-mail già presente");
            }
        }
        rs.close();
        db.close();
    }

    /**
     * inserisce i dati relativi all'account nella tabella ACCOUNT
     * @param account Ponyaccount
     *                @param password password dell'account
     * @see PonyAccount
     * @return ritorna true se la registrazione è avvenuta con successo oppure false se ci sono stati problemi
     */
    public boolean registrazioneAccount(PonyAccount account, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        int codiceaccesso = UtilClass.generateAccessCode();

        ContentValues cv = new ContentValues();

        cv.put("username", account.getUsername());
        cv.put("email", account.getEmail());
        cv.put("nome", account.getNome());
        cv.put("cognome", account.getCognome());
        cv.put("password", password);
        cv.put("codice_accesso", Integer.valueOf(codiceaccesso));
        cv.put("attivo", Integer.valueOf(1));

        long  checkInsert= db.insert("ACCOUNT", null, cv);
        db.close();
        if(checkInsert==-1){
            return false;
        }
        else{
            //invia mail
            return true;
        }
    }


    //LOGIN
    /**
     *  seleziona i dati dell'account partendo dallo username inserito, se questo non è presenta nel database genera un'eccezzione
     * @param username username inserito dall'utente
     * @param passcod password inserito dall'utente
     * @return ritorna PonyAccount contenente i dati dell'account di login
     * @throws Exception generata in caso di credenziali errate (username non presente o password non corrispondente)
     */
    public PonyAccount login(String username, String passcod) throws Exception {
        Exception e = new Exception("Credenziali errate");
        PonyAccount account;
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(DbString.enableCaseSensitive);
        Cursor rs = db.rawQuery("SELECT * FROM ACCOUNT WHERE username LIKE \"" + username + "\" LIMIT 1", null);
        if(rs.getCount()>0){
            rs.moveToFirst();
            account=new PonyAccount(
                    rs.getString(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3) );
            String dbpass = rs.getString(4);
            String dbcodacc = String.valueOf(rs.getInt(5));
            if ((dbpass.equals(passcod)) || (dbcodacc.equals(passcod))) {
                //setto attivo=1 per questo account
                ContentValues cv = new ContentValues();
                cv.put("attivo", "1");
                db.update("ACCOUNT", cv, "username LIKE ?", new String[]{username});

                //credenziali corrette
                return account;
            } else {
                //credenziali errate
                rs.close();
                db.close();
                throw e;
            }

        }else{
            //account non presente
            rs.close();
            db.close();
            throw new Exception("Username non presente");
        }
    }


    //LOGOUT
    /**
     * aggiorna l'account in modo che non sia piu attivo (setta attivo = 0)
     * @param account account del logout
     * @return ritorna l'esito dell'aggiornamento
     */
    public boolean logout(PonyAccount account){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = true;
        ContentValues cv = new ContentValues();
        cv.put("attivo", "0");
        int row = db.update("ACCOUNT", cv, "username LIKE ?", new String[]{account.getUsername()});
        if(row!=1){
            result = false;
        }
        return result;
    }

    /**
     * metodo che permette il recupero di dati relativi all'account
     * @param username useranme dell'account da ricercare
     * @return ritorna un oggetto di tipo PonyAccount in caso di successo
     * @throws Exception genere un'eccezzione in caso di insuccesso (problemi con il database)
     */
    public PonyAccount recuperoDatiAccount(String username) throws  Exception{
        SQLiteDatabase db = this.getReadableDatabase();
        PonyAccount account;
        db.execSQL(DbString.enableCaseSensitive);
        Cursor rs = db.rawQuery(DbString.selectAccount, new String[]{username});
        if(rs.getCount()>0){
            rs.moveToFirst();
            account=new PonyAccount(
                    rs.getString(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3) );
        }else{
            rs.close();
            db.close();
            throw new Exception("ERRORE: account non trovato");
        }

        rs.close();
        db.close();
        return account;
    }

    /**
     * metodo che aggiorna il codice di accesso dell'account apassato nel database e
     * invia la mail all'email specifica dell'account con il nuovo codice
     */
    public void updateAccessCode(String username) throws Exception{
        SQLiteDatabase db= this.getWritableDatabase();
        int newcode=UtilClass.generateAccessCode();

        PonyAccount account = recuperoDatiAccount(username);

        db.execSQL("UPDATE ACCOUNT SET codice_accesso = \"" + newcode +"\" WHERE username LIKE \"" + account.getUsername() + "\";");

        db.close();
    }

    /**
     * seleziona tutti gli account presenti nel database
     * @return ritorna una lista di PonyAccount
     */
    public List<PonyAccount> selectAllAccount(){
        SQLiteDatabase db= this.getWritableDatabase();
        List<PonyAccount> returnList= new ArrayList<>();

        Cursor rs = db.rawQuery(DbString.SELECT_ALL_ACCOUNT,null);

        if(rs.moveToFirst()){
            do{
                String Username=rs.getString(0);
                String Email=rs.getString(1);
                String Nome=rs.getString(2);
                String Cognome=rs.getString(3);
                PonyAccount pa=new PonyAccount(Username, Email, Nome, Cognome);
                returnList.add(pa);
            }while(rs.moveToNext());
        }
        rs.close();
        db.close();
        return returnList;
    }

    //ENTRATE

    public void addNewEntrata(Entrata entrata){

    }

    /**
     * permette di estrapolare dal database tutte le entrate del mese selezionato,
     * @param meseAnno mese di cui si vogliono selezionare le ntrate
     * @param username username dell'account di cui si vogliono selezionare le entrate
     * @return ritorna una lista di entrate ordinate per data
     * @throws Exception "Entrata non presente" genera eccezzioni in caso non ci siano
     */
    public List<Entrata> getAllEntrateMese(String meseAnno, String username) throws Exception{
        List<Entrata> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        LocalDate meseAnnoData = LocalDate.parse(meseAnno, formatter);
        YearMonth yearMonth = YearMonth.of(meseAnnoData.getYear(), meseAnnoData.getMonth());

        long startMonth = UtilClass.localDateToUnixTime(UtilClass.getFirstDayOfMonth(yearMonth));

        long endMonth = UtilClass.localDateToUnixTime(UtilClass.getLastDayOfMonth(yearMonth));

        Cursor rs = db.rawQuery(DbString.selectAllEntrateMese, new String[]{username, String.valueOf(startMonth), String.valueOf(endMonth)});
        if(rs.getCount()>0){
            //cursor pieno inserisco Entrata fino a che sono state selezionate dal database
            rs.moveToFirst();
            do{
                returnList.add(new Entrata(
                        UtilClass.unixTimeToLocalDate(rs.getLong(0)),
                        LocalTime.parse(rs.getString(1)),
                        LocalTime.parse(rs.getString(2)),
                        LocalTime.parse(rs.getString(3)),
                        rs.getDouble(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getString(7))
                );

            }while (rs.moveToNext());

        }else{
            //cursor vuoto
            rs.close();
            db.close();
            throw new Exception("Entrata non presente!");
        }

        return returnList;

    }

    /**
     * ricerca l'entrata corriscpondente alla data passata, genera eccezzioni in caso di errore di
     * formattazione della stringa passata oppure di entrata non presente
     * @param data data di cui si vuole ricercare la'entrata nel seguente foramto (dd
     * @param username username dell'account
     * @return ritorna l'entrata ricercata
     * @throws Exception generate in caso di formattazione della stringa non corretta oppure entrata non presente
     */
    public  Entrata getEntrata(String data, String username) throws Exception{
        SQLiteDatabase db = this.getReadableDatabase();

        //data convertita in LocalDate
        LocalDate localDate = UtilClass.stringToLocalDate(data);

        //data convertita in unixTime
        long unixTimeData = UtilClass.localDateToUnixTime(localDate);
        Cursor rs= db.rawQuery(DbString.selectEntrata, new String[]{username, String.valueOf(unixTimeData)} );
        if(rs.getCount()>0){
            rs.moveToFirst();
            return new Entrata(
                    UtilClass.unixTimeToLocalDate(rs.getLong(0)),
                    LocalTime.parse(rs.getString(1)),
                    LocalTime.parse(rs.getString(2)),
                    LocalTime.parse(rs.getString(3)),
                    rs.getDouble(4),
                    rs.getDouble(5),
                    rs.getDouble(6),
                    rs.getString(7)
            );
        }else{
            //entrata non presente
            rs.close();
            db.close();
            throw  new Exception("Entrata non presente!");
        }
    }

    /**
     * metodo che permette di ottenere una lista dei turni settimanali
     * @param username username dell'account
     * @param start giorno di inizio della settimana da selezionare
     * @param end giorno di fine della settimana da selezionare
     * @return ritorna la lista con tutti i turni settimanali
     * * @throws Exception in caso di assenza di turni
     */
    public List<Turno> getTurniFromInterval(String username, LocalDate start, LocalDate end) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Turno> returnList = new ArrayList<>();

        long unixTimeStartWeek = UtilClass.localDateToUnixTime(start);
        long unixTimeEndWeek = UtilClass.localDateToUnixTime(end);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Cursor rs = db.rawQuery(DbString.selectTurniSettimanali,
                new String[]{username, String.valueOf(unixTimeStartWeek), String.valueOf(unixTimeEndWeek)});
        if(rs.getCount()>0){
            rs.moveToFirst();
            do{
                returnList.add(new Turno(
                        UtilClass.unixTimeToLocalDate(rs.getLong(1)),
                        LocalTime.parse(rs.getString(2), timeFormatter),
                        LocalTime.parse(rs.getString(3), timeFormatter))
                );
            }while(rs.moveToNext());

            rs.close();
            db.close();
            return returnList;
        }else{
            rs.close();
            db.close();
            throw new Exception("Nessun turno presente");
        }
    }

    /**
     * permette di estrapolare dal database i costi e i consumi di un determinato mese e username
     * @param meseAnno mese da analizzarre
     * @param username username dell'account
     * @return ritorna un'instanza di costi con i dati del mese selezionato
     * @throws Exception in caso non ci siano costi inseriti nel database
     */
    public Costo getCostiMensili(String meseAnno, String username) throws Exception {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rs= db.rawQuery(DbString.selectCostiMensili, new String[]{username, meseAnno});
        if(rs.getCount()>0){
            return  new Costo(rs.getString(1),
                    rs.getDouble(2),
                    rs.getDouble(3));
        }else{
            //non ci sono costi inseriti per il mese selezionato
            rs.close();
            db.close();
            throw new Exception("Non ci sono costi inseriti!");
        }
    }

    /**
     * software di calcolo delle entrate nettte mesili ovvero quelle lorde (entrate + mancia) - i costi [(kmtot / consumomedio) * costocarburante]
     * @param meseAnno mese da analizzare
     * @param username username dell'account
     * @return ritorna le entrate mensili nette
     */
    public double getTotMensile(String meseAnno, String username){
        List<Entrata> list;
        double totMensile=0;
        double kmMensili=0;
        Costo costiMensili;
        try {
            list = this.getAllEntrateMese(meseAnno, username);
            if(!list.isEmpty()){
               for(Entrata e : list){
                   totMensile+=(e.getEntrate()+e.getMancia());
                   kmMensili+=e.getKm();
               }
               costiMensili = getCostiMensili(meseAnno, username);
               totMensile-=(kmMensili/costiMensili.getConsumoMedio())*costiMensili.getCostoCarburante();
            }
            return totMensile;
        } catch (Exception e) {
            return totMensile;
        }

    }

    /**
     * permette di ottenere l'ultimo account attivo sull'app
     * @return ritorna il ponyaccount con i dati istanziati
     * @throws Exception in caso di nessuno o piu account attivi
     */
    public PonyAccount getActiveAccount() throws Exception{
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL(DbString.enableCaseSensitive);
        Cursor rs=db.rawQuery(DbString.selectActiveAccount, null);
        if(rs.getCount()>0){
            rs.moveToFirst();
            if (rs.getCount()>1){
                rs.close();
                db.close();
                throw new Exception("Piu account attivi!\nEsegui il logout");
            }
            return new PonyAccount(
                    rs.getString(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3) );
        }else{
            rs.close();
            db.close();
            throw new Exception("Nessun account attivo!\nEsegui il login o registrati");
        }
    }

    /**
     * permette prima di cercare all'interno del database se è presente il turno in quella data
     * in caso sia presente allora viene eseguito l'update in caso non lo sia viene inserita una nuova riga alla tabella del database
     * @param account account attivo in quel momento
     * @param turno turno interessato alle modifiche
     * @throws Exception in caso vi siano problemi viene lanciata questa eccezione: "Errori interni ci scusiamo per il disagio.\nRIPROVARE!"
     */
    public void modificaTurno(PonyAccount account, Turno turno) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Exception e = new Exception("Errori interni ci scusiamo per il disagio.\nRIPROVARE!");
        long unixTime = UtilClass.localDateToUnixTime(turno.getData());
        ContentValues cv = new ContentValues();
        cv.put("username", account.getUsername());
        cv.put("data", unixTime);
        cv.put("ora_inizio", turno.getOraInizio().format(timeFormatter));
        cv.put("ora_fine", turno.getOraFine().format(timeFormatter));
        //controllo se il turno è presente
        Cursor rs = db.rawQuery(DbString.checkPresenzaTurno, new String[]{String.valueOf(unixTime),account.getUsername()});
        if(rs.getCount()>0){
            rs.moveToFirst();
            if(rs.getInt(0) == 0){
                //ilturno non è presente lo dobbiamo inserire
                long result = db.insert("TURNI", null, cv );
                rs.close();
                db.close();
                if(result == -1){
                    throw e;
                }
            }else{
                //il turno è gia presente update
                int result = db.update("TURNI", cv, "username LIKE ? AND data LIKE ?", new String[]{account.getUsername(), String.valueOf(unixTime)});
                rs.close();
                db.close();
                if(result != 1){
                    throw e;
                }
            }
        }else{
            rs.close();
            db.close();
            throw e;
        }
    }

    /**
     * permette di salvare una destinazione all'interno del databse
     * @param account account attivo
     * @param destinazione destinazione da salvare
     * @throws Exception in caso di errori
     */
    public  void salvaDestinazione(PonyAccount account, Destinazione destinazione) throws Exception{
        SQLiteDatabase db = this.getWritableDatabase();
        Exception e = new Exception("Errori interni ci scusiamo per il disagio.\nRIPROVARE!");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        long unixTime = UtilClass.localDateToUnixTime(destinazione.getDataUltimaModifica());
        String oraModifica = destinazione.getOraUltimaModifica().format(DateTimeFormatter.ofPattern("HH:mm"));
        ContentValues cv = new ContentValues();
        cv.put("username", account.getUsername());
        cv.put("data_modifica", unixTime);
        cv.put("ora_modifica", oraModifica);
        cv.put("indirizzo", destinazione.getIndirizzo().toStringViaCivico());
        cv.put("citta", destinazione.getIndirizzo().getCitta());
        cv.put("cap", destinazione.getIndirizzo().getCap());
        cv.put("provincia", destinazione.getIndirizzo().getProvincia());
        cv.put("latitudine", destinazione.getLatitudine());
        cv.put("longitudine", destinazione.getLongitudine());
        cv.put("note", destinazione.getNote());
        long result;
        try{
             result = db.insert("DESTINAZIONE", "ora_modifica, note, cap, mancia", cv );
        }catch (SQLiteConstraintException constraintException){
            db.close();
            throw new Exception("Destinazione già presente nel database; RIPROVA!");
        }
        if(result == -1){
            db.close();
            throw e;
        }
        db.close();


    }

    /**
     * permette di cercare all'interno del database una o piu destinazioni
     * @param account account attivo
     * @param indirizzo indirizzo ricercato dall'utente
     * @return ritorna la lista delle destinazioni corrispondenti all'interno del database
     * @throws Exception in caso di errore o di mancanza di corrispondenza
     */
    public List<Destinazione> searchDestinazione(PonyAccount account, String indirizzo) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM DESTINAZIONE WHERE username LIKE ? AND indirizzo LIKE \'%"+indirizzo+"%\' ORDER BY data_modifica DESC;", new String[]{account.getUsername()});
        List<Destinazione> returnList = new ArrayList<>();
        if(rs.getCount()>0){
            rs.moveToFirst();
            do{
                Indirizzo ind = new Indirizzo();
                ind.setViaFromViaCivico(rs.getString(3));
                ind.setCivicoFromViaCivico(rs.getString(3));
                ind.setCitta(rs.getString(4));
                ind.setCap(rs.getInt(5));
                ind.setProvincia(rs.getString(6));
                returnList.add(new Destinazione(ind,
                        UtilClass.unixTimeToLocalDate(rs.getLong(1)),
                        LocalTime.parse(rs.getString(2), DateTimeFormatter.ofPattern("HH:mm")),
                        rs.getDouble(9),
                        rs.getDouble(8),
                        rs.getDouble(7),
                        rs.getString(10)));
            }while(rs.moveToNext());
            rs.close();
            db.close();
            return  returnList;
        }else{
            rs.close();
            db.close();
            throw new Exception("Nessuna destinazione corrisponde alla ricerca");
        }
    }

    /**
     * permette di recuperare la lista di tutte le destinazioni presenti nel database
     * @param account account attivo
     * @return ritrona la lista delle destinazioni presenti nel database
     * @throws Exception in caso di errore o destinazioni non presenti
     */
    public List<Destinazione> getAllDestinazioni(PonyAccount account) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Destinazione> returnList = new ArrayList<>();
        Cursor rs = db.rawQuery(DbString.selectAllDestinazioni, new String[]{account.getUsername()});
        if(rs.getCount()>0){
            rs.moveToFirst();
            do{
                Indirizzo ind = new Indirizzo();
                ind.setViaFromViaCivico(rs.getString(3));
                ind.setCivicoFromViaCivico(rs.getString(3));
                ind.setCitta(rs.getString(4));
                ind.setCap(rs.getInt(5));
                ind.setProvincia(rs.getString(6));

                returnList.add(new Destinazione(ind,
                        UtilClass.unixTimeToLocalDate(rs.getLong(1)),
                                LocalTime.parse(rs.getString(2), DateTimeFormatter.ofPattern("HH:mm")),
                                rs.getDouble(9),
                                rs.getDouble(8),
                                rs.getDouble(7),
                                rs.getString(10))
                        );
            }while(rs.moveToNext());
            return returnList;
        }else{
            rs.close();
            db.close();
            throw new Exception("Nessuna destinazione presente");
        }
    }

    /**
     * permette di modificare una destinazione precedentemente inserita
     * @param account account attivo
     * @param oldDestinazione campi della vecchia destinazione
     * @param newDestinazione campi della nuova destinazione
     * @throws Exception in caso di errore
     */
    public void updateDestinazione(PonyAccount account, Destinazione oldDestinazione, Destinazione newDestinazione) throws Exception{
        SQLiteDatabase db = this.getWritableDatabase();
        Exception e = new Exception("Errori interni ci scusiamo per il disagio.\nRIPROVARE!");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        long unixTime = UtilClass.localDateToUnixTime(newDestinazione.getDataUltimaModifica());
        String oraModifica = newDestinazione.getOraUltimaModifica().format(DateTimeFormatter.ofPattern("HH:mm"));
        ContentValues cvNew = new ContentValues();
        cvNew.put("account", account.getUsername());
        cvNew.put("username", account.getUsername());
        cvNew.put("data_modifica", unixTime);
        cvNew.put("ora_modifica", oraModifica);
        cvNew.put("indirizzo", newDestinazione.getIndirizzo().toStringViaCivico());
        cvNew.put("citta", newDestinazione.getIndirizzo().getCitta());
        cvNew.put("cap", newDestinazione.getIndirizzo().getCap());
        cvNew.put("provincia", newDestinazione.getIndirizzo().getProvincia());
        cvNew.put("latitudine", newDestinazione.getLatitudine());
        cvNew.put("longitudine", newDestinazione.getLongitudine());
        cvNew.put("note", newDestinazione.getLongitudine());
        
        int result = db.update("DESTINAZIONE", cvNew, "username LIKE ? AND indirizzo LIKE ? AND citta LIKE ?",
                new String[]{account.getUsername(), oldDestinazione.getIndirizzo().toStringViaCivico(), oldDestinazione.getIndirizzo().getCitta()} );

        db.close();
        if(result != 1){
            throw e;
        }
    }


    /**
     * permette di modificare i costi se già presenti in quel meseAnno oppure inserirli in caso non siano presenti
     * @param account account attivo
     * @param meseAnno meseAnno di cui si vuole modificare i costi
     * @param costo nuovi costi
     * @throws Exception in caso di errori
     */
    public void modCostiConsumi(PonyAccount account, String meseAnno, Costo costo) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        Exception e = new Exception("Errori interni ci scusiamo per il disagio.\nRIPROVARE!");
        ContentValues cvNew = new ContentValues();
        cvNew.put("username", account.getUsername());
        cvNew.put("mese_anno", meseAnno);
        cvNew.put("costo_carburante", costo.getCostoCarburante());
        cvNew.put("consumo_medio", costo.getConsumoMedio());

        Cursor rs = db.rawQuery(DbString.checkPresenzaCosti, new String[]{meseAnno, account.getUsername()});
        if(rs.getCount()>0){
            rs.moveToFirst();
            if(rs.getInt(0)==1){
                //costi presenti update
                int result = db.update("COSTI_CONSUMI", cvNew, "username LIKE ? AND mese_anno LIKE ?",
                        new String[]{account.getUsername(), meseAnno});
                db.close();
                if(result != 1){
                    throw e;
                }

            }else{
                //costi non presenti insert
                long result = db.insert("COSTI_CONSUMI", null, cvNew);
                if(result == -1){
                    throw e;
                }
            }

        }else{
            throw e;
        }

    }
}