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

public class PowerPointOpener {
    private Context mContext;

    public PowerPointOpener(Context context){
        mContext= context;
    }

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

        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Applicazione non trovata", Toast.LENGTH_SHORT).show();
        }
    }
}
