package aakashshah.breakout;

import java.util.Random;

public class Star {
    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    /*
                        * Author: Rahul Sengupta
                        * */
    //initialize the individual star
    public Star(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(10);
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }
    /*
                            * Author: Rahul Sengupta
                            * */
    //update the co-ordinates of the star from right to left
    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;
        if (x < 0) {
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }
    /*
                            * Author: Rahul Sengupta
                            * */
    //return the start width
    public float getStarWidth() {
        float minX = 1.0f;
        float maxX = 4.0f;
        Random rand = new Random();
        float finalX = rand.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }
    /*
                            * Author: Rahul Sengupta
                            * */
    //return the x co-ordinate of the star
    public int getX() {
        return x;
    }
    /*
                            * Author: Rahul Sengupta
                            * */
    //return the y co-ordinate of the star
    public int getY() {
        return y;
    }
}
