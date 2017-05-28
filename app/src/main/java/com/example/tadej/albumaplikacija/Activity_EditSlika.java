package com.example.tadej.albumaplikacija;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Slika;



import com.example.Album;
import com.example.DataAll;
import com.squareup.picasso.Picasso;

//map
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Activity_EditSlika extends AppCompatActivity implements MapEventsReceiver {

    ApplicationMy app;
    private EditText naslov;
    private EditText longitude;
    private EditText latitude;
    private EditText dat;
    private EditText search;

    // private TextView userIdMain;
    private  ImageView slikaIv;
    private  Album al;
    private  Slika sl;

    //map
    private IMapController mapController;
    private MapView map;
   // private MapEventsReceiver mReceive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__edit_slika);

        app = (ApplicationMy) getApplication();
        slikaIv = (ImageView) findViewById(R.id.imViSL);
        naslov = (EditText) findViewById(R.id.edtxtNaslSL);
        dat = (EditText) findViewById(R.id.edtxtDatSL);
        latitude = (EditText) findViewById(R.id.edtxtLatSL);
        longitude = (EditText) findViewById(R.id.edtxtLanSL);
        search = (EditText) findViewById(R.id.edtxtSrcSL);


        map = (MapView) findViewById(R.id.mapAlb);
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
        mapController.setZoom(12);//nastavi približek

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setSlika(extras.getString(Album.slika_ID),extras.getString(DataAll.ALBUM_ID));//kličemo sliko po slika id in album iz extras
            GeoPoint startPoint = new GeoPoint(sl.getSirinaZeml(), sl.getVisinaZeml());//prav (sirina,visina)
            dodajOznako(startPoint);
            mapController.setCenter(startPoint);
        } else {
            System.out.println("Nič ni v extras!");
        }

    }//konec on create


    public void dodajOznako(GeoPoint tocka) {

        Marker oznaka = new Marker(map);
        oznaka.setPosition(tocka);
        oznaka.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        oznaka.setIcon(getResources().getDrawable(R.drawable.person));
        for (int i = map.getOverlays().size()-1; i>=0; i--){//izbris starih oznak
            if(map.getOverlays().get(i) instanceof Marker){
                map.getOverlays().remove(i);
            }
        }
        map.getOverlays().add(oznaka);


        map.invalidate();

    }
    public void save(){ //shrani podatke v razred album

        al.getSlikaByID(sl.getIdSlike()).setImeSlike(naslov.getText().toString());//išče po albumu z idjem slike
        al.getSlikaByID(sl.getIdSlike()).setSirinaZeml(Double.parseDouble(latitude.getText().toString()));
        al.getSlikaByID(sl.getIdSlike()).setVisinaZeml(Double.parseDouble(longitude.getText().toString()));
        al.getSlikaByID(sl.getIdSlike()).setImeSlike(naslov.getText().toString());

        if(app.save())//shrani vse podatke v app, ki nato shrani v json
            System.out.println("Uspešno shranjeno");
    }//konec on save

    void setSlika(String slikaID, String albumID){
        sl = app.getSlikaByID(slikaID,albumID);
        updateSlika(sl);
    }//konec setSlika

    void setAlbum(String ID){
        al = app.getAlbumByID(ID);
    }//konec setAlbum

    public void updateSlika(Slika sl){
        naslov.setText(sl.getImeSlike());
        dat.setText(sl.getDatum());
        latitude.setText(String.valueOf(sl.getVisinaZeml()));//višina
        longitude.setText(String.valueOf(sl.getSirinaZeml()));//dolžina
        try {
            Picasso
                    .with(getApplicationContext())
                    .load("file://" + sl.getPathFile())
                    .centerCrop() // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .resize(80, 80)
                    .into(slikaIv);                // ivSlika.setImageBitmap(BitmapFactory.decodeFile(al.getSeznamSlik().get(i).getPathFile()));
            //slMain.setText(al.getIdAlbum());
        }
        catch(Exception e){

        }
    }//konec update

    @Override
    protected void onResume() {

        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setSlika(extras.getString(Album.slika_ID),extras.getString(DataAll.ALBUM_ID));//kličemo sliko po slika id in album iz extras
            setAlbum(extras.getString(DataAll.ALBUM_ID));
        } else {
            System.out.println("Nič ni v extras!");
        }

           /* MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                latitude.setText(String.valueOf(p.getLatitude()));
                longitude.setText(String.valueOf(p.getLongitude()));
                dodajOznako(p);*/
                /*sl.setSirinaZeml(p.getLatitude());
                sl.setVisinaZeml(p.getLongitude());*/
          /*      Toast.makeText(getBaseContext(),"Nova lokacija: "+p.getLatitude() + " - "+p.getLongitude(),Toast.LENGTH_LONG).show();
                return true;
            }
        };
        MapEventsOverlay evOverlay = new MapEventsOverlay(this,mReceive);
        map.getOverlays().add(evOverlay);*/

//        dodajOznako(new GeoPoint(sl.getSirinaZeml(),sl.getVisinaZeml()));

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(mapEventsOverlay);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    public GeoPoint getLocationFromAddress(String strAddress){//Lokacijo dobimo iz niza, ki ga vnesemo

        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> address = geoCoder.getFromLocationName(strAddress, 1);
            double latitude = address.get(0).getLatitude();
            double longitude = address.get(0).getLongitude();
            return new GeoPoint(latitude,longitude);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onClickIsciLokacijo(View v) {
        try {
            GeoPoint novaLok = getLocationFromAddress(search.getText().toString());
            Toast.makeText(getBaseContext(),"Lokacija: "+search.getText().toString(),Toast.LENGTH_LONG).show();
            latitude.setText(String.valueOf(novaLok.getLatitude()));
            longitude.setText(String.valueOf(novaLok.getLongitude()));
            mapController.setCenter(novaLok);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


/*nočni način
* if (mNightMode)
			map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);

* */
    public void onClickSaveMe(View v) {
        save();
        finish();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        latitude.setText(String.valueOf(p.getLatitude()));
        longitude.setText(String.valueOf(p.getLongitude()));
        dodajOznako(p);// blokira Helper!!!!!!
                sl.setSirinaZeml(p.getLatitude());
                sl.setVisinaZeml(p.getLongitude());
                Toast.makeText(getBaseContext(),"Nova lokacija: "+p.getLatitude() + " - "+p.getLongitude(),Toast.LENGTH_LONG).show();
               return true;
    }
}
