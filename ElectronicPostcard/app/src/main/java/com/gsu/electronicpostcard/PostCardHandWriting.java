package com.gsu.electronicpostcard;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.provider.Settings;
import android.renderscript.Type;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCardHandWriting extends PostCardElement {
    private String text = "";
    private int lineHeight = 0;
    public PostCardHandWriting(String txt) {
        setText(txt);
    }

    @Override
    public void render(Canvas canvas) {
        Model.paint.setColor(0xFF000000);
        Model.paint.setTextSize(20);
        Model.paint.setTypeface(Typeface.create("cursive", Typeface.NORMAL));
        Matrix matrix = new Matrix();
        matrix.setTranslate(-width / 2, -height / 2 + lineHeight + 4);
        matrix.postScale((float) scale, (float) scale);
        matrix.postRotate((float) Math.toDegrees(rotation));
        matrix.postTranslate(positionX, positionY);
        canvas.setMatrix(matrix);

        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            canvas.drawText(line, 0, lineHeight * i, Model.paint);
        }
        matrix.setTranslate(0, 0);
        canvas.setMatrix(matrix);
    }

    public void setText(String newText) {
        Model.paint.setTextSize(20);
        Model.paint.setTypeface(Typeface.create("cursive", Typeface.NORMAL));
        int maxWidth = 0;
        text = newText;
        String[] lines = text.split("\n");
        Rect bound = new Rect();
        Model.paint.getTextBounds(text, 0, text.length(), bound);
        lineHeight = bound.height();
        for (String line : lines) {
            Model.paint.getTextBounds(line, 0, line.length(), bound);
            if (bound.width() > maxWidth) maxWidth = bound.width();
        }
        width = maxWidth;
        height = lineHeight * lines.length;
        System.out.println(width + ", " + height);
    }
}
