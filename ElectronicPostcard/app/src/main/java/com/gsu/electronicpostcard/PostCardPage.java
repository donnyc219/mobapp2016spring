package com.gsu.electronicpostcard;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCardPage {
    ArrayList<PostCardElement> elementList = new ArrayList<>();

    public void render(Bitmap canvas) {
        for (PostCardElement element : elementList) {
            element.render(canvas);
        }
    }
}
