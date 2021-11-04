package com.example.ponyhelper.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;



/**
 * classe per l'invio di mail tramite intent android
 */
public class EmailSender {

    private Context mContext;

    public EmailSender(Context context){
        mContext=context;
    }

    /**
     * metodo che permette di inviare una mail mediante intent android.
     * in caso sul dispositivo non ci siano applicazioni di inviao di posta eletrronica installate viene stampato un toast message
     * @param activity activity chiamante
     * @param destinatario destinatario dalla mail
     * @param oggetto oggettto della mail
     */
    public void sendMail(String destinatario, String oggetto){


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+destinatario));
        intent.putExtra(Intent.EXTRA_SUBJECT, oggetto);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{
            mContext.startActivity(intent);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            Toast.makeText(mContext, "Nessuna applicazione trovata per l'invio di posta elettronica", Toast.LENGTH_SHORT).show();
        }


    }
}
