package com.gsu.electronicpostcard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AddElementsActivity extends AppCompatActivity {
    private void handwriting1() {

    }
    private void clipart1() {
        ImageView ca1 = (ImageView) findViewById(R.id.ca ); //1
        ca1.setOnClickListener( new View.OnClickListener() { //2
            public void onClick(View v) {
                Intent intent1 = new Intent(AddElementsActivity. this , //3
                        ClipArtSelectionActivity. class );
                startActivity(intent1);
            }
        });
    }
    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_elements);

        ImageView gal1 = (ImageView) findViewById(R.id.gal);
        gal1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        ImageView hw1 = (ImageView) findViewById(R.id.hw ); //1
        hw1.setOnClickListener( new View.OnClickListener() { //2
            public void onClick(View v) {
                Intent intent = new Intent(AddElementsActivity. this , //3
                        HandwritingGeneratorActivity. class );
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            PostCardPage currentPage = Model.currentPostCard.pages[Model.currentPage];
            PostCardImage imageElement = new PostCardImage(BitmapFactory.decodeFile(picturePath));
            currentPage.addElement(imageElement);
            Intent intent = new Intent(this, EditPostcardActivity.class);
            startActivity(intent);
        }


    }
}



