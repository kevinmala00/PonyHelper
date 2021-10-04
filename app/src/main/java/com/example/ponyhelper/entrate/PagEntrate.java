package com.example.ponyhelper.entrate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ponyhelper.HomePage;
import com.example.ponyhelper.PagCalcoloTot;
import com.example.ponyhelper.PagInfo;
import com.example.ponyhelper.PagMenu;
import com.example.ponyhelper.PagModificaTurni;
import com.example.ponyhelper.PagProfilo;
import com.example.ponyhelper.R;
import com.example.ponyhelper.body.Entrata;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.destinationManagment.PagDestinazioni;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PagEntrate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private PonyAccount account;
    private DbHelper dbhelper;
    private List<Entrata> listEntrate;
    private EntrateAdapter entrateAdapter;
    private YearMonth meseAnnoSel;
    private  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //definisco le view del menu
    View navHeader;
    TextView navUsername, navEmail;

    //definisco le componenti grafiche dell'activity
    ImageButton ibAddEntrata, ibSearchEntrata, ibModCosti, ibNextMonth, ibPreviousMonth;
    EditText etSearchEntrata;
    RecyclerView rvEntrate;
    TextView tvMeseAnno;

    //componenti popUp mod entrata
    Dialog dialogModEntrata;
    Spinner sGiornoData, sMeseData, sAnnoData, sOraInizio, sOraFine;
    EditText etEntrate, etMancia, etKmPercorsi, etAltro;
    TextView tvOreTotali;
    Button bSalvaEntrata;
    private List<Integer>  listaGiorniMese, listaAnniPossibili;
    ArrayAdapter<Integer> spinnerGiornoAdapter, spinnerAnnoAdapter;
    ArrayAdapter<String> spinnerOrarioInizioAdapter, spinnerOrarioFineAdapter, spinnerMeseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_entrate);
        //setto la toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        //elimino il titolo dalla toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled (false);

        //setto il navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        //setto la navigation view
        navigationView=findViewById(R.id.navigation_view_pag_entrate);
        navigationView.setCheckedItem(R.id.nav_item_entrate);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navHeader = navigationView.getHeaderView(0);
        navUsername = navHeader.findViewById(R.id.tv_usernameNavMenu);
        navEmail = navHeader.findViewById(R.id.tv_navEmail);


        dbhelper=new DbHelper(PagEntrate.this);
        try{
            account = dbhelper.getActiveAccount();
            navUsername.setText(account.getUsername());
            navEmail.setText(account.getEmail());


        } catch (Exception e) {
            Toast.makeText(PagEntrate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally{
            meseAnnoSel = YearMonth.from(LocalDate.now());
            String mese = meseAnnoSel.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            int anno = meseAnnoSel.getYear();
            String stringMeseAnno = mese + "\t\t" + anno;

            ibAddEntrata=findViewById(R.id.b_aggiunginentrata);
            ibModCosti=findViewById(R.id.b_gestiscicosti);
            ibNextMonth=findViewById(R.id.b_prossimomese);
            ibPreviousMonth=findViewById(R.id.b_meseprecedente);
            ibSearchEntrata=findViewById(R.id.b_searcheentrata);
            etSearchEntrata=findViewById(R.id.et_searchentrate);
            tvMeseAnno=findViewById(R.id.tv_mesecorrente);
            rvEntrate=findViewById(R.id.rv_entrate);

            ibAddEntrata.setOnClickListener(addEntrata);
            ibSearchEntrata.setOnClickListener(searchEntrata);
            ibModCosti.setOnClickListener(modCosti);
            tvMeseAnno.setText(stringMeseAnno);

            try{

                listEntrate = dbhelper.getAllEntrateMese(meseAnnoSel, account.getUsername());
                entrateAdapter = new EntrateAdapter(PagEntrate.this, listEntrate);
                rvEntrate.setAdapter(entrateAdapter);
                rvEntrate.setLayoutManager(new LinearLayoutManager(this));
            }catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(PagEntrate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    View.OnClickListener addEntrata = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(account==null){
                Toast.makeText(PagEntrate.this, "Effettua il login o registrati per aggiungere un'entrata", Toast.LENGTH_SHORT).show();
            }
            else{
                dialogModEntrata = new Dialog(PagEntrate.this);
                dialogModEntrata.getWindow();
                dialogModEntrata.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogModEntrata.setContentView(R.layout.popup_add_entrata);
                dialogModEntrata.setCancelable(true);

                sGiornoData=dialogModEntrata.findViewById(R.id.s_giornoDataModEntrata);
                sMeseData=dialogModEntrata.findViewById(R.id.s_meseDataModEntrata);
                sAnnoData=dialogModEntrata.findViewById(R.id.s_annoDataModEntrata);
                sOraInizio=dialogModEntrata.findViewById(R.id.s_oraInizioModEntrata);
                sOraFine=dialogModEntrata.findViewById(R.id.s_oraFineModEntrata);
                etAltro=dialogModEntrata.findViewById(R.id.et_altroModEntrata);
                etEntrate=dialogModEntrata.findViewById(R.id.et_entrateModEntrata);
                etMancia=dialogModEntrata.findViewById(R.id.et_manciaModEntrata);
                etKmPercorsi=dialogModEntrata.findViewById(R.id.et_kmPercorsiModEntrata);
                tvOreTotali=dialogModEntrata.findViewById(R.id.tv_oreTotModEntrata);
                bSalvaEntrata=dialogModEntrata.findViewById(R.id.b_salvaModEntrata);

                //SETTTO GLI SPINNER CREO LE LISTE, GLI ADAPTER E LI COLLEGO
                listaAnniPossibili=dbhelper.getAnniPossibiliFromEntrate(account.getUsername());

                spinnerMeseAdapter = new ArrayAdapter<String>(PagEntrate.this, R.layout.spinner_item, getResources().getStringArray(R.array.numeroMesi) );
                spinnerAnnoAdapter = new ArrayAdapter<Integer>(PagEntrate.this, R.layout.spinner_item, listaAnniPossibili);
                spinnerOrarioInizioAdapter = new ArrayAdapter<String>(PagEntrate.this, R.layout.spinner_item, getResources().getStringArray(R.array.orari));
                spinnerOrarioFineAdapter = new ArrayAdapter<String>(PagEntrate.this, R.layout.spinner_item, getResources().getStringArray(R.array.orari));

                listaGiorniMese = new ArrayList<>();
                setListaGiorniPossibili(spinnerMeseAdapter.getPosition(String.valueOf(meseAnnoSel.getMonth().getValue())),
                        spinnerAnnoAdapter.getPosition(meseAnnoSel.getYear()));
                spinnerGiornoAdapter = new ArrayAdapter<>(PagEntrate.this, R.layout.spinner_item, listaGiorniMese);

                sOraFine.setAdapter(spinnerOrarioFineAdapter);
                sOraInizio.setAdapter(spinnerOrarioInizioAdapter);
                sAnnoData.setAdapter(spinnerAnnoAdapter);
                sMeseData.setAdapter(spinnerMeseAdapter);
                sGiornoData.setAdapter(spinnerGiornoAdapter);


                sMeseData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        setListaGiorniPossibili(Integer.parseInt(spinnerMeseAdapter.getItem(position)),
                                Integer.parseInt(String.valueOf(sAnnoData.getSelectedItem())));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sAnnoData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        setListaGiorniPossibili(Integer.parseInt(String.valueOf(sMeseData.getSelectedItem())),
                                Integer.parseInt(String.valueOf(spinnerAnnoAdapter.getItem(position))));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sOraInizio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                       setOreTotTextView(String.valueOf(spinnerOrarioInizioAdapter.getItem(position)), String.valueOf(sOraFine.getSelectedItem()));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sOraFine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        setOreTotTextView(String.valueOf(sOraInizio.getSelectedItem()), String.valueOf(spinnerOrarioFineAdapter.getItem(position)));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sGiornoData.setSelection(spinnerGiornoAdapter.getPosition(LocalDate.now().getDayOfMonth()));
                sMeseData.setSelection(spinnerMeseAdapter.getPosition(String.valueOf(LocalDate.now().getMonthValue())));
                sAnnoData.setSelection(spinnerAnnoAdapter.getPosition(LocalDate.now().getYear()));



                bSalvaEntrata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //facccio i vari controlli e salvo l'esito nella variabile check
                        boolean check = true;
                        if(!checkCampoObbligatorio(etEntrate)){
                            check=false;
                        }
                        if(!checkCampoObbligatorio(etMancia)){
                            check=false;
                        }
                        if(!checkCampoObbligatorio(etKmPercorsi)){
                            check=false;
                        }
                        if (check) {
                            //icontrolli sono andati a buon fine istanzio l'entrata e l'aggiongo al db

                            //leggo i dati
                            LocalDate data = LocalDate.of(Integer.parseInt(String.valueOf(sAnnoData.getSelectedItem())),
                                    Integer.parseInt(String.valueOf(sMeseData.getSelectedItem())),
                                    Integer.parseInt(String.valueOf(sGiornoData.getSelectedItem())));
                            LocalTime oraInizio = LocalTime.parse(String.valueOf(sOraInizio.getSelectedItem()), timeFormatter);
                            LocalTime oraFine = LocalTime.parse(String.valueOf(sOraFine.getSelectedItem()), timeFormatter);
                            LocalTime oreTot = oraFine.minusHours(oraInizio.getHour()).minusMinutes(oraInizio.getMinute());
                            double paga =Double.parseDouble(String.valueOf(etEntrate.getText()));
                            double mancia = Double.parseDouble(String.valueOf(etMancia.getText()));
                            double kmPercorsi = Double.parseDouble(String.valueOf(etKmPercorsi.getText()));
                            String note = String.valueOf(etAltro.getText());

                            //inizializzo entrata
                            Entrata entrata = new Entrata(data, oraInizio, oraFine, oreTot, paga, mancia, kmPercorsi, note);



                            try{
                                //controllo se è gia presente all'interno del db
                                if(dbhelper.checkPresenzaEntrata(entrata, account.getUsername()) == 1){
                                    //update
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PagEntrate.this);
                                    builder.setTitle("MODIFICA ENTRATA");
                                    builder.setMessage("Esiste già un'entrata inserita in quella data, sei sicuro di volerla modificare?");
                                    builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                Entrata oldEntrata = dbhelper.searchEntrata(UtilClass.localDateToUnixTime(entrata.getData()), account.getUsername());
                                                int index = searchIndexOfEntrataInList(oldEntrata);
                                                dialogModEntrata.dismiss();
                                                dbhelper.updateEntrata(oldEntrata, entrata, account.getUsername());
                                                updateSingleEntrateInList(index, entrata);
                                                Toast.makeText(PagEntrate.this, "Modifica avvenuta correttamente", Toast.LENGTH_SHORT).show();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                Toast.makeText(PagEntrate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }else{
                                    //insert
                                    try {
                                        dialogModEntrata.dismiss();
                                        dbhelper.addNewEntrata(entrata, account.getUsername());
                                        addSingleEntrataToList(entrata);
                                        Toast.makeText(PagEntrate.this, "Inserimento avvenuto correttamente", Toast.LENGTH_SHORT).show();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Toast.makeText(PagEntrate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(PagEntrate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialogModEntrata.show();
            }

        }
    };



    View.OnClickListener modCosti = new View.OnClickListener() {
        //componenti popUp mod costi
        Dialog dialogModCosti;
        TextView tvMese;
        EditText etCostoCarburante, etConsumoMedio;
        Button bSalvaCosti;
        @Override
        public void onClick(View v) {
            if(account==null){
                Toast.makeText(PagEntrate.this, "Effettua il login o registrati per aggiungere un'entrata", Toast.LENGTH_SHORT).show();
            }
            else{
                dialogModCosti = new Dialog(PagEntrate.this);
                dialogModCosti.getWindow();
                dialogModCosti.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogModCosti.setContentView(R.layout.popup_mod_costi);
                dialogModCosti.setCancelable(true);

                String mese = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
                int anno = LocalDate.now().getYear();
                String stringMeseAnno = mese + "\t\t" + anno;

                tvMese=dialogModCosti.findViewById(R.id.tv_meseModCostiConsumi);
                tvMese.setText(stringMeseAnno);

                etCostoCarburante=dialogModCosti.findViewById(R.id.et_costoCarburanteAlLitro);
                etConsumoMedio=dialogModCosti.findViewById(R.id.et_consumoMedioModCosti);
                bSalvaCosti=dialogModCosti.findViewById(R.id.b_salvaModCosti);

                bSalvaCosti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialogModCosti.show();

            }
        }
    };

    View.OnClickListener searchEntrata = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String searchedEntrata = String.valueOf(etSearchEntrata.getText());
            try {
                updateAllEntrateToList(dbhelper.searchEntrata(searchedEntrata, account.getUsername()));
            }catch(Exception e){
                e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(PagEntrate.this);
                builder.setTitle("ERRORE");
                builder.setMessage(e.getMessage());
                builder.show();
            }
        }
    };

    private void updateAllEntrateToList(Entrata searchEntrata) {
        if(!listEntrate.isEmpty()){
            listEntrate.clear();
        }
        listEntrate.add(0,searchEntrata);
        entrateAdapter.notifyDataSetChanged();
    }


    private void addSingleEntrataToList(Entrata entrata) {
        int i =0 ;
        Entrata e = listEntrate.get(i);
        while(UtilClass.localDateToUnixTime(e.getData())<UtilClass.localDateToUnixTime(entrata.getData())){
            i=i+1;
            e = listEntrate.get(i);
        }
        listEntrate.add(i, entrata);
        entrateAdapter.notifyItemInserted(i);
    }

    private void updateSingleEntrateInList(int index, Entrata newEntrata){
        listEntrate.set(index, newEntrata);
        entrateAdapter.notifyItemChanged(index);
    }

    private void setOreTotTextView(String oreInizio, String oreFine) {
        LocalTime oraInizio = LocalTime.parse(oreInizio, timeFormatter);
        LocalTime oraFine = LocalTime.parse(oreFine, timeFormatter);
        LocalTime oreTot = oraFine.minusHours(oraInizio.getHour()).minusMinutes(oraInizio.getMinute());
        String oreTotali = oreTot.getHour() + ":" + oreTot.getMinute();
        tvOreTotali.setText(oreTotali);
    }

    void setListaGiorniPossibili(int mese, int anno){
        boolean isLeap = Year.isLeap(anno);
        int lastDay = Month.of(mese).length(isLeap);
        listaGiorniMese.clear();
        for(int i = 1; i<=lastDay; i++){
            listaGiorniMese.add(i);
        }

    }

    public int searchIndexOfEntrataInList(Entrata entrata){
        for (Entrata e : listEntrate){
            if(UtilClass.localDateToUnixTime(e.getData()) > UtilClass.localDateToUnixTime(entrata.getData())){
                break;
            }else if(UtilClass.localDateToUnixTime(e.getData()) == UtilClass.localDateToUnixTime(entrata.getData())){
                return listEntrate.indexOf(e);
            }
        }
        return -1;
    }

    public boolean checkCampoObbligatorio(EditText editText){
        if( editText.getText().toString().length() != 0 ) {
            return true;
        }else{
            editText.setError("Campo obbligatorio");
            return false;
        }
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
                finish();
                Intent openMainActivity = new Intent(PagEntrate.this, HomePage.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
                break;

            }
            case R.id.nav_item_destinazioni: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagDestinazioni.class));
                break;
            }
            case R.id.nav_item_turni: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagModificaTurni.class));
                break;
            }
            case R.id.nav_item_entrate: {
                break;

            }
            case R.id.nav_item_menu: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagMenu.class));
                break;
            }
            case R.id.nav_item_calcola_tot: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagCalcoloTot.class));
                break;
            }
            case R.id.nav_item_profilo: {
                finish();
                startActivity(new Intent(PagEntrate.this, PagProfilo.class));
                break;
            }
            case R.id.nav_item_info: {
                finish();
                startActivity(new Intent(PagEntrate.this,  PagInfo.class));
                break;
            }
            case R.id.nav_item_logout:{
                UtilClass.logout(PagEntrate.this, account);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}