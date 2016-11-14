package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class EditPostcardActivity extends AppCompatActivity {
    Bitmap canvasBitmap;
    ImageView[] pageButtons = new ImageView[4];
    private int[] pointerId = new int[2];
    private PointF[] originalPointer = new PointF[2];
    private PointF[] currentPointer = new PointF[2];
    private double originalScale, originalRotation;
    private int originalPositionX, originalPositionY;
    private int touchMode = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Model.context = this;
        originalPointer[0] = new PointF();
        originalPointer[1] = new PointF();
        currentPointer[0] = new PointF();
        currentPointer[1] = new PointF();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_postcard);
        final ImageView canvasView = (ImageView) findViewById(R.id.imgCanvas);
        canvasView.post(new Runnable() {
            @Override
            public void run() {
                canvasBitmap = Bitmap.createBitmap(canvasView.getWidth(), canvasView.getHeight(), Bitmap.Config.ARGB_8888);
                refreshPage();
            }
        });
        for (int i = 0; i < pageButtons.length; i++) {
            pageButtons[i] = (ImageView) findViewById(getId("imgPage" + (i + 1), "id"));
            pageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.currentPage = Integer.parseInt((String) v.getTag());
                    refreshPageIcons();
                    refreshPage();
                }
            });
        }
        refreshPageIcons();
        Model.currentPostCard = new PostCard();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // Add element edit control
        canvasView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (motionEvent.getPointerCount() == 2 && Model.selectedElement != null) {
                            if (touchMode == 0) {
                                pointerId[0] = motionEvent.getPointerId(0);
                                pointerId[1] = motionEvent.getPointerId(1);
                                originalPointer[0].x = motionEvent.getX(0);
                                originalPointer[0].y = motionEvent.getY(0);
                                originalPointer[1].x = motionEvent.getX(1);
                                originalPointer[1].y = motionEvent.getY(1);
                                originalPositionX = Model.selectedElement.positionX;
                                originalPositionY = Model.selectedElement.positionY;
                                originalRotation = Model.selectedElement.rotation;
                                originalScale = Model.selectedElement.scale;
                                touchMode = 1;
                            } else if (touchMode == 1) {
                                int pointerIndex1 = motionEvent.findPointerIndex(pointerId[0]);
                                int pointerIndex2 = motionEvent.findPointerIndex(pointerId[1]);
                                currentPointer[0].x = motionEvent.getX(pointerIndex1);
                                currentPointer[0].y = motionEvent.getY(pointerIndex1);
                                currentPointer[1].x = motionEvent.getX(pointerIndex2);
                                currentPointer[1].y = motionEvent.getY(pointerIndex2);

                                int originalCenterX = (int) ((originalPointer[0].x + originalPointer[1].x) / 2);
                                int originalCenterY = (int) ((originalPointer[0].y + originalPointer[1].y) / 2);
                                int currentCenterX = (int) ((currentPointer[0].x + currentPointer[1].x) / 2);
                                int currentCenterY = (int) ((currentPointer[0].y + currentPointer[1].y) / 2);

                                double originalDegree = Math.atan2(
                                        originalPointer[0].y - originalPointer[1].y,
                                        originalPointer[0].x - originalPointer[1].x);
                                double currentDegree = Math.atan2(
                                        currentPointer[0].y - currentPointer[1].y,
                                        currentPointer[0].x - currentPointer[1].x);
                                double originalDistance = Math.sqrt(
                                        (originalPointer[0].x - originalPointer[1].x)
                                                * (originalPointer[0].x - originalPointer[1].x)
                                                + (originalPointer[0].y - originalPointer[1].y)
                                                * (originalPointer[0].y - originalPointer[1].y));
                                double currentDistance = Math.sqrt(
                                        (currentPointer[0].x - currentPointer[1].x)
                                                * (currentPointer[0].x - currentPointer[1].x)
                                                + (currentPointer[0].y - currentPointer[1].y)
                                                * (currentPointer[0].y - currentPointer[1].y));
                                Model.selectedElement.scale = originalScale * currentDistance / originalDistance;
                                Model.selectedElement.positionX = originalPositionX + currentCenterX - originalCenterX;
                                Model.selectedElement.positionY = originalPositionY + currentCenterY - originalCenterY;
                                Model.selectedElement.rotation = originalRotation + currentDegree - originalDegree;
                                refreshPage();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        touchMode = 0;
                }
                return true;
            }
        });
    }

    void refreshPageIcons() {
        for (int i = 0; i < pageButtons.length; i++) {
            if (i != Model.currentPage) {
                pageButtons[i].setImageResource(getId("page_" + (i + 1), "drawable"));
            } else {
                pageButtons[i].setImageResource(getId("page_" + (Model.currentPage + 1) + "_s", "drawable"));
            }
        }
    }

    int getId(String name, String type) {
        return getResources().getIdentifier(name, type, getPackageName());
    }

    void refreshPage() {
        PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
        currentPage.render();
        float pageRatio = (float) currentPage.bitmap.getWidth() / currentPage.bitmap.getHeight();
        float screenRatio = (float) canvasBitmap.getWidth() / canvasBitmap.getHeight();
        int width = 0, height = 0, x = 0, y = 0;
        if (pageRatio < screenRatio) { // If screen is wider than page.
            height = canvasBitmap.getHeight();
            width = (int) (height * pageRatio);
        } else {
            width = canvasBitmap.getWidth();
            height = (int) (width / pageRatio);
        }
        x = (canvasBitmap.getWidth() - width) / 2;
        y = (canvasBitmap.getHeight() - height) / 2;
        Matrix matrix = new Matrix();
        RectF srcRect = new RectF(0, 0, currentPage.bitmap.getWidth(), currentPage.bitmap.getHeight());
        RectF dstRect = new RectF(x, y, x + width, y + height);
        matrix.setRectToRect(srcRect, dstRect, Matrix.ScaleToFit.START);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFFFFFFF);
        Canvas canvas = new Canvas(canvasBitmap);
        canvas.drawRect(0, 0, canvasBitmap.getWidth(), canvasBitmap.getHeight(), paint);
        canvas.drawBitmap(currentPage.bitmap, matrix, paint);
        ((ImageView) findViewById(R.id.imgCanvas)).setImageBitmap(canvasBitmap);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("EditPostcard Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
