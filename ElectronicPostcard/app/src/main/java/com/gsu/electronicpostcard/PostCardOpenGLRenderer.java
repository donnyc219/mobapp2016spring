package com.gsu.electronicpostcard;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jack on 11/15/2016.
 */

public class PostCardOpenGLRenderer implements GLSurfaceView.Renderer {
    private float vertices[][] = new float[4][12];
    private float texture[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };
    FloatBuffer vertexBuffer[] = new FloatBuffer[4];
    FloatBuffer textureBuffer;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        for (PostCardPage page : Model.currentPostCard.pages) {
            page.drawBoundingBox = false;
            page.render();
            page.drawBoundingBox = true;
        }
        vertices[0][0] = 0; vertices[0][1] = 1.41375f; vertices[0][2] = 0;
        vertices[0][3] = -1f; vertices[0][4] = 1.41375f; vertices[0][5] = 0;
        vertices[0][6] = 0; vertices[0][7] = 0; vertices[0][8] = 0;
        vertices[0][9] = -1f; vertices[0][10] = 0; vertices[0][11] = 0;

        vertices[1][0] = -1f; vertices[1][1] = 1.41375f; vertices[1][2] = 0;
        vertices[1][3] = 0; vertices[1][4] = 1.41375f; vertices[1][5] = 0;
        vertices[1][6] = -1f; vertices[1][7] = 0; vertices[1][8] = 0;
        vertices[1][9] = 0; vertices[1][10] = 0; vertices[1][11] = 0;

        vertices[2][0] = 0; vertices[2][1] = 1.41375f; vertices[2][2] = 0;
        vertices[2][3] = 1f; vertices[2][4] = 1.41375f; vertices[2][5] = 0;
        vertices[2][6] = 0; vertices[2][7] = 0; vertices[2][8] = 0;
        vertices[2][9] = 1f; vertices[2][10] = 0; vertices[2][11] = 0;

        vertices[3][0] = 1f; vertices[3][1] = 1.41375f; vertices[3][2] = 0;
        vertices[3][3] = 0; vertices[3][4] = 1.41375f; vertices[3][5] = 0;
        vertices[3][6] = 1f; vertices[3][7] = 0; vertices[3][8] = 0;
        vertices[3][9] = 0; vertices[3][10] = 0; vertices[3][11] = 0;

        for (int i = 0; i < vertices.length; i++) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices[i].length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertexBuffer[i] = byteBuffer.asFloatBuffer();
            vertexBuffer[i].put(vertices[i]);
            vertexBuffer[i].position(0);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
