package com.example.tadej.albumaplikacija;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DataAll;
import com.example.Album;
import com.example.Slika;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ActivityMain extends AppCompatActivity {
    ApplicationMy app;
    EditText naslovMain;
    EditText datMain;
    TextView userIdMain;
    ImageView slikaMain;
    Album al;

    private double lat;
    private double len;

    private String picDirPath;
    private Slika tmpSlika;
    String ID;
    private static final int RESULT_LOAD_IMAGE = 1;
    public static final int IMAGE_GALLERY_REQUEST = 20;//v activity main lahko oblikujemo album
    public static String NEW_LOCATION_ID="NEW_LOCATION";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    boolean stateNew;
/////RecycleView za seznam slik
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
/////--------------------------------------------------

/*
*     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
* */

    //Preveri za dovoljenje

    /*int permissionCheck = ContextCompat.checkSelfPermission(ActivityMain.this,
            Manifest.permission.READ_EXTERNAL_STORAGE);*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (ApplicationMy) getApplication();
        naslovMain =(EditText) findViewById(R.id.naslMain);
        datMain =(EditText) findViewById(R.id.datMain);
        userIdMain =(TextView) findViewById(R.id.idMain);
        stateNew = false;
        ID="";
        //slikaMain =(ImageView) findViewById(R.id.slikaMain);

        ///zahteva dovoljenje za pranje iz zunanjega diska
        if (ContextCompat.checkSelfPermission(ActivityMain.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        Bundle extras = getIntent().getExtras();//sprejmemo put extra podatke ki smo jih poslali iz activity_zacetna ,ce so na voljo
        if (extras != null) {

        mRecyclerView = (RecyclerView) findViewById(R.id.recViewMain);//poisce recView v activityResources design
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterSlike(app.getAlbumByID(extras.getString(DataAll.ALBUM_ID)), this);// po idju albuma ugotovimo kateri album smo izbrali v recycleViewAlbumov
        mRecyclerView.setAdapter(mAdapter);
    } else {
        System.out.println("Nič ni v extras!");
    }

        //on swipe delete sliko
        setDeleteOnSwipe(mRecyclerView);

    }//konec onCreate


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void save(){

        al.setNaslov(naslovMain.getText().toString());
       // al.setDate(new Date(DataAll.dt.format(datMain.getText().toString())).getTime());
        //            letniCas = ft.parse(letCasStr);//pretvorba iz string v long Date


      //  al.setDate(Long.parseLong((datMain.getText().toString())));//
        if(app.save())
            System.out.println("Uspešno shranjeno");
    }//konec on save

    public void update(Album al){
        naslovMain.setText(""+al.getNaslov());
        datMain.setText(DataAll.dt.format(new Date(al.getDate())));
       // datMain.setText(DataAll.dt.format(al.getDate()));
        userIdMain.setText(al.getIdUser());
       //slMain.setText(al.getIdAlbum());
    }//konec update

    void setAlbum(String ID){
    al = app.getAlbumByID(ID);
        update(al);
    }//konec setAlbum

  /*  public void onClickSeznamSlik(View v) {
        Intent i = new Intent(getBaseContext(), Activity_seznamSlik.class);
        i.putExtra("album",getIntent().getExtras().getString(DataAll.ALBUM_ID));//album je ključ, podatke ki jih pošiljamo
        startActivity(i);
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();//sprejmemo put extra podatke ki smo jih poslali iz activity_zacetna ,ce so na voljo
        if (extras != null) {
            setAlbum(extras.getString(DataAll.ALBUM_ID));
        } else {
            System.out.println("Nič ni v extras!");
        }

        mAdapter.notifyDataSetChanged();//adapter za slike se osveži

    }//konec onResume

    public void onClickGalerijaAlb(View v){//odpre galerijo
        Intent galeryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        /*Intent galeryIntent = new Intent();
        galeryIntent.setAction(Intent.ACTION_GET_CONTENT);*/
       galeryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galeryIntent,RESULT_LOAD_IMAGE);
        save();//shrani spremembe v albumu
    }//konec onClickGalerija
/*
    public static Bitmap decodeUri(Context context, Uri uri, final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }//konec on decode Uri
*/

    public static String getRealPathFromUri(Context context, Uri contentUri) {//vrne originalno pot slike
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }//konec getRealPathFromUri

  /*  ///Rotira sliko če je obrnjena
    private Bitmap rotateImage(Bitmap bitmap, String path){
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(path);
        }catch(IOException e){
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return rotatedBitmap;
    }*/

    @Override//vrne rezultat iz galerije slik
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            //images = data.getData();
            for(int i=0; i< data.getClipData().getItemCount();i++) {
                Uri selImage = data.getClipData().getItemAt(i).getUri();
                String path = getRealPathFromUri(ActivityMain.this, selImage);//vrne original pot do slike
                System.out.println("++data" + data.getClipData().getItemCount());//izpis stevila slik

                try {
                    ExifInterface exif = new ExifInterface(path);
                    float[] cord = new float[2];
                    if (exif.getLatLong(cord)) {
                        System.out.println(cord[0] + " " + cord[1]);
                        lat = (double) cord[0];//latitude
                        len = (double) cord[1];//longatude
                    } else {
                        lat = 0.0;
                        len = 0.0;
                    }
                    String naslovSl = path.substring(path.lastIndexOf("/") + 1);// iz poti prebere naslov slike
                    al.getSeznamSlik().add(new Slika(naslovSl, lat, len, exif.getAttribute(ExifInterface.TAG_DATETIME), path, al.getIdUser()));//shrani novo sliko v album seznam slik

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }//konec on Activity Result

    // izbriše Sliko ob swapanju
    public void setDeleteOnSwipe(final RecyclerView mRecyclerView) {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                Bundle extras = getIntent().getExtras();//dobimo id albuma, v katerem se nahajamo
                                if (extras != null) {
                                    app.removeSlikaByPosition(extras.getString(DataAll.ALBUM_ID),viewHolder.getAdapterPosition());//izbrišemo iz albuma v katerem se nahajamo
                                } else {
                                    System.out.println("Nič ni v extras!");
                                }
                                app.save();
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                builder.setTitle("Delete picture");
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                ;
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

                builder.show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void onClickSaveMe(View v) {
        //if (stateNew) app.getAll().dodajAlbumList(al);//če se staje spremeni dodamo album v seznam albumov
        save();

        finish();
    }
}
