package com.example.ponyhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.body.Turno;
import com.example.ponyhelper.datamanagment.DbHelper;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class TurniAdapter extends RecyclerView.Adapter<TurniAdapter.ViewHolder> {

    private List<Turno> mlistTurni;
    private Context mContext;
    private String mUsername;
    private DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");

    public TurniAdapter(List<Turno> listTurni, Context context, String username){
        mlistTurni = listTurni;
        mContext=context;
        mUsername=username;
    }

    @NonNull
    @Override
    public TurniAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View turniView = inflater.inflate(R.layout.linea_turni, parent, false);

        // Return a new holder instance
        return new ViewHolder(turniView);
    }

    @Override
    public void onBindViewHolder(TurniAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Turno turno = mlistTurni.get(position);

        //riempio i singoli elementi delle righe prelevando i dati dal turno
        TextView tvGiorno = holder.tvGiorno;
        String Giorno = turno.getData().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        tvGiorno.setText(Giorno);

        TextView tvOraInizioTurno = holder.tvOraInizio;
        String oraInizioTurno = turno.getOraInizio().format(timeFormatter);
        tvOraInizioTurno.setText(oraInizioTurno);

        TextView tvOraFineTurno = holder.tvOraFine;
        String oraFineTurno = turno.getOraFine().format(timeFormatter);
        tvOraFineTurno.setText(oraFineTurno);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("ELIMINARE TURNO?");
                builder.setMessage("Sei sicuro di voler eliminare il turno di: "+Giorno+" ?" );
                builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHelper dbHelper = new DbHelper(mContext);
                        try{
                            dbHelper.deleteTurno(turno, mUsername);
                            int index = holder.getAdapterPosition();
                            mlistTurni.remove(index);
                            ((ViewManager)holder.itemView.getParent()).removeView(holder.itemView);


                            Toast.makeText(mContext, "Turno eliminato definitivamente", Toast.LENGTH_SHORT).show();

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
    }


    // ritorna il numero totali di item
    @Override
    public int getItemCount() {
        return mlistTurni.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvGiorno;
        public TextView tvOraInizio;
        public TextView tvOraFine;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGiorno=itemView.findViewById(R.id.tv_GiornoSettimana);
            tvOraInizio=itemView.findViewById(R.id.tv_OraInizioTurno);
            tvOraFine=itemView.findViewById(R.id.tv_OraFineTurno);
        }
    }
}
