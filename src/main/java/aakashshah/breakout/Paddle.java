package aakashshah.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Paddle {
    public static Bitmap bitmap;
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    private RectF rect;
    private float length;
    private float height;
    private float x;
    private float y;
    private float paddleSpeed;
    // Is the paddle moving and in which direction
    private int paddleMoving = STOPPED;

    /*
    * Author: Aakash Shah
    * */
    //Initialize the paddle
    public Paddle(Context context, int screenX, int screenY) {
        length = 200;
        height = 4;

        // Start paddle in roughly the sceen centre
        x = screenX / 2;
        y = screenY - 20;

        rect = new RectF(x, y, x + length, y + height);

        // How fast is the paddle in pixels per second
        paddleSpeed = 600;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.paddle, opt);
        Bitmap b = Bitmap.createScaledBitmap(bitmap, 220, 70, false);
        bitmap = b;
    }
    /*
        * Author: Aakash Shah
        * */
    public RectF getRect() {
        return rect;
    }

    /*
    * Author: Rahul Sengupta
    * */
    // This method will be used to change/set if the paddle is going left, right or nowhere
    public void setMovementState(int state) {
        paddleMoving = state;
    }

    // This update method will be called from update in BreakoutView
    // It determines if the paddle needs to move and changes the coordinates
    // contained in rect if necessary
    /*
    * Author: Rahul Sengupta
    * */
    public void update(long fps) {
        if (paddleMoving == LEFT) {
            x = x - paddleSpeed / fps;
        }

        if (paddleMoving == RIGHT) {
            x = x + paddleSpeed / fps;
        }

        rect.left = x;
        rect.right = x + length;
    }
    /*
        * Author: Rahul Sengupta
        * */
    //reset the position of the paddle
    public void reset(int x, int y) {
        rect.top = y - 84;
        rect.bottom = y;
        rect.left = x / 2;
        rect.right = x / 2 + length;
    }

    /*
        * Author: Aakash Shah
        * */
    //update the length of the paddle based on the powerup
    public void updatelength(String s) {
        if (s == "big") {
            length = 300;
            if (bitmap.isMutable()) {
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 320, 70, false);
                bitmap = b;
            }
        } else {
            length = 100;
            Bitmap b = Bitmap.createScaledBitmap(bitmap, 120, 70, false);
            bitmap = b;
        }
    }
    /*
            * Author: Aakash Shah
            * */
    //return the paddle to its original size once power up expires
    public void originalSize() {
        length = 200;
        Bitmap b = Bitmap.createScaledBitmap(bitmap, 220, 70, false);
        bitmap = b;
    }
    /*
            * Author: Aakash Shah
            * */
    //return the left bound of the paddle
    public float returnLeft() {
        return rect.left;
    }
    /*
            * Author: Aakash Shah
            * */
    //return the right bound of the paddle
    public float returnRight() {
        return rect.right;
    }
}