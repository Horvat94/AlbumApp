package com.example.tadej.albumaplikacija;

/**
 * Created by Tadej on 20.3.2017.
 */
import android.app.Application;
import com.example.DataAll;
import com.example.Slika;
import com.example.Album;
import java.util.List;
import java.io.File;

public class ApplicationMy extends Application{
    DataAll all;
    private static final String DATA_MAP = "albumdatamap";
    private static final String FILE_NAME = "albumApp.json";

    public void onCreate(){
        super.onCreate();
        if(!load())
        all =DataAll.scenarijA();
    }


    public DataAll getAll() {
        return all;
    }

    public Slika getTestSlika(){
        return all.getAlbum().getSeznamSlik().get(0);
    }

    /*public Slika getSlikaById(){
        return all.getAlbum();
    }*/


    public Album getTestAlbum() {
        return all.getAlbum(0);
    }

    public Album getNewAlbum(String n) {
        return all.getNewAlbum(n);
    }


    public Album getAlbumByID(String id){
        return all.getAlbumByID(id);
    }

    public Slika getSlikaByID(String idSlika, String idAlbum){
        return all.getAlbumByID(idAlbum).getSlikaByID(idSlika);
    }

    public List<Album> getAlbumAll(){
        return all.getAlbumAll();
    }


////////////////////////////////////////////////////////////////Gson shrani

    public boolean save() {
        File file = new File(this.getExternalFilesDir(DATA_MAP), "" + FILE_NAME);
        return ApplicationJson.save(all,file);
    }

    /////////////////////////////////////////////////////////////Gson naloži
    public boolean load(){
        File file = new File(this.getExternalFilesDir(DATA_MAP), "" + FILE_NAME);
        DataAll tmp = ApplicationJson.load(file);
        if (tmp!=null) all = tmp;
        else return false;
        return true;
    }
    ////////////////////////////////////////////////////////////kliče on swipe
    public void removeLocationByPosition(int adapterPosition) {
        all.getAlbumAll().remove(adapterPosition);
    }

    public void removeSlikaByPosition(String ID,int adapterPosition) {
        all.getAlbumByID(ID).getSeznamSlik().remove(adapterPosition);
    }

}