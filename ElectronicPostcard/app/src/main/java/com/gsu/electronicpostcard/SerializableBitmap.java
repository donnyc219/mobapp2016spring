package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Jack on 11/15/2016.
 */

public class SerializableBitmap implements Serializable {
    Bitmap bitmap;

    public SerializableBitmap(Bitmap newBitmap) {
        bitmap = newBitmap;
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException{
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte byteBuffer[] = byteStream.toByteArray();
        outputStream.write(byteBuffer);
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException{
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int i;
        while ((i = inputStream.read()) != -1) {
            byteStream.write(i);
        }
        byte byteBuffer[] = byteStream.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(byteBuffer, 0, byteBuffer.length);
    }

    public int getWidth() {return bitmap.getWidth();}
    public int getHeight() {return bitmap.getHeight();}
}
