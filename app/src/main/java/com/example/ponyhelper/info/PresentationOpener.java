package com.example.ponyhelper.info;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.example.ponyhelper.util.PowerPointOpener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PresentationOpener {

    private Context mContext;
    private PowerPointOpener powerPointOpener;

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public PresentationOpener(Context context){
        mContext=context;
        powerPointOpener = new PowerPointOpener(mContext);
    }

    void open(){
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream inputStream = assetManager.open("PresentazionePonyHelper.pptx");
            String path = mContext.getFilesDir().getPath()+"/PresentazionePonyHelper.pptx";
            File presentazione = new File(path);
            if(presentazione.getTotalSpace()==0){
                copyInputStreamToFile(inputStream, presentazione);
            }

            if(presentazione.setReadOnly()){
                powerPointOpener.openPPT(path);
            }else{
                throw new Exception("Errori interni. Ci scusiamo. Riprovare");
            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }
}
