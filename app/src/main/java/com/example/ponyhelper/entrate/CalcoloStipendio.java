package com.example.ponyhelper.entrate;

import android.content.Context;

import com.example.ponyhelper.body.Costo;
import com.example.ponyhelper.body.Entrata;
import com.example.ponyhelper.datamanagment.DbHelper;
import com.example.ponyhelper.util.UtilClass;

import java.time.YearMonth;
import java.util.List;

public class CalcoloStipendio {

    private final DbHelper dbHelper;

    public CalcoloStipendio(Context context){
        dbHelper=new DbHelper(context);
    }


    public double calcolaStipendioNettoMensile(YearMonth meseAnno, String username){
            List<Entrata> list;
            double totMensile=0, kmMensili=0, spesaTot = 0, totNettoMensile = 0;
            Costo costiMensili;
            try {
                list = dbHelper.getAllEntrateMese(meseAnno, username);
                if(!list.isEmpty()){
                    for(Entrata e : list){
                        totMensile+=(e.getEntrate()+e.getMancia());
                        kmMensili+=e.getKm();
                    }
                    costiMensili = dbHelper.getCostiMensili(UtilClass.yearMonthToLocalLanguageString(meseAnno), username);
                    spesaTot = (kmMensili*costiMensili.getCostoCarburante()) / costiMensili.getConsumoMedio();
                    totNettoMensile = totMensile - spesaTot;
                }
                return totNettoMensile;
            } catch (Exception e) {
                e.printStackTrace();
                return totNettoMensile;
            }

    }
}
