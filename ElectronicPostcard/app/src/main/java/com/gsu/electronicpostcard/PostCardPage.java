package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCardPage  implements Serializable {
    final static int WIDTH = 800;
    final static int HEIGHT = 1311;
    final static int CORNER_RADIUS = 20; // Size of the corner of the bounding box
    boolean drawBoundingBox = false;
    Bitmap bitmap;
    private Bitmap background;
    ArrayList<PostCardElement> elementList = new ArrayList<>();

    public PostCardPage() {
        bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        System.out.println(bitmap.isMutable());
    }

    public void setBackground(Bitmap inputBitmap) {
        background = Bitmap.createScaledBitmap(inputBitmap, WIDTH, HEIGHT, true);
    }

    public void render() {
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        Canvas canvas = new Canvas(bitmap);
        if (background != null) {
            canvas.drawBitmap(background, 0, 0, paint);
        }
        for (PostCardElement element : elementList) {
            element.render(bitmap);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF55AAFF);
        if (drawBoundingBox && Model.selectedElement != null && elementList.contains(Model.selectedElement)) {
            Point[] corners = Model.selectedElement.getBoundingBox();
            for (Point corner : corners) {
                canvas.drawCircle((float) corner.x, (float) corner.y, CORNER_RADIUS, paint);
            }
        }
    }

    public PostCardElement getSelectedElement(int x, int y) {
        for (int i = elementList.size() - 1; i >= 0; i--) {
            Point[] corner = elementList.get(i).getBoundingBox();
            boolean match = true;
            for (int j = 0; j < corner.length; i++) {
                Point vector1 = new Point(corner[j].x - x, corner[j].y - y);
                Point vector2 = new Point(corner[(j + 1) % corner.length].x - corner[j].x,
                        corner[(j + 1) % corner.length].y - corner[j].y);
                int crossProduct = vector1.x * vector2.y - vector1.y * vector2.x;
                if (crossProduct < 0) { // Pointer is outside of the bounding box.
                    match = false;
                    break;
                }
            }
            if (match) return elementList.get(i);
        }
        return null;
    }
}
