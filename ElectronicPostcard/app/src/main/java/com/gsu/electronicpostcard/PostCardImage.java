package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCardImage extends PostCardElement {
    Bitmap image = null;
    public PostCardImage() {
        super();
        width = 100;
        height = 100;
    }
    public PostCardImage(Bitmap image) {
        super();
        this.image = Bitmap.createBitmap(image);
        width = image.getWidth();
        height = image.getHeight();
    }
    @Override
    public void render(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(-image.getWidth() / 2, -image.getHeight() / 2);
        matrix.postScale((float) scale, (float) scale);
        matrix.postRotate((float) Math.toDegrees(rotation));
        matrix.postTranslate(positionX, positionY);
        Model.paint.setFilterBitmap(true);
        canvas.drawBitmap(image, matrix, Model.paint);
    }
}
