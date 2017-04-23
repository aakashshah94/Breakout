package aakashshah.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Ball {


    public static float xVelocity;
    public static float yVelocity;
    public static float maxXVelocity = 200;
    public static float minXVelocity = -200;
    public static Bitmap ball_bitmap;
    RectF rect;
    float ballWidth = 10;
    float ballHeight = 10;

    /*
    * Author: Rahul Sengupta
    * */
    public Ball(Context con, int screenX, int screenY) {

        // Start the ball travelling straight up at 100 pixels per second
        xVelocity = 200;
        yVelocity = -400;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;

        ball_bitmap = BitmapFactory.decodeResource(con.getResources(), R.mipmap.ball, opt);
        Bitmap b = Bitmap.createScaledBitmap(ball_bitmap, 15, 15, false);
        ball_bitmap = b;

        // Place the ball in the centre of the screen at the bottom
        rect = new RectF();

    }

    /*
        * Author: Rahul Sengupta
        * */
    //return the bounds of the ball
/*
    * Author: Rahul Sengupta
    * */
    public RectF getRect() {
        return rect;
    }

    public void update(long fps) {
        rect.left = rect.left + (xVelocity / fps);
        rect.top = rect.top + (yVelocity / fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
    }

    /*
        * Author: Rahul Sengupta
        * */
    //reverse Y axis direction
    public void reverseYVelocity() {
        yVelocity = -yVelocity;
    }

    /*
        * Author: Rahul Sengupta
        * */
    //reverse X axis direction
    public void reverseXVelocity() {
        xVelocity = -xVelocity;
    }

    /*
        * Author: Rahul Sengupta
        * */
    //determine the X axis direction on ball intersect with the paddle
    public void setRandomXVelocity() {
        Random generator = new Random();
        int answer = generator.nextInt(2);
        if (answer == 0) {
            reverseXVelocity();
        }
    }

    /*
        * Author: Rahul Sengupta
        * */
    //clear obstacle
    public void clearObstacleY(float y) {
        rect.bottom = y;
        rect.top = y - ballHeight;
    }

    /*
        * Author: Rahul Sengupta
        * */
    //clear obstacle
    public void clearObstacleX(float x) {
        rect.left = x;
        rect.right = x + ballWidth;
    }

    /*
        * Author: Rahul Sengupta
        * */
    //reset the position of the ball
    public void reset(int x, int y) {
        rect.left = x / 2;
        rect.top = y - 20;
        rect.right = x / 2 + ballWidth;
        rect.bottom = y - 20 - ballHeight;
    }
}