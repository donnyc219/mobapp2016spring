package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EditPostcardActivity extends AppCompatActivity {
    ImageView[] pageButtons = new ImageView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_postcard);
        for (int i = 0; i < pageButtons.length; i++) {
            pageButtons[i] = (ImageView) findViewById(getId("imgPage" + (i + 1), "id"));
            pageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.currentPage = Integer.parseInt((String) v.getTag());
                    refreshPageIcons();
                }
            });
        }
        refreshPageIcons();
        Model.currentPostCard = new PostCard();

    }

    void refreshPageIcons() {
        for (int i = 0; i < pageButtons.length; i++) {
            if (i != Model.currentPage) {
                pageButtons[i].setImageResource(getId("page_" + (i + 1), "drawable"));
            } else {
                pageButtons[i].setImageResource(getId("page_" + (Model.currentPage + 1) + "_s", "drawable"));
            }
        }
    }

    int getId(String name, String type) {
        return getResources().getIdentifier(name, type, getPackageName());
    }
}
