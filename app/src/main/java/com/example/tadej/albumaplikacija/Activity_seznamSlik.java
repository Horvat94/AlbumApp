package com.example.tadej.albumaplikacija;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;import android.support.v7.widget.RecyclerView;

import com.example.Album;


public class Activity_seznamSlik extends AppCompatActivity {
    ApplicationMy app;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String albumID = getIntent().getExtras().getString("album");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seznam_slik);

        mRecyclerView = (RecyclerView) findViewById(R.id.recViewSeznamSlik);//poisce recView v activityResources design
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        app = (ApplicationMy) getApplication();
        mAdapter = new AdapterSlike(app.getAll().getAlbumByID(albumID), this);//vrne ID albuma ki je trenutno odprt , vrne ta activity
        mRecyclerView.setAdapter(mAdapter);

    }
}
