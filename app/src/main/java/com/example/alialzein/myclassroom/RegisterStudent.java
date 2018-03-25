package com.example.alialzein.myclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterStudent extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private Button register;
    private EditText name , email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        mAuth = FirebaseAuth.getInstance();
        register = (Button) findViewById(R.id.reg_std_btn);
        name = (EditText) findViewById(R.id.Sname);
        email = (EditText) findViewById(R.id.Semail);
        password = (EditText) findViewById(R.id.Spassword);
        loadingBar = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = name.getText().toString();
                String Useremail = email.getText().toString();
                String Userpass = password.getText().toString();
                CreateAccount(Username, Useremail, Userpass);
            }
        });



    }

    private void CreateAccount(final String username, final String useremail, final String userpass) {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(useremail) || TextUtils.isEmpty(userpass)) {
            Toast.makeText(RegisterStudent.this,"You have Some Missing Fields",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please Wait, while we are creating account for you");
            loadingBar.show();
        mAuth.createUserWithEmailAndPassword(useremail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                 String id=   mAuth.getCurrentUser().getUid();
                    DatabaseReference Current_user = FirebaseDatabase.getInstance().getReference().child("users").child(id);
                    Map user_info = new HashMap();
                    user_info.put("name", username);
                    user_info.put("password", userpass);
                    user_info.put("Profile_Image", "null");
                    user_info.put("Thumb_Profile_Image", "null");
                    user_info.put("kind","student");
                    Current_user.setValue(user_info);

                    SavedSharedPreferences.setIsstudent(RegisterStudent.this, "1");
                    SavedSharedPreferences.setEmail(RegisterStudent.this, useremail);
                    SavedSharedPreferences.setPassword(RegisterStudent.this, userpass);


                    Intent to_Student_board = new Intent(RegisterStudent.this, Student_board.class);
                    to_Student_board.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(to_Student_board);
                    finish();
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(RegisterStudent.this, "You are already registered", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(RegisterStudent.this, "Some error occured please try again", Toast.LENGTH_SHORT).show();
                    }
                }
                loadingBar.dismiss();
            }
        });



        }




    }
}
