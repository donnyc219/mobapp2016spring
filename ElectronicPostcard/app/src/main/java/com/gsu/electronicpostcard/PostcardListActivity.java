package com.gsu.electronicpostcard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import static com.gsu.electronicpostcard.Model.context;

public class PostcardListActivity extends AppCompatActivity {

    // Adaptor
    public class PostcardListViewAdaptor extends ArrayAdapter<String> {

        Context mContext;
        ArrayList<String> mDataSource;

        public PostcardListViewAdaptor(Context context, ArrayList<String> objects) {
            super(context, R.layout.postcard_listview_item, objects);

            mContext = context;
            mDataSource = objects;
        }

        public int getCount()
        {
            return mDataSource.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

            if (convertView==null){
                // create view
                convertView = inflater.inflate(R.layout.postcard_listview_item, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.textView2);
            String filename = mDataSource.get(position);

            String[] parts = filename.split("\\.");

            tv.setText(parts[0]);

            return convertView;
        }
    }

    ArrayList<String> dataSource;
    PostcardListViewAdaptor adaptor;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard_list);


        String filename = FileHelper.getPostcardSerializePath();
        dataSource = getListFiles(new File(filename));

        adaptor = new PostcardListViewAdaptor(getApplicationContext(), dataSource);
        listView = (ListView)findViewById(R.id.postcard_listview);
        listView.setAdapter(adaptor);

        // onClickListener for items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // when an item is clicked
//                String str = dataSource.get(i);
                // grab postcard, deserialize file
                itemClicked(i);
            }

        });

    }

    final public void itemClicked(int position){
//        Log.v("aaa", "ddd" + mdata);
        String filename = dataSource.get(position);
        Model.currentPostCard = readSerializePostcard(filename);
        Intent i = new Intent(this, ViewCardActivity.class);
        startActivity(i);
    }

    public void onAddNewPostcard(View view){
//        Model.currentPostCard = new PostCard();
//        serializePostcard(Model.currentPostCard);
//        readSerializePostcard();

        askFilename();

//        Intent i = new Intent(this, TemplateSelectionActivity.class);
//        startActivity(i);
    }

    public void serializePostcard(PostCard p, String filename){

//        String filename = "testFilemost.ptc";
        filename += ".ptc";

        ObjectOutput out = null;

        // get the path to save the serialized file
        String sdcardPath = FileHelper.getPostcardSerializePath() + "/" + filename;

        try {
            out = new ObjectOutputStream(new FileOutputStream(sdcardPath));
            out.writeObject(p);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("FileNotFoundException", "FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("IOException", "IOException");
        }

    }

    public PostCard readSerializePostcard(String filename){
        ObjectInputStream input;
//        String filename = "testFilemost.ptc";
        filename += ".ptc";
        String sdcardPath = FileHelper.getPostcardSerializePath() + "/" + filename;
        PostCard p = null;

        try {
            input = new ObjectInputStream(new FileInputStream(new File(sdcardPath)));
            p = (PostCard) input.readObject();

            input.close();

        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (p==null){

            Log.v("p", "null");
        } else {

            Log.v("p", "not null");
        }

        return p;
    }


    public void askFilename(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input a file name");


        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filename = input.getText().toString();

                goBackToActivity(filename);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void goBackToActivity(String filename){
        Model.currentPostCard = new PostCard();
        Model.currentPostCard.name = filename;
        serializePostcard(Model.currentPostCard, filename);

        // go toactivity
        Intent i = new Intent(this, TemplateSelectionActivity.class);
        startActivity(i);

        if (dataSource==null){
            dataSource = new ArrayList<String>();
        }

        dataSource.add(filename);
        adaptor.notifyDataSetChanged();
        Log.v("count", "" + dataSource.size());
    }

    private ArrayList<String> getListFiles(File parentDir) {
        ArrayList<String> inFiles = new ArrayList<String>();
        File[] files = parentDir.listFiles();
        if (files == null) return inFiles; // Empty list
        for (File file : files) {
//            if (file.isDirectory()) {
//                inFiles.addAll(getListFiles(file));
//            } else {
//                if(file.getName().endsWith(".ptc")){
//                    inFiles.add(file);
//                }
//            }
            if(file.getName().endsWith(".ptc")){
                String str = file.getName();
//                str.split(".");
//                file.getName().split(".")[0];
                inFiles.add(file.getName());
            }
        }
        return inFiles;
    }

}
