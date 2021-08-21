package com.example.ponyhelper.datamanagment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.ponyhelper.PagLogin;
import com.example.ponyhelper.body.Entrate;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.util.UtilClass;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    /**
     * costruttore della classe dbhelper
     * @param context   context nel momento della creazione.
     */
    public DbHelper(Context context){
      super(context,DataBaseString.DBNAME, null, 1);
    }

    /**
     * Metodo chiamato quando il database viene creato la prima volta. Esecuzione dello script.
     *
     * @param db il database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseString.CREATION_ACCOUNT);
        db.execSQL(DataBaseString.CREATION_PRODOTTO);
        db.execSQL(DataBaseString.CREATION_ENTRATE);
        db.execSQL(DataBaseString.CREATION_DESTINAZIONE);
        db.execSQL(DataBaseString.CREATION_TURNI);
        db.execSQL(DataBaseString.CREATION_IMPASTO);
        db.execSQL(DataBaseString.CREATION_TIPOLOGIA);
        db.execSQL(DataBaseString.CREATION_INGREDIENTE);
        db.execSQL(DataBaseString.CREATION_PRODOTTO_TIPOLOGIA);
        db.execSQL(DataBaseString.CREATION_PRODOTTO_IMPASTO);
        db.execSQL(DataBaseString.CREATION_PRODOTTO_INGREDIENTE);
        db.execSQL(DataBaseString.CREATION_COSTI_CONSUMI);

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
        db.execSQL(DataBaseString.UPGRADE_SCRIPT);
    }

    /**
     * Consente di controllare se è gia presente all'interno del database l'account con email o username che stanno cercando di inserire
     * @param username username con il quale ci si vuole registrare
     * @param email emil con la qulae ci si vuole registrare
     * @throws Exception genera un eccezione se username o email sono gia presenti
     */
    public void checkRegistrazione(String username, String email) throws Exception{
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor rs=db.rawQuery("SELECT username " +
                "FROM ACCOUNT WHERE username LIKE " + "\"" + username + "\"" + " LIMIT 1;", null);

        if(rs.getCount()>0){
            rs.close();
            throw new Exception("Username già presente");
        }


        rs=db.rawQuery("SELECT email " +
                "FROM ACCOUNT WHERE email LIKE " + "\"" + email + "\"" + " LIMIT 1;", null);

        if(rs.getCount()>0){
            rs.close();
            throw new Exception("E-mail già presente");
        }
        db.close();
    }

    /**
     * inserisce i dati relativi all'account nella tabella ACCOUNT
     * @param account Ponyaccount
     *                @see PonyAccount
     * @param password password dell'account
     * @return ritorna true se la registrazione è avvenuta con successo oppure false se ci sono stati problemi
     */
    public boolean registrazioneAccount(PonyAccount account, String password, Activity activity){
        SQLiteDatabase db = this.getWritableDatabase();
        int codiceaccesso = UtilClass.generateAccessCode();

        ContentValues cv = new ContentValues();

        cv.put("username", account.getUsername());
        cv.put("email", account.getEmail());
        cv.put("nome", account.getNome());
        cv.put("cognome", account.getCognome());
        cv.put("password", password);
        cv.put("codice_accesso", Integer.valueOf(codiceaccesso));

        long  checkInsert= db.insert("ACCOUNT", null, cv);
        if(checkInsert==-1){
            db.close();
            return false;
        }
        else{
            //invia mail
            db.close();
            return true;
        }
    }

    /**
     *  seleziona i dati dell'account partendo dallo username inserito, se questo non è presenta nel database genera un'eccezzione
     * @param username username inserito dall'utente
     * @param passcod password inserito dall'utente
     * @return ritorna PonyAccount contenente i dati dell'account di login
     * @throws Exception generata in caso di credenziali errate (username non presente o password non corrispondente)
     */
    public PonyAccount checkLogin(String username, String passcod) throws Exception {
        Exception e = new Exception("Credenziali errate");
        PonyAccount account;
        SQLiteDatabase db= this.getReadableDatabase();
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

    /**
     * metodo che permette il recupero di dati relativi all'account
     * @param username useranme dell'account da ricercare
     * @return ritorna un oggetto di tipo PonyAccount in caso di successo
     * @throws Exception genere un'eccezzione in caso di insuccesso (problemi con il database)
     */
    public PonyAccount recuperoDatiAccount(String username) throws  Exception{
        SQLiteDatabase db = this.getReadableDatabase();
        PonyAccount account;
        Cursor rs = db.rawQuery("SELECT * FROM ACCOUNT WHERE username LIKE ? LIMIT 1;", new String[]{username});
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
    public void updateAccessCode(String username, Activity activity) throws Exception{
        SQLiteDatabase db= this.getWritableDatabase();
        int newcode=UtilClass.generateAccessCode();

        PonyAccount account = recuperoDatiAccount(username);

        db.execSQL("UPDATE ACCOUNT SET codice_accesso = " + "\"" + newcode +"\"" + " WHERE username LIKE " + "\"" + account.getUsername() + "\";");

        db.close();
    }

    /**
     * seleziona tutti gli account presenti nel database
     * @return ritorna una lista di PonyAccount
     */
    public List<PonyAccount> selectAllAccount(){
        SQLiteDatabase db= this.getWritableDatabase();
        List<PonyAccount> returnList= new ArrayList<>();

        Cursor rs = db.rawQuery(DataBaseString.SELECT_ALL_ACCOUNT,null);

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
    public List<Entrate> selectEntrateMese(String mese, String username) throws Exception{
        List<Entrate> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        return returnList;

    }
}