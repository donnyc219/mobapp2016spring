package com.gsu.electronicpostcard;

import android.support.v7.app.AppCompatActivity;

public class ClipArtSelectionActivity extends AppCompatActivity {

        private static int RESULT_LOAD_IMAGE = 1;


//        @Override
        public void onCreate() {
/*            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_clip_art_selection);
            /*Grid = (GridView) findViewById(R.id.grid);
            Grid.setAdapter(adapter);
            View myview = gridview;
            LayoutInflater li = getLayoutInflater();


            ImageView image1 = (ImageView) findViewById(R.id.img1);
            ImageView image2 = (ImageView) findViewById(R.id.img2);
            ImageView image3 = (ImageView) findViewById(R.id.img3);
            ImageView image4 = (ImageView) findViewById(R.id.img4);
            ImageView image5 = (ImageView) findViewById(R.id.img5);
            ImageView image6 = (ImageView) findViewById(R.id.img6);
            image1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent i = new Intent( Intent.ACTION_PICK, EditPostcardActivity.class);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });*/
        }

/*
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {.Images.Media.DATA };

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
        */
}
