package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.Point;

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

    public PostCardElement getSelectedElement(int x, int y) {
        for (int i = elementList.size() - 1; i >= 0; i++) {
            Point[] corner = elementList.get(i).getBoundingBox();
            boolean match = true;
            for (int j = 0; j < corner.length; i++) {
                Point vector1 = new Point(corner[j].x - x, corner[j].y - y);
                Point vector2 = new Point(corner[(j + 1) % corner.length].x - corner[j].x,
                        corner[(j + 1) % corner.length].y - corner[j].y);
                int crossProduct = vector1.x * vector2.y - vector1.y * vector2.x;
                if (crossProduct >= 0) match = false; // Mouse is outside of the bounding box
            }
            if (match) return elementList.get(i);
        }
        return null;
    }
}
