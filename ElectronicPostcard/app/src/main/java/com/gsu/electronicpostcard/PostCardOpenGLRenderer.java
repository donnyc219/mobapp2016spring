package com.gsu.electronicpostcard;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class PostCardOpenGLRenderer implements GLSurfaceView.Renderer {
    final private float CARD_ANGLE = 40f;
    final private float DISTANCE = 2.8f;
    final private float DECELERATION = .12f;
    float rotationX = 30;
    float rotationY = 0;
    float rotationYVelocity = 0;
    float fov = 60;
    private int globalWidth = 1, globalHeight = 1;
    private float currentFOV = 60;
    private float vertices[][] = new float[4][12];
    private float texture[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };
    private int[] textureName = new int[4];
    private FloatBuffer vertexBuffer[] = new FloatBuffer[4];
    private FloatBuffer textureBuffer;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);

        vertices[0][0] = 0; vertices[0][1] = 0.706875f; vertices[0][2] = 0;
        vertices[0][3] = -1f; vertices[0][4] = 0.706875f; vertices[0][5] = 0;
        vertices[0][6] = 0; vertices[0][7] = -0.706875f; vertices[0][8] = 0;
        vertices[0][9] = -1f; vertices[0][10] = -0.706875f; vertices[0][11] = 0;

        vertices[1][0] = -1f; vertices[1][1] = 0.706875f; vertices[1][2] = 0;
        vertices[1][3] = 0; vertices[1][4] = 0.706875f; vertices[1][5] = 0;
        vertices[1][6] = -1f; vertices[1][7] = -0.706875f; vertices[1][8] = 0;
        vertices[1][9] = 0; vertices[1][10] = -0.706875f; vertices[1][11] = 0;

        vertices[2][0] = 0; vertices[2][1] = 0.706875f; vertices[2][2] = 0;
        vertices[2][3] = 1f; vertices[2][4] = 0.706875f; vertices[2][5] = 0;
        vertices[2][6] = 0; vertices[2][7] = -0.706875f; vertices[2][8] = 0;
        vertices[2][9] = 1f; vertices[2][10] = -0.706875f; vertices[2][11] = 0;

        vertices[3][0] = 1f; vertices[3][1] = 0.706875f; vertices[3][2] = 0;
        vertices[3][3] = 0; vertices[3][4] = 0.706875f; vertices[3][5] = 0;
        vertices[3][6] = 1f; vertices[3][7] = -0.706875f; vertices[3][8] = 0;
        vertices[3][9] = 0; vertices[3][10] = -0.706875f; vertices[3][11] = 0;

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

        gl.glGenTextures(4, textureName, 0);
        for (int i = 0; i < 4; i++) {
            PostCardPage page = Model.currentPostCard.pages[i];
            page.drawBoundingBox = false;
            page.render();
            page.drawBoundingBox = true;
            createTexture(gl, page.bitmap.bitmap, i);
        }

        rotationYVelocity = -(float) Math.sqrt(2 * DECELERATION * 360); // For giggles
    }

    private void createTexture(GL10 gl, Bitmap bitmap, int textureIndex) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName[textureIndex]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        globalWidth = width;
        globalHeight = height;
        changeFocalLength(gl);
    }

    private void drawPage(GL10 gl, int index) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName[index]);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glFrontFace(GL10.GL_CW);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer[index]);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices[index].length / 3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    private void changeFocalLength(GL10 gl) {
        gl.glViewport(0, 0, globalWidth, globalHeight);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, currentFOV, (float) globalWidth / (float) globalHeight, 0.1f, 100);
        gl.glViewport(0, 0, globalWidth, globalHeight);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (fov != currentFOV) {
            currentFOV = fov;
            changeFocalLength(gl);
        }
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -DISTANCE);
        gl.glRotatef(rotationX, 1, 0, 0);
        gl.glRotatef(rotationY, 0, 1, 0);
        gl.glTranslatef(0, 0, -.2f);
        gl.glPushMatrix();
        gl.glRotatef(CARD_ANGLE, 0, 1, 0);
        drawPage(gl, 0);
        drawPage(gl, 1);
        gl.glPopMatrix();
        gl.glRotatef(-CARD_ANGLE, 0, 1, 0);
        drawPage(gl, 2);
        drawPage(gl, 3);
        rotationY += rotationYVelocity;
        if (rotationYVelocity < -DECELERATION || rotationYVelocity > DECELERATION) {
            rotationYVelocity -= DECELERATION * rotationYVelocity / Math.abs(rotationYVelocity);
        } else {
            rotationYVelocity = 0;
        }
        rotationY = rotationY % 360;
        rotationX = rotationX % 360;
    }
}
