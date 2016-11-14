package com.gsu.electronicpostcard;

import android.content.Context;

/**
 * Created by Jack on 11/13/2016.
 */

public class Model {
    private Model() {} // To prevent instantiation.

    static Context context;
    static PostCard currentPostCard;
    static int currentPage = 0;
    static PostCardElement selectedElement;
}
