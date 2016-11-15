package com.gsu.electronicpostcard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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

            return convertView;
        }
    }

    ArrayList<String> dataSource;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard_list);

        dataSource = new ArrayList<String>();
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");
        dataSource.add("hello");

        PostcardListViewAdaptor adaptor = new PostcardListViewAdaptor(getApplicationContext(), dataSource);
        listView = (ListView)findViewById(R.id.postcard_listview);
        listView.setAdapter(adaptor);

        // onClickListener for items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // when an item is clicked
                Log.v("aaa", "ddd");
            }
        });

    }

    public void onAddNewPostcard(View view){
        Model.currentPostCard = new PostCard();
        serializePostcard(Model.currentPostCard);
        Intent i = new Intent(this, TemplateSelectionActivity.class);
        startActivity(i);
    }

    public void serializePostcard(PostCard p){

        String filename = "testFilemost.srl";

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



}
