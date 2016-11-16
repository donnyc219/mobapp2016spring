package com.gsu.electronicpostcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HandwritingGeneratorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Model.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwriting_generator);
        findViewById(R.id.btnCreateText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
                String text = ((TextView) findViewById(R.id.txtHandwriting)).getText().toString();
                currentPage.addElement(new PostCardHandWriting(text));
                Intent intent = new Intent(Model.context, EditPostcardActivity.class);
                startActivity(intent);
            }
        });
    }
}
