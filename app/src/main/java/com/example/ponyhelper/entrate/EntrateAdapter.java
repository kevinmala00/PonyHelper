package com.example.ponyhelper.entrate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.R;
import com.example.ponyhelper.body.Entrata;
import com.example.ponyhelper.datamanagment.DbHelper;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EntrateAdapter extends RecyclerView.Adapter<EntrateAdapter.EntrateViewHolder> {
    Context mContext;
    private List<Entrata> mListEntrate;

    public EntrateAdapter(Context context, List<Entrata> listEntrate){
        mContext= context;
        mListEntrate = listEntrate;
    }

    @NonNull
    @Override
    public EntrateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View entrataView = inflater.inflate(R.layout.linea_entrate, parent, false);
        entrataView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("ELIMINARE ENTRATA?");
                builder.setMessage("Sei sicuro di voler eliminare l'entrata?");
                builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHelper dbHelper = new DbHelper(mContext);

                    }
                });
                builder.show();
                return true;
            }
        });

        // Return a new holder instance
        return new EntrateAdapter.EntrateViewHolder(entrataView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrateViewHolder holder, int position) {
        //prendo i dati dell'entrata in base alla posizine
        Entrata entrata = mListEntrate.get(position);

        //riempio i singoli elementi con l'entrata cossrispodente
        holder.tvData.setText(entrata.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        holder.tvKmPercorsi.setText(String.valueOf(entrata.getKm()));
        holder.tvMancia.setText(String.valueOf(entrata.getMancia()));
        holder.tvOreTot.setText(String.valueOf(entrata.getOreTot()));
        holder.bInfo.setOnClickListener(v -> {
            //aprire popup info dest
        });

    }

    @Override
    public int getItemCount() {
        return mListEntrate.size();
    }

    public static class EntrateViewHolder extends  RecyclerView.ViewHolder{
        TextView  tvData, tvOreTot, tvMancia, tvKmPercorsi;
        ImageButton bInfo;
        public EntrateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData=itemView.findViewById(R.id.tv_dataentrate);
            tvMancia=itemView.findViewById(R.id.tv_mancia);
            tvOreTot=itemView.findViewById(R.id.tv_oretot);
            tvKmPercorsi=itemView.findViewById(R.id.tv_kmpercorsi);
            bInfo=itemView.findViewById(R.id.b_infoentrate);
        }
    }
}
