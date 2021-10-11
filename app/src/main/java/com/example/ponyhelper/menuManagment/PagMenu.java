package com.example.ponyhelper.menuManagment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.example.ponyhelper.NavigationActivity;
import com.example.ponyhelper.R;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.infoEaiutoSupporto.PagInfo;
import com.google.android.material.navigation.NavigationView;

public class PagMenu extends NavigationActivity {
    PonyAccount account;
    DbHelper dbhelper;

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_menu);

        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_item_menu);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //istanzio le view del navmenu
        View headerView=navigationView.getHeaderView(0);
        TextView tvNavUsername= headerView.findViewById(R.id.tv_usernameNavMenu);
        TextView tvNavEmail=headerView.findViewById(R.id.tv_navEmail);

        dbhelper = new DbHelper(PagMenu.this);

        try {
            //otennego i dati dell'account attivo
            account = dbhelper.getActiveAccount();
            //setto le text view account e email ne navigation menu con quelle correnti
            tvNavUsername.setText(account.getUsername());
            tvNavEmail.setText(account.getEmail());


        } catch (Exception e) {
            Toast.makeText(PagMenu.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}