package com.oakspro.flipshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static  int SPLASH=6000;
    Animation topAnim, bottomAnim;
    ImageView image1, image2;
    TextView title, caption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set ids

        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        title=findViewById(R.id.title);
        caption=findViewById(R.id.caption);

        //animation
        topAnim= AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim=AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        title.setAnimation(topAnim);
        image1.setAnimation(topAnim);

        image2.setAnimation(bottomAnim);
        caption.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH);



    }


}