package com.gsu.electronicpostcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static android.R.attr.value;

public class ViewCardActivity extends AppCompatActivity {
    PostCardOpenGLRenderer renderer;
    float prevX = -1, prevY = -1;
    final float SENSITIVITY = .8f;
    private void takescreen(){
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory() + "/" + "Pictures/Screenshots/" + "1.jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);



            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Model.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);
        renderer = new PostCardOpenGLRenderer();
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.RGB_888);
        glSurfaceView.setRenderer(renderer);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (prevX >= 0 && prevY > 0) {
                            renderer.rotationYVelocity = SENSITIVITY * (event.getX() - prevX);
                            renderer.rotationX += SENSITIVITY * (event.getY() - prevY);
                        }
                        prevX = event.getX();
                        prevY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        prevX = -1;
                        prevY = -1;
                }
                return true;
            }
        });

        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Model.context, EditPostcardActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnBackToList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Model.context, PostcardListActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v) {
                takescreen();
                Intent myIntent = new Intent(ViewCardActivity.this, SendOptionsActivity.class);
                myIntent.putExtra("key", value); //Optional parameters
                ViewCardActivity.this.startActivity(myIntent);
            }
        });
    }
}