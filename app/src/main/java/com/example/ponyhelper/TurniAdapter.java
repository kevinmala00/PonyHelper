package com.example.ponyhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ponyhelper.body.Turno;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class TurniAdapter extends RecyclerView.Adapter<TurniAdapter.ViewHolder> {

    private List<Turno> mlistTurni;

    public TurniAdapter(List<Turno> listTurni){
        mlistTurni = listTurni;
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
        String oraInizioTurno = turno.getOraInizio().format(DateTimeFormatter.ofPattern("HH:mm"));
        tvOraInizioTurno.setText(oraInizioTurno);

        TextView tvOraFineTurno = holder.tvOraFine;
        String oraFineTurno = turno.getOraFine().format(DateTimeFormatter.ofPattern("HH:mm"));
        tvOraFineTurno.setText(oraFineTurno);
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
