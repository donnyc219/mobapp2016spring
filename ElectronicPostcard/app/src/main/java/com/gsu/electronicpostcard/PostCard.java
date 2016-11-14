package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCard  implements Serializable {
    PostCardPage[] pages = new PostCardPage[4];
    String name = "";
    public PostCard() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        for (int i = 0; i < pages.length; i++) {
            pages[i] = new PostCardPage();
            pages[i].drawBoundingBox = true;
        }
    }

    public void changeTemplate(Bitmap template) {
        for (int i = 0; i < pages.length; i++) {
            pages[i].setBackground(template);
        }
    }
}
