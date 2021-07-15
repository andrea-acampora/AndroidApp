package com.example.next2me.utils;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class FaceDetector {


    FaceDetectorOptions realTimeOpts;
    com.google.mlkit.vision.face.FaceDetector detector;

    public FaceDetector() {
        realTimeOpts = new FaceDetectorOptions.Builder()
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .build();
        detector = FaceDetection.getClient();

    }

    public boolean isFaceDetected(Bitmap imagebtm){
        InputImage image = InputImage.fromBitmap(imagebtm,0);
        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                faces -> {
                                   //setFaceDetected(faces.size() > 0);
                                   //Log.d("face", "faccia dec = " + (faces.size() > 0));
                                })
                        .addOnFailureListener(
                                e -> { });
        return result.getResult().size() > 0;
    }

    /*public boolean isFaceDetected(){
        return faceDetected;
    }

    private void setFaceDetected(boolean detected){
        faceDetected = detected;
        Log.d("face", "var globale = " + faceDetected);
    }*/
}
