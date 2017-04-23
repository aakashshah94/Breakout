package aakashshah.breakout;

import android.graphics.Color;
import android.graphics.RectF;
public class Power {
    int cr;
    RectF rect;
    int power_hight;
    int power_width;
    int yvelocity;
    boolean visibility;
    private int x;
    private int y;
    //initialize the power
    /*
                        * Author: Aakash Shah
                        * */
    public Power(String s, int xx, int yy) {
        if (s == "big") {
            cr = Color.MAGENTA;
        } else if (s == "short") {
            cr = Color.DKGRAY;
        } else {
            cr = Color.YELLOW;
        }
        power_hight = 30;
        power_width = 40;
        yvelocity = 200;
        visibility = true;
        rect = new RectF();
        x = xx;
        y = yy;
        rect.left = x;
        rect.top = y;
        rect.right = x + power_width;
        rect.bottom = y + power_hight;
    }
    /*
                        * Author: Aakash Shah
                        * */
    //get the bounds of the power
    public RectF getRect() {
        return rect;
    }
    /*
                            * Author: Aakash Shah
                            * */
    //update the coordinates of the power drop
    public void update(long fps) {
        rect.left = x;
        rect.top = rect.top + (yvelocity / fps);
        rect.right = rect.left + power_width;
        rect.bottom = rect.top - power_hight;
    }
}
