package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCard  implements Serializable {
    final int BITMAP_PADDING = 20;
    final int BITMAP_GAP = 60;
    PostCardPage[] pages = new PostCardPage[4];
    String name = "";
    public PostCard() {
        for (int i = 0; i < pages.length; i++) {
            pages[i] = new PostCardPage();
            pages[i].drawBoundingBox = true;
        }
    }

    public void changeTemplate(String resourceName) {
        pages[0].setBackground(resourceName);
        pages[1].setBackground(resourceName);
        pages[2].setBackground(resourceName);
        pages[3].setBackground(resourceName);
        pages[1].drawBackground = false;
        pages[2].drawBackground = false;
    }

    public Bitmap drawToBitmap() {
        for (int i = 0; i < pages.length; i++) {
            pages[i].drawBoundingBox = false;
            pages[i].render();
            pages[i].drawBoundingBox = true;
        }
        Bitmap result = Bitmap.createBitmap(PostCardPage.WIDTH * 2 + BITMAP_PADDING * 2,
                PostCardPage.HEIGHT * 2 + BITMAP_PADDING * 2 + BITMAP_GAP, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        Matrix matrix = new Matrix();
        matrix.setTranslate(BITMAP_PADDING, BITMAP_PADDING);
        canvas.drawBitmap(pages[3].bitmap, matrix, null);

        matrix.setTranslate(BITMAP_PADDING + PostCardPage.WIDTH, BITMAP_PADDING);
        canvas.drawBitmap(pages[0].bitmap, matrix, null);

        matrix.setTranslate(BITMAP_PADDING, BITMAP_PADDING + BITMAP_GAP + PostCardPage.HEIGHT);
        canvas.drawBitmap(pages[1].bitmap, matrix, null);

        matrix.setTranslate(BITMAP_PADDING + PostCardPage.WIDTH,
                BITMAP_PADDING + BITMAP_GAP + PostCardPage.HEIGHT);
        canvas.drawBitmap(pages[2].bitmap, matrix, null);
        return result;
    }
}
