package com.gsu.electronicpostcard;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import static android.R.attr.value;
import static android.content.Intent.*;
import static com.gsu.electronicpostcard.R.drawable.gallery;
import static com.gsu.electronicpostcard.R.id.activity_print_send;

public class SendOptionsActivity extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
     */

    private void takescreen(){

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory() + "/" + "Pictures/Screenshots/" + "1.jpg";
            Intent intent1 = new Intent();
            File imgFile = new  File("/sdcard/Images/test_image.jpg");

            Toast.makeText(SendOptionsActivity.this, "The file is Stored at: "+mPath,
                    Toast.LENGTH_LONG).show();

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent1 = new Intent();
        intent1.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent1.setDataAndType(uri, "image/*");
        startActivity(intent1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_options);

        ImageButton email = (ImageButton) findViewById(R.id.mail);

        ImageButton save = (ImageButton) findViewById(R.id.gallery);
        save.setOnClickListener(new  View.OnClickListener(){
                                    public void onClick(View v) {
                                        takescreen();
                                    }
        });



        ImageButton print = (ImageButton) findViewById(R.id.printer);
        print.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v) {

                Intent myIntent = new Intent(SendOptionsActivity.this, PrintSendActivity.class);
                myIntent.putExtra("key", value); //Optional parameters
                SendOptionsActivity.this.startActivity(myIntent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() { //error @.setOnClickListener Cannot resolve symbol


            public void onClick(View v) {



                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/image");
                intent.putExtra(EXTRA_EMAIL, new String[]{"some@email.address"});
                intent.putExtra(EXTRA_SUBJECT, "subject");
                intent.putExtra(EXTRA_TEXT, "mail body");
                String mpath = Environment.getExternalStorageDirectory() + "/" + "Pictures/Screenshots/1.jpg";
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + mpath));

                /*final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);*/
                startActivity(Intent.createChooser(intent, "Choose an email client: "));
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SendOptions Page") // TODO: Define a title for the content shown.
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
