package com.example.ponyhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ponyhelper.accountManagment.PagProfilo;
import com.example.ponyhelper.destinationManagment.PagDestinazioni;
import com.example.ponyhelper.entrate.PagEntrate;
import com.example.ponyhelper.homeEturni.HomePage;
import com.example.ponyhelper.homeEturni.PagModificaTurni;
import com.example.ponyhelper.info.PagInfo;
import com.example.ponyhelper.util.EmailSender;
import com.google.android.material.navigation.NavigationView;

public abstract class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    protected DrawerLayout drawerLayout;
    protected  NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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
                if(this instanceof HomePage){
                    break;
                }
                finish();
                Intent openMainActivity = new Intent(this, HomePage.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
                break;
            }
            case R.id.nav_item_destinazioni: {
                launchActivity(this, PagDestinazioni.class );
                break;
            }
            case R.id.nav_item_turni: {
                launchActivity(this, PagModificaTurni.class );
                break;

            }
            case R.id.nav_item_entrate: {
                launchActivity(this, PagEntrate.class );
                break;
            }
            //case R.id.nav_item_menu: {
            //    launchActivity(this, PagMenu.class );
            //    break;
            //}
            case R.id.nav_item_profilo: {
                launchActivity(this, PagProfilo.class );
                break;
            }
            case R.id.nav_item_info: {

                launchActivity(this, PagInfo.class );
                break;
            }
            case R.id.nav_item_contattaci: {

                EmailSender emailSender = new EmailSender();
                emailSender.sendMail(NavigationActivity.this,
                        "ponyhelper@gmail.com",
                        "",
                        "CONTATTO SUPPORTO PONYHELPER");
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchActivity(Context closedContext, Class newActivity){
        if(!(closedContext instanceof HomePage)){
            finish();
        }
        Intent open = new Intent(closedContext, newActivity);
        closedContext.startActivity(open);
    }
}
