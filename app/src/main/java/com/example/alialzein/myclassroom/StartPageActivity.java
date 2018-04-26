package com.example.alialzein.myclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class StartPageActivity extends AppCompatActivity {
    private Button login_btn, register_student_btn, register_instructor_btn;
    private EditText EditText_email, EditText_password;
    int isStudent = 0;
    private FirebaseAuth auth;
    String uid = null;
    private ProgressDialog loadingBar;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        login_btn = (Button) findViewById(R.id.login_btn);
        register_instructor_btn = (Button) findViewById(R.id.register_instructur_btn);
        register_student_btn = (Button) findViewById(R.id.register_student_btn);
        EditText_email = (EditText) findViewById(R.id.login_email);
        EditText_password = (EditText) findViewById(R.id.login_password);
        loadingBar = new ProgressDialog(this);
        usersRef = FirebaseDatabase.getInstance().getReference().
                child("users");


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setTitle("Logging In...");
                loadingBar.setMessage("Please Wait, while we are checking your information");
                loadingBar.show();

                 final String email = EditText_email.getText().toString();
                 final String password = EditText_password.getText().toString();
             //   Log.i("email", email);
                final ArrayList<String> kind = new ArrayList<>();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(StartPageActivity.this, "You have some missing fields", Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = auth.getCurrentUser();
                                uid = currentUser.getUid();
                                //to get the id of the device, each device has unique id
                                String DeviceToken = FirebaseInstanceId.getInstance().getToken();
                                usersRef.child(uid).child("device_token").setValue(DeviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                                DatabaseReference Current_user_info = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("kind");
                                Current_user_info.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String value = dataSnapshot.getValue(String.class);
                                        //  Log.i("value", value);

                                        kind.add(value);
                                        Log.i("kind", kind.get(0));
                                        if (kind.get(0).equals("student")) {
                                            isStudent = 1;
                                        }
                                        if (isStudent == 1) {
                                            SavedSharedPreferences.setIsstudent(StartPageActivity.this, "1");
                                            SavedSharedPreferences.setEmail(StartPageActivity.this, email);
                                            SavedSharedPreferences.setPassword(StartPageActivity.this, password);

                                            Intent Student_board = new Intent(StartPageActivity.this, Student_board.class);
                                            Student_board.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//to not allow the user to press back to this activity
                                            startActivity(Student_board);
                                            finish();
                                        } else {
                                            SavedSharedPreferences.setIsstudent(StartPageActivity.this, "2");
                                            SavedSharedPreferences.setEmail(StartPageActivity.this, email);
                                            SavedSharedPreferences.setPassword(StartPageActivity.this, password);

                                            Intent Instructor_board = new Intent(StartPageActivity.this, Instructor_board.class);
                                            Instructor_board.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//to not allow the user to press back to this activity
                                            startActivity(Instructor_board);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            loadingBar.dismiss();
                        }
                    });
                }
            }
        });


        register_instructor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegister_instructor = new Intent(StartPageActivity.this, RegisterInstructor.class);
                startActivity(toRegister_instructor);
            }
        });

        register_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegister_student = new Intent(StartPageActivity.this, RegisterStudent.class);
                startActivity(toRegister_student);
            }
        });
    }

}
