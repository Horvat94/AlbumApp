package com.example.tadej.albumaplikacija;

/**
 * Created by Tadej on 21.3.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import java.text.Format;
import com.example.DataAll;
import android.view.LayoutInflater;
import com.example.Album;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


class AdapterAlbum extends RecyclerView.Adapter<AdapterAlbum.ViewHolder> {
    DataAll all;
    Activity ac;

    public AdapterAlbum(DataAll all, Activity ac) {
        this.all = all;
        this.ac = ac;
    }
    ///izgled View Holder
    public static class ViewHolder extends RecyclerView.ViewHolder {//okvir za seznam
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView letniCasLabel;

        public ConstraintLayout loutRow;
        public ImageView iv;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.txtViewRow1);
            txtFooter = (TextView) v.findViewById(R.id.txtViewRow2);
            iv = (ImageView) v.findViewById(R.id.imageViewRow);
            loutRow = (ConstraintLayout) v.findViewById(R.id.layoutRow);
            letniCasLabel = (TextView) v.findViewById(R.id.colLabelZac);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    private static void startDView(String albumID, Activity ac) {//zazna zagon albuma iz seznama activityZacetna
        Intent i = new Intent(ac.getBaseContext(), ActivityAlbum.class);//zažene activity album
        i.putExtra(DataAll.ALBUM_ID, albumID);
        ac.startActivity(i);
    }

    private static void startEditView(String albumID, Activity ac) {//zazna zagon urejanje albuma iz seznama activityZacetna
        Intent i = new Intent(ac.getBaseContext(), ActivityMain.class);//zažene activity album
        i.putExtra(DataAll.ALBUM_ID, albumID);
        ac.startActivity(i);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // definiranje spremenljivk za vmesnik

        final Album trenutni = all.getAlbumLok(position);
        Date date = new Date(trenutni.getDate());//long date format
        SimpleDateFormat ft = new SimpleDateFormat ("dd.MM");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String datAlbum = format.format(date);//dobim string v tej obliki dd.MM.yyyy HH:mm:ss



        final String naslov = trenutni.getNaslov();
        final String idUsr = trenutni.getIdUser();
        holder.txtHeader.setText(naslov + ": " + datAlbum);//izpis glave naslov albuma trenutni.getDate()
        holder.txtFooter.setText(trenutni.getIdUser());//izpis noge id uporabnika
        //holder.iv.setImageDrawable(Drawable.createFromPath(trenutni.getSeznamSlik().get(0).getPathFile()));//izris slikice
       // holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.common_google_signin_btn_icon_dark));//izris slikice
        if(!trenutni.getSeznamSlik().isEmpty())
            Picasso
                    .with(this.ac.getApplicationContext())
                    .load("file://" + trenutni.getSeznamSlik().get(0).getPathFile())
                    .centerCrop() // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .resize(80, 80)
                    .into(holder.iv);

///////////////////////////////////////////////////////////Barvanje headerov in footerjev
        if (position%2==1) {
            holder.txtHeader.setTextColor(Color.DKGRAY);
            holder.txtFooter.setTextColor(Color.DKGRAY);

        } else {
            holder.txtHeader.setTextColor(Color.BLUE); //VAJE 7
            holder.txtFooter.setTextColor(Color.BLUE);
        }

        /*holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//ko tapnemo na header
                AdapterAlbum.startDView(trenutni.getIdAlbum(), ac);
            }
        });

        holder.txtFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//ko tapnemo na footer
                AdapterAlbum.startDView(trenutni.getIdAlbum(), ac);
            }
        });*/

        try {
            Date letniCas = format.parse(datAlbum);//pretvorba iz string v long Date
            String letCasStr = ft.format(letniCas);//pretvorba iz long v string
            letniCas = ft.parse(letCasStr);//pretvorba iz string v long Date
            int cas = getLetniCas(letniCas);//vrne kateri letni cas je
            if(cas == 1){
                holder.letniCasLabel.setBackgroundColor(Color.rgb(251,92,17));
            }
            else if(cas == 2){
                holder.letniCasLabel.setBackgroundColor(Color.rgb(48,134,222));
            }
            else if(cas == 3){
                holder.letniCasLabel.setBackgroundColor(Color.rgb(205,39,251));
            }
            else if(cas == 4){
                holder.letniCasLabel.setBackgroundColor(Color.rgb(222,201,48));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.loutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//ko tapnemo na footer
                AdapterAlbum.startDView(trenutni.getIdAlbum(), ac);
            }
        });
        holder.loutRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AdapterAlbum.startEditView(trenutni.getIdAlbum(), ac);
            return false;
            }
        });


    }

    int getLetniCas(Date testDate) {
        SimpleDateFormat ft = new SimpleDateFormat ("dd.MM");
        Date jesen;
        Date zima;
        Date pomlad;
        Date poletje;

        try {
            jesen = ft.parse("23.09");
            zima = ft.parse("21.12");
            pomlad = ft.parse("21.03");
            poletje = ft.parse("21.06");
            if (!(testDate.before(jesen) || testDate.after(zima)))
                return 1;//jesen
            else if (!(testDate.before(zima) || testDate.after(pomlad)))
                return 2;//zima
            else if (!(testDate.before(pomlad) || testDate.after(poletje)))
                return 3;//pomlad
            else if (!(testDate.before(poletje) || testDate.after(jesen)))
                return 4;//poletje

        }catch (ParseException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return all.getAlbumSize();
    }




}
