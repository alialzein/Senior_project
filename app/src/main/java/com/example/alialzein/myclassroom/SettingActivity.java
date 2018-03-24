package com.example.alialzein.myclassroom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
     private CircleImageView settingProfileImage;
     private TextView settingUsename;
    private Button changeProfileImage;
    private FirebaseAuth mauth;
    private DatabaseReference userInfoReference;
    private final static int gallery_pick=1;
    private StorageReference profileImgStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingProfileImage = (CircleImageView) findViewById(R.id.Setting_profile_img);
        settingUsename = (TextView) findViewById(R.id.setting_username);
        changeProfileImage = (Button) findViewById(R.id.changeProfileImage);
        mauth = FirebaseAuth.getInstance();
        String userId = mauth.getCurrentUser().getUid();
        userInfoReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        profileImgStorageRef = FirebaseStorage.getInstance().getReference().child("Profile_Images");

        userInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("Profile_Image").getValue().toString();
                settingUsename.setText(name);
                if(!image.equals("null")){
                Picasso.with(SettingActivity.this).load(image).into(settingProfileImage);
            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //from library of crop image and select image,automatically get the result in onActivityResult
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String userId = mauth.getCurrentUser().getUid();//get user id
                StorageReference imgPath = profileImgStorageRef.child(userId+".jpg");//image file path in database
                imgPath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Storing Your Image...", Toast.LENGTH_SHORT).show();
                            String downloadUrl = task.getResult().getDownloadUrl().toString();
                            userInfoReference.child("Profile_Image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SettingActivity.this, "image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(SettingActivity.this, "Error occurred during saving your image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
