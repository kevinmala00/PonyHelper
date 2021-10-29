package com.example.ponyhelper.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class EmailSender {
    public void sendMail(Activity activity, String destinatario, String oggetto){



        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, destinatario);
        intent.putExtra(Intent.EXTRA_SUBJECT, oggetto);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }

    }
}
