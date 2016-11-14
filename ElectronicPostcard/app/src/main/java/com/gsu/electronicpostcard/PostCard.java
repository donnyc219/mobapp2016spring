package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCard  implements Serializable {
    PostCardPage[] pages = new PostCardPage[4];

    public PostCard() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        for (int i = 0; i < pages.length; i++) {
            pages[i] = new PostCardPage();
            pages[i].drawBoundingBox = true;
        }
        pages[0].setBackground(BitmapFactory.decodeResource(Model.context.getResources(), R.drawable.cake1, options));
        pages[1].setBackground(BitmapFactory.decodeResource(Model.context.getResources(), R.drawable.balloon1, options));
        pages[2].setBackground(BitmapFactory.decodeResource(Model.context.getResources(), R.drawable.flowers1, options));
        pages[3].setBackground(BitmapFactory.decodeResource(Model.context.getResources(), R.drawable.flowers, options));
        pages[0].elementList.add(new PostCardClipArt(R.drawable.page_1));
        Model.selectedElement = pages[0].elementList.get(0);
        Model.selectedElement.positionX = PostCardPage.WIDTH / 2;
        Model.selectedElement.positionY = PostCardPage.HEIGHT / 2;
    }
}
