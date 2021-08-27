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
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.google.android.material.navigation.NavigationView;

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

        View lModificaTurno=findViewById(R.id.l_modificaturno);
        lModificaTurno.setClipToOutline(true);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(PagModificaTurni.this);
                builder.setTitle("LOGOUT");
                builder.setMessage("Sei sicuro di voler effettuare il logout?");
                builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dbhelper.logout(account);
                        } catch (Exception e) {
                            Toast.makeText(PagModificaTurni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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