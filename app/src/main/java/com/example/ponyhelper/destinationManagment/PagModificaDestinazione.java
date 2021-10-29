package com.example.ponyhelper.destinationManagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ponyhelper.R;
import com.example.ponyhelper.body.Destinazione;
import com.example.ponyhelper.body.Indirizzo;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PagModificaDestinazione extends AppCompatActivity {
    private EditText etIndirizzo, etCivico, etCitta, etProvincia, etCap, etNote, etMancia;
    private TextView tvLatitudine, tvLongitudine;
    private  ImageButton b_callBack, ibRefreshGps;
    private Button bSalva;

    private Destinazione mDestinazione;
    private Location mLocation;
    boolean mAggiornamento = false;
    private Destinazione mOldDestinazione;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallBack;
    public static  final int LOCATION_PERMISSION_CODE = 100;
    public static  final int DEFAULT_UPDATE_INTERVAL = 3;
    public static  final int FAST_UPDATE_INTERVAL = 1;





    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_modifica_destinazione);

        etIndirizzo = findViewById(R.id.et_indirizzoModDest);
        etCivico = findViewById(R.id.et_civicoModDest);
        etCitta = findViewById(R.id.et_cittaModDest);
        etProvincia = findViewById(R.id.et_provinciaModDest);
        etCap = findViewById(R.id.et_capModDest);
        etNote = findViewById(R.id.et_noteModDest);
        etMancia = findViewById(R.id.et_manciaModDest);
        bSalva = findViewById(R.id.b_salvaModDest);
        tvLatitudine = findViewById(R.id.tv_latitudineModDest);
        tvLongitudine = findViewById(R.id.tv_longitudineModDest);
        ibRefreshGps = findViewById(R.id.ib_refreshGpsModDest);
        b_callBack= findViewById(R.id.ib_returnBack);

        Bundle datiPassati = getIntent().getExtras();
        mAggiornamento = datiPassati.getBoolean("aggiornamento");
        if (mAggiornamento) {
            Indirizzo oldIndirizzo = new Indirizzo();
            oldIndirizzo.setVia(datiPassati.getString("via"));
            oldIndirizzo.setCivico(datiPassati.getString("civico"));
            oldIndirizzo.setCitta(datiPassati.getString("citta"));
            oldIndirizzo.setCap(datiPassati.getInt("cap"));
            oldIndirizzo.setProvincia(datiPassati.getString("provincia"));

            mOldDestinazione = new Destinazione(
                    oldIndirizzo,
                    UtilClass.unixTimeToLocalDate(datiPassati.getLong("dataUltimaModifica")),
                    LocalTime.parse(datiPassati.getString("oraUltimaModifica"), DateTimeFormatter.ofPattern("HH:mm")),
                    datiPassati.getDouble("mancia"),
                    datiPassati.getDouble("longitudine"),
                    datiPassati.getDouble("latitudine"),
                    datiPassati.getString("note")
            );

        }

        //setto le proprieta del locationrequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLocation= locationResult.getLastLocation();
            }
        };

        mFusedLocationClient= new FusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper());




        if (mAggiornamento) {
            etIndirizzo.setText(String.valueOf(mOldDestinazione.getIndirizzo().getVia()));
            etCivico.setText(String.valueOf(mOldDestinazione.getIndirizzo().getCivico()));
            etCitta.setText(String.valueOf(mOldDestinazione.getIndirizzo().getCitta()));
            etProvincia.setText(String.valueOf(mOldDestinazione.getIndirizzo().getProvincia()));
            etCap.setText(String.valueOf(mOldDestinazione.getIndirizzo().getCap()));
            etNote.setText(mOldDestinazione.getNote());
            etMancia.setText(String.valueOf(mOldDestinazione.getMancia()));
            tvLatitudine.setText(String.valueOf(mOldDestinazione.getLatitudine()));
            tvLongitudine.setText(String.valueOf(mOldDestinazione.getLongitudine()));
        }

        bSalva.setOnClickListener(salvaDestinazione);
        b_callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ibRefreshGps.setOnClickListener(v -> {
            try {

                if (mAggiornamento) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PagModificaDestinazione.this);
                    builder.setTitle("AGGIORNAMENTO DESTINAZIONE");
                    builder.setMessage("Confermi di volere aggiornare la posizione corrente? Ricordati che stai aggiornando una destinazione perderai i dati precedenti");
                    builder.setPositiveButton("Conferma", (dialog, which) -> {
                        updateGPS();
                    });
                    builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());
                    builder.show();
                } else {
                    updateGPS();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    View.OnClickListener salvaDestinazione = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //variabile che indica l'esito dei vari controlli
            boolean check = true;
            if (!checkCampoObbligatorio(etIndirizzo)) {
                check = false;
            }
            if (!checkCampoObbligatorio(etCivico)) {
                check = false;
            }
            if (!checkCampoObbligatorio(etCitta)) {
                check = false;
            }
            if (!checkCampoObbligatorio(etProvincia)) {
                check = false;
            }

            String via = etIndirizzo.getText().toString();
            String civico = etCivico.getText().toString();
            String citta = etCitta.getText().toString();
            String provincia = etProvincia.getText().toString();
            int cap = 0;
            String note = null;
            double mancia = 0;
            if (etCap.getText().length() != 0) {
                cap = Integer.parseInt(etCap.getText().toString());
            }
            if (etNote.getText().length() != 0) {
                note = etNote.getText().toString();
            }
            if (etMancia.getText().length() != 0) {
                mancia = Double.parseDouble(etMancia.getText().toString());
            }

            if (check) {
                double latitudine = mLocation.getLatitude();
                double longitudine = mLocation.getLongitude();
                Indirizzo indirizzo = new Indirizzo(via, civico, citta, provincia, cap);
                mDestinazione = new Destinazione(indirizzo, LocalDate.now(), LocalTime.now(), mancia, longitudine, latitudine, note);
                try {
                    DbHelper dbHelper = new DbHelper(PagModificaDestinazione.this);
                    if (mAggiornamento) {
                        mDestinazione.setDataUltimaModifica(LocalDate.now());
                        mDestinazione.setOraUltimaModifica(LocalTime.now());
                        dbHelper.updateDestinazione(dbHelper.getActiveAccount(), mOldDestinazione, mDestinazione);
                        Toast.makeText(PagModificaDestinazione.this, "Destinazione aggiornata correttemente", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.salvaDestinazione(dbHelper.getActiveAccount(), mDestinazione);
                        Toast.makeText(PagModificaDestinazione.this, "Destinazione inserita correttemente", Toast.LENGTH_SHORT).show();
                    }
                    onBackPressed();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PagModificaDestinazione.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PagModificaDestinazione.this);
                builder.setTitle("ERRORE");
                builder.setMessage("Non è stato possibilie salvare la destinazione: controlla i campi obbligatori e di aver fornito l'accesso alla geolocalizzazione.");
                builder.show();
            }

        }
    };

    public boolean checkCampoObbligatorio(EditText editText) {
        if (editText.getText().toString().length() != 0) {
            return true;
        } else {
            editText.setError("Campo obbligatorio");
            return false;
        }
    }


    private void updateGPS(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //abbiamo la permission, update location and textview
                    mLocation=location;
                    updateLocationTextView(location);
                }
            });
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_CODE);
            }
        }
    }

    private void updateLocationTextView(Location location) {
        tvLongitudine.setText(String.valueOf(location.getLongitude()));
        tvLatitudine.setText(String.valueOf(location.getLatitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case LOCATION_PERMISSION_CODE:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(this, "Devi fornire il permesso di acesso alla posizione", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw new IllegalStateException("Valore inaspettato: " + requestCode);
        }
    }



}