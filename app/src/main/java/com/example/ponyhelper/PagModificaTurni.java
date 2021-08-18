package com.example.ponyhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class PagModificaTurni extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_modifica_turni);

        View lModificaTurno=findViewById(R.id.l_modificaturno);
        lModificaTurno.setClipToOutline(true);
    }
}