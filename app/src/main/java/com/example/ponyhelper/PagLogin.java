package com.example.ponyhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class PagLogin extends Activity {
    PonyAccount  account;
    DbHelper dbhelper;
    String username;
    String passcod;
    TextInputLayout itUsername;
    TextInputLayout itPasscod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_login);
        dbhelper = new DbHelper(PagLogin.this);

        //inizializzo i text imput con il quale l'utenet interagisce
        itUsername = findViewById(R.id.it_usernamepaglog);
        itPasscod = findViewById(R.id.it_passcodpaglog);

        //text view contente messaggio cambio codice con relaticvo OnClickListener
        TextView tvMessNewCode = findViewById(R.id.tv_messnewcodelogin);
        tvMessNewCode.setOnClickListener(updateAccessCode);

        //setto l'onClickListenere in modo che cliccando blogin si sttivi login
        Button bLogin = findViewById(R.id.b_loginpaglogin);
        bLogin.setOnClickListener(login);
    }

    //OnClickListener che permette il login
    View.OnClickListener login= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {

                username = Objects.requireNonNull(itUsername.getEditText()).getText().toString();
                passcod = Objects.requireNonNull(itPasscod.getEditText()).getText().toString();

                account = dbhelper.checkLogin(username, passcod);
                Bundle accountBundle = UtilClass.salvataggioAccount(account);
                Intent openHomePag = new Intent(PagLogin.this, HomePage.class);
                openHomePag.putExtra("account", accountBundle);
                startActivity(openHomePag);
            } catch (Exception e) {
                Toast.makeText(PagLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    //OnClickListener per settare un nuovo codice di accesso
    View.OnClickListener updateAccessCode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                dbhelper.updateAccessCode(username);
                Toast.makeText(PagLogin.this, "Nuovo codice inviato alla mail", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(PagLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

}



