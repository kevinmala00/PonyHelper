package com.example.ponyhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.body.Turno;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    PonyAccount account;
    DbHelper dbhelper;
    List<Turno> turniSettimanali;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //setto la toolbar
        toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView = findViewById(R.id.navigation_view_home_page);
        navigationView.setCheckedItem(R.id.nav_item_home);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        dbhelper = new DbHelper(HomePage.this);

        //INIZIALIZZO LE VARIE VIEW
        //recycler view
        RecyclerView rvTurniSettimanali = findViewById(R.id.rv_turnisettimanali);
        //view del navigationHeader, tv username e email presenti in esso
        View headerView = navigationView.getHeaderView(0);
        TextView tvNavUsername = headerView.findViewById(R.id.tv_usernameNavMenu);
        TextView tvNavEmail = headerView.findViewById(R.id.tv_navEmail);
        TextView tvDurataSettimana = findViewById(R.id.tv_durataSettimana);
        TextView tvMeseAnno = findViewById(R.id.tv_meseanno);
        TextView tvEntrateMensili = findViewById(R.id.tv_entratemensili);

        //SETTO IL TESTO DELLE VARIE TEXTVIEW
        //meseanno con il mese e anno  corrente
        String mese = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int anno = LocalDate.now().getYear();
        String stringMeseAnno = mese + "\t\t" + anno;
        tvMeseAnno.setText(stringMeseAnno);

        //la durata della settimana partendo dal primo giorno di questa settimana fino all'ultimo
        String giornoInizio = UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("dd"));
        String giornoFine = UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern("dd"));
        String durataSettimana = "Dal\t\t" + giornoInizio + "\t\tal\t\t" + giornoFine;
        tvDurataSettimana.setText(durataSettimana);



        try {
            //otennego i dati dell'account attivo
            account = dbhelper.getActiveAccount();
            Toast.makeText(HomePage.this, "Benvenuto " + account.getUsername(), Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        //setto le text view account e email ne navigation manu con quelle correnti
        tvNavUsername.setText(account.getUsername());
        tvNavEmail.setText(account.getEmail());

        try {
            //ottengo la lista dei turni settimanali
            turniSettimanali = dbhelper.getTurniFromInterval(account.getUsername(),
                    UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.MONDAY),
                    UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.SUNDAY));

            //popolo la recycler view con la lista dei turni settimanali
            TurniAdapter turniAdapter = new TurniAdapter(turniSettimanali);
            rvTurniSettimanali.setAdapter(turniAdapter);
            rvTurniSettimanali.setLayoutManager(new LinearLayoutManager(this));
        }catch (Exception e){
            Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //totmensile con i ricavi netti mensili
        String entrateMensili = "ENTRATE MENSILI:\t\t" + dbhelper.getTotMensile(stringMeseAnno, account.getUsername()) + "\t\t€";
        tvEntrateMensili.setText(entrateMensili);

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
                break;
            }
            case R.id.nav_item_destinazioni: {
                startActivity(new Intent(HomePage.this, PagDestinazioni.class));
                break;
            }
            case R.id.nav_item_turni: {

                startActivity(new Intent(HomePage.this, PagModificaTurni.class));
                break;
            }
            case R.id.nav_item_entrate: {
                startActivity(new Intent(HomePage.this, PagEntrate.class));
                break;
            }
            case R.id.nav_item_menu: {
                startActivity(new Intent(HomePage.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {
                startActivity(new Intent(HomePage.this, PagCalcoloTot.class));
                break;
            }
            case R.id.nav_item_profilo: {
                startActivity(new Intent(HomePage.this, PagProfilo.class));
                break;
            }
            case R.id.nav_item_info: {
                startActivity(new Intent(HomePage.this,  PagInfo.class));
                break;
            }
            case R.id.nav_item_logout:{
                UtilClass.logout(HomePage.this, account);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }





}