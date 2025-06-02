package com.example.mad_final_project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


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

        textView.setText(taskText);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Add strikethrough
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                // Remove after delay
                new Handler().postDelayed(() -> {
                    taskListLayout.removeView(taskView);

                    // ✅ Delete from DB
                    notesDb.deleteNote(taskText);

                }, 2000);
            } else {
                // Remove strikethrough
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });

        taskListLayout.addView(taskView);
    }
}