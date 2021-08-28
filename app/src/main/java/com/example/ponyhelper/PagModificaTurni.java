package com.example.ponyhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class PagModificaTurni extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    PonyAccount account;
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
        Button bModifica = findViewById(R.id.b_modificaturni);




        //SETTO IL TESTO DELLE VARIE TEXTVIEW
        //meseanno con il mese e anno  corrente
        String mese=LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int anno=LocalDate.now().getYear();
        String stringMeseAnno= mese + "\t\t" + anno;
        tvMeseAnno.setText(stringMeseAnno);
        //la durata della settimana partendo dal primo giorno di questa settimana fino all'ultimo
        String giornoInizio = UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("dd"));
        String giornoFine = UtilClass.getDayOfDataWeek(LocalDate.now(), DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern("dd"));
        String durataSettimana = "Dal\t\t" + giornoInizio + "\t\tal\t\t" + giornoFine;
        tvDurataSettimana.setText(durataSettimana);

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
                Toast.makeText(PagModificaTurni.this, "Benvenuto " + account.getUsername(), Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Toast.makeText(PagModificaTurni.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        //setto le text view account e email ne navigation manu con quelle correnti
        tvNavUsername.setText(account.getUsername());
        tvNavEmail.setText(account.getEmail());

        bModifica.setOnClickListener(modificaTurno);


    }

    //ONCLICKLISTENER MODIFICA TURNO
    View.OnClickListener modificaTurno = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

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
                recreate();
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