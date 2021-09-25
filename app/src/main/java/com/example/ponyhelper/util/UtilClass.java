package com.example.ponyhelper.util;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ponyhelper.PagProfilo;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

public class UtilClass {
    public static final String EMAIL_SUBJECT= "ponyhelper@gmail.com";
    public static final String EMAIL_BODY_FIRSTREG= "BENVENUTO IN PONYHELPER!!\nCiao, ti diamo il benvenuto nella comunità di PonyHelper, " +
            "con noi gestirai al meglio il tuo lavoro di fattorino.\n" +
            "Di seguito troverai il tuo nuovo codice di accesso:\n";
    public static final String EMAIL_BODY_NEWCODE= "NUOVO CODICE DI ACCESSO.\nCome da lei richiesto in seguito troverà il nuvo codice di accesso:\n";

    /**
     * genera il codice di accesso relativo all'account,
     * @return codice di accesso, un numero casuale tra 0 e 9999
     */
    public static int generateAccessCode(){
        Random rnd= new Random();
        return rnd.nextInt(9999);
    }

    /**
     * setta il textchanger del edittext passato in modo che appena vnega inserito il testo
     * venga rimosso qualsiasi errore riscontrato in precedenze
     * @param textInput textinput di cui settare il texchanger del proprio edittext
     */
    public static void eliminaErroreCampoObbligatorio(TextInputLayout textInput){
        EditText editInput = textInput.getEditText();
        Objects.requireNonNull(editInput).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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


    /**
     * permette di calcolare il giorno specificato in TargetDayNumberInWeek
     * della settimana corrispondente a quella della data passata
     * @param data data di cui si vuole analizzare la settimana
     * @param targetDayNumberInWeek giorno di cui si vuole calcolare la data
     * @return ritorna la data del giorno target
     */
    public static LocalDate getDayOfDataWeek(LocalDate data, DayOfWeek targetDayNumberInWeek){
        LocalDate target;

        //calcolo il numero del giorno della settimana dalla data passata
        int day = data.getDayOfWeek().getValue();

        //calcolo lo spostamento da effettuare per raggiungere il giorno target
        int differenza = targetDayNumberInWeek.getValue() - day;

        //ritorno la nuova data sottraendo (targetDayNumber - day) giorni da quella passata
        return data.plusDays(differenza);
    }

    /**
     * converte un UnixTime(Long) nella corrispondente data(LocalDate)
     * @param unixTime unixtime da convertire
     * @return la data (Date) corrispondente al unixTime passato
     */
    public static LocalDate unixTimeToLocalDate(long unixTime){
        return Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * converte una data (Date) nel corrispondente UnixTime (Long)
     * @param data data da convertire
     * @return ritorna un long corrispondente alla data passata in unixTime
     */
    public static long localDateToUnixTime(LocalDate data){
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        return data.atStartOfDay(zoneId).toEpochSecond();
    }

    /**
     * consente di convertire una stringa del formato 'gg/MM/yyyy' in localdate
     * @param data stringa da convertire
     * @return ritorna la LocalDate convertirìta a partire dalla stringa
     */
    public static LocalDate stringToLocalDate(String data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return  LocalDate.parse(data, formatter);
    }

    /**
     * calcola il primo giorno del mese passato
     * @param meseAnno mese del quale si vuole calcolare il primo giorno giorno
     * @return data del primo giorno del mese
     */
    public static LocalDate getFirstDayOfMonth(YearMonth meseAnno){
        return meseAnno.atEndOfMonth().plusDays(-(meseAnno.lengthOfMonth()-1));
    }

    /**
     * calcola l'ultimo giorno del mese passato
     * @param meseAnno mese del quale si vuole calcolare l'ultimo giorno
     * @return data dell'ultimo giorno del mese
     */
    public static LocalDate getLastDayOfMonth(YearMonth meseAnno){
        return meseAnno.atEndOfMonth();
    }


    /**
     * esegue il logout scollegando anche l'account
     * @param activity activity nella quale il metodo è stato chiamato
     * @param account accont di cui effettuare il logout
     */
    public static void logout(Activity activity, PonyAccount account){
        DbHelper dbhelper = new DbHelper(activity);
        if(account==null){
            Toast.makeText(activity, "Non esiste alcun account attivo. impossibile effettuare il logout", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("LOGOUT");
        builder.setMessage("Sei sicuro di voler effettuare il logout?");
        builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dbhelper.logout(account);
                } catch (Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                activity.finishAffinity();

                System.exit(0);
            }
        });
        builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }





}
