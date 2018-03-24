package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alialzein.myclassroom.Fragments.InstructorClassroomFragment;
import com.example.alialzein.myclassroom.Fragments.InstructorQuizFragment;
import com.example.alialzein.myclassroom.Fragments.InstructorWallFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Instructor_board extends AppCompatActivity {
    private FirebaseAuth auth;
    private ViewPager myViewPager;
    private TabLayout myTabeLayout;
    private AppBarLayout myAppBarLayout;
    private TabPagerAdapter myTabPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_board);
        auth=FirebaseAuth.getInstance();
        myAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        myTabeLayout = (TabLayout) findViewById(R.id.main_tabs_instructor);
        myViewPager = (ViewPager) findViewById(R.id.viewpager_instructor);

        myTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

        myTabPagerAdapter.addFragment(new InstructorWallFragment(),"Wall");
        myTabPagerAdapter.addFragment(new InstructorClassroomFragment(),"Classroom");
        myTabPagerAdapter.addFragment(new InstructorQuizFragment(),"Quiz");
        myViewPager.setAdapter(myTabPagerAdapter);

        myTabeLayout.setupWithViewPager(myViewPager);
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            LogoutUser();
        }
    }

    private void LogoutUser() {

        SavedSharedPreferences.setIsstudent(Instructor_board.this, "");
        SavedSharedPreferences.setEmail(Instructor_board.this, "");
        SavedSharedPreferences.setPassword(Instructor_board.this, "");

        Intent StartPageIntent = new Intent(Instructor_board.this, StartPageActivity.class);
        StartPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//to not allow the user to press back to this activity
        startActivity(StartPageIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout_btn) {
            auth.signOut();
            LogoutUser();
        }
        if (item.getItemId() == R.id.account_settings) {
            Intent toSetting=new Intent(Instructor_board.this, SettingActivity.class);
            startActivity(toSetting);
        }
        return true;
    }
}
