package com.example.ponyhelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomePage extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Calendar cal= Calendar.getInstance();


        TextView tvMese=findViewById(R.id.tv_mesehomepage);
        TextView tvAnno=findViewById(R.id.tv_annohomepage);
        TextView tvGiornoInizio=findViewById(R.id.tv_giornoinizio);
        TextView tvGiornoFine = findViewById(R.id.tv_giornofine);




    }




}