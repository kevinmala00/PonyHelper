package com.example.ponyhelper;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.body.Destinazione;
import com.example.ponyhelper.body.Indirizzo;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class PagDestinazioni extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private static final int PERMISSION_FINE_LOCATION = 99;
    PonyAccount account;
    DbHelper dbhelper;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Destinazione mDestinazione;
    Location mLocation;
    boolean aggiornamento = false;
    List<Destinazione> listDestinazione;
    DestinazioniAdapter destinazioniAdapter;

    boolean check = true;

    static final int DEFAULT_UPDATE_INTERVAL = 10;
    static final int FAST_UPDATE_INTERVAL = 3;

    //riferimentio agli elemnti grafici presenti nel poPuP
    EditText etIndirizzo, etCivico, etCitta, etCap, etProvincia, etNote, etMancia;
    TextView tvLatitudine, tvLongitudine;
    ImageButton ibRefreshGps;
    Button bSalva;

    //riferimenti agli elementi grafici dell'activity
    TextView tvNavUsername, tvNavEmail;
    ImageButton bAddDestinazione, bSearchDestinazione;
    EditText etSearchDestinazione;
    View navHeader;
    RecyclerView rvDestinazioni;

    //google API per i servizi di geolocalizzazione
    FusedLocationProviderClient fusedLocationProviderClient;

    //racchiude le impostazioni relative a FusedLocationProviderClient
    LocationRequest locationRequest;

    //gestore del popUp modifica destinazione
    Dialog dialogModDest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_destinazioni);

        //setto la toolbar
        toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView = findViewById(R.id.navigation_view_pag_destinazioni);
        navigationView.setCheckedItem(R.id.nav_item_destinazioni);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //setto le impostazioni della geolocalizzazione
        locationRequest= new LocationRequest();
        locationRequest.setInterval(1000*DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000*FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //ISTANZIO LE VARIE VIEW
        navHeader = navigationView.getHeaderView(0);
        tvNavUsername = navHeader.findViewById(R.id.tv_usernameNavMenu);
        tvNavEmail = navHeader.findViewById(R.id.tv_navEmail);
        bAddDestinazione = findViewById(R.id.b_aggiunginDestinazione);
        etSearchDestinazione = findViewById(R.id.et_searchDestinazioni);
        bSearchDestinazione=findViewById(R.id.b_searcheDestinazione);
        rvDestinazioni=findViewById(R.id.rv_destinazioni);


        dbhelper = new DbHelper(PagDestinazioni.this);
        try {
            //Recupero i dati dell'account attivo
            account = dbhelper.getActiveAccount();

            //setto il testo nel nav header
            tvNavUsername.setText(account.getUsername());
            tvNavEmail.setText(account.getEmail());

            bAddDestinazione.setOnClickListener(addDestinazione);
            bSearchDestinazione.setOnClickListener(searchDestinazione);

            try{
                //otenngo la lista di destinazione dal database
                listDestinazione=dbhelper.getAllDestinazioni(account);

                //popolo la recyclerview
                destinazioniAdapter = new DestinazioniAdapter(listDestinazione, PagDestinazioni.this);
                rvDestinazioni.setAdapter(destinazioniAdapter);
                rvDestinazioni.setLayoutManager(new LinearLayoutManager(this));



            }catch (Exception e){
                Toast.makeText(PagDestinazioni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } catch (Exception e) {
            Toast.makeText(PagDestinazioni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    View.OnClickListener searchDestinazione = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String indirizzo = String.valueOf(etSearchDestinazione.getText());
            if(indirizzo.trim().length()!=0){
                try{
                    List<Destinazione> searchListDestinazione = dbhelper.searchDestinazione(account, indirizzo);
                    updateDestList(searchListDestinazione);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(PagDestinazioni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    View.OnClickListener addDestinazione = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            //apro il popUp mod dest
            dialogModDest = new Dialog(PagDestinazioni.this);
            dialogModDest.getWindow();
            dialogModDest.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogModDest.setContentView(R.layout.popup_add_dest);
            dialogModDest.setCancelable(true);

            etIndirizzo = dialogModDest.findViewById(R.id.et_indirizzoModDest);
            etCivico = dialogModDest.findViewById(R.id.et_civicoModDest);
            etCitta = dialogModDest.findViewById(R.id.et_cittaModDest);
            etProvincia = dialogModDest.findViewById(R.id.et_provinciaModDest);
            etCap = dialogModDest.findViewById(R.id.et_capModDest);
            etNote = dialogModDest.findViewById(R.id.et_noteModDest);
            etMancia = dialogModDest.findViewById(R.id.et_manciaModDest);
            bSalva = dialogModDest.findViewById(R.id.b_salvaModDest);
            tvLatitudine= dialogModDest.findViewById(R.id.tv_latitudineModDest);
            tvLongitudine= dialogModDest.findViewById(R.id.tv_longitudineModDest);
            ibRefreshGps= dialogModDest.findViewById(R.id.ib_refreshGpsModDest);

            //Controllo se si mDestinazione non null allora è aggiornamento
            if(mDestinazione !=null){
                //inserisco i campi negli edit text
                etIndirizzo.setText(mDestinazione.getIndirizzo().getVia());
                etCivico.setText(mDestinazione.getIndirizzo().getCivico());
                etCitta.setText(mDestinazione.getIndirizzo().getCitta());
                etProvincia.setText(mDestinazione.getIndirizzo().getProvincia());
                etCap.setText(mDestinazione.getIndirizzo().getCap());
                etMancia.setText(String.valueOf(mDestinazione.getMancia()));
                etNote.setText(mDestinazione.getNote());
                tvLatitudine.setText(String.valueOf(mDestinazione.getLatitudine()));
                tvLongitudine.setText(String.valueOf(mDestinazione.getLongitudine()));
                aggiornamento = true;
            }


            ibRefreshGps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //se è aggiornamento chiedo conferma per poter aggiornare la posizione
                    if(aggiornamento){
                        AlertDialog.Builder builder= new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("AGGIORNAMENTO DESTINAZIONE");
                        builder.setMessage("Confermi di volere aggiornare la posizione corrente? Ricordati che stai aggiornando una destinazione perderai i dati precedenti");
                        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateGPS();
                                Toast.makeText(getApplicationContext(), "Posizione aggiornata", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        updateGPS();
                        Toast.makeText(getApplicationContext(), "Posizione aggiornata", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            bSalva.setOnClickListener(salvaDestinazione);

            dialogModDest.show();

        }
    };


    View.OnClickListener salvaDestinazione = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Destinazione oldDestinazione = null;
            //se si tratta di un aggiornamento allora salvo la vecchia destinazione
            if(aggiornamento){
                oldDestinazione=mDestinazione;
            }


            check=true;


            if(!checkCampoObbligatorio(etIndirizzo)){
                check=false;
            }
            if(!checkCampoObbligatorio(etCivico)){
                check=false;
            }
            if(!checkCampoObbligatorio(etCitta)){
                check=false;
            }
            if(!checkCampoObbligatorio(etProvincia)){
                check=false;
            }


            String via = etIndirizzo.getText().toString();
            String civico = etCivico.getText().toString();
            String citta = etCitta.getText().toString();
            String provincia = etProvincia.getText().toString();
            int cap = 0;
            String note = null;
            double mancia =  0;
            if(etCap.getText().length()!=0){
                cap= Integer.parseInt(etCap.getText().toString());
            }
            if(etNote.getText().length()!=0){
                note= etNote.getText().toString();
            }
            if(etMancia.getText().length()!=0){
                mancia= Double.parseDouble(etMancia.getText().toString());
            }



            if(check) {
                try {
                    double latitudine = mLocation.getLatitude();
                    double longitudine = mLocation.getLongitude();
                    Indirizzo indirizzo = new Indirizzo(via, civico, citta, provincia, cap);
                    mDestinazione = new Destinazione(indirizzo, LocalDate.now(), LocalTime.now(), mancia, longitudine, latitudine, note);


                    if(aggiornamento){
                        //se si tratta di un aggiornametno faccio l'update della vecchia destinazione e comunico la modifica all'adapter
                        dbhelper.updateDestinazione(account, oldDestinazione, mDestinazione);
                        Toast.makeText(PagDestinazioni.this, "Destinazione aggiornata con successo", Toast.LENGTH_SHORT).show();
                        updateSingleDestToList(listDestinazione.indexOf(oldDestinazione), mDestinazione);
                    }
                    else {
                        //altrimenti salvo la nuova destinazione e comunico il cambiamento all'adapter
                        dbhelper.salvaDestinazione(account, mDestinazione);
                        Toast.makeText(PagDestinazioni.this, "Destinazione aggiunta con successo", Toast.LENGTH_SHORT).show();
                        addDestToList(mDestinazione);
                    }

                    dialogModDest.dismiss();
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("ERRORE:", e.getMessage());
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(PagDestinazioni.this);
                builder.setTitle("ERRORE");
                builder.setMessage("Non è stato possibilie salvare la destinazione: controlla i campi obbligatori e di aver fornito l'accesso alla geolocalizzazione.");
                builder.show();
            }

        }
    };

    private void removeDestToList(int removeIndex){
        listDestinazione.remove(removeIndex);
        destinazioniAdapter.notifyItemRemoved(removeIndex);
    }
    
    private void addDestToList(Destinazione newDest) {
        listDestinazione.add(0, newDest);
        destinazioniAdapter.notifyItemInserted(0);
    }

    private void updateSingleDestToList(int updateIndex, Destinazione newDest) {
        listDestinazione.set(updateIndex, newDest);
        destinazioniAdapter.notifyItemChanged(updateIndex);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    check=false;
                    Toast.makeText(this, "E' necessario fornire il permesso all'accesso della posizione", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * metodo per aggiornare la lista contenente i dati della recyclerview, e informare l'adapter
     * @param newListDestinazione nuova lista contenente i nuovi dati
     */
    public void updateDestList(List<Destinazione> newListDestinazione){
        listDestinazione.clear();
        listDestinazione.addAll(newListDestinazione);
        destinazioniAdapter.notifyDataSetChanged();
    }
    
    
    private void updateGPS(){
        //check delle permission
        //salvataggio della posizione

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //l'utente ha fornito il permesso alla
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //accedo alla posizione
                    saveLocation(location);

                }
            });
        }else{
            //permesso non concesso
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION );
        }
    }

    private void saveLocation(Location location){
        mLocation=location;
        tvLatitudine.setText(String.valueOf(mLocation.getLatitude()));
        tvLongitudine.setText(String.valueOf(mLocation.getLongitude()));
    }


    public boolean checkCampoObbligatorio(EditText editText){
        if( editText.getText().toString().length() != 0 ) {
            return true;
        }else{
            editText.setError("Campo obbligatorio");
            return false;
        }

    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_item_home: {
                startActivity(new Intent(PagDestinazioni.this, HomePage.class));
                break;

            }
            case R.id.nav_item_destinazioni: {

                break;
            }
            case R.id.nav_item_turni: {

                startActivity(new Intent(PagDestinazioni.this, PagModificaTurni.class));
                break;
            }
            case R.id.nav_item_entrate: {
                startActivity(new Intent(PagDestinazioni.this, PagEntrate.class));
                break;
            }
            case R.id.nav_item_menu: {
                startActivity(new Intent(PagDestinazioni.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {
                startActivity(new Intent(PagDestinazioni.this, PagCalcoloTot.class));
                break;
            }
            case R.id.nav_item_profilo: {
                startActivity(new Intent(PagDestinazioni.this, PagProfilo.class));
                break;
            }
            case R.id.nav_item_info: {
                startActivity(new Intent(PagDestinazioni.this,  PagInfo.class));
                break;
            }
            case R.id.nav_item_logout:{

                UtilClass.logout(PagDestinazioni.this, account);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}