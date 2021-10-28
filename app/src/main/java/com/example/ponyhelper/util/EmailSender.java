package com.example.ponyhelper.util;

import android.app.Activity;
import android.content.Intent;

public class EmailSender {
    public void sendMail(Activity activity, String to, String message, String subject){



        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);

        //need this to prompts email client only
        email.setType("message/rfc822");

        activity.startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
