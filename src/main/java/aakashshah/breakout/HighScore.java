package aakashshah.breakout;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HighScore extends AppCompatActivity {

    ListView view;
    /*
        * Author: Rahul Sengupta
        * */
    //arraylist to hold the highscore values
    static ArrayList<HashMap<String,String>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        view = (ListView)findViewById(R.id.lv_hs);
        prepareData();
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.highscore_lv,new String[]{"name","score"},new int[]{R.id.name,R.id.score});
        view.setAdapter(adapter);
    }
    /*
            * Author: Rahul Sengupta
            * */
    static void prepareData()
    {
        list.clear();
        HashMap<String,String> map = new HashMap<>();
        map.put("name","NAME");
        map.put("score","SCORE");
        list.add(map);
        map = new HashMap<String,String>();
        map.put("name","Rahul");
        map.put("score","290");
        list.add(map);
        map = new HashMap<String,String>();
        map.put("name","Aakash");
        map.put("score","280");
        list.add(map);
        map = new HashMap<String,String>();
        map.put("name","Rahul");
        map.put("score","275");
        list.add(map);
    }
}
