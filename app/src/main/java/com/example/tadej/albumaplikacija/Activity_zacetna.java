package com.example.tadej.albumaplikacija;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import  com.example.Album;
import com.example.DataAll;
import android.support.design.widget.FloatingActionButton;



public class Activity_zacetna extends AppCompatActivity {//v activityZacetna je seznam albumov v katerega lahko dodajamo albume z FloatingButton
    ApplicationMy app;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zacetna);//kje se bo reccleView izvajal
        mRecyclerView = (RecyclerView) findViewById(R.id.recViewZac);//poisce recView v activityResources design
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        app = (ApplicationMy) getApplication();
        mAdapter = new AdapterAlbum(app.getAll(), this);
        mRecyclerView.setAdapter(mAdapter);

        //gumb za dodajanje albumov
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatBtn);// +gumb za dodajanje albumov
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//odpre activityMain za dodajanje novih albumov
                Intent i = new Intent(getBaseContext(), ActivityMain.class);
                i.putExtra(DataAll.ALBUM_ID, app.getNewAlbum("").getIdAlbum());
                startActivity(i);
            }
        });
        //on swipe delete
        setDeleteOnSwipe(mRecyclerView);
    }

    // izbriše album
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
                                app.removeLocationByPosition(viewHolder.getAdapterPosition());
                                app.save();
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_zacetna.this);
                builder.setTitle("Delete location");
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

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
