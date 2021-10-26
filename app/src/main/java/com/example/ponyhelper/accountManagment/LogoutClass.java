package com.example.ponyhelper.accountManagment;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;


public class LogoutClass {
    /**
     * esegue il logout scollegando anche l'account
     * @param activity activity nella quale il metodo è stato chiamato
     * @param account accont di cui effettuare il logout
     */
    void logout(Activity activity, PonyAccount account){
        DbHelper dbhelper = new DbHelper(activity);
        if(account==null){
            Toast.makeText(activity, "Non esiste alcun account attivo. impossibile effettuare il logout", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("LOGOUT");
        builder.setMessage("Sei sicuro di voler effettuare il logout?");
        builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dbhelper.logout(account);
                } catch (Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                activity.finishAffinity();

                System.exit(0);
            }
        });
        builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
