package com.example.aldres.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aldres on 19.07.2018.
 */

public class Project {

    private String title;
    private List<Todo> todos = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public void addNewTodo(Todo todo){
        this.todos.add(todo);
    }
}
