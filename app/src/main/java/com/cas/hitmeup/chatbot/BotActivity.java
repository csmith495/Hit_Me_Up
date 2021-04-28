package com.cas.hitmeup.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cas.hitmeup.R;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;

public class BotActivity extends AppCompatActivity {

    ImageView mic;
    TextView userTestTV, agentTestTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);

        mic = findViewById(R.id.imgMic);
        userTestTV = findViewById(R.id.tvOne);
        agentTestTV = findViewById(R.id.tvTwo);

        final AIConfiguration config = new AIConfiguration("990a807fb9e87c06430fcfe62949792baa9f7105",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        AIService aiService = AIService.getService(getApplicationContext(), config);
        aiService.setListener(new AIListener() {
            @Override
            public void onResult(AIResponse result) {
                agentTestTV.setText(result.getResult().getFulfillment().getSpeech());
                userTestTV.setText(result.getResult().getResolvedQuery());
            }

            @Override
            public void onError(AIError error) {
                mic.setImageResource(R.drawable.mic23);
            }

            @Override
            public void onAudioLevel(float level) {

            }

            @Override
            public void onListeningStarted() {
                mic.setImageResource(R.drawable.mic1);
            }

            @Override
            public void onListeningCanceled() {
                mic.setImageResource(R.drawable.mic23);
            }

            @Override
            public void onListeningFinished() {
                mic.setImageResource(R.drawable.mic23);
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();
            }
        });

    }
}