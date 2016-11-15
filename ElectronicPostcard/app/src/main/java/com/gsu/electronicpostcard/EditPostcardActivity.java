package com.gsu.electronicpostcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class EditPostcardActivity extends AppCompatActivity {
    boolean doodle = false;
    boolean erase = false;
    private float DOODLE_STROKE_WIDTH = 10;
    private int ERASE_RADIUS = 45;
    private PointF prevLocation;
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
                        if (!doodle && !erase) {
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
                        } else { // Doodling mode
                            if (touchMode == 0) {
                                touchMode = 1;

                            } else if (touchMode == 1) {

                                PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
                                PointF currentLocation = getLocationOnPage(motionEvent.getX(),
                                        motionEvent.getY());
                                if (doodle) {
                                    Model.paint.setColor(0xFFFF4499);
                                    Model.paint.setStrokeWidth(DOODLE_STROKE_WIDTH);
                                    Canvas canvas = new Canvas(currentPage.doodle.bitmap);
                                    Model.paint.setStyle(Paint.Style.FILL_AND_STROKE);
                                    canvas.drawLine(prevLocation.x, prevLocation.y,
                                            currentLocation.x, currentLocation.y, Model.paint);
                                }
                                if (erase) {
                                    eraseBitmap(currentPage.doodle.bitmap, (int) currentLocation.x,
                                            (int) currentLocation.y);
                                }
                            }
                            refreshPage();
                            prevLocation = getLocationOnPage(motionEvent.getX(), motionEvent.getY());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!doodle && !erase && touchMode != 1 && motionEvent.getPointerCount() == 1) {
                            PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
                            PointF locationOnPage = getLocationOnPage(motionEvent.getX(), motionEvent.getY());
                            Model.selectedElement = currentPage.getSelectedElement((int) locationOnPage.x,
                                    (int) locationOnPage.y);
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
        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostcardListActivity.serializePostcard(Model.currentPostCard);
                Intent intent = new Intent(Model.context, ViewCardActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnDoodle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.selectedElement = null;
                erase = false;
                doodle = !doodle;
                refreshDoodleButtons();
            }
        });
        findViewById(R.id.btnErase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.selectedElement = null;
                doodle = false;
                erase = !erase;
                refreshDoodleButtons();
            }
        });
    }

    private void eraseBitmap(Bitmap bitmap, int centerX, int centerY) {
        for (int y = -ERASE_RADIUS; y <= ERASE_RADIUS; y++) {
            if (centerY + y < 0) y = -centerY;
            if (centerY + y >= bitmap.getHeight()) break;
            int xRange = (int) Math.sqrt(ERASE_RADIUS * ERASE_RADIUS - y * y);
            for (int x = -xRange; x <= xRange; x++) {
                if (centerX + x < 0) x = -centerX;
                if (centerX + x >= bitmap.getWidth()) break;
                bitmap.setPixel(centerX + x, centerY + y, 0x00000000);
            }
        }
    }
    private void refreshDoodleButtons() {
        ((ImageView) findViewById(R.id.btnDoodle)).setImageResource(
                doodle ? R.drawable.doodle_down : R.drawable.doodle_up);
        ((ImageView) findViewById(R.id.btnErase)).setImageResource(
                erase ? R.drawable.erase_down : R.drawable.erase_up);
    }
    PointF getLocationOnPage(float pointerX, float pointerY) {
        PointF result = new PointF();
        PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
        result.x = ((pointerX - pageOffsetX)
                * currentPage.bitmap.getWidth() / pageWidth);
        result.y = ((pointerY - pageOffsetY)
                * currentPage.bitmap.getHeight() / pageHeight);
        return result;
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
        canvas.drawBitmap(currentPage.bitmap.bitmap, matrix, paint);
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
