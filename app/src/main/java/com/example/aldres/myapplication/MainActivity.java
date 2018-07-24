package com.example.aldres.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private MyAdapter mAdapter;
    private List<Project> projects = new ArrayList<Project>();
    private ListView listView;
    public static final String PROJECT_TITLES_ID = "PROJECT_TITLES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton actionButton = findViewById(R.id.fab);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        updateProjects();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTodoActivity.class);
                String[] projectTitles = new String[projects.size()];
                for (int i = 0; i < projectTitles.length ; i++) {
                    projectTitles[i] = projects.get(i).getTitle();
                }
                intent.putExtra(PROJECT_TITLES_ID, projectTitles);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateProjects();
    }

    private void updateProjects(){
        Ion.with(this).load(getString(R.string.get_projects_json)).asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if(result!=null){
                            projects.clear();
                            for (final JsonElement projectJsonElement : result) {
                                projects.add(new Gson().fromJson(projectJsonElement, Project.class));
                            }
                            Ion.with(MainActivity.this).load(getString(R.string.get_todos_json)).asJsonArray()
                                    .setCallback(new FutureCallback<JsonArray>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonArray result) {
                                            if(result!=null){
                                                for (final JsonElement todoJsonElement : result) {
                                                    int projId = todoJsonElement.getAsJsonObject().get("project_id").getAsInt() - 1;
                                                    projects.get(projId).addNewTodo(new Gson().fromJson(todoJsonElement, Todo.class));
                                                }

                                                listView = findViewById(R.id.projectsView);
                                                mAdapter = new MyAdapter(MainActivity.this, projects);
                                                listView.setAdapter(mAdapter);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}

