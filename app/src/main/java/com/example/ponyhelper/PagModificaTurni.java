package com.example.ponyhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.body.Turno;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PagModificaTurni extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private PonyAccount account;
    private Turno turno;
    DbHelper dbhelper;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
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
        navigationView=findViewById(R.id.navigation_view_pag_modifica_turni);
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

        //creao la lista dei giorni selezionabili e li collego allo spinner tramite l'arrayadapter
        LocalDate giorno=LocalDate.from(monday);
        List<String> giorniSettimana = new ArrayList<>();
        for(int i = 0; i<=6; i++){
            giorniSettimana.add(giorno.plusDays(i).format(DateTimeFormatter.ofPattern("dd")));
        }

        //CREO E SETTO GLI SPINNER ADAPTER
        ArrayAdapter<String> spinnerAdapterGiorni = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, giorniSettimana);
        sGiorno.setAdapter(spinnerAdapterGiorni);
        ArrayAdapter<String> spinnerAdapterOre = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.orari));
        sOraInizio.setAdapter(spinnerAdapterOre);
        sOraFine.setAdapter(spinnerAdapterOre);

        dbhelper = new DbHelper(PagModificaTurni.this);

        if (savedInstanceState != null) {
            //ripristino i dati dal bundle passato dall'activity precedente
            Bundle accountBundle = getIntent().getExtras().getBundle("account");
            if(accountBundle!=null){
                account = UtilClass.ripristinoAccount(accountBundle);
            }
        }else{
            try {
                //otennego i dati dell'account attivo
                account = dbhelper.getActiveAccount();


            } catch (Exception e) {
                Toast.makeText(PagModificaTurni.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        //setto le text view account e email ne navigation menu con quelle correnti
        tvNavUsername.setText(account.getUsername());
        tvNavEmail.setText(account.getEmail());



        bModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ripristino i dati selezionat dall'utente
                LocalDate dataTurno = LocalDate.of(LocalDate.now().getYear(),
                        LocalDate.now().getMonth(), Integer.parseInt(sGiorno.getSelectedItem().toString()));
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
                            } catch (Exception e) {
                                Toast.makeText(PagModificaTurni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }


            }
        });


    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_item_home: {
                startActivity(new Intent(PagModificaTurni.this, HomePage.class));
                break;

            }
            case R.id.nav_item_destinazioni: {
                startActivity(new Intent(PagModificaTurni.this, PagDestinazioni.class));
                break;
            }
            case R.id.nav_item_turni: {
                 break;

            }
            case R.id.nav_item_entrate: {
                startActivity(new Intent(PagModificaTurni.this,  PagEntrate.class));
                break;
            }
            case R.id.nav_item_menu: {
                startActivity(new Intent(PagModificaTurni.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {
                startActivity(new Intent(PagModificaTurni.this, PagCalcoloTot.class));
                break;
            }
            case R.id.nav_item_profilo: {
                startActivity(new Intent(PagModificaTurni.this, PagProfilo.class));
                break;
            }
            case R.id.nav_item_info: {
                startActivity(new Intent(PagModificaTurni.this, PagInfo.class));
                break;
            }
            case R.id.nav_item_logout:{

                UtilClass.logout(PagModificaTurni.this, account);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}