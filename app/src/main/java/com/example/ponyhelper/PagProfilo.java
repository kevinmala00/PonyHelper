package com.example.ponyhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

public class PagProfilo extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    PonyAccount account;
    DbHelper dbhelper;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_profilo);

        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView=findViewById(R.id.navigation_view_pag_profilo);
        navigationView.setCheckedItem(R.id.nav_item_profilo);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //ISTANZIO DBHELPER
        dbhelper= new DbHelper(PagProfilo.this);

        //INIZIALIZZO LE VARIE VIEW
        TextView tvUsername = findViewById(R.id.tv_usernamePagProfilo);
        TextView tvEmail = findViewById(R.id.tv_emailPagProfilo);
        TextView tvNome = findViewById(R.id.tv_nomePagProfilo);
        TextView tvCognome = findViewById(R.id.tv_cognomePagProfilo);
        ImageButton bLogout = findViewById(R.id.b_logoutPagProfilo);
        Button bLogin = findViewById(R.id.b_loginPagProfilo);
        Button bRegistrati = findViewById(R.id.b_registratiPagProfilo);

        View headerView=navigationView.getHeaderView(0);
        TextView tvNavUsername= headerView.findViewById(R.id.tv_usernameNavMenu);
        TextView tvNavEmail=headerView.findViewById(R.id.tv_navEmail);


        //RECUPERO I DATI DELL'ACCOUNT ATTIVO
        try{
            account = dbhelper.getActiveAccount();
            //SETTO IL TESTO DELLE TEXTVIEW
            tvUsername.setText(account.getUsername());
            tvEmail.setText(account.getEmail());
            tvNome.setText(account.getNome());
            tvCognome.setText(account.getCognome());

            //SETTO USERNAME E EMAIL NEL NAV HEADER
            tvNavUsername.setText(account.getUsername());
            tvNavEmail.setText(account.getEmail());


        } catch (Exception e) {
            Toast.makeText(PagProfilo.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //SETTO L'ONCLICKLISTENER DI B_LOGOUT
        bLogout.setOnClickListener(logout);
        bLogin.setOnClickListener(login);
        bRegistrati.setOnClickListener(registrati);


    }

    //ONCLICKLISTENER LOGOUT
    View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UtilClass.logout(PagProfilo.this, account);
        }
    };


    //ONCLICKLISTENER LOGIN
    View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent =  new Intent(PagProfilo.this, PagLogin.class);
            startActivity(intent);
        }
    };

    //ONCLICKLISTENER REGISTRATI
    View.OnClickListener registrati = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent =  new Intent(PagProfilo.this, PagReg.class);
            startActivity(intent);
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
                Intent openMainActivity = new Intent(PagProfilo.this, HomePage.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
                break;


            }
            case R.id.nav_item_destinazioni: {
                finish();
                startActivity(new Intent(PagProfilo.this, PagDestinazioni.class));
                break;
            }
            case R.id.nav_item_turni: {
                finish();
                startActivity(new Intent(PagProfilo.this, PagModificaTurni.class));
                break;

            }
            case R.id.nav_item_entrate: {
                finish();
                startActivity(new Intent(PagProfilo.this,  PagEntrate.class));
                break;
            }
            case R.id.nav_item_menu: {
                finish();
                startActivity(new Intent(PagProfilo.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {
                finish();
                startActivity(new Intent(PagProfilo.this, PagCalcoloTot.class));
                break;
            }
            case R.id.nav_item_profilo: {

                break;
            }
            case R.id.nav_item_info: {
                finish();
                startActivity(new Intent(PagProfilo.this, PagInfo.class));
                break;
            }
            case R.id.nav_item_logout: {
                UtilClass.logout(PagProfilo.this, account);
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}