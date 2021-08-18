package com.example.ponyhelper;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;

import java.util.List;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper dbhelper;

        try{
            dbhelper = new DbHelper(MainActivity.this);
            List<PonyAccount> resultList = dbhelper.selectAllAccount();
            Toast.makeText(MainActivity.this, resultList.toString(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        /*bottone registrati che apre pagreg
        */
        Button bReg=findViewById(R.id.b_registrati);
        bReg.setOnClickListener(v -> {
            try {
                Intent openPagReg = new Intent(MainActivity.this, PagReg.class);
                startActivity(openPagReg);
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*bottone checkLogin che apre paglogin
         */
        Button bLogin=findViewById(R.id.b_login);
        bLogin.setOnClickListener(v -> {
            Intent openPagLogin = new Intent(MainActivity.this, PagLogin.class);
            startActivity(openPagLogin);
        });
    }
}
