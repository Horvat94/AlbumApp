package com.example;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;



/**
 * Created by Tadej on 27.2.2017.
 */

public class Album {
    public static final String slika_ID = "slika-idXX";
    String idAlbum; //id albuma kateri je unikaten
    String naslov;  // naslov albuma
    ArrayList<Slika> seznamSlik;
    String idUser;      // id uporabnika kateremu pripada album
    public static SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    long date;


    public Album(String naslov, String idUser) {
        this.idAlbum = UUID.randomUUID().toString().replaceAll("-", "");
        this.naslov = naslov;
        this.idUser = idUser;
        seznamSlik = new ArrayList<Slika>();
        this.date = Calendar.getInstance().getTimeInMillis();
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String nasl) {
        naslov = nasl;
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUsr) {
        idUser = idUsr;}

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public void dodajSliko(String naslov, Double sZem, Double vZem, String datum, String pathF){
        Slika tmp = new Slika(naslov,sZem,vZem,datum,pathF,this.idUser);
        seznamSlik.add(tmp);
    }
    public Slika getSlikaLok(int lok){
        return seznamSlik.get(lok);
    }

    public Slika getSlikaByID(String ID) {
        for (Slika sl : seznamSlik)
            if (sl.getIdSlike().equals(ID))
                return sl;
        return null;
    }

    public int getSlikeSize(){
        return seznamSlik.size();
    }

    public ArrayList<Slika> getSeznamSlik() {
        return seznamSlik;
    }

    public void setSeznamSlik(ArrayList<Slika> seznamSlik) {
        this.seznamSlik = seznamSlik;
    }

    public void izpis(){
        for(Slika g : seznamSlik)
            System.out.println("slika:\n"+g.toString());
}

    public long getDate() {
        return date;
    }

    public void setDate(long dt) {
        date = dt;
    }

    @Override
    public String toString() {
        return "Album{" +
                "idAlbum='" + idAlbum + '\'' +
                ", naslov='" + naslov + '\'' +
                ", idUser='" + idUser + '\'' +
                ", seznamSlik=" + seznamSlik+
                '}';
    }

}
