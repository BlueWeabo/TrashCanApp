package com.blueweabo.trashcanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.BoringLayout;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference database;
    private final List<ProgressBar> trashCanProgresses = new ArrayList<>();
    private final List<TextView> trashCans = new ArrayList<>();
    private final List<Marker> trashCanLocations = new ArrayList<>();
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        TextView name = findViewById(R.id.loginNameTittle);
        name.setText("Username");
        TextView pass = findViewById(R.id.loginPasswordTittle);
        pass.setText("Password");
        Button btn = findViewById(R.id.loginButton);
        btn.setOnClickListener(view -> {



            successfulLogin();
        });
    }

    protected void successfulLogin() {
        setContentView(R.layout.trash_can_activity);
        openDatabase();
        addValueListener();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams map = findViewById(R.id.map).getLayoutParams();
        map.height = metrics.heightPixels * 60 / 100;
        ViewGroup.LayoutParams trashInfo = findViewById(R.id.line).getLayoutParams();
        trashInfo.height = metrics.heightPixels * 40 / 100;
    }

    public void openDatabase() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void addValueListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RelativeLayout layout = findViewById(R.id.line);
                int i = 0;
                for (DataSnapshot childSnapshot:snapshot.child("smartbin").getChildren()) {
                    Bin bin = Objects.requireNonNull(childSnapshot.getValue(Bin.class));
                    if (i >= trashCans.size()) {
                        ProgressBar progressBar = new ProgressBar(layout.getContext(),
                                null,
                                android.R.attr.progressBarStyleHorizontal);
                        progressBar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        progressBar.setMax(100);
                        progressBar.setScaleY(6f);
                        progressBar.setSecondaryProgress(100);
                        progressBar.setSecondaryProgressTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                        layout.addView(progressBar, new LinearLayout.LayoutParams(layout.getWidth(), 100));
                        RelativeLayout.LayoutParams progressParam = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
                        progressParam.height = 60;
                        progressParam.width = layout.getWidth();
                        progressParam.topMargin = 90 * i;
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(50, 155, 50)));
                        TextView text = new TextView(layout.getContext());
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        text.setWidth(layout.getWidth());
                        text.setTextColor(Color.rgb(255, 255, 255));
                        layout.addView(text);
                        RelativeLayout.LayoutParams textParam = (RelativeLayout.LayoutParams) text.getLayoutParams();
                        textParam.height = 60;
                        textParam.width = layout.getWidth();
                        textParam.topMargin = 90 * i;
                        LatLng binLoc = new LatLng(bin.getLocation().getLatitude(), bin.getLocation().getLongitude());
                        MarkerOptions markerOpt = new MarkerOptions().position(binLoc)
                                .title(String.format("%.2f", bin.getPercentage()) + "% filled");
                        Marker marker = map.addMarker(markerOpt);
                        trashCanLocations.add(marker);
                        trashCanProgresses.add(progressBar);
                        trashCans.add(text);
                    }
                    trashCanProgresses.get(i).setProgress((int)Math.floor(bin.getPercentage()));
                    trashCans.get(i).setText(String.format("%.2f", bin.getPercentage()) + "% filled");
                    trashCanLocations.get(i).setTitle(String.format("%.2f", bin.getPercentage()) + "% filled");
                    trashCanLocations.get(i++).setPosition(new LatLng(bin.getLocation().getLatitude(), bin.getLocation().getLongitude()));
                }
                while (trashCans.size() > i) {
                    View trash = trashCans.remove(trashCans.size() - 1);
                    layout.removeView(trash);
                    View prog = trashCanProgresses.remove(trashCanProgresses.size() - 1);
                    layout.removeView(prog);
                    Marker mark = trashCanLocations.remove(trashCanLocations.size() - 1);
                    mark.remove();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database.addValueEventListener(listener);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        map = googleMap;
    }
}