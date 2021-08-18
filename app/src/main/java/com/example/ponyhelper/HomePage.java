package com.example.ponyhelper;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import com.example.ponyhelper.util.UtilClass;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HomePage extends Activity {



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



        TextView tvMese=findViewById(R.id.tv_mesehomepage);
        TextView tvAnno=findViewById(R.id.tv_annohomepage);
        TextView tvGiornoInizio=findViewById(R.id.tv_giornoinizio);
        TextView tvGiornoFine = findViewById(R.id.tv_giornofine);

        //setto tvmese con il mese corrente e tvgiornoinizio e tvgiornofine con i cosrrispettivi gironi di inizio e fine settimana corrente
        tvMese.setText(LocalDate.now().getMonth().toString());
        tvGiornoInizio.setText( UtilClass.getCurrentMonday(new Date()).toString());


    }




}