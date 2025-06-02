package com.example.mad_final_project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_final_project.NotesDb;

public class MainActivity extends AppCompatActivity {

    private LinearLayout taskListLayout;
    private NotesDb notesDb;  // ✅ Database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskListLayout = findViewById(R.id.taskListLayout);
        notesDb = new NotesDb(this);  // ✅ Initialize database

        Button btnBored = findViewById(R.id.btnBored);
        btnBored.setOnClickListener(v -> {
            new FetchBoredActivityTask().execute("https://www.boredapi.com/api/activity");
        });

        // ✅ Load saved tasks from database
        Cursor cursor = notesDb.getAllNotes();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndexOrThrow(NotesDb.COLUMN_CONTENT));
                addTaskToList(content);
            }
            cursor.close();
        }

        // ✅ Set up mic button
        findViewById(R.id.btnMic).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VoiceInputActivity.class);
            startActivityForResult(intent, 123); // or startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            String newTask = data.getStringExtra("new_task");
            if (newTask != null && !newTask.isEmpty()) {
                // ✅ Save to DB
                long timestamp = System.currentTimeMillis();
                notesDb.insertNote(newTask, timestamp);

                // ✅ Add to UI
                addTaskToList(newTask);
            }
        }
    }

    private void addTaskToList(String taskText) {
        View taskView = getLayoutInflater().inflate(R.layout.item_task, null);
        CheckBox checkBox = taskView.findViewById(R.id.checkboxTask);
        TextView textView = taskView.findViewById(R.id.textViewTask);
        ImageButton btnSearch = taskView.findViewById(R.id.btnSearchTask);  // new

        textView.setText(taskText);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                new Handler().postDelayed(() -> {
                    taskListLayout.removeView(taskView);
                    notesDb.deleteNote(taskText);
                }, 2000);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });








        // ✅ Implicit Intent to search task on Google
        btnSearch.setOnClickListener(v -> {
            String query = Uri.encode(taskText);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + query));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "No browser app found to perform web search", Toast.LENGTH_SHORT).show();
            }
        });

        taskListLayout.addView(taskView);
    }

    private class FetchBoredActivityTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                return jsonObject.getString("activity");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String activity) {
            if (activity != null) {
                notesDb.insertNote(activity, System.currentTimeMillis());
                addTaskToList(activity);
                Toast.makeText(MainActivity.this, "New suggestion added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch activity", Toast.LENGTH_SHORT).show();
            }
        }
    }


}