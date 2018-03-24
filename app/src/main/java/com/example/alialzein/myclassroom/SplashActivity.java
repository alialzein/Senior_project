package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    int isStudent = 0;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SavedSharedPreferences.getIsstudent(SplashActivity.this) == null){
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, StartPageActivity.class));
                    SplashActivity.this.finish();
                }else if (SavedSharedPreferences.getIsstudent(SplashActivity.this).equals("1")){
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, Student_board.class));
                    SplashActivity.this.finish();
                }else {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, Instructor_board.class));
                    SplashActivity.this.finish();
                }
            }
        },2000);

    }
}
