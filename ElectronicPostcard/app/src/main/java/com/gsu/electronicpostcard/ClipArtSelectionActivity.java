package com.gsu.electronicpostcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ClipArtSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Model.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_art_selection);
    }
}
