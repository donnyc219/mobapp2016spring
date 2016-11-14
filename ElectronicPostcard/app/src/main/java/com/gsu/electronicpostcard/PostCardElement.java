package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by Jack on 11/11/2016.
 */

public abstract class PostCardElement implements Serializable{
    int positionX = 0, positionY = 0; // Position x and y are the coordinate of the center of the element.
    int width, height;
    double scale = 1;
    double rotation;
    String name = "";

    abstract public void render(Bitmap bitmap);

    // Method to get the bounding box of the object.
    // The method returns an array of 4 points representing the 4 corners of the elements
    // adjusted for position, size, scale and rotation.
    public Point[] getBoundingBox() {
        Point[] result = new Point[4];
        result[0] = new Point(-width / 2, -height / 2);
        result[1] = new Point(width / 2, -height / 2);
        result[2] = new Point(width / 2, height / 2);
        result[3] = new Point(-width / 2, height / 2);

        for (Point corner : result) {
            corner.x *= scale;
            corner.y *= scale;
            int tempX = corner.x;
            corner.x = (int) (corner.x * Math.cos(rotation) - corner.y * Math.sin(rotation));
            corner.y = (int) (tempX * Math.cos(rotation) + corner.y * Math.sin(rotation));
            corner.x += positionX;
            corner.y += positionY;
        }

        return result;
    }
}
