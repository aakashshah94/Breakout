package aakashshah.breakout;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Random;

public class Play extends AppCompatActivity implements SensorEventListener {
    public boolean sensorenable = false;
    MediaPlayer player;
    BreakoutView breakoutView;
    private SensorManager manager;
    private Sensor accelerometer;
    private float last_y;

    /*
            * Author: Aakash Shah
            * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView and set it as the view
        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);
        player = MediaPlayer.create(this, R.raw.ingame);
        player.start();
        //set the orientation of the screen to landscape
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //initialize the sensors
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
            * Author: Aakash Shah
            * */
    //grab the sensor event
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensorenable) {
            Sensor mySensor = event.sensor;
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                if (y > last_y && Ball.xVelocity + 50 <= Ball.maxXVelocity) {
                    Ball.xVelocity += 50;
                } else if (y < last_y && Math.abs(Ball.xVelocity - 50) <= Math.abs(Ball.minXVelocity)) {
                    Ball.xVelocity += -50;
                }
                last_y = y;
            }
        }
    }

    /*
            * Author: Aakash Shah
            * */
    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();
        // Tell the gameView resume method to execute
        breakoutView.resume();
        player.start();
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
            * Author: Aakash Shah
            * */
    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        breakoutView.pause();
        manager.unregisterListener(this);
        player.pause();
    }

    /*
            * Author: Aakash Shah
            * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }

    //the inner class which is responsible for the game screen
    class BreakoutView extends SurfaceView implements Runnable {

        Bitmap play, pause, image;
        // This is our thread
        Thread gameThread = null;
        GestureDetector gd;
        boolean power;
        int powerdrop_x;
        int powerdrop_y;
        int numpowers;
        SurfaceHolder ourHolder;
        volatile boolean playing;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        long fps;
        int screenX;
        int screenY;
        Paddle paddle;
        Ball ball;
        Brick[] bricks = new Brick[200];
        int numBricks = 0;
        SoundPool soundPool;
        int beep1ID = -1;
        int beep2ID = -1;
        int beep3ID = -1;
        int loseLifeID = -1;
        int explodeID = -1;
        float padx;
        int pady;
        float padx_ball, pady_ball;
        int score = 0;
        int lives = 3;
        Power ps[] = new Power[200];
        LinkedList<Power> pw = new LinkedList<>();
        int num = 0;
        int numBricks2 = 0;
        private ArrayList<Star> stars = new
                ArrayList<Star>();
        private long timeThisFrame;

        /*
                * Author: Aakash Shah
                * */
        public BreakoutView(Context context) {
            super(context);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inMutable = true;
            play = BitmapFactory.decodeResource(context.getResources(), R.drawable.play, opt);
            pause = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause, opt);
            Bitmap b = Bitmap.createScaledBitmap(play, 150, 150, false);
            play = b;
            b = Bitmap.createScaledBitmap(pause, 150, 150, false);
            pause = b;
            image = pause;
            gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (e.getX() > screenX / 11 * 9 && e.getY() < screenY / 2) {
                        if (playing == true) {
                            pause();
                            image = play;
                            draw();
                        } else {
                            if (lives != 0)
                                onResume();
                            image = pause;
                            draw();
                        }
                    }
                    return true;
                }
            });

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Get a Display object to access screen details
            Display display = getWindowManager().getDefaultDisplay();
            // Load the resolution into a Point object
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;
            int starnums = 100;
            for (int i = 0; i < starnums; i++) {
                Star s = new Star(screenX, screenY);
                stars.add(s);
            }
            paddle = new Paddle(context, screenX, screenY);
            //Initialize a ball
            ball = new Ball(context, screenX, screenY);
            padx = screenX / 2;
            pady = screenY - 20;
            padx_ball = screenX / 2;
            pady_ball = screenY - 20;
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            try {
                // Create objects of the 2 required classes
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;
                // Load our fx in memory ready for use
                descriptor = assetManager.openFd("beep1.ogg");
                beep1ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep2.ogg");
                beep2ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep3.ogg");
                beep3ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("loseLife.ogg");
                loseLifeID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("explode.ogg");
                explodeID = soundPool.load(descriptor, 0);

            } catch (IOException e) {
                Log.e("error", "failed to load sound files");
            }
            createBricks();
            createBricksAndRestart();
        }

        /*
                * Author: Rahul Sengupta
                * */
        //create bricks on random
        public void createBricks() {
            Random gen = new Random();
            int brickWidth = screenX / 11;
            int brickHeight = screenY / 15;
            int type;
            numBricks = 0;
            for (int column = 0; column < 9; column++) {
                for (int row = 0; row < 6; row++) {
                    type = gen.nextInt(7);
                    bricks[numBricks] = new Brick(this.getContext(), row, column, brickWidth, brickHeight, type, 255);
                    boolean vis = gen.nextInt(6) == 0;
                    if (vis) {
                        bricks[numBricks].isVisible = false;
                    } else {
                        numBricks2++;
                    }
                    numBricks++;
                }
            }
        }

        /*
                        * Author: Rahul Sengupta
                        * */
        //reload the game
        public void createBricksAndRestart() {
            ball.reset(screenX, screenY);
            paddle.reset(screenX, screenY);
            padx = paddle.getRect().left;
            padx_ball = ball.getRect().left;
            pady_ball = ball.getRect().top;
            pw.clear();
            paused = true;
            sensorenable = false;
            // if game over reset scores and lives
            if (lives == 0) {
                // Build a wall of bricks
                playing = false;
            }
        }

        /*
                        * Author: Rahul Sengupta
                        * */
        //the thread function running the game
        @Override
        public void run() {
            while (playing) {
                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();
                // Update the frame
                if (!paused) {
                    update();
                }
                // Draw the frame
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }

        /*
                        * Author: Aakash Shah
                        * */
        //update and check for collisions,movement etc.
        public void update() {
            //draw the stars in the background
            for (Star s : stars) {
                s.update(1);
            }
            num++;
            paddle.update(fps);
            ball.update(fps);
            pady_ball = ball.getRect().top;
            padx_ball = ball.getRect().left;
            padx = paddle.getRect().left;
            // Check for ball colliding with a brick
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                        if (bricks[i].numberOfTaps == 2) {
                            Paint bpaint = new Paint();
                            bpaint.setAlpha(120);
                            bricks[i].brickColor = bpaint;
                            bricks[i].numberOfTaps = 1;
                            ball.reverseYVelocity();
                            score = score + 10;
                            soundPool.play(explodeID, 1, 1, 0, 0, 1);
                        } else {
                            bricks[i].setInvisible();
                            ball.reverseYVelocity();
                            score = score + 10;
                            soundPool.play(explodeID, 1, 1, 0, 0, 1);
                            numBricks2--;
                        }
                        if (bricks[i].power_big) {
                            power = true;
                            powerdrop_x = (int) bricks[i].getRect().centerX();
                            powerdrop_y = (int) bricks[i].getRect().bottom;
                            Power pow = new Power("big", powerdrop_x, powerdrop_y);
                            pw.add(pow);
                        } else if (bricks[i].power_short) {
                            power = true;
                            powerdrop_x = (int) bricks[i].getRect().centerX();
                            powerdrop_y = (int) bricks[i].getRect().bottom;
                            Power pow = new Power("short", powerdrop_x, powerdrop_y);
                            pw.add(pow);
                        } else if (bricks[i].power_sensora) {
                            powerdrop_x = (int) bricks[i].getRect().centerX();
                            powerdrop_y = (int) bricks[i].getRect().bottom;
                            Power pow = new Power("sensi", powerdrop_x, powerdrop_y);
                            pw.add(pow);
                        }
                    }
                }
            }
            //check if user grabs power
            if (pw.size() > 0 && num > 1) {
                int j;
                for (j = 0; j < pw.size(); j++) {
                    pw.get(j).update(fps);
                    final int cr = pw.get(j).cr;
                    if (RectF.intersects(paddle.getRect(), pw.get(j).getRect())) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CountDownTimer(6500, 1000) {
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    public void onFinish() {
                                        if (cr == Color.YELLOW) {
                                            sensorenable = false;

                                        } else {
                                            paddle.originalSize();
                                        }
                                    }

                                }.start();
                            }
                        });
                        soundPool.play(beep1ID, 1, 1, 0, 0, 1);

                        if (pw.get(j).cr == Color.MAGENTA) {
                            paddle.updatelength("big");

                        } else if (pw.get(j).cr == Color.DKGRAY)
                            paddle.updatelength("short");
                        else
                            sensorenable = true;
                        pw.remove(j);
                    } else if (pw.get(j).getRect().bottom > screenY) {
                        pw.remove(j);
                        soundPool.play(beep1ID, 1, 1, 0, 0, 1);
                    }

                }
            }
            // Check for ball colliding with paddle
            if (RectF.intersects(paddle.getRect(), ball.getRect())) {
                ball.setRandomXVelocity();
                ball.reverseYVelocity();
                ball.clearObstacleY(paddle.getRect().top - 2);
                soundPool.play(beep1ID, 1, 1, 0, 0, 1);
            }
            // Bounce the ball back when it hits the bottom of screen
            if (ball.getRect().bottom > screenY) {
                // Lose a life
                lives--;
                paddle.originalSize();
                soundPool.play(loseLifeID, 1, 1, 0, 0, 1);
                createBricksAndRestart();
            }
            // Bounce the ball back when it hits the top of screen
            if (ball.getRect().top < 0) {
                ball.reverseYVelocity();
                ball.clearObstacleY(12);
                soundPool.play(beep2ID, 1, 1, 0, 0, 1);
            }
            // If the ball hits left wall bounce
            if (ball.getRect().left < 0) {
                ball.reverseXVelocity();
                ball.clearObstacleX(2);
                soundPool.play(beep3ID, 1, 1, 0, 0, 1);
            }
            // If the ball hits right wall bounce
            if (ball.getRect().right > screenX / 11 * 9) {
                ball.reverseXVelocity();
                ball.clearObstacleX(screenX / 11 * 9 - 12);
                soundPool.play(beep3ID, 1, 1, 0, 0, 1);
            }
            // Pause if cleared screen
            if (score == numBricks * 10) {
                paused = true;
                createBricksAndRestart();
            }
        }

        /*
                        * Author: Aakash Shah
                        * */
        // Draw the newly updated scene
        public void draw() {
            int c1, c2, c3, c4;
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.BLACK);
                paint.setColor(Color.WHITE);
                //drawing all stars
                for (Star s : stars) {
                    paint.setStrokeWidth(s.getStarWidth());
                    canvas.drawPoint(s.getX(), s.getY(), paint);
                }
                canvas.drawBitmap(image, screenX / 11 * 9 + 90, screenY / 6, new Paint());
                // Draw the ball
                if (paused) {
                    canvas.drawBitmap(ball.ball_bitmap, padx_ball + 50, pady_ball - 50, new Paint());
                } else {
                    paint.setColor(Color.CYAN);
                    canvas.drawBitmap(ball.ball_bitmap, padx_ball, pady_ball - 10, new Paint());
                }
                paint.setColor(Color.argb(255, 255, 255, 255));
                // Draw the paddle
                canvas.drawBitmap(Paddle.bitmap, padx - 10, pady - 40, new Paint());
                paint.setColor(Color.YELLOW);
                canvas.drawLine(screenX / 11 * 9, screenY, screenX / 11 * 9 + 2, 0, paint);
                // Change the brush color for drawing
                if (pw.size() > 0) {
                    int i;
                    for (i = 0; i < pw.size(); i++) {
                        paint.setColor(pw.get(i).cr);
                        canvas.drawRect(pw.get(i).getRect(), paint);
                    }
                }
                // Draw the bricks if visible
                for (int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        Paint brickPaint = new Paint();
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(bricks[i].getRect(), brickPaint);
                        canvas.drawBitmap(bricks[i].brick_bitmap, bricks[i].getRect().left, bricks[i].getRect().top, bricks[i].brickColor);
                    }
                }
                // Choose the brush color for drawing
                paint.setColor(Color.argb(193, 193, 193, 255));
                // Draw the score and lives
                paint.setTextSize(90);
                canvas.drawText("Score", screenX / 11 * 9 + 40, screenY / 2 + 20, paint);
                canvas.drawText("" + score, screenX / 11 * 9 + 100, screenY / 2 + 120, paint);
                canvas.drawText("Lives", screenX / 11 * 9 + 40, screenY / 2 + 240, paint);
                canvas.drawText("" + lives, screenX / 11 * 9 + 100, screenY / 2 + 340, paint);
                if (numBricks2 == 0) {
                    paint.setTextSize(70);
                    canvas.drawText("Congratulations!! You won the game", screenX / 3, screenY / 2, paint);
                }
                // Has the player lost?
                if (lives <= 0) {
                    paint.setTextSize(70);
                    canvas.drawText("Better luck next time :)", screenX / 3, screenY / 2, paint);
                }
                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        /*
                        * Author: Rahul Sengupa
                        * */
        //pause the game
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

        /*
                                * Author: Rahul Sengupa
                                * */
        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        /*
                                * Author: Rahul Sengupa
                                * */
        //register the touches
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            float leftXValue = paddle.returnLeft();
            float rightXValue = paddle.returnRight();
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    if (paused == true) {
                        ball.reverseYVelocity();
                    }
                    paused = false;
                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:
                    paddle.setMovementState(paddle.STOPPED);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float currentX = motionEvent.getX();
                    // going backwards: pushing stuff to the right
                    if (currentX > paddle.returnRight() && paddle.returnRight() < screenX / 11 * 9) {
                        paddle.setMovementState(paddle.RIGHT);
                    } else if (currentX > paddle.returnRight() && paddle.returnRight() > screenX / 11 * 9) {
                        paddle.setMovementState(paddle.STOPPED);
                    } else if (currentX < paddle.returnLeft()) {
                        paddle.setMovementState(paddle.LEFT);
                    } else {
                        paddle.setMovementState(paddle.STOPPED);
                    }
                    break;
            }
            gd.onTouchEvent(motionEvent);
            return true;
        }
    }
}