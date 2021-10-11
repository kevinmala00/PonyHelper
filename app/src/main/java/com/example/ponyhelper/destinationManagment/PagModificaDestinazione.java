package com.example.ponyhelper.destinationManagment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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

import java.time.LocalDate;
import java.time.LocalTime;

public class PagModificaDestinazione extends AppCompatActivity {
    private EditText etIndirizzo, etCivico, etCitta, etProvincia, etCap, etNote, etMancia;
    private TextView tvLatitudine, tvLongitudine;

    private Destinazione mDestinazione;
    private Location mLocation;
    boolean mAggiornamento = false;
    private Destinazione mOldDestinazione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_modifica_destinazione);

        GpsTracker gps = new GpsTracker(PagModificaDestinazione.this);
        mLocation= gps.getLocation();

        etIndirizzo = findViewById(R.id.et_indirizzoModDest);
        etCivico = findViewById(R.id.et_civicoModDest);
        etCitta = findViewById(R.id.et_cittaModDest);
        etProvincia = findViewById(R.id.et_provinciaModDest);
        etCap = findViewById(R.id.et_capModDest);
        etNote = findViewById(R.id.et_noteModDest);
        etMancia = findViewById(R.id.et_manciaModDest);
        Button bSalva = findViewById(R.id.b_salvaModDest);
        tvLatitudine = findViewById(R.id.tv_latitudineModDest);
        tvLongitudine = findViewById(R.id.tv_longitudineModDest);
        ImageButton ibRefreshGps = findViewById(R.id.ib_refreshGpsModDest);

        if(mAggiornamento){
            etIndirizzo.setText(String.valueOf(mDestinazione.getIndirizzo().getVia()));
            etCivico.setText(String.valueOf(mDestinazione.getIndirizzo().getCivico()));
            etCitta.setText(String.valueOf(mDestinazione.getIndirizzo().getCitta()));
            etProvincia.setText(String.valueOf(mDestinazione.getIndirizzo().getProvincia()));
            etCap.setText(String.valueOf(mDestinazione.getIndirizzo().getCap()));
            etNote.setText(mDestinazione.getNote());
            etMancia.setText(String.valueOf(mDestinazione.getMancia()));
        }

        bSalva.setOnClickListener(salvaDestinazione);

        ibRefreshGps.setOnClickListener(v -> {
            if(mAggiornamento){
                AlertDialog.Builder builder = new AlertDialog.Builder(PagModificaDestinazione.this);
                builder.setTitle("AGGIORNAMENTO DESTINAZIONE");
                builder.setMessage("Confermi di volere aggiornare la posizione corrente? Ricordati che stai aggiornando una destinazione perderai i dati precedenti");
                builder.setPositiveButton("Conferma", (dialog, which) -> {
                    mLocation= gps.getLocation();
                    tvLatitudine.setText(String.valueOf(mLocation.getLatitude()));
                    tvLongitudine.setText(String.valueOf(mLocation.getLongitude()));
                    Toast.makeText(PagModificaDestinazione.this, "Posizione aggiornata", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());
                builder.show();
            }else{
                mLocation= gps.getLocation();
                tvLatitudine.setText(String.valueOf(mLocation.getLatitude()));
                tvLongitudine.setText(String.valueOf(mLocation.getLongitude()));
                Toast.makeText(PagModificaDestinazione.this, "Posizione aggiornata", Toast.LENGTH_SHORT).show();

            }
        });



    }


    View.OnClickListener salvaDestinazione = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //variabile che indica l'esito dei vari controlli
            boolean check = true;
            if(!checkCampoObbligatorio(etIndirizzo)){
                check =false;
            }
            if(!checkCampoObbligatorio(etCivico)){
                check =false;
            }
            if(!checkCampoObbligatorio(etCitta)){
                check =false;
            }
            if(!checkCampoObbligatorio(etProvincia)){
                check =false;
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
                double latitudine = mLocation.getLatitude();
                double longitudine = mLocation.getLongitude();
                Indirizzo indirizzo = new Indirizzo(via, civico, citta, provincia, cap);
                mDestinazione = new Destinazione(indirizzo, LocalDate.now(), LocalTime.now(), mancia, longitudine, latitudine, note);
                try{
                    DbHelper dbHelper = new DbHelper(PagModificaDestinazione.this);
                    if(mAggiornamento){
                        dbHelper.updateDestinazione(dbHelper.getActiveAccount(), mOldDestinazione,  mDestinazione);
                        Toast.makeText(PagModificaDestinazione.this, "Destinazione aggiornata correttemente", Toast.LENGTH_SHORT).show();
                    }else {
                        dbHelper.salvaDestinazione(dbHelper.getActiveAccount(), mDestinazione);
                        Toast.makeText(PagModificaDestinazione.this, "Destinazione inserita correttemente", Toast.LENGTH_SHORT).show();
                    }
                    returnToPagDestinazioni();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(PagModificaDestinazione.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(PagModificaDestinazione.this);
                builder.setTitle("ERRORE");
                builder.setMessage("Non è stato possibilie salvare la destinazione: controlla i campi obbligatori e di aver fornito l'accesso alla geolocalizzazione.");
                builder.show();
            }

        }
    };

    public boolean checkCampoObbligatorio(EditText editText){
        if( editText.getText().toString().length() != 0 ) {
            return true;
        }else{
            editText.setError("Campo obbligatorio");
            return false;
        }
    }

    private  void returnToPagDestinazioni(){
        finish();
        Intent returnToPagDestinazioni =  new Intent(PagModificaDestinazione.this, PagDestinazioni.class);
        startActivity(returnToPagDestinazioni);
    }
}
