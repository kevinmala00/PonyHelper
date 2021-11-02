package com.example.ponyhelper.info;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.ponyhelper.NavigationActivity;
import com.example.ponyhelper.R;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PagInfo extends NavigationActivity {
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    private PonyAccount account;
    Button bOpenPresentazione;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_info);


        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_item_info);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView=navigationView.getHeaderView(0);
        TextView tvNavUsername= headerView.findViewById(R.id.tv_usernameNavMenu);
        TextView tvNavEmail=headerView.findViewById(R.id.tv_navEmail);

        DbHelper dbhelper = new DbHelper(PagInfo.this);

        try {
            //otennego i dati dell'account attivo
            account = dbhelper.getActiveAccount();
            //setto le text view account e email ne navigation menu con quelle correnti
            tvNavUsername.setText(account.getUsername());
            tvNavEmail.setText(account.getEmail());


        } catch (Exception e) {
            Toast.makeText(PagInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        finally {


            bOpenPresentazione= findViewById(R.id.b_openPresentazione);
            bOpenPresentazione.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   PresentationOpener presentationOpener = new PresentationOpener(PagInfo.this);
                   presentationOpener.open();
                }
            });
        }
    }

}


