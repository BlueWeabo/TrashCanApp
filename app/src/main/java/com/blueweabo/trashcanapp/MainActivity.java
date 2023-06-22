package com.blueweabo.trashcanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        updateTextViews(R.id.loginTittle, R.string.login);
        updateTextViews(R.id.loginNameTittle, R.string.username);
        updateTextViews(R.id.loginPasswordTittle, R.string.password);
        updateButtonText(R.id.loginButton, R.string.login);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            setContentView(R.layout.trash_can_activity);
            openDatabase();
            addValueListener();
        });
    }

    protected void updateTextViews(int viewId, int textId) {
        TextView view = findViewById(viewId);
        view.setText(textId);
    }

    protected void updateButtonText(int buttonId, int textId) {
        Button button = findViewById(buttonId);
        button.setText(textId);
    }

    public void openDatabase() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void addValueListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TrashCan trashCan = snapshot.getValue(TrashCan.class);
                if (trashCan == null) return;
                TextView text = findViewById(R.id.trashCan1View);
                text.setText(String.valueOf(trashCan.percentageFilled));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database.addValueEventListener(listener);
    }
}