package com.blueweabo.trashcanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;
    private List<Float> percentages = new ArrayList<>();
    private List<ProgressBar> trashCanProgresses = new ArrayList<>();
    private List<TextView> trashCans = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trash_can_activity);
        openDatabase();
        addValueListener();
    }

    public void openDatabase() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void addValueListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinearLayout layout = findViewById(R.id.line);
                percentages.clear();
                int i = 0;
                for (DataSnapshot childSnapshot:snapshot.child("smartbin").getChildren()) {
                    if (i >= trashCans.size()) {
                        ProgressBar progressBar = new ProgressBar(layout.getContext(),
                                null,
                                android.R.attr.progressBarStyleHorizontal);
                        progressBar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        progressBar.setMax(100);
                        progressBar.setScaleY(10f);
                        layout.addView(progressBar, LinearLayout.LayoutParams);
                        trashCanProgresses.add(progressBar);
                    }
                    float filledAmount = childSnapshot.getValue(Float.class);
                    trashCanProgresses.get(i++).setProgress((int)Math.floor(filledAmount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database.addValueEventListener(listener);
    }
}