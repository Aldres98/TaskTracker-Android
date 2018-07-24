package com.example.aldres.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateTodoActivity extends AppCompatActivity {

    EditText todoText;
    ImageButton backButton;
    private ListView listView;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        todoText = findViewById(R.id.editText);
        mToolbar = findViewById(R.id.add_todo_toolbar);
        backButton = findViewById(R.id.back_button);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        listView = findViewById(R.id.projectsAvaible);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.projects_to_choose, intent.getStringArrayExtra(MainActivity.PROJECT_TITLES_ID));
        listView.setAdapter(adapter);
        listView.performItemClick(listView, 0, 0);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_todo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_todo:
                if (todoText.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Текст не может быть пуст", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObject params = new JsonObject();
                    params.addProperty("text", todoText.getText().toString());
                    params.addProperty("project_id", listView.getCheckedItemPosition() + 1);
                    params.addProperty("isCompleted", false);

                    Ion.with(this)
                            .load("https://radiant-island-23944.herokuapp.com/todo/create")
                            .setJsonObjectBody(params)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e != null)
                                        Log.e("ERROR", e.getMessage());
                                }
                            });

                    setResult(RESULT_OK);
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
