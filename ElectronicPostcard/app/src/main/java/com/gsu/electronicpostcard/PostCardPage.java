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
    SerializableBitmap bitmap;
    private SerializableBitmap background;
    private SerializableBitmap doodle;
    private ArrayList<PostCardElement> elementList = new ArrayList<>();

    public PostCardPage() {
        bitmap = new SerializableBitmap(Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888));
        doodle = new SerializableBitmap(Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888));
    }

    public void setBackground(Bitmap inputBitmap) {
        background = new SerializableBitmap(Bitmap.createScaledBitmap(inputBitmap, WIDTH, HEIGHT, true));
    }

    public void addElement(PostCardElement element) {
        element.scale = 1;
        if (element.width > bitmap.getWidth() / 4) {
            element.scale = (float) (bitmap.getWidth() / 4) / element.width;
        }
        element.positionX = bitmap.getWidth() / 2;
        element.positionY = bitmap.getHeight() / 2;
        elementList.add(element);
        Model.selectedElement = element;
    }

    public void deleteElement(PostCardElement element) {
        elementList.remove(element);
    }

    public void render() {
        Model.paint.setFilterBitmap(true);
        Model.paint.setStyle(Paint.Style.FILL);
        Canvas canvas = new Canvas(bitmap.bitmap);

        // Draw background
        Model.paint.setColor(0xFFFFFFFF);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), Model.paint);
        if (background != null) {
            canvas.drawBitmap(background.bitmap, 0, 0, Model.paint);
        }

        // Draw elements
        for (PostCardElement element : elementList) {
            element.render(canvas);
        }

        // Draw bounding box.
        Model.paint.setStyle(Paint.Style.FILL);
        Model.paint.setColor(0xFF55AAFF);
        if (drawBoundingBox && Model.selectedElement != null && elementList.contains(Model.selectedElement)) {
            Point[] corners = Model.selectedElement.getBoundingBox();
            for (Point corner : corners) {
                canvas.drawCircle((float) corner.x, (float) corner.y, CORNER_RADIUS, Model.paint);
            }
        }

        // Draw doodle layer.
        canvas.drawBitmap(doodle.bitmap, 0, 0, Model.paint);
    }

    public PostCardElement getSelectedElement(int x, int y) {
        for (int i = elementList.size() - 1; i >= 0; i--) {
            Point[] corner = elementList.get(i).getBoundingBox();
            boolean match = true;
            for (int j = 0; j < corner.length; j++) {
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
