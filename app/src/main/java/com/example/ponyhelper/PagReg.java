package com.example.ponyhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbManager;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PagReg extends Activity {
    DbManager db=new DbManager(getApplicationContext());
    PonyAccount account=new PonyAccount();

    //variabile che rappresenta l'esito dei controlli
    boolean check;

    TextInputLayout itUsername=findViewById(R.id.it_usernamepagreg);
    TextInputLayout itNome=findViewById(R.id.it_nomepagreg);
    TextInputLayout itCognome=findViewById(R.id.it_cognomepagreg);
    TextInputLayout itEmail=findViewById(R.id.it_emailpagreg);
    TextInputLayout itPassword = findViewById(R.id.it_passwordpagreg);
    TextInputLayout itConfermaPass=findViewById(R.id.it_confermapassword);
    TextInputLayout itDataNascita=findViewById(R.id.it_datanscitareg);
    TextView tvMess=findViewById(R.id.tv_messreg);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_reg);



        account.setUsername(Objects.requireNonNull(itUsername.getEditText()).getText().toString());
        account.setNome(Objects.requireNonNull(itNome.getEditText()).getText().toString());
        account.setCognome(Objects.requireNonNull(itCognome.getEditText()).getText().toString());
        account.setEmail(Objects.requireNonNull(itEmail.getEditText()).getText().toString());
        account.setPassword(Objects.requireNonNull(itPassword.getEditText()).getText().toString());
        account.setDataNascita(Objects.requireNonNull(itDataNascita.getEditText()).getText().toString());

        String ConfermaPassword= Objects.requireNonNull(itConfermaPass.getEditText()).getText().toString();

        //setto gli onTextChanger degli edit text presenti
        eliminaErrore(itUsername);
        eliminaErrore(itNome);
        eliminaErrore(itCognome);
        eliminaErrore(itDataNascita);
        eliminaErrore(itPassword);
        eliminaErrore(itEmail);
        eliminaErrore(itConfermaPass);


        Button breg=findViewById(R.id.b_registratipagreg);
        breg.setOnClickListener(v -> {

            try {
                db.checkRegistrazione(account.getUsername(), account.getEmail());
            } catch (Exception e) {
                check=false;
                tvMess.setText(e.getMessage());
            }

            check = UtilClass.checkCampoObbligatorio(itUsername);
            check = UtilClass.checkCampoObbligatorio(itNome);
            check = UtilClass.checkCampoObbligatorio(itCognome);
            check = UtilClass.checkCampoObbligatorio(itDataNascita);
            check = UtilClass.checkCampoObbligatorio(itPassword);
            check = UtilClass.checkCampoObbligatorio(itEmail);
            check = UtilClass.checkCampoObbligatorio(itConfermaPass);

            //controllo che le password siano uguali
            if(!(ConfermaPassword.equals(account.getPassword()))) {
                check=false;
                itPassword.setError("Password differenti");
                itConfermaPass.setError("Password differenti");
            }


            //se il controllo è OK passo alla prossima activity
            if(check) {
                Intent openHomePage = new Intent(getApplicationContext(), HomePage.class);
                Bundle accountBundle = UtilClass.salvataggioAccount(account);

                openHomePage.putExtra("account", accountBundle);
                startActivity(openHomePage);
            }
        });
    }



    /**
     * setta il textchanger del edittext passato in modo che appena vnega inserito il testo
     * venga rimosso qualsiasi errore riscontrato in precedenze
     * @param textInput textinput di cui settare il texchanger del proprio edittext
     */
    public void eliminaErrore(TextInputLayout textInput){
        EditText editInput = textInput.getEditText();
        Objects.requireNonNull(editInput).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

