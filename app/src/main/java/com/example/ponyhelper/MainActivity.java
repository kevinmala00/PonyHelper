package com.example.ponyhelper;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.ponyhelper.datamanagment.DbManager;


public class MainActivity extends Activity {

    private DbManager dbmanager = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvmessconn=findViewById(R.id.tv_messconn);

        dbmanager=new DbManager(this);
        dbmanager.open();


        tvmessconn.setText(R.string.connessioneriuscita);





        /*bottone registrati che apre pagreg
        */
        Button bReg=findViewById(R.id.b_registrati);
        bReg.setOnClickListener(v -> {
            Intent openPagReg=new Intent(getApplicationContext(), PagReg.class);
            startActivity(openPagReg);
        });

        /*bottone checkLogin che apre paglogin
         */
        Button bLogin=findViewById(R.id.b_login);
        bLogin.setOnClickListener(v -> {
            Intent openPagLogin = new Intent(getApplicationContext(), PagLogin.class);
            startActivity(openPagLogin);
        });
    }
}
