package com.example.ponyhelper.accountManagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.example.ponyhelper.NavigationActivity;
import com.example.ponyhelper.R;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.homeEturni.HomePage;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class PagLogin extends NavigationActivity {
    PonyAccount account;
    DbHelper dbhelper;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    String username;
    String passcod;
    TextInputLayout itUsername;
    TextInputLayout itPasscod;
    boolean utenteAttivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_login);
        dbhelper = new DbHelper(PagLogin.this);

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

        //istanzio le view del navmenu
        View headerView=navigationView.getHeaderView(0);
        TextView tvNavUsername= headerView.findViewById(R.id.tv_usernameNavMenu);
        TextView tvNavEmail=headerView.findViewById(R.id.tv_navEmail);

        //inizializzo i text imput con il quale l'utenet interagisce
        itUsername = findViewById(R.id.it_usernamepaglog);
        itPasscod = findViewById(R.id.it_passcodpaglog);
        UtilClass.eliminaErroreCampoObbligatorio(itUsername);
        UtilClass.eliminaErroreCampoObbligatorio(itPasscod);

        //RECUPERO I DATI DELL'ACCOUNT ATTIVO
        try{
            account = dbhelper.getActiveAccount();
            utenteAttivo = true;
            //SETTO USERNAME E EMAIL NEL NAV HEADER
            tvNavUsername.setText(account.getUsername());
            tvNavEmail.setText(account.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }


        //setto l'onClickListenere in modo che cliccando blogin si sttivi login
        Button bLogin = findViewById(R.id.b_loginpaglogin);
        bLogin.setOnClickListener(login);
    }

    //OnClickListener che permette il login
    View.OnClickListener login= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(utenteAttivo){
                Toast.makeText(PagLogin.this, "Esiste gia un utente attivo!\nEffettuare il logout.", Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    if(UtilClass.checkCampoObbligatorio(itUsername) && UtilClass.checkCampoObbligatorio(itPasscod)){
                        username = Objects.requireNonNull(itUsername.getEditText()).getText().toString();
                        passcod = Objects.requireNonNull(itPasscod.getEditText()).getText().toString();

                        account = dbhelper.login(username, passcod);
                        Bundle accountBundle = UtilClass.salvataggioAccount(account);
                        Intent openHomePag = new Intent(PagLogin.this, HomePage.class);
                        openHomePag.putExtra("account", accountBundle);
                        startActivity(openHomePag);
                    }

                } catch (Exception e) {
                    Toast.makeText(PagLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }
    };
}



