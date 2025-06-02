package com.example.mad_final_project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceInputActivity extends AppCompatActivity {
    private ImageButton btnMicStop; // New stop button
    private boolean isRecording = false;
    private EditText etRecognizedText;
    private ImageButton btnMicStart;
    private Button btnDone;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_input);

        etRecognizedText = findViewById(R.id.etRecognizedText);
        btnMicStart = findViewById(R.id.btnMicStart);
        btnMicStop = findViewById(R.id.btnMicStop); // find view
        btnDone = findViewById(R.id.btnDone);

        // Check audio permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
                Toast.makeText(VoiceInputActivity.this, "Error recognizing speech", Toast.LENGTH_SHORT).show();
                stopListeningUI();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && matches.size() > 0) {
                    etRecognizedText.setText(matches.get(0));
                }
                stopListeningUI(); // reset UI
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        btnMicStart.setOnClickListener(v -> {
            if (!isRecording) {
                isRecording = true;
                speechRecognizer.startListening(speechRecognizerIntent);
                btnMicStart.setVisibility(View.GONE);
                btnMicStop.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
            }
        });

        btnMicStop.setOnClickListener(v -> {
            if (isRecording) {
                isRecording = false;
                speechRecognizer.stopListening();
                stopListeningUI();
            }
        });

        btnDone.setOnClickListener(v -> {
            String task = etRecognizedText.getText().toString().trim();
            if (!task.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_task", task);
                setResult(RESULT_OK, resultIntent);
            }
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private void stopListeningUI() {
        btnMicStart.setVisibility(View.VISIBLE);
        btnMicStop.setVisibility(View.GONE);
        isRecording = false;
    }
}