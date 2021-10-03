package com.example.ponyhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.destinationManagment.PagDestinazioni;
import com.example.ponyhelper.entrate.PagEntrate;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

public class PagCalcoloTot extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    PonyAccount account;
    DbHelper dbhelper;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_calcolo_tot);


        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView=findViewById(R.id.navigation_view_pag_calcolo_tot);
        navigationView.setCheckedItem(R.id.nav_item_calcola_tot);
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
                startActivity(new Intent(PagCalcoloTot.this, HomePage.class));
                break;

            }
            case R.id.nav_item_destinazioni: {
                startActivity(new Intent(PagCalcoloTot.this, PagDestinazioni.class));
                break;
            }
            case R.id.nav_item_turni: {

                startActivity(new Intent(PagCalcoloTot.this, PagModificaTurni.class));
                break;
            }
            case R.id.nav_item_entrate: {
                startActivity(new Intent(PagCalcoloTot.this, PagEntrate.class));
                break;
            }
            case R.id.nav_item_menu: {
                startActivity(new Intent(PagCalcoloTot.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {

                break;
            }
            case R.id.nav_item_profilo: {
                startActivity(new Intent(PagCalcoloTot.this, PagProfilo.class));
                break;
            }
            case R.id.nav_item_info: {
                startActivity(new Intent(PagCalcoloTot.this,  PagInfo.class));
                break;
            }
            case R.id.nav_item_logout:{

                UtilClass.logout(PagCalcoloTot.this, account);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}