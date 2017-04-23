/*=============================================================================
 |   Assignment:  CS6326 Project
 |    Author(s):  Rahul Sengupta & Aakash Shah
 |	  Net-ID(s):  rxs161630        axs165231
 |      Subject:  Human Computer Interactions
 |	   Due Date:  12/1/2016
 |
 +-----------------------------------------------------------------------------
 |
 |  Description:  Breakout game with sensors and power ups.
 |
 |    Algorithm:  -
 |
 |   Required Features Not Included:  -
 |
 |   Known Bugs:  -
 |
 *===========================================================================*/

package aakashshah.breakout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton button, vol, highscore;
    boolean isPlaying = true;
    MediaPlayer player;
    long position = 0;
    /*
            * Author: Rahul Sengupta
            * */
    //Override the oncreate of the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the orientation of the screen to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        button = (ImageButton) findViewById(R.id.play);
        vol = (ImageButton) findViewById(R.id.vol);
        highscore = (ImageButton) findViewById(R.id.button4);
        player = MediaPlayer.create(this, R.raw.pocket);

        //proceed to playing the game
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                position = player.getCurrentPosition();
                Intent intent = new Intent(MainActivity.this, Play.class);
                startActivity(intent);
            }
        });

        //view the highscore
        highscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                position = player.getCurrentPosition();
                Intent intent = new Intent(MainActivity.this, HighScore.class);
                startActivity(intent);
            }
        });

        //enable/disable the volume
        vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    player.stop();
                    isPlaying = false;
                    vol.setBackgroundResource(R.drawable.soundoff);
                } else {
                    player.seekTo((int) position);
                    player.start();
                    isPlaying = true;
                    vol.setBackgroundResource(R.drawable.soundon);
                }
            }
        });
    }
    /*
            * Author: Rahul Sengupta
            * */
    @Override
    protected void onResume() {
        super.onResume();
        player.start();
    }
    /*
            * Author: Rahul Sengupta
            * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }

    /*
        * Author: Rahul Sengupta
        * */
    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }
}
