package com.example.alialzein.myclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterInstructor extends AppCompatActivity {
 private Button register_instructor;
 private EditText name , email, password,getted_password;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    // int q=0;
    private ArrayList<String> instructor_pass_array=new ArrayList<>();
    private ArrayList<String> keys=new ArrayList<>();
    boolean correct_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_instructor);
        mAuth = FirebaseAuth.getInstance();
        register_instructor = (Button) findViewById(R.id.reg_inst_btn);
        name = (EditText) findViewById(R.id.Iname);
        email = (EditText) findViewById(R.id.Iemail);
        password = (EditText) findViewById(R.id.Ipassword);
        getted_password = (EditText) findViewById(R.id.getted_pass);
        loadingBar = new ProgressDialog(this);

        register_instructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String instructor_name = name.getText().toString();
                String instructor_email = email.getText().toString();
                String instructor_password= password.getText().toString();
                String instructor_getted_password = getted_password.getText().toString();
                Creat_instructor_account(instructor_name, instructor_email, instructor_password, instructor_getted_password);
            }

        });
    }

    private void Creat_instructor_account(final String instructor_name, final String instructor_email, final String instructor_password, final String instructor_getted_password) {
        if (TextUtils.isEmpty(instructor_name) || TextUtils.isEmpty(instructor_email) || TextUtils.isEmpty(instructor_password) || TextUtils.isEmpty(instructor_getted_password)) {
            Toast.makeText(RegisterInstructor.this,"You have Some Missing Fields",Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference getted_password = FirebaseDatabase.getInstance().getReference().child("Instructor_register_passwords");
            getted_password.keepSynced(true);
            getted_password.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String value = dataSnapshot.getValue(String.class);
                    instructor_pass_array.add(value);
                    String key = dataSnapshot.getKey();
                    keys.add(key);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    String value = dataSnapshot.getValue(String.class);
                    String key = dataSnapshot.getKey();
                    int index = keys.indexOf(key);
                    instructor_pass_array.set(index,value);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //to check if the password in the database
            for(int q=0;q<instructor_pass_array.size();q++){
                 Log.i("value", instructor_pass_array.get(q));
                //Log.i("value1", instructor_getted_password);
                if(instructor_pass_array.get(q).equals(instructor_getted_password)){
                    Log.i("correctpass", String.valueOf(correct_pass));
                    correct_pass = true;}


            }

            if (correct_pass) {
                loadingBar.setTitle("Creating New Account");
                loadingBar.setMessage("Please Wait, while we are creating account for you");
                loadingBar.show();

                mAuth.createUserWithEmailAndPassword(instructor_email, instructor_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String DeviceToken = FirebaseInstanceId.getInstance().getToken();
                            String id = mAuth.getCurrentUser().getUid();
                            DatabaseReference Current_user = FirebaseDatabase.getInstance().getReference().child("users").child(id);
                            DatabaseReference InstructorRef = FirebaseDatabase.getInstance().getReference().child("instructors").child(id);
                            Map user_info = new HashMap();
                            user_info.put("name", instructor_name);
                            user_info.put("password", instructor_password);
                            user_info.put("profile_image", "null");
                            user_info.put("thumb_profile_image", "null");
                            user_info.put("kind", "instructor");
                            user_info.put("email",instructor_email);
                            user_info.put("device_token",DeviceToken);
                            Current_user.setValue(user_info);
                            InstructorRef.setValue(user_info);

                            SavedSharedPreferences.setIsstudent(RegisterInstructor.this, "2");
                            SavedSharedPreferences.setEmail(RegisterInstructor.this, instructor_email);
                            SavedSharedPreferences.setPassword(RegisterInstructor.this, instructor_password);

                            Intent to_Instructor_board = new Intent(RegisterInstructor.this, Instructor_board.class);
                            to_Instructor_board.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(to_Instructor_board);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterInstructor.this, "You are already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(RegisterInstructor.this, "Some error occured please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        loadingBar.dismiss();
                    }
                });


            }else
            { Toast.makeText(RegisterInstructor.this, "You have entered a wrong password", Toast.LENGTH_SHORT).show();}

        }
    }
}
