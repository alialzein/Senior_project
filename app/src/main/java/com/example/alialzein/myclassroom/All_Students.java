package com.example.alialzein.myclassroom;

import android.content.Context;

/**
 * Created by ALiAlzein on 3/28/2018.
 */

public class All_Students {
    public String email;
    public String name;
    public String thumb_profile_image;

    public All_Students(String email, String name, String thumb_profile_image) {
        this.email = email;
        this.name = name;
        this.thumb_profile_image = thumb_profile_image;
    }

    public All_Students() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_profile_image() {
        return thumb_profile_image;
    }

    public void setThumb_profile_image(Context ctx,String thumb_profile_image) {
        this.thumb_profile_image = thumb_profile_image;
    }
}
