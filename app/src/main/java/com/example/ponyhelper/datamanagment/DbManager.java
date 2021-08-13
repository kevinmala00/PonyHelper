package com.example.ponyhelper.datamanagment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.util.UtilClass;


/**
 * @author kevin
 * classe dbmanager, classe che permette la gestione del database creato
 */
public class DbManager {

    private DbHelper dbhelper;

    /**
     * rappresenta il riferimento al database da utilizzare
     */
    public static SQLiteDatabase db;


    /**
     * costruttore della classe dbmanager
     * @param ctx contesto dell'applicazione
     */
    public DbManager(Context ctx) {
        dbhelper=new DbHelper(ctx);
    }

    /**
     * apre il database accessibile anche in modalità scrittura,
     * setta l'attributo db con il nuovo db aperto
     */
    public void open(){
        db=dbhelper.getWritableDatabase();
    }

    /**
     * chiude il riferimento al database
     */
    public void close(){
        if(db.isOpen()){
            db.close();
        }
    }

    /**
     * Consente di controllare se è gia presente all'interno del database l'account con email o username che stanno cercando di inserire
     * @param username username con il quale ci si vuole registrare
     * @param email emil con la qulae ci si vuole registrare
     * @throws Exception genera un eccezzione se username o email sono gia presenti
     */
    public void checkRegistrazione(String username, String email) throws Exception{
        Cursor rs=db.rawQuery("SELECT username " +
                "FROM ACCOUNT WHERE username LIKE ? LIMIT 1;", new String[]{username});
        String dbusername=rs.getString(1);
        if(dbusername!=null){
            throw new Exception("Username già presente");
        }
        rs.close();
        rs=db.rawQuery("SELECT email " +
                "FROM ACCOUNT WHERE email LIKE ? LIMIT 1;", new String[]{email});
        String dbemail=rs.getString(1);
        if(dbemail!=null){
            throw new Exception("E-mail già presente");
        }
        rs.close();
    }

    /**
     * inserisce i dati relativi all'account nella tabella ACCOUNT
     * @param account Ponyaccount
     * @see PonyAccount
     */
    public void registrazioneAccount(PonyAccount account, Activity activity){
        int codiceaccesso= UtilClass.generateAccessCode();
        db.execSQL("INSERT INTO ACCOUNT VALUES (" +
                 account.getUsername() + ","
                + account.getEmail() + ","
                + account.getNome() + ","
                + account.getCognome() + ","
                + account.getPassword() + ","
                + codiceaccesso + ","
                + account.getDataNascita() + ")");

        UtilClass.sendMail(account.getEmail(), UtilClass.EMAIL_SUBJECT_FIRSTREG, UtilClass.EMAIL_BODY_FIRSTREG, activity, codiceaccesso);

    }

    /**
     * controlla la presenza dell'account nel database e ne verifica le credenziali
     * @param credenziali vettore di stringhe che contiene username e passcod inseriti dall'utente
     * @throws Exception genera un eccezzione se le credenziali sono errate (sia in caso di account non esistente che password errata)
     */
    public void checkLogin(String[] credenziali) throws Exception {
        String passcod = credenziali[1];
        Cursor rs = db.rawQuery("SELECT password, codice_accesso FROM ACCOUNT WHERE username LIKE ? LIMIT 1;", credenziali);

        String dbpass = rs.getString(1);
        String dbcodacc = String.valueOf(rs.getInt(2));

        if ((dbpass == passcod) || (dbcodacc == passcod)) {
            //credenziali corrette
        } else {
            throw new Exception("credenziali errate");
        }
    }

    /**
     * metodo che aggiorna il codice di accesso dell'account apassato nel database e
     * invia la mail all'email specifica dell'account con il nuovo codice
     * @param account account da aggiornare
     */
    public void updateAccessCode(PonyAccount account){
        Integer newcode=UtilClass.generateAccessCode();
        Cursor rs = db.rawQuery("UPDATE ACCOUNT SET codice_accesso = ? WHERE username LIKE ?", new String[]{newcode.toString(), account.getUsername()});
        rs.close();
    }






}
