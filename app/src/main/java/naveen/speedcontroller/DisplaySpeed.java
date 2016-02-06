package naveen.speedcontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class DisplaySpeed extends AppCompatActivity {
    Button startButton,stopButton;
    SessionSharedPrefs session;
    Timer mytimer;
    TimerTask task;
    TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_speed);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        session = new SessionSharedPrefs(this);
        startButton = (Button)findViewById(R.id.startButton);
        stopButton = (Button)findViewById(R.id.stopButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        distance = (TextView)findViewById(R.id.distance);
        distance.setText("Distance: "+session.getDistance_total());
        if(session.getServicevariable()==1){
            stopButton.setEnabled(false);
        }else if(session.getServicevariable()==2){
            startButton.setEnabled(false);
            startTimer();
        }
    }

    public void startTimer(){
        Log.i("distance","start timer");
        if (mytimer!=null){
            return;
        }
        mytimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        distance.setText("Distance: "+session.getDistance_total());
                    }
                });

            }
        };
        mytimer.schedule(task,0,10000);
    }

    public void startCheckingSpeed(View view) {
        Intent intent = new Intent(this,SpeedUpdatingService.class);
        startService(intent);
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        session.setServicevariable(2);
        startTimer();
    }

    public void stopCheckingSpeed(View view) {
        Intent intent = new Intent(this,SpeedUpdatingService.class);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        stopService(intent);
        session.setServicevariable(1);
        if (mytimer!=null){
            mytimer.cancel();
            mytimer=null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mytimer!=null){
            mytimer.cancel();
            mytimer=null;
        }
    }
}
