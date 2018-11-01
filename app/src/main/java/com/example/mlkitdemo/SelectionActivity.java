package com.example.mlkitdemo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectionActivity extends AppCompatActivity {

    private MaterialButton btnTextRecognition, btnImageLabeling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        btnImageLabeling = findViewById(R.id.btn_image_labeling);
        btnTextRecognition = findViewById(R.id.ban_text_recognition);

        btnTextRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionActivity.this, MainActivity.class));
            }
        });
        btnImageLabeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionActivity.this, ImageLabelingActivity.class));
            }
        });
    }
}
