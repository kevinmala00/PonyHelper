package com.example.ponyhelper.datamanagment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    static SQLiteDatabase db;

    /**
     * costruttore della classe dbhelper
     * @param context   context nel momento della creazione.
     */
    public DbHelper(Context context){
      super(context,DataBaseString.DBNAME, null, 1);
    }

    /**
     * Metodo chiamato quando il database viene creato la prima volta. Esecuzione dello script.
     *
     * @param db il database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseString.CREATION_SCRIPT);
    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseString.UPGRADE_SCRIPT);
    }
}