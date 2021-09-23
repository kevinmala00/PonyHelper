package com.example.ponyhelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.body.Destinazione;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DestinazioniAdapter extends RecyclerView.Adapter<DestinazioniAdapter.ViewHolder> {
    private  Context mContext;
    private List<Destinazione> mlistDest;

    public DestinazioniAdapter(List<Destinazione> listDest, Context context){
        mContext= context;
       mlistDest = listDest;
    }


    @NonNull
    @Override
    public DestinazioniAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View destView = inflater.inflate(R.layout.linea_consegne, parent, false);

        // Return a new holder instance
        return new ViewHolder(destView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
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

        ImageButton bPosition = holder.bPosition;
        bPosition.setOnClickListener(new View.OnClickListener() {
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


    }

    @Override
    public int getItemCount() {
        return mlistDest.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIndirizzoCompleto;
        public TextView tvDataOraUltimaMod;
        public TextView tvMancia;
        public TextView tvNote;
        public ImageButton bPosition;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndirizzoCompleto=itemView.findViewById(R.id.tv_indirizzocompleto);
            tvDataOraUltimaMod=itemView.findViewById(R.id.tv_dataultimamoddestinazione);
            tvMancia=itemView.findViewById(R.id.tv_manciadestinazione);
            tvNote=itemView.findViewById(R.id.tv_notedestinazione);
            bPosition=itemView.findViewById(R.id.b_openMaps);


        }
    }
}
