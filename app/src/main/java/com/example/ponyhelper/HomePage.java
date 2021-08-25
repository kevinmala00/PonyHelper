package com.example.ponyhelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.body.Turno;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


public class HomePage extends Activity {
PonyAccount account;
DbHelper dbhelper;
List<Turno> turniSettimanali;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dbhelper=new DbHelper(HomePage.this);

        //rispristino i dati dell'account dal bundle passato dall'activity precedente
        Bundle DatiPassati = getIntent().getExtras();
        Bundle AccountBundle = DatiPassati.getBundle("account");

        //istanzio un nuovo pony account con i dati ricevuti dall'activity precedente
        account = UtilClass.ripristinoAccount(AccountBundle);
        Toast.makeText(HomePage.this, "Benvenuto " + account.getUsername(), Toast.LENGTH_LONG).show();



        TextView tvMeseAnno=findViewById(R.id.tv_meseanno);
        TextView tvGiornoInizio=findViewById(R.id.tv_giornoinizio);
        TextView tvGiornoFine = findViewById(R.id.tv_giornofine);

        //setto tvmeseanno con il mese e anno  corrente e tvgiornoinizio e tvgiornofine con i cosrrispettivi gironi di inizio e fine settimana corrente
        String mese=LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int anno=LocalDate.now().getYear();
        String stringMeseAnno= mese + "\t\t" + anno;
        tvMeseAnno.setText(stringMeseAnno);

        tvGiornoInizio.setText(UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("dd")));
        tvGiornoFine.setText(UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern("dd")));

        //popolo la recycler view con la lista dei turni settimanali
        RecyclerView rvTurniSettimanali = findViewById(R.id.rv_turnisettimanali);
        try {
            turniSettimanali=dbhelper.getTurniFromInterval(account.getUsername(),
                    UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.MONDAY),
                    UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.SUNDAY));

            TurniAdapter turniAdapter = new TurniAdapter(turniSettimanali);
            rvTurniSettimanali.setAdapter(turniAdapter);
            rvTurniSettimanali.setLayoutManager(new LinearLayoutManager(this));

        } catch (Exception e) {
            Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //riempio il totmensile con i ricavi netti mensili
        TextView tvTotMensile = findViewById(R.id.tv_totmensile);
        tvTotMensile.setText(String.valueOf(dbhelper.getTotMensile(stringMeseAnno, account.getUsername())));





    }


}