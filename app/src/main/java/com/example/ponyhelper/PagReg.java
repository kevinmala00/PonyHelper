package com.example.ponyhelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PagReg extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    PonyAccount account;
    DbHelper dbhelper;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //variabile che rappresenta l'esito dei controlli
    boolean check;
    TextInputLayout itUsername;
    TextInputLayout itNome;
    TextInputLayout itCognome;
    TextInputLayout itEmail;
    TextInputLayout itPassword;
    TextInputLayout itConfermaPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_reg);

        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView=findViewById(R.id.navigation_view_pag_reg);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        itUsername=findViewById(R.id.it_usernamepagreg);
        itNome=findViewById(R.id.it_nomepagreg);
        itCognome=findViewById(R.id.it_cognomepagreg);
        itEmail=findViewById(R.id.it_emailpagreg);
        itPassword = findViewById(R.id.it_passwordpagreg);
        itConfermaPass=findViewById(R.id.it_confermapassword);




        //setto gli onTextChanger degli edit text presenti
        UtilClass.eliminaErroreCampoObbligatorio(itUsername);
        UtilClass.eliminaErroreCampoObbligatorio(itNome);
        UtilClass.eliminaErroreCampoObbligatorio(itCognome);
        UtilClass.eliminaErroreCampoObbligatorio(itPassword);
        UtilClass.eliminaErroreCampoObbligatorio(itEmail);
        UtilClass.eliminaErroreCampoObbligatorio(itConfermaPass);

        //inserimento terminato controllo i parametri
        Button breg=findViewById(R.id.b_registratipagreg);
        breg.setOnClickListener(registrazione);
    }


    View.OnClickListener registrazione = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //salvo i dati inseriti dall'utente nell'account
            account.setUsername(Objects.requireNonNull(itUsername.getEditText()).getText().toString());
            account.setNome(Objects.requireNonNull(itNome.getEditText()).getText().toString());
            account.setCognome(Objects.requireNonNull(itCognome.getEditText()).getText().toString());
            account.setEmail(Objects.requireNonNull(itEmail.getEditText()).getText().toString());

            String Password = Objects.requireNonNull(itPassword.getEditText()).getText().toString();
            String ConfermaPassword = Objects.requireNonNull(itConfermaPass.getEditText()).getText().toString();

            DbHelper dbhelper = new DbHelper(PagReg.this);
            try {
                //controllo se i dati sono già in uso nel database
                dbhelper.checkRegistrazione(account.getUsername(), account.getEmail());

                //controllo i campi obbligatori
                check = UtilClass.checkCampoObbligatorio(itUsername);
                check = UtilClass.checkCampoObbligatorio(itNome);
                check = UtilClass.checkCampoObbligatorio(itCognome);
                check = UtilClass.checkCampoObbligatorio(itPassword);
                check = UtilClass.checkCampoObbligatorio(itEmail);
                check = UtilClass.checkCampoObbligatorio(itConfermaPass);

                //controllo che le password siano uguali
                if (!(ConfermaPassword.equals(Password))) {
                    check = false;
                    itPassword.setError("Password differenti");
                    itConfermaPass.setError("Password differenti");
                }


                //se il controllo è OK passo alla prossima activity, portando i dati dell'account
                if (check) {
                    Intent openHomePage = new Intent(PagReg.this, HomePage.class);
                    Bundle accountBundle = UtilClass.salvataggioAccount(account);

                    if (dbhelper.registrazioneAccount(account, Password)) {
                        openHomePage.putExtra("account", accountBundle);
                        PagReg.this.startActivity(openHomePage);
                    } else {
                        Toast.makeText(PagReg.this, "Errori nella registrazione. \nRiprova", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                check = false;
                Toast.makeText(PagReg.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
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
                startActivity(new Intent(PagReg.this, HomePage.class));
                break;

            }
            case R.id.nav_item_destinazioni: {
                startActivity(new Intent(PagReg.this, PagDestinazioni.class));
                break;
            }
            case R.id.nav_item_turni: {

                startActivity(new Intent(PagReg.this, PagModificaTurni.class));
                break;
            }
            case R.id.nav_item_entrate: {
                startActivity(new Intent(PagReg.this,  PagEntrate.class));
                break;
            }
            case R.id.nav_item_menu: {
                startActivity(new Intent(PagReg.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {
                startActivity(new Intent(PagReg.this, PagCalcoloTot.class));
                break;
            }
            case R.id.nav_item_profilo: {
                startActivity(new Intent(PagReg.this, PagProfilo.class));
                break;
            }
            case R.id.nav_item_info: {
                startActivity(new Intent(PagReg.this, PagInfo.class));
                break;
            }
            case R.id.nav_item_logout:{

                AlertDialog.Builder builder = new AlertDialog.Builder(PagReg.this);
                builder.setTitle("LOGOUT");
                builder.setMessage("Sei sicuro di voler effettuare il logout?");
                builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dbhelper.logout(account);
                        } catch (Exception e) {
                            Toast.makeText(PagReg.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        finishAffinity();

                        System.exit(0);
                    }
                });
                builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}

