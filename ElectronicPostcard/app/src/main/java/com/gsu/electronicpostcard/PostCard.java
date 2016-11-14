package com.gsu.electronicpostcard;

import java.io.Serializable;

/**
 * Created by Jack on 11/11/2016.
 */

public class PostCard  implements Serializable {
    PostCardPage[] pages = new PostCardPage[4];

    public PostCard() {
        for (int i = 0; i < pages.length; i++) {
            pages[i] = new PostCardPage();
        }
    }
}
