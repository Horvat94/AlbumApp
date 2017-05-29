package com.example.tadej.albumaplikacija;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;
//map
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import com.example.Album;
import com.example.DataAll;
import com.example.Slika;
import com.squareup.picasso.Picasso;

public class ActivityAlbum extends AppCompatActivity {//activityAlbum ima vpogled v album z slikami

    boolean stateNew;
    private IMapController mapController;
    ApplicationMy app;
    private MapView map;
    ImageView ivSlika;
    TextView edNaslov;
    Album al;
    String ID;
    TextView tvDatum;
    ConstraintLayout layOut;
    SeekBar seekBar;
    GooglePolylineOverlay gp;
    boolean mNightMode;
//    SharedPreferences prefs = getSharedPreferences("OSMNAVIGATOR", MODE_PRIVATE);
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        app = (ApplicationMy) getApplication();
        ivSlika = (ImageView) findViewById(R.id.imgViewAlb);
        edNaslov = (TextView) findViewById(R.id.txtNaslAlb);
        seekBar = (SeekBar) findViewById(R.id.seekBarAlb);
        map = (MapView) findViewById(R.id.mapAlb);
        layOut = (ConstraintLayout) findViewById(R.id.albBackLayot);

        ///////////////map
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);//user agent
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        /////privzeta nastavitev karte
        mapController = map.getController();
        mapController.setZoom(16);
       // mNightMode = prefs.getBoolean("NIGHT_MODE", false);

        ///izris poti
        gp = new GooglePolylineOverlay(Color.RED,8);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setAlbum(extras.getString(DataAll.ALBUM_ID));
            GeoPoint startPoint = new GeoPoint(al.getSeznamSlik().get(progress).getSirinaZeml(), al.getSeznamSlik().get(progress).getVisinaZeml());
            mapController.setCenter(startPoint);
        } else {
            System.out.println("Nič ni v extras!");
        }

        ////////------------------------------------------*/
    }

    void setAlbum(String ID){//poišče album po ID-ju
        al = app.getAlbumByID(ID);
        update(al);
    }
    void nMode(){
         if (mNightMode)
            map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
    }

    public void update(Album al){//posodobitev prikaza
        edNaslov.setText(al.getNaslov());
        if(!al.getSeznamSlik().isEmpty())
        Picasso
                .with(getApplicationContext())
                .load("file://"+al.getSeznamSlik().get(0).getPathFile())
                .resize(600, 300) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                .into(ivSlika);
                 izrisOznacb();
        layOut.setBackgroundColor(Color.rgb(182,245,229));

    }

    /*public void save(){
        al.setNaslov(edNaslov.getText().toString());
        app.save();
    }*/
////////////////////////////////count sort za urejanje datumov not
    static public  int[] countSort(int max, List<Integer> A) {

        int[] C = new int[(max + 1)];

        //List.Clear(C, 0, max+1);//nastavimo vse elemente na 0

        for(int i : A)
        {
            C[i] = C[i] + 1;//prištejemo 1 vsaki obstoječi vrednosti
        }

        for (int i = 0; i < C.length; i++)
        {
            if(i>0)
                C[i] = C[i] + C[i - 1];
        }
        int[] B = new int[A.size()];
        //List<Integer> B = new ArrayList<Integer>();
        Integer j;
        Collections.reverse(A);
        for(int i : A)
        {
            j = C[i]-1;
            B[j] = i;
            C[i]= C[i]-1;
        }
        return B;
    }//konec countSort

    public void onClickSaveMe(View v) {
        if (stateNew) app.getAll().getAlbum();
        //save();
        finish();
    }

    ///izriše označbe na mapi
    public void izrisOznacb(){
        map.getOverlays().clear();
        Collections.sort(al.getSeznamSlik(), new CustomComparator());//uredi seznam slik
        for(Slika s : al.getSeznamSlik())
        {
            Marker oznaka = new Marker(map);
            oznaka.setPosition(new GeoPoint(s.getSirinaZeml(),s.getVisinaZeml()));
            oznaka.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
            oznaka.setTitle(s.getDatum());
            map.getOverlays().add(oznaka);
            gp.addPoint(s.getSirinaZeml(),s.getVisinaZeml());// točka za risanje poti
        }
        map.invalidate();
        map.getOverlays().add(gp);//izris poti na mapi v svojem sloju

    }
/*
    public void dodajOznako(GeoPoint tocka){
        Marker oznaka = new Marker(map);
        oznaka.setPosition(tocka);
        oznaka.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        map.getOverlays().clear();
        map.getOverlays().add(oznaka);
        map.invalidate();
    }*/

public class CustomComparator implements Comparator<Slika> {//urejanje slik po datumu
    DateFormat f = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
    @Override
    public int compare(Slika o1, Slika o2) {
            return (o1.getDatum()).compareTo(o2.getDatum());

    }
}


    @Override
    protected void onResume() {
        super.onResume();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                setAlbum(extras.getString(DataAll.ALBUM_ID));
                seekBar.setMax(al.getSlikeSize()-1);
                seekBar.setProgress(progress);
                izrisOznacb();

            } else {
                System.out.println("Nič ni v extras!");
            }

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {/// za prebiranje slik v albumu

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                progress = i;
                GeoPoint startPoint = new GeoPoint(al.getSeznamSlik().get(progress).getSirinaZeml(), al.getSeznamSlik().get(progress).getVisinaZeml());
                mapController.setCenter(startPoint);
                if(!al.getSeznamSlik().isEmpty())
                Picasso
                        .with(getApplicationContext())
                        .load("file://"+al.getSeznamSlik().get(progress).getPathFile())
                        .resize(600, 300) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                        .into(ivSlika);// ivSlika.setImageBitmap(BitmapFactory.decodeFile(al.getSeznamSlik().get(i).getPathFile()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }
}