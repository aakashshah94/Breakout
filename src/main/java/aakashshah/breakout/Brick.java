package aakashshah.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Brick {
    public Bitmap brick_bitmap;
    public boolean isVisible;
    public int alphaValue;
    public int numberOfTaps;
    public Paint brickColor;
    boolean power_short = false;
    boolean power_big = false;
    boolean power_sensora = false;
    private RectF rect;

    /*
    * Author: Aakash Shah
    * */
    //initialize the brick
    public Brick(Context context, int row, int column, int width, int height, int type, int alphaVal) {
        alphaValue = alphaVal;
        isVisible = true;
        brickColor = new Paint();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        //decide brick bitmap based on the random incoming type
        if (type == 0) {
            brick_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick_blue, opt);
        } else if (type == 1) {
            brick_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick_green, opt);
        } else if (type == 2) {
            brick_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick_red, opt);
        } else if (type == 3) {
            brick_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick_pink, opt);
        } else if (type == 4) {
            brick_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick_orange, opt);
        } else if (type == 5) {
            brick_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick_yellow, opt);
        } else if (type == 6) {
            brick_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick_voilet, opt);
        }
        //scale the bitmap
        Bitmap b = Bitmap.createScaledBitmap(brick_bitmap, width, height, false);
        brick_bitmap = b;
        Random rand = new Random();

        //determine the number of taps either 1 or 2 based on probablity
        boolean val = rand.nextInt(30) == 0;
        if (val) {
            numberOfTaps = 2;
        } else {
            numberOfTaps = 1;
            int userKiKismat = rand.nextInt(50);
            if (userKiKismat >= 0 && userKiKismat <= 4) {
                power_big = true;
            } else if (userKiKismat >= 20 && userKiKismat <= 24)
                power_short = true;
            else if (userKiKismat >= 45 && userKiKismat <= 49) {
                power_sensora = true;
            }
        }
        int padding = 1;
        rect = new RectF(column * width,
                row * height + padding,
                column * width + width,
                row * height + height - padding);
    }

    /*
        * Author: Aakash Shah
        * */
    //retrieve the brick bounds
    public RectF getRect() {
        return this.rect;
    }

    /*
        * Author: Aakash Shah
        * */
    //make the brick invisible on intersection with the ball
    public void setInvisible() {
        isVisible = false;
    }

    /*
        * Author: Aakash Shah
        * */
    //retrieve the visiblity of the brick
    public boolean getVisibility() {
        return isVisible;
    }
}