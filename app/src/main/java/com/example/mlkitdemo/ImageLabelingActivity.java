package com.example.mlkitdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;

import java.util.List;

public class ImageLabelingActivity extends AppCompatActivity {

    private static final String TAG = ImageLabelingActivity.class.getSimpleName();
    private MaterialButton btnTakePicture, btnImageLabeling;
    private AppCompatImageView ivImageCapture;
    private AppCompatTextView tvImageLabel;

    private final int CAMERA_PERMISSION_REQUEST = 10;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_labeling);

        btnImageLabeling = findViewById(R.id.btn_image_labeling);
        btnTakePicture = findViewById(R.id.btn_take_picture);

        ivImageCapture = findViewById(R.id.iv_image_capture);

        tvImageLabel = findViewById(R.id.tv_image_label);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PERMISSION_REQUEST);
            }
        });

        btnImageLabeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                FirebaseVisionLabelDetectorOptions options =
//                        new FirebaseVisionLabelDetectorOptions.Builder()
//                                .setConfidenceThreshold(0.8f)
//                                .build();

                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

                FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                        .getVisionLabelDetector();

                Task<List<FirebaseVisionLabel>> result =
                        detector.detectInImage(image)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                            @Override
                                            public void onSuccess(List<FirebaseVisionLabel> labels) {
                                                // Task completed successfully
                                                // ...
                                                String text = "";
                                                for (FirebaseVisionLabel label : labels) {
                                                    text += label.getLabel() + " , ";
                                                    String entityId = label.getEntityId();
                                                    float confidence = label.getConfidence();
                                                    Log.e(TAG, "onSuccess: " + text);
                                                }
                                                tvImageLabel.setText(text);
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ImageLabelingActivity.this, "Fail to recognize text", Toast.LENGTH_SHORT).show();
                                            }
                                        });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PERMISSION_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ivImageCapture.setImageBitmap(imageBitmap);
        } else {
            Toast.makeText(this, "Something wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}
