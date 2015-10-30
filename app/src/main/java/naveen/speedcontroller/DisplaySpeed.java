package naveen.speedcontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DisplaySpeed extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button startButton,stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_speed);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pref = getSharedPreferences("naveen.speedcontroller", MODE_PRIVATE);
        editor = pref.edit();
        startButton = (Button)findViewById(R.id.startButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        if(pref.getInt("servicevariable",1)==1){
            stopButton.setEnabled(false);
        }else if(pref.getInt("servicevariable",1)==2){
            startButton.setEnabled(false);
        }

    }

    public void startCheckingSpeed(View view) {
        Intent intent = new Intent(this,SpeedUpdatingService.class);
        startService(intent);
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        editor.putInt("servicevariable", 2);
        editor.commit();


    }

    public void stopCheckingSpeed(View view) {
        Intent intent = new Intent(this,SpeedUpdatingService.class);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        stopService(intent);
        editor.putInt("servicevariable",1);
        editor.commit();
    }
}
