package com.gsu.electronicpostcard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ClipArtSelectionActivity extends AppCompatActivity {
//    private static int RESULT_LOAD_IMAGE = 1;
    private final int IMAGE_PER_ROW = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Model.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_art_selection);
        final GridLayout grid = (GridLayout) findViewById(R.id.grid);
        grid.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ImageView newView = new ImageView(Model.context);
                    final int resourceId = getResources().getIdentifier(
                            "img" + (i + 1), "drawable", getPackageName());
                    newView.setImageResource(resourceId);
                    newView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.
                            LayoutParams(grid.getWidth() / 3, grid.getWidth() / 3);
                    newView.setLayoutParams(params);
                    grid.addView(newView);
                    newView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
                            currentPage.addElement(new PostCardClipArt(resourceId));
                            Intent intent = new Intent(Model.context, EditPostcardActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

//        GridView  gv = (GridView)findViewById(R.id.grid);
//        final int numVisibleChildren = gv.getChildCount();
//        final int firstVisiblePosition = gv.getFirstVisiblePosition();
//
//        for ( int i = 0; i < numVisibleChildren; i++ ) {
//            int positionOfView = firstVisiblePosition + i;
//            if()
//
//            if (positionOfView == positionIamLookingFor)/* position is the image hat we are clicking on*/ {
//                View view = gv.getChildAt(i);
//            }
//        }
//        final ImageView image1 = (ImageView) findViewById(R.id.img1);
//        Uri path = Uri.parse("android.resource://com.gsu.electronicpostcard/" + R.drawable.img1);
//        image1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Uri path = Uri.parse("android.resource://com.gsu.electronicpostcard/" + R.drawable.img1);
//                Intent i = new Intent(Intent.ACTION_PICK,path);
//
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//        });
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
//            PostCardImage imageElement = new PostCardImage(BitmapFactory.decodeFile(picturePath));
//            currentPage.addElement(imageElement);
//            Intent intent = new Intent(this, EditPostcardActivity.class);
//            startActivity(intent);
//        }
//
//
//    }
}



