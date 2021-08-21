package com.example.ponyhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PagReg extends Activity {
    PonyAccount account=new PonyAccount();

    //variabile che rappresenta l'esito dei controlli
    boolean check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_reg);

        TextInputLayout itUsername=findViewById(R.id.it_usernamepagreg);
        TextInputLayout itNome=findViewById(R.id.it_nomepagreg);
        TextInputLayout itCognome=findViewById(R.id.it_cognomepagreg);
        TextInputLayout itEmail=findViewById(R.id.it_emailpagreg);
        TextInputLayout itPassword = findViewById(R.id.it_passwordpagreg);
        TextInputLayout itConfermaPass=findViewById(R.id.it_confermapassword);




        //setto gli onTextChanger degli edit text presenti
        UtilClass.eliminaErroreCampoObbligatorio(itUsername);
        UtilClass.eliminaErroreCampoObbligatorio(itNome);
        UtilClass.eliminaErroreCampoObbligatorio(itCognome);
        UtilClass.eliminaErroreCampoObbligatorio(itPassword);
        UtilClass.eliminaErroreCampoObbligatorio(itEmail);
        UtilClass.eliminaErroreCampoObbligatorio(itConfermaPass);

        //inserimento terminato controllo i parametri
        Button breg=findViewById(R.id.b_registratipagreg);
        breg.setOnClickListener(v -> {

            //salvo i dati inseriti dall'utente nell'account
            account.setUsername(Objects.requireNonNull(itUsername.getEditText()).getText().toString());
            account.setNome(Objects.requireNonNull(itNome.getEditText()).getText().toString());
            account.setCognome(Objects.requireNonNull(itCognome.getEditText()).getText().toString());
            account.setEmail(Objects.requireNonNull(itEmail.getEditText()).getText().toString());

            String Password = Objects.requireNonNull(itPassword.getEditText()).getText().toString();
            String ConfermaPassword= Objects.requireNonNull(itConfermaPass.getEditText()).getText().toString();

            DbHelper dbhelper = new DbHelper(PagReg.this);
            try {
                //controllo se i dati sono già in uso nel database
                dbhelper.checkRegistrazione(account.getUsername(), account.getEmail());

                //controllo i campi obbligatori
                check = UtilClass.checkCampoObbligatorio(itUsername);
                check = UtilClass.checkCampoObbligatorio(itNome);
                check = UtilClass.checkCampoObbligatorio(itCognome);
                check = UtilClass.checkCampoObbligatorio(itPassword);
                check = UtilClass.checkCampoObbligatorio(itEmail);
                check = UtilClass.checkCampoObbligatorio(itConfermaPass);

                //controllo che le password siano uguali
                if(!(ConfermaPassword.equals(Password))) {
                    check=false;
                    itPassword.setError("Password differenti");
                    itConfermaPass.setError("Password differenti");
                }


                //se il controllo è OK passo alla prossima activity, portando i dati dell'account
                if(check) {
                    Intent openHomePage = new Intent(PagReg.this, HomePage.class);
                    Bundle accountBundle = UtilClass.salvataggioAccount(account);

                    dbhelper.registrazioneAccount(account, Password, PagReg.this);

                    openHomePage.putExtra("account", accountBundle);
                    startActivity(openHomePage);
                }
            } catch (Exception e) {
                check=false;
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
                Log.e("errore", e.getMessage());
            }


        });
    }
}

