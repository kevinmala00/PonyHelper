package com.example.ponyhelper.util;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.Random;

public class UtilClass {
    public static final String EMAIL_SUBJECT_FIRSTREG= "BENVENUTO IN PONYHELPER!!";
    public static final String EMAIL_SUBJECT_NEWCODE= "NUOVO CODICE DI ACCESSO";
    public static final String EMAIL_BODY_FIRSTREG= "Ciao ti diamo il benvenuto nella comunità di PonyHelper, " +
            "con noi gestirai al meglio il tuo lavoro di fattorino.\n" +
            "Di seguito troverai il tuo nuovo codice di accesso:\n";
    public static final String EMAIL_BODY_NEWCODE= "Come da lei richiesto in seguito troverà il nuvo codice di accesso:\n";

    /**
     *invia una mail all'indirizzo prescelto
     * @param email email destinataria
     * @param body corpo della mail
     * @param subject soggetto della mail
     * @param activity activit dalla quale viene chiamato il metodo
     * @param accessCode codice di accesso
     */
    public static void sendMail(String email, String subject, String body, Activity activity, int accessCode){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , body + accessCode);
        try {
            activity.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity.getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * genera il codice di accesso relativo all'account,
     * @return codice di accesso, un numero casuale tra 0 e 9999
     */
    public static int generateAccessCode(){
        Random rnd= new Random();
        return rnd.nextInt(9999);
    }


    /**
     * consente di passare i dati relativi all'account da un'activity all'altra
     * @param account account di cui si vogliono passare dopo
     * @return ritorna il bundle di dati contenente i dati relativi all'account
     */
    public static Bundle salvataggioAccount(PonyAccount account){
        Bundle AccountBundle = new Bundle();
        AccountBundle.putString("username", account.getUsername());
        AccountBundle.putString("nome", account.getNome());
        AccountBundle.putString("cognome", account.getCognome());
        AccountBundle.putString("email", account.getUsername());
        AccountBundle.putString("password", account.getPassword());
        AccountBundle.putString("datanascita", account.getDataNascita());
        AccountBundle.putInt("accesscode", account.getAccessCode());

        return AccountBundle;
    }

    /**
     * consente di ripristinare i dati dall'account all'apertura di una nuova activity
     * @param bundle bundle di dati ricevuto dall'activity precedente
     * @return ritorna un'istanza della classe PonyAccount con i valori da ripristinare
     */
    public static PonyAccount ripristinoAccount(Bundle bundle){
        PonyAccount account=new PonyAccount();
        account.setUsername(bundle.getString("username"));
        account.setNome(bundle.getString("nome"));
        account.setCognome(bundle.getString("cognome"));
        account.setEmail(bundle.getString("email"));
        account.setPassword(bundle.getString("password"));
        account.setDataNascita(bundle.getString("datanascita"));
        account.setAccessCode(bundle.getInt("accesscode"));
        return account;
    }

    /**
     * controlla che i campi obbligatori siano stati inseriti
     * @param textInput imputtext che si deve controllare
     * @return ritorna boolean  con l'esito del controllo true nessun problema false problemi riscontrati
     */
    public static boolean checkCampoObbligatorio(TextInputLayout textInput){
        boolean check=true;
        String text = Objects.requireNonNull(textInput.getEditText()).getText().toString();
        if(text.isEmpty()){
            textInput.setError("Campo obbligatorio");
            check = false;
        }
        return check;
    }



}
