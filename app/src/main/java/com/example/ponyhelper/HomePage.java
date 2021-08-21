package com.example.ponyhelper;

import android.app.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.util.UtilClass;

import java.time.LocalDate;
import java.util.Date;


public class HomePage extends Activity {
PonyAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //rispristino i dati dell'account dal bundle passato dall'activity precedente
        Bundle DatiPassati = getIntent().getExtras();
        Bundle AccountBundle = DatiPassati.getBundle("account");

        //istanzio un nuovo pony account con i dati ricevuti dall'activity precedente
        account = UtilClass.ripristinoAccount(AccountBundle);
        Toast.makeText(HomePage.this, "Benvenuto " + account.getUsername(), Toast.LENGTH_LONG).show();



        TextView tvMese=findViewById(R.id.tv_mesehomepage);
        TextView tvAnno=findViewById(R.id.tv_annohomepage);
        TextView tvGiornoInizio=findViewById(R.id.tv_giornoinizio);
        TextView tvGiornoFine = findViewById(R.id.tv_giornofine);

        //setto tvmese con il mese corrente e tvgiornoinizio e tvgiornofine con i cosrrispettivi gironi di inizio e fine settimana corrente
        tvMese.setText(LocalDate.now().getMonth().toString());
        tvGiornoInizio.setText( UtilClass.getCurrentMonday(new Date()).toString());


    }




}