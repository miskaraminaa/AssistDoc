package ma.ensa.www.assistdoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import java.util.Timer;
import java.util.TimerTask;


public class Home extends AppCompatActivity {
Timer t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i1 = new Intent(Home.this, IntroActivity.class);
                startActivity(i1);
                Log.d("homeactivity","Home gone");
                finish();
            }
        },2000);
    }
}
