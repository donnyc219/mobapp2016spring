package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCardClipArt extends PostCardImage {
    public PostCardClipArt(int resourceId) {
        super();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        image = BitmapFactory.decodeResource(Model.context.getResources(), resourceId, options);
        width = image.getWidth();
        height = image.getHeight();
    }
}
