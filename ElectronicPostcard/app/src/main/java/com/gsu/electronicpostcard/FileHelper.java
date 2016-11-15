package com.gsu.electronicpostcard;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import static java.io.File.separator;

/**
 * Created by Donny on 11/15/16.
 */

public class FileHelper {
    public static String getPostcardSerializePath(){
        String path = Environment.getExternalStorageDirectory().getPath() + separator + "postcard_serialize";
        File f = new File(path);
        if (f.exists()){
            Log.v("FileHelper", "isDirectory");
        } else {
            // create the folder
            f.mkdirs();

        }

        return path;
    }
}
