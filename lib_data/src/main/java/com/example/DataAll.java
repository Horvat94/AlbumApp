package com.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Tadej on 26.2.2017.
 */

public class DataAll {
    public static final String ALBUM_ID = "album-idXX";
    public static SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy");
    private Album album;
    private User uporabnik;
    private ArrayList<Album> albumList;

    public DataAll() {
        this.uporabnik = new User("neznan.nedolocen@predal.domena","vzdevek");
        this.album = new Album("ni definiran",getUporabnik().getIdUser());
        //album = new Album("ni definiran","Horvat.Tadej5@gmail.com");
        albumList = new ArrayList<>();
    }

    public Album dodajAlbum(String naslov){
        Album tmp = new Album(naslov,getUporabnik().getIdUser());
        getAlbumList().add(tmp);
        return tmp;
    }


    public int getAlbumSize() {
        return albumList.size();
    }

    public List<Album> getAlbumAll() {
        return albumList;
    }


    public Album getAlbumByID(String ID) {
        for (Album al : albumList)
            if (al.getIdAlbum().equals(ID))
                return al;
        return null;
    }



    public Album getAlbum() {
        return album;
    }
    public Album getAlbumLok( int lok) {
        return albumList.get(lok);
    }

    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public void dodajAlbumList(Album a){
        albumList.add(a);
    }

    public void setAlbum(Album album) {
        this.album = album;
    }


    public User getUporabnik() {
        return uporabnik;
    }
    public void setUporabnik(User uporabnik) {
        this.uporabnik = uporabnik;
    }


    @Override
    public String toString() {
        return "DataAll{" +
                "uporabnik=" + uporabnik +
               // ", seznamStrani=" + seznamStrani.toString() +
                //", seznamSlik=" + seznamSlik.toString() +
                '}';
    }
  /*  public void izpis(){
        for(ArrayList<Slika> s: seznamStrani){
            for(Slika g : s)
                System.out.println("slika:\n"+g.toString());
        }
    }
*/
  public Album getAlbum(int i){
      return albumList.get(i);
  }

    public Album getNewAlbum(String naslov) {
        return dodajAlbum(naslov);
    }

    public static DataAll scenarijA(){
        DataAll da = new DataAll();
        Date danes = new Date();
        da.uporabnik = new User("Tadej.Horvat5@gmail.com","Tedi");
       /* da.album  = new Album("Izlet na Dunaj",da.getUporabnik().getIdUser());
        da.album.setDate(1220227200);*/
      /*  da.getAlbum().dodajSliko("nekaj",21.1353556,22.2345689,"3.5.2015","slika.jpg");
        da.getAlbum().dodajSliko("Dunajska cesta",21.1223556,221.455689," ","DSC00152.jpeg");
        da.getAlbum().dodajSliko("Zivalski vrt",21.1353556,22.2345689,"","DSC00112.jpeg");
        da.getAlbum().dodajSliko("Naravni muzej",21.12345556,22.3455689,"","DSC00122.jpeg");
        da.getAlbum().dodajSliko("Palača",21.1223556,22.2755689,"","DSC00142.jpeg");
        da.getAlbum().dodajSliko("Alexander platz",21.1227236,22.142789,"","DSC00192.jpeg");
        da.getAlbum().dodajSliko("Kebab stand",21.2423556,22.157689,"","DSC00162.jpeg");

        da.getAlbum().dodajSliko("Winer Strasse",21.1223556,22.1455689,"","DSC00212.jpeg");
        da.getAlbum().dodajSliko("Muzej",21.1353556,22.2345689,"","DSC00213.jpeg");
        da.getAlbum().dodajSliko("Center",21.12345556,22.3455689,"","DSC00214.jpeg");
        da.getAlbum().dodajSliko("Štefanova basilika",211223556,222755689,"","DSC00215.jpeg");
        da.getAlbum().dodajSliko("Opera house",21.1227236,22.142789,"","DSC00216.jpeg");
        da.getAlbum().dodajSliko("Stanovanje",21.2423556,22.157689,"","DSC00217.jpeg");

        da.getAlbum().dodajSliko("Bus Station",21.1223556,22.1455689,"","DSC00161.jpeg");
        da.getAlbum().dodajSliko("Muzej BMW",21.1353556,22.2345689,"","DSC00162.jpeg");
        da.getAlbum().dodajSliko("Cineplex",21.12345556,22.3455689,"","DSC00167.jpeg");
        da.getAlbum().dodajSliko("Butanicni Vrt",21.1223556,22.2755689,"","DSC00168.jpeg");
        da.getAlbum().dodajSliko("Park Marije Terezije",21.227236,22.142789,"","DSC00180.jpeg");
        da.getAlbum().dodajSliko("Jutranji sprehod",21.6666666,22.157689,"","DSC00232.jpeg");*/
        da.dodajAlbumList(da.getAlbum());

        //da.getAlbum().izpis();
      /* da.album  = new Album("Izlet na Madzarsko",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220227700);

        da.album  = new Album("Izlet na Cesko",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220227600);

        da.album  = new Album("Morje 2016",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220227500);

        da.album  = new Album("Morje 2017",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220227400);

        da.album  = new Album("Izlet v Avstrijo",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220221200);

        da.album  = new Album("Porec 2015",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220222200);

        da.album  = new Album("Haiti ",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220223200);

        da.album  = new Album("London z solo",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220224200);

        da.album  = new Album("New York",da.getUporabnik().getIdUser());
        da.dodajAlbumList(da.getAlbum());
        da.album.setDate(1220226200);
*/
        return da;
    }

}
