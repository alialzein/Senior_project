package com.example.alialzein.myclassroom;

public class commentsClass {
    private String message;
    private String name;
    private String sender_id;
    private long time;

    public commentsClass() {
    }

    public commentsClass(String message, String name, String sender_id, long time) {
        this.message = message;
        this.name = name;
        this.sender_id = sender_id;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
