package com.gsu.electronicpostcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PrintSendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Model.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_send);
    }
}
