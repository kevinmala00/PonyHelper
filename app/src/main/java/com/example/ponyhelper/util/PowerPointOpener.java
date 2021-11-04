package com.example.ponyhelper.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.ponyhelper.info.PagInfo;

import java.io.File;


/**
 * classe che permette l'apertura di file power point mediane intent android.
 * @author kevin
 */
public class PowerPointOpener {
    private Context mContext;

    public PowerPointOpener(Context context){
        mContext= context;
    }


    /**
     * metodo che permetta l'apertura di un file ppt il cui path specificato dal parametro passato.
     * Si utilizzano intent android. nelle versioni più recenti di android si preleva l'uri del file attraverso il file provider per non esposrre il path
     * in caso non vi sia alcuna applicazione che supparta l'apertura di file power piitn installata sul dispositivo genera un toast message
     * @param path
     */
    public void openPPT(final String path) {
        File file = new File(path);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Nessuna applicazione trovata per l'apertura di file PowerPoint", Toast.LENGTH_SHORT).show();
        }
    }
}
