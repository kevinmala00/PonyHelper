package com.example.ponyhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.destinationManagment.PagDestinazioni;
import com.example.ponyhelper.entrate.PagEntrate;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PagReg extends NavigationActivity {
    PonyAccount account;
    DbHelper dbhelper;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    //variabile che rappresenta l'esito dei controlli
    boolean check = true;
    TextInputLayout itUsername, itNome, itCognome, itEmail, itPassword, itConfermaPass;
    TextView tvNavUsername, tvNavEmail;

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
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_item_profilo);
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

        View headerView=navigationView.getHeaderView(0);
        tvNavUsername= headerView.findViewById(R.id.tv_usernameNavMenu);
        tvNavEmail=headerView.findViewById(R.id.tv_navEmail);

        //setto gli onTextChanger degli edit text presenti
        UtilClass.eliminaErroreCampoObbligatorio(itUsername);
        UtilClass.eliminaErroreCampoObbligatorio(itNome);
        UtilClass.eliminaErroreCampoObbligatorio(itCognome);
        UtilClass.eliminaErroreCampoObbligatorio(itPassword);
        UtilClass.eliminaErroreCampoObbligatorio(itEmail);
        UtilClass.eliminaErroreCampoObbligatorio(itConfermaPass);

        //inserimento terminato controllo i parametri
        Button breg=findViewById(R.id.b_registratipagreg);
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper = new DbHelper(PagReg.this);

                try {
                    try{
                        account = dbhelper.getActiveAccount();
                        tvNavUsername.setText(account.getUsername());
                        tvNavEmail.setText(account.getEmail());
                        Toast.makeText(PagReg.this, "Esiste gia un account attivo. \nEffettua il Logout!", Toast.LENGTH_LONG).show();
                        check=false;
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //controllo i campi obbligatori
                    if(!UtilClass.checkCampoObbligatorio(itUsername)){
                        check=false;
                    }
                    if(!UtilClass.checkCampoObbligatorio(itNome)){
                        check=false;
                    }
                    if(!UtilClass.checkCampoObbligatorio(itCognome)){
                        check= false;
                    }
                    if(!UtilClass.checkCampoObbligatorio(itPassword)){
                        check= false;
                    }
                    if(!UtilClass.checkCampoObbligatorio(itConfermaPass)){
                        check= false;
                    }
                    if(!UtilClass.checkCampoObbligatorio(itEmail)){
                        check= false;
                    }

                    String Password = Objects.requireNonNull(itPassword.getEditText()).getText().toString();
                    String ConfermaPassword = Objects.requireNonNull(itConfermaPass.getEditText()).getText().toString();

                    //controllo che le password siano uguali
                    if (!(ConfermaPassword.equals(Password))) {
                        check = false;
                        itPassword.setError("Password differenti");
                        itConfermaPass.setError("Password differenti");
                    }

                    //se il controllo è OK passo alla prossima activity, portando i dati dell'account
                    if (check) {

                        //salvo i dati inseriti dall'utente nell'account
                        account = new PonyAccount();
                        account.setUsername(Objects.requireNonNull(itUsername.getEditText()).getText().toString());
                        account.setNome(Objects.requireNonNull(itNome.getEditText()).getText().toString());
                        account.setCognome(Objects.requireNonNull(itCognome.getEditText()).getText().toString());
                        account.setEmail(Objects.requireNonNull(itEmail.getEditText()).getText().toString());

                        //controllo se i dati sono già in uso nel database
                        dbhelper.checkRegistrazione(account.getUsername(), account.getEmail());

                        //SETTO USERNAME E EMAIL NEL NAV HEADER
                        tvNavUsername.setText(account.getUsername());
                        tvNavEmail.setText(account.getEmail());

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
                    Toast.makeText(PagReg.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

}

