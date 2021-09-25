package com.example.ponyhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

/**
 * activity introdutticva per la durata di 2 secondi prima dell'apertura della home page
 */
public class IntroPage extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(IntroPage.this, HomePage.class);
                finish();
                startActivity(intent);
            }
        }, 2000);   //2 seconds


    }
}
