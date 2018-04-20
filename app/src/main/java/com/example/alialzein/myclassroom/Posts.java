package com.example.alialzein.myclassroom;

public class Posts {
    private String message;
    private boolean seen;
    private long time;
    private String type;
    private String email;
    private String classroom_id;
    private String post_push_id;

    public Posts() {

    }

    public Posts(String message, boolean seen, long time, String type,String email,String classroom_id,String post_push_id) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.type = type;
        this.email = email;
        this.classroom_id = classroom_id;
        this.post_push_id = post_push_id;
    }

    public String getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(String classroom_id) {
        this.classroom_id = classroom_id;
    }

    public String getPost_push_id() {
        return post_push_id;
    }

    public void setPost_push_id(String post_push_id) {
        this.post_push_id = post_push_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
