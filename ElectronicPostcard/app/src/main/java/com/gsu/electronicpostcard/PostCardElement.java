package com.gsu.electronicpostcard;

import android.graphics.Bitmap;

/**
 * Created by Jack on 11/11/2016.
 */

public abstract class PostCardElement {
    int positionX = 0, positionY = 0;
    double scaleX = 1, scaleY = 1;
    double rotation;

    abstract public void render(Bitmap bitmap);
}
