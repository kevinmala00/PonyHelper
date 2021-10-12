package com.example.ponyhelper.destinationManagment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.R;
import com.example.ponyhelper.body.Destinazione;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DestinazioniAdapter extends RecyclerView.Adapter<DestinazioniAdapter.DestinazioniViewHolder>{
    private  Context mContext;
    private List<Destinazione> mlistDest;
    private  String mUsername;

    public DestinazioniAdapter(List<Destinazione> listDest, Context context, String username){
        mContext= context;
        mlistDest = listDest;
        mUsername = username;
    }


    @NonNull
    @Override
    public DestinazioniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View destView = inflater.inflate(R.layout.linea_consegne, parent, false);

        // Return a new holder instance
        return new DestinazioniViewHolder(destView);
    }



    @Override
    public void onBindViewHolder(@NonNull DestinazioniViewHolder holder, int position) {
        // prendo il singolo elemento in base alla sua posizione nella reciclerview
        Destinazione destinazione = mlistDest.get(position);
        //riempio i singoli elementi delle righe prelevando i dati dal turno
        TextView tvIndirizzoCompleto = holder.tvIndirizzoCompleto;
        String indirirzzo = destinazione.getIndirizzo().toStringFullAddress();
        tvIndirizzoCompleto.setText(indirirzzo);

        TextView tvDataOraUltimaMod= holder.tvDataOraUltimaMod;
        String dataOraMod = destinazione.getDataUltimaModifica().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\t\t" + destinazione.getOraUltimaModifica().format(DateTimeFormatter.ofPattern("HH : mm"));
        tvDataOraUltimaMod.setText(dataOraMod);

        TextView tvMancia= holder.tvMancia;
        String mancia= destinazione.getMancia() + "€";
        tvMancia.setText(mancia);

        TextView tvNote= holder.tvNote;
        tvNote.setText(destinazione.getNote());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("ELIMINARE DESTINAZIONE?");
                builder.setMessage("Sei sicuro di voler eliminare la destinazione: "+destinazione.getIndirizzo().toStringFullAddress()+" ?" );
                builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try{

                            DbHelper dbHelper = new DbHelper(mContext);
                            dbHelper.deleteDestinazione(destinazione, mUsername);
                            int index = holder.getAdapterPosition();
                            mlistDest.remove(index);
                            ((ViewManager)holder.itemView.getParent()).removeView(holder.itemView);
                            Toast.makeText(mContext, "Destinazione eliminata definitivamente", Toast.LENGTH_SHORT).show();

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });

        ImageButton bbOpenMaps = holder.bOpenMaps;
        bbOpenMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoLocation = "google.navigation:q="+destinazione.getLatitudine() +"," + destinazione.getLongitudine();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(geoLocation));
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogInfoDest = new Dialog(mContext);
                dialogInfoDest.getWindow();
                dialogInfoDest.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogInfoDest.setContentView(R.layout.popup_info_dest);
                dialogInfoDest.setCancelable(true);

                TextView tvIndirizzo, tvUltimaModifica, tvMancia, tvNote, tvLatitudine, tvLongitudine;
                ImageButton bModifica;

                tvIndirizzo=dialogInfoDest.findViewById(R.id.tv_indirizzoInfoDest);
                tvUltimaModifica=dialogInfoDest.findViewById(R.id.tv_dataOraUltimaModificaInfoDest);
                tvMancia=dialogInfoDest.findViewById(R.id.tv_manciaInfoDest);
                tvNote=dialogInfoDest.findViewById(R.id.tv_noteInfoDest);
                bModifica=dialogInfoDest.findViewById(R.id.b_modDestInfoDest);
                tvLatitudine=dialogInfoDest.findViewById(R.id.tv_latitudineInfoDest);
                tvLongitudine=dialogInfoDest.findViewById(R.id.tv_longitudineInfoDest);


                tvIndirizzo.setText(indirirzzo);
                tvUltimaModifica.setText(dataOraMod);
                tvMancia.setText(mancia);
                tvNote.setText(destinazione.getNote());
                tvLatitudine.setText(String.valueOf(destinazione.getLatitudine()));
                tvLongitudine.setText(String.valueOf(destinazione.getLongitudine()));


                bModifica.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogInfoDest.dismiss();
                        //Passo all'activity modifica
                        Intent openModDest = new Intent(mContext, PagModificaDestinazione.class);

                        openModDest.putExtra("aggiornamento", true);
                        openModDest.putExtra("via", destinazione.getIndirizzo().getVia());
                        openModDest.putExtra("civico", destinazione.getIndirizzo().getCivico());
                        openModDest.putExtra("citta", destinazione.getIndirizzo().getCitta());
                        openModDest.putExtra("cap", destinazione.getIndirizzo().getCap());
                        openModDest.putExtra("provincia", destinazione.getIndirizzo().getProvincia());

                        openModDest.putExtra("dataUltimaModifica", UtilClass.localDateToUnixTime(destinazione.getDataUltimaModifica()));
                        openModDest.putExtra("oraUltimaModifica", destinazione.getOraUltimaModifica().format(DateTimeFormatter.ofPattern("HH:mm")));
                        openModDest.putExtra("mancia", destinazione.getMancia());
                        openModDest.putExtra("latitudine", destinazione.getLatitudine());
                        openModDest.putExtra("longitudine", destinazione.getLongitudine());
                        openModDest.putExtra("note", destinazione.getNote());
                        mContext.startActivity(openModDest);
                    }
                });

                dialogInfoDest.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistDest.size();
    }

    public static class DestinazioniViewHolder extends RecyclerView.ViewHolder{
        public TextView tvIndirizzoCompleto, tvDataOraUltimaMod, tvMancia, tvNote;
        public ImageButton bOpenMaps;


        public DestinazioniViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndirizzoCompleto=itemView.findViewById(R.id.tv_indirizzocompleto);
            tvDataOraUltimaMod=itemView.findViewById(R.id.tv_dataultimamoddestinazione);
            tvMancia=itemView.findViewById(R.id.tv_manciadestinazione);
            tvNote=itemView.findViewById(R.id.tv_notedestinazione);
            bOpenMaps =itemView.findViewById(R.id.b_openMaps);
        }
    }
}
