package com.example.ponyhelper.destinationManagment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.NavigationActivity;
import com.example.ponyhelper.R;
import com.example.ponyhelper.body.Destinazione;
import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;

import java.util.List;


public class PagDestinazioni extends NavigationActivity {
    private PonyAccount account;
    private DbHelper dbhelper;
    private Destinazione mDestinazione;
    private List<Destinazione> listDestinazione;
    private DestinazioniAdapter destinazioniAdapter;

    private EditText etSearchDestinazione;
    private RecyclerView rvDestinazioni;

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;


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
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_item_destinazioni);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //ISTANZIO LE VARIE VIEW
        View navHeader = navigationView.getHeaderView(0);
        //riferimenti agli elementi grafici dell'activity
        TextView tvNavUsername = navHeader.findViewById(R.id.tv_usernameNavMenu);
        TextView tvNavEmail = navHeader.findViewById(R.id.tv_navEmail);
        ImageButton bAddDestinazione = findViewById(R.id.b_aggiunginDestinazione);
        etSearchDestinazione = findViewById(R.id.et_searchDestinazioni);
        ImageButton bSearchDestinazione = findViewById(R.id.b_searcheDestinazione);
        rvDestinazioni = findViewById(R.id.rv_destinazioni);


        dbhelper = new DbHelper(PagDestinazioni.this);
        try {
            //Recupero i dati dell'account attivo
            account = dbhelper.getActiveAccount();

            //setto il testo nel nav header
            tvNavUsername.setText(account.getUsername());
            tvNavEmail.setText(account.getEmail());

            bAddDestinazione.setOnClickListener(addDestinazione);
            bSearchDestinazione.setOnClickListener(searchDestinazione);

            try {
                //otenngo la lista di destinazione dal database
                listDestinazione = dbhelper.getAllDestinazioni(account);

                //popolo la recyclerview
                destinazioniAdapter = new DestinazioniAdapter(listDestinazione, PagDestinazioni.this, account.getUsername());
                rvDestinazioni.setAdapter(destinazioniAdapter);
                rvDestinazioni.setLayoutManager(new LinearLayoutManager(this));


                etSearchDestinazione.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            findDestination();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            } catch (Exception e) {
                Toast.makeText(PagDestinazioni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        } catch (Exception e) {
            Toast.makeText(PagDestinazioni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //otenngo la lista di destinazione dal database
            listDestinazione = dbhelper.getAllDestinazioni(account);

            //popolo la recyclerview
            destinazioniAdapter = new DestinazioniAdapter(listDestinazione, PagDestinazioni.this, account.getUsername());
            rvDestinazioni.setAdapter(destinazioniAdapter);
            rvDestinazioni.setLayoutManager(new LinearLayoutManager(this));
        }catch (Exception e){
            Toast.makeText(PagDestinazioni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    View.OnClickListener searchDestinazione = v -> {
        try {
            findDestination();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PagDestinazioni.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener addDestinazione = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (account == null) {
                Toast.makeText(PagDestinazioni.this, "Effettua il login o registrati per aggiungere un'entrata", Toast.LENGTH_SHORT).show();
            } else {
                //passa a pagmoddest
                Intent openPagModDest = new Intent(PagDestinazioni.this, PagModificaDestinazione.class);
                openPagModDest.putExtra("aggiornamento", false);
                startActivity(openPagModDest);
            }
        }
    };


    /**
     * metodo per ricercare una destinazione all'interno del database
     * @throws Exception derivanti dal metodo searchDestinazione
     */
    private void findDestination() throws Exception {
        String indirizzo = String.valueOf(etSearchDestinazione.getText());
        if (indirizzo.length() != 0) {

            List<Destinazione> searchListDestinazione = dbhelper.searchDestinazione(account, indirizzo);
            updateDestList(searchListDestinazione);

        } else {
            List<Destinazione> searchListDestinazione = dbhelper.getAllDestinazioni(account);
            updateDestList(searchListDestinazione);
        }
    }


    /**
     * metodo per rimuovere un singolo elelmento dalla lista e informare l'adapter della recycler view in modod che ricarichi la recyclerview
     * @param removeIndex indice dell'elemento da rimuovere
     */
    private void removeSingleDestToList(int removeIndex) {
        listDestinazione.remove(removeIndex);
        destinazioniAdapter.notifyItemRemoved(removeIndex);
    }

    /**
     * metodo per aggiungere un unico elemento alla lista e informare l'adapter della recycler view in modod che ricarichi la recyclerview, aggiunge tale elemento in cima alla lista in posizione '0'
     * @param newDest nuova destinazione
     * @see Destinazione
     */
    private void addSingleDestToList(Destinazione newDest) {
        listDestinazione.add(0, newDest);
        destinazioniAdapter.notifyItemInserted(0);
    }

    /**
     * metodo per aggiornare un singolo elemento della lista e informare l'adapter della recycler view in modod che ricarichi la recyclerview
     * @param updateIndex indice dell'elemento
     * @param newDest nuovo elemento
     */
    private void updateSingleDestToList(int updateIndex, Destinazione newDest) {
        listDestinazione.set(updateIndex, newDest);
        destinazioniAdapter.notifyItemChanged(updateIndex);
    }

    /**
     * metodo per aggiornare la lista contenente i dati della recyclerview, e e informare l'adapter della recycler view in modod che ricarichi la recyclerview
     * @param newListDestinazione nuova lista contenente i nuovi dati
     */
    public void updateDestList(List<Destinazione> newListDestinazione) {
        if(!listDestinazione.isEmpty()){listDestinazione.clear();}
        listDestinazione.addAll(newListDestinazione);
        destinazioniAdapter.notifyDataSetChanged();
    }
}