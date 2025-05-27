package com.example.mad_final_project;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout taskListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskListLayout = findViewById(R.id.taskListLayout);

        findViewById(R.id.btnMic).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VoiceInputActivity.class);
            startActivityForResult(intent, 123);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            String newTask = data.getStringExtra("new_task");
            if (newTask != null && !newTask.isEmpty()) {
                // Add task to list and update adapter
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

                // Remove task after 3 seconds
                new Handler().postDelayed(() -> {
                    taskListLayout.removeView(taskView);
                }, 3000);
            } else {
                // Remove strikethrough if unchecked
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });

        taskListLayout.addView(taskView);
    }
}