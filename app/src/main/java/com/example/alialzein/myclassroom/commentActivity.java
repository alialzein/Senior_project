package com.example.alialzein.myclassroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

public class commentActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        setTitle("Comments");


        detector = new SimpleGestureFilter(this,this);



    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN :
                finish();
                overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);
                str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDoubleTap() {

        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.no_change,R.anim.slide_down_info);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
