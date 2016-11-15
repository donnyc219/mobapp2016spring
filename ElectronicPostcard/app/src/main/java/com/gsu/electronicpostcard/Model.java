package com.gsu.electronicpostcard;

import android.content.Context;
import android.graphics.Paint;

/**
 * Created by Jack on 11/13/2016.
 */

public class Model {
    private Model() {} // To prevent instantiation.
    static Paint paint = new Paint();
    static Context context;
    static PostCard currentPostCard;
    static int currentPage = 0;
    static PostCardElement selectedElement;
}
