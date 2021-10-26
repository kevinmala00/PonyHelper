package com.example.ponyhelper.accountManagment;

import android.app.Activity;

import com.example.ponyhelper.body.PonyAccount;
import com.example.ponyhelper.datamanagment.DbHelper;

public class ProfileDeleter {
    void deleteProfile(Activity activity, PonyAccount account) throws Exception {
        DbHelper dbHelper = new DbHelper(activity);
        dbHelper.deleteProfile(account.getUsername());
    }
}
