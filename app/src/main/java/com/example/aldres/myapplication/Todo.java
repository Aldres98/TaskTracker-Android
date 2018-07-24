package com.example.aldres.myapplication;

/**
 * Created by Aldres on 19.07.2018.
 */

public class Todo {

    private int position;
    private int id;
    private String text;
    private boolean isCompleted;



    public Todo(String text, boolean isCompleted) {
        this.text = text;
        this.isCompleted = isCompleted;
        this.position = -1;
        this.id = -1;
    }

    public Todo(String text) {
        this.text = text;
        this.isCompleted = false;
        this.position = -1;
        this.id = -1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
