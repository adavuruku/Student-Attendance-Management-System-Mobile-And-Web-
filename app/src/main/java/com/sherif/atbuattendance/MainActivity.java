package com.sherif.atbuattendance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView txt, txt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScaleInTopAnimator animator = new ScaleInTopAnimator();
        img = (ImageView) findViewById(R.id.app);
        txt = (TextView) findViewById(R.id.name);
        txt2 = (TextView) findViewById(R.id.ee);
        boolean goingDown = false;
        AnimationUtils.imagegroup(img,goingDown);
        AnimationUtils.animate(txt,goingDown);
        AnimationUtils.animate(txt2,goingDown);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startProg();
            }
        },2000);
    }

    public void startProg(){
        //retrieve content saved in preference - politicsData
        //load login
       // clearImage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openLogin();
            }
        },1000);
    }

    public void openLogin(){
        Intent intent = new Intent (this,login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    public void clearImage(){
        try {
            boolean goingDown =true;
            AnimationUtils.imagegroup(img,goingDown);
            AnimationUtils.animate(txt,goingDown);
            AnimationUtils.animate(txt2,goingDown);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
