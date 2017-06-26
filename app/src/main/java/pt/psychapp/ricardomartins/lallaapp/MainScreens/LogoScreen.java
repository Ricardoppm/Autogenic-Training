package pt.psychapp.ricardomartins.lallaapp.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ricardomartins.lallaapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LogoScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_name_screen);


        final Handler mhandler = new Handler();
        new Thread( new Thread() {
            @Override
            public void run() {
                // This is the delay
                try{
                    this.sleep(1000*3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getBaseContext(),EntryScreen.class);
                        startActivity(i);
                    }
                });

            }
        }).start();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
