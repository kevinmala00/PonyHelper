package com.example.ponyhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class PagEntrate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    PonyAccount account;
    DbHelper dbhelper;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //definisco le view del menu
    View navHeader;
    TextView navUsername, navEmail;

    //definisco le componenti grafiche dell'activity
    ImageButton ibAddEntrata, ibSearchEntrata, ibModCosti, ibNextMonth, ibPreviousMonth;
    EditText etSearchEntrata;
    RecyclerView rvEntrate;
    TextView tvMeseAnno;

    //componenti popUp mod entrata
    Dialog dialogModEntrata;
    EditText etData, etOraInizio, etOraFine, etEntrate, etMancia, etKmPercorsi, etAltro;
    TextView tvOreTotali;
    Button bSalvaEntrata;

    //componenti popUp mod costi
    Dialog dialogModCosti;
    EditText tvMese, etCostoCarburante, etConsumoMedio;
    Button bSalvaCosti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_entrate);
        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView=findViewById(R.id.navigation_view_pag_entrate);
        navigationView.setCheckedItem(R.id.nav_item_entrate);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navHeader = navigationView.getHeaderView(0);
        navUsername = navHeader.findViewById(R.id.tv_usernameNavMenu);
        navEmail = navHeader.findViewById(R.id.tv_navEmail);




        dbhelper=new DbHelper(PagEntrate.this);
        account=new PonyAccount();

        try{
            account = dbhelper.getActiveAccount();
            navUsername.setText(account.getUsername());
            navEmail.setText(account.getEmail());





        } catch (Exception e) {
            Toast.makeText(PagEntrate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally{
            String mese = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            int anno = LocalDate.now().getYear();
            String stringMeseAnno = mese + "\t\t" + anno;

            ibAddEntrata=findViewById(R.id.b_aggiunginentrata);
            ibModCosti=findViewById(R.id.b_gestiscicosti);
            ibNextMonth=findViewById(R.id.b_prossimomese);
            ibPreviousMonth=findViewById(R.id.b_meseprecedente);
            ibSearchEntrata=findViewById(R.id.b_searcheentrata);
            etSearchEntrata=findViewById(R.id.et_searchentrate);
            tvMeseAnno=findViewById(R.id.tv_mesecorrente);
            rvEntrate=findViewById(R.id.rv_entrate);

            ibAddEntrata.setOnClickListener(addEntrata);
            ibSearchEntrata.setOnClickListener(searchEntrata);
            ibModCosti.setOnClickListener(modCosti);
            tvMeseAnno.setText(stringMeseAnno);





        }


    }


    View.OnClickListener addEntrata = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(account==null){
                Toast.makeText(PagEntrate.this, "Effettua il login o registrati per aggiungere un'entrata", Toast.LENGTH_SHORT).show();
            }
            else{
                dialogModEntrata = new Dialog(PagEntrate.this);
                dialogModEntrata.getWindow();
                dialogModEntrata.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogModEntrata.setContentView(R.layout.popup_add_entrata);
                dialogModEntrata.setCancelable(true);





                dialogModEntrata.show();




            }






        }
    };

    View.OnClickListener modCosti = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(account==null){
                Toast.makeText(PagEntrate.this, "Effettua il login o registrati per aggiungere un'entrata", Toast.LENGTH_SHORT).show();
            }
            else{
                dialogModCosti = new Dialog(PagEntrate.this);
                dialogModCosti.getWindow();
                dialogModCosti.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogModCosti.setContentView(R.layout.popup_mod_costi);
                dialogModCosti.setCancelable(true);

                String mese = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
                int anno = LocalDate.now().getYear();
                String stringMeseAnno = mese + "\t\t" + anno;

                tvMese.findViewById(R.id.tv_meseModCosti);
                tvMese.setText(stringMeseAnno);

                etCostoCarburante=findViewById(R.id.et_costoCarburanteModCosti);
                etConsumoMedio=findViewById(R.id.et_consumoMedioModCosti);
                bSalvaCosti=findViewById(R.id.b_salvaModCosti);

                bSalvaCosti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });



                dialogModCosti.show();

            }
        }
    };

    View.OnClickListener searchEntrata = new View.OnClickListener() {
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
                finish();
                Intent openMainActivity = new Intent(PagEntrate.this, HomePage.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
                break;

            }
            case R.id.nav_item_destinazioni: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagDestinazioni.class));
                break;
            }
            case R.id.nav_item_turni: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagModificaTurni.class));
                break;
            }
            case R.id.nav_item_entrate: {
                break;

            }
            case R.id.nav_item_menu: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagCalcoloTot.class));
                break;
            }
            case R.id.nav_item_profilo: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagProfilo.class));
                break;
            }
            case R.id.nav_item_info: {
                finish();
                startActivity(new Intent(PagEntrate.this,  PagInfo.class));
                break;
            }
            case R.id.nav_item_logout:{
                UtilClass.logout(PagEntrate.this, account);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}