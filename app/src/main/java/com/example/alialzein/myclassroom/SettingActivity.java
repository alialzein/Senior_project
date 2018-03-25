package com.example.alialzein.myclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {
    private CircleImageView settingProfileImage;
    private TextView settingUsename;
    private Button changeProfileImage;
    private FirebaseAuth mauth;
    private DatabaseReference userInfoReference;
    private final static int gallery_pick = 1;
    private StorageReference profileImgStorageRef;
    private StorageReference thumbImgStorageRef;
    Bitmap thumb_bitmap = null;
    private ProgressDialog loadingbar;

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
        thumbImgStorageRef = FirebaseStorage.getInstance().getReference().child("Thumb_Images");
        loadingbar = new ProgressDialog(this);

        userInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("Thumb_Profile_Image").getValue().toString();
                settingUsename.setText(name);
                if (!image.equals("null")) {
                    Picasso.with(SettingActivity.this).load(image).placeholder(R.drawable.default_profile_img).into(settingProfileImage);
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
                        .setAspectRatio(1, 1)
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
                loadingbar.setTitle("Updating profile image");
                loadingbar.setMessage("please wait while we updating your profile image");
                loadingbar.show();
                Uri resultUri = result.getUri();
                //for compress the image
                File thumbFilePathUri = new File(resultUri.getPath());
                try {
                    thumb_bitmap = new Compressor(this).
                            setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .compressToBitmap(thumbFilePathUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                String userId = mauth.getCurrentUser().getUid();//get user id
                StorageReference imgPath = profileImgStorageRef.child(userId + ".jpg");//image file path in database
                final StorageReference thumbImgPath = thumbImgStorageRef.child(userId + ".jpg");//thumbimage file path in database
                imgPath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Storing Your Image...", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumbImgPath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumbDownloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                    if (task.isSuccessful()) {
                                        Map updateUserData = new HashMap();
                                        updateUserData.put("Profile_Image", downloadUrl);
                                        updateUserData.put("Thumb_Profile_Image", thumbDownloadUrl);

                                        userInfoReference.updateChildren(updateUserData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SettingActivity.this, "image uploaded successfully", Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();
                                            }
                                        });
                                    }


                                }
                            });

                        } else {
                            Toast.makeText(SettingActivity.this, "Error occurred during saving your image", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
