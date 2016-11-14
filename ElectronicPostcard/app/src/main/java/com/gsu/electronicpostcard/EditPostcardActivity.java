package com.gsu.electronicpostcard;

import android.content.Intent;
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

public class EditPostcardActivity extends AppCompatActivity {
    Bitmap canvasBitmap;
    ImageView[] pageButtons = new ImageView[4];
    private int[] pointerId = new int[2];
    private PointF[] originalPointer = new PointF[2];
    private PointF[] currentPointer = new PointF[2];
    private double originalScale, originalRotation;
    private int originalPositionX, originalPositionY;
    private int touchMode = 0;
    int pageWidth = 0, pageHeight = 0, pageOffsetX = 0, pageOffsetY = 0;

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
                        if (touchMode != 1 && motionEvent.getPointerCount() == 1) {
                            PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
                            int x = (int) ((motionEvent.getX() - pageOffsetX)
                                    * currentPage.bitmap.getWidth() / pageWidth);
                            int y = (int) ((motionEvent.getY() - pageOffsetY)
                                    * currentPage.bitmap.getHeight() / pageHeight);
                            Model.selectedElement = currentPage.getSelectedElement(x, y);
                            refreshPage();
                        }
                        touchMode = 0;
                }
                return true;
            }
        });

        findViewById(R.id.btnAddElement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Model.context, AddElementsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnDeleteElement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Model.selectedElement != null) {
                    Model.currentPostCard.pages[Model.currentPage].deleteElement(Model.selectedElement);
                    Model.selectedElement = null;
                    refreshPage();
                }
            }
        });
        findViewById(R.id.btnTemplate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Model.context, TemplateSelectionActivity.class);
                startActivity(intent);
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
        if (pageRatio < screenRatio) { // If screen is wider than page.
            pageHeight = canvasBitmap.getHeight();
            pageWidth = (int) (pageHeight * pageRatio);
        } else {
            pageWidth = canvasBitmap.getWidth();
            pageHeight = (int) (pageWidth / pageRatio);
        }
        pageOffsetX = (canvasBitmap.getWidth() - pageWidth) / 2;
        pageOffsetY = (canvasBitmap.getHeight() - pageHeight) / 2;
        Matrix matrix = new Matrix();
        RectF srcRect = new RectF(0, 0, currentPage.bitmap.getWidth(), currentPage.bitmap.getHeight());
        RectF dstRect = new RectF(pageOffsetX, pageOffsetY, pageOffsetX + pageWidth, pageOffsetY + pageHeight);
        matrix.setRectToRect(srcRect, dstRect, Matrix.ScaleToFit.START);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF99BBFF);
        Canvas canvas = new Canvas(canvasBitmap);
        canvas.drawRect(0, 0, canvasBitmap.getWidth(), canvasBitmap.getHeight(), paint);
        canvas.drawBitmap(currentPage.bitmap, matrix, paint);
        ((ImageView) findViewById(R.id.imgCanvas)).setImageBitmap(canvasBitmap);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
