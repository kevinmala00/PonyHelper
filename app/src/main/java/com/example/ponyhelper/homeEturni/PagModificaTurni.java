package com.example.ponyhelper.homeEturni;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.example.ponyhelper.NavigationActivity;
import com.example.ponyhelper.R;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.body.Turno;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PagModificaTurni extends NavigationActivity {
    private PonyAccount account;
    private Turno turno;
    DbHelper dbhelper;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_modifica_turni);

        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_item_turni);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //ISTANZIO LE VIEW
        View headerView=navigationView.getHeaderView(0);
        TextView tvNavUsername= headerView.findViewById(R.id.tv_usernameNavMenu);
        TextView tvNavEmail=headerView.findViewById(R.id.tv_navEmail);
        TextView tvDurataSettimana = findViewById(R.id.tv_durataSettimana);
        TextView tvMeseAnno=findViewById(R.id.tv_meseanno);
        Spinner sGiorno = findViewById(R.id.s_giornomodificaturno);
        Spinner sOraInizio = findViewById(R.id.s_orainiziomodificaturno);
        Spinner sOraFine =  findViewById(R.id.s_orafinemodificaturno);
        Button bModifica = findViewById(R.id.b_modificaturni);




        //SETTO IL TESTO DELLE VARIE TEXTVIEW
        //meseanno con il mese e anno  corrente
        String mese=LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int anno=LocalDate.now().getYear();
        String stringMeseAnno= mese + "\t\t" + anno;
        tvMeseAnno.setText(stringMeseAnno);
        //la durata della settimana partendo dal primo giorno di questa settimana fino all'ultimo
        LocalDate monday = UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.MONDAY);
        LocalDate sunday = UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.SUNDAY);
        String giornoInizio = monday.format(DateTimeFormatter.ofPattern("dd"));
        String giornoFine = sunday.format(DateTimeFormatter.ofPattern("dd"));
        String durataSettimana = "Dal\t\t" + giornoInizio + "\t\tal\t\t" + giornoFine;
        tvDurataSettimana.setText(durataSettimana);

        //guardo se si cambia mese durente la settimana
        if (Integer.parseInt(giornoFine) < Integer.parseInt(giornoInizio)) {

        }

        //creao la lista dei giorni selezionabili e li collego allo spinner tramite l'arrayadapter
        LocalDate giorno=LocalDate.from(monday);
        List<String> giorniSettimana = new ArrayList<>();
        for(int i = 0; i<=6; i++){
            giorniSettimana.add(giorno.plusDays(i).format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        }

        //CREO E SETTO GLI SPINNER ADAPTER
        ArrayAdapter<String> spinnerAdapterGiorni = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, giorniSettimana);
        sGiorno.setAdapter(spinnerAdapterGiorni);
        ArrayAdapter<String> spinnerAdapterOre = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.orari));
        sOraInizio.setAdapter(spinnerAdapterOre);
        sOraFine.setAdapter(spinnerAdapterOre);

        dbhelper = new DbHelper(PagModificaTurni.this);

            try {
                //otennego i dati dell'account attivo
                account = dbhelper.getActiveAccount();
                //setto le text view account e email ne navigation menu con quelle correnti
                tvNavUsername.setText(account.getUsername());
                tvNavEmail.setText(account.getEmail());


            } catch (Exception e) {
                Toast.makeText(PagModificaTurni.this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }






        bModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account==null){
                    Toast.makeText(PagModificaTurni.this, "Effettua il login o registrati!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //ripristino i dati selezionat dall'utente
                    LocalDate dataTurno = LocalDate.parse(sGiorno.getSelectedItem().toString(), DateTimeFormatter.ofPattern("dd/MM/yy"));
                    LocalTime oraInizio= LocalTime.parse(sOraInizio.getSelectedItem().toString());
                    LocalTime oraFine= LocalTime.parse(sOraFine.getSelectedItem().toString());

                    if(oraFine.isBefore(oraInizio)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PagModificaTurni.this);
                        builder.setTitle("ERRORE INSERIMENTO DATI");
                        builder.setMessage("L'orario di fine turno non deve precedere quello di inizio!!");
                        builder.show();
                    }else{
                        turno = new Turno(dataTurno, oraInizio, oraFine);

                        AlertDialog.Builder builder = new AlertDialog.Builder(PagModificaTurni.this);
                        builder.setTitle("MODIFICA/INSERIMENTO TURNO");
                        builder.setMessage("Sei sicuro di voler modificare il turno da te selezionato?\n" +
                                "\nData: "+ dataTurno.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                                "\nora inizio: " + oraInizio.format(DateTimeFormatter.ofPattern("HH:mm")) +
                                "\nora fine: " + oraFine.format(DateTimeFormatter.ofPattern("HH:mm")));
                        builder.setNegativeButton("ANNULLA",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    dbhelper.modificaTurno(account, turno);
                                    Toast.makeText(PagModificaTurni.this, "Modifica avvenuta con successo", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(PagModificaTurni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();
                    }
                }



            }
        });


    }


}