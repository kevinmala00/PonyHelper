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
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

public class PagInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    PonyAccount account;
    DbHelper dbhelper;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

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
        navigationView=findViewById(R.id.navigation_view_pag_info);
        navigationView.setCheckedItem(R.id.nav_item_info);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
                startActivity(new Intent(PagInfo.this, HomePage.class));
                finish();
                break;

            }
            case R.id.nav_item_destinazioni: {
                startActivity(new Intent(PagInfo.this, PagDestinazioni.class));
                finish();
                break;
            }
            case R.id.nav_item_turni: {

                startActivity(new Intent(PagInfo.this, PagModificaTurni.class));
                finish();
                break;
            }
            case R.id.nav_item_entrate: {
                startActivity(new Intent(PagInfo.this,  PagEntrate.class));
                finish();
                break;
            }
            case R.id.nav_item_menu: {
                startActivity(new Intent(PagInfo.this, PagMenu.class));
                finish();
                break;
            }
            case R.id.nav_item_calcola_tot: {
                startActivity(new Intent(PagInfo.this, PagCalcoloTot.class));
                finish();
                break;
            }
            case R.id.nav_item_profilo: {
                startActivity(new Intent(PagInfo.this, PagProfilo.class));
                finish();
                break;
            }
            case R.id.nav_item_info: {
                break;
            }
            case R.id.nav_item_logout:{

                UtilClass.logout(PagInfo.this, account);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}