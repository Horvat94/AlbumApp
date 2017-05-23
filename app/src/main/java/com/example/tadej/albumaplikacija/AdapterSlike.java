package com.example.tadej.albumaplikacija;

/**
 * Created by Tadej on 21.3.2017.
 */

import android.app.Activity;
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
import com.example.Slika;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


class AdapterSlike extends RecyclerView.Adapter<AdapterSlike.ViewHolder> {
    Album al;
    Activity act;

    public AdapterSlike(Album al, Activity a) {
        this.al = al;
        this.act = a;
    }
    ///izgled View Holder
    public static class ViewHolder extends RecyclerView.ViewHolder {//okvir za seznam
        public TextView tvNaslov;
        public TextView tvLatitude;
        public TextView tvLongitude;
        public TextView tvDatum;
        public ImageView imageViw;
        public ConstraintLayout layoutMainSlika;

        public ViewHolder(View v) {
            super(v);
            tvNaslov = (TextView) v.findViewById(R.id.tvMSNaslov);
            tvLatitude = (TextView) v.findViewById(R.id.tvMSLat);
            tvLongitude = (TextView) v.findViewById(R.id.tvMSLet);
            tvDatum = (TextView) v.findViewById(R.id.tvMSDatum);
            imageViw = (ImageView) v.findViewById(R.id.imageVMS);
            layoutMainSlika = (ConstraintLayout) v.findViewById((R.id.layoutMainSlika));
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity__main__slike, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    private static void editSView(String slikaID, String albumID, Activity ac) {//zazna zagon albuma iz seznama activityZacetna
        Intent i = new Intent(ac.getBaseContext(), Activity_EditSlika.class);//zažene activity album
        i.putExtra(DataAll.ALBUM_ID, albumID);
        i.putExtra(Album.slika_ID, slikaID);
        ac.startActivity(i);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // definiranje spremenljivk za vmesnik

        final Slika trenutna = al.getSlikaLok(position);
        //Date date = new Date(trenutni.getDate());
      //  Format format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
      //  format.format(date);
        final String naslov = trenutna.getImeSlike();
        final String lat = String.valueOf(trenutna.getSirinaZeml());
        final String lon = String.valueOf(trenutna.getVisinaZeml());
        final String dat = trenutna.getDatum();
        holder.tvNaslov.setText(naslov);//izpis glave naslov albuma trenutni.getDate()
        holder.tvLatitude.setText(lat);
        holder.tvLongitude.setText(lon);
        holder.tvDatum.setText(dat);

///////////////////////////////////////////////////////////Barvanje headerov in footerjev
        if(!al.getSeznamSlik().isEmpty())
        Picasso.with(holder.imageViw.getContext())//izris slike v seznamu
                .load("file://"+trenutna.getPathFile())
                .resize(80, 80)
                .centerCrop()
                .into(holder.imageViw);
        if (position%2==1) {
            holder.tvNaslov.setTextColor(Color.BLUE);
            holder.tvLatitude.setTextColor(Color.BLUE);
            holder.tvLongitude.setTextColor(Color.BLUE);
            holder.tvDatum.setTextColor(Color.BLUE);
        } else {
            holder.tvNaslov.setTextColor(Color.BLACK);
            holder.tvLatitude.setTextColor(Color.BLACK);
            holder.tvLongitude.setTextColor(Color.BLACK);
            holder.tvDatum.setTextColor(Color.BLACK);
        }

        holder.layoutMainSlika.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AdapterSlike.editSView(trenutna.getIdSlike(),al.getIdAlbum(), act);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.getSlikeSize();
    }


}
