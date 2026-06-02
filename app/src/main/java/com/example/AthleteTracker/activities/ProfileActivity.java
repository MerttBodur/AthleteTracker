package com.example.AthleteTracker.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.AthleteTracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    String[] sports = {"Soccer", "Basketball", "Volleyball", "Tackle Football"};
    String[] experiences = {"0-1 Years", "1-3 Years", "3-5 Years", "5+ Years"};

    HashMap<String, String[]> positionMap = new HashMap<>();
    String selectedSport = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        positionMap.put("Soccer", new String[]{"Goalkeeper", "Centre Back", "Left-Right Back", "Central Defensive Midfielder", "Central Midfielder", "Central Attacking Midfielder", "Winger", "Centre Forward"});
        positionMap.put("Basketball", new String[]{"Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center"});
        positionMap.put("Volleyball", new String[]{"Setter", "Hitter", "Libero", "Defensive Specialist"});
        positionMap.put("Tackle Football", new String[]{"Quarterback", "Running Back", "Wide Receiver", "Offensive Linemen", "Tight End", "H-Back", "Defensive Linemen", "Linebacker", "Defensive Back"});

        Button btnSport = findViewById(R.id.btn_sport);
        Button btnPosition = findViewById(R.id.btn_position);
        Button btnExperience = findViewById(R.id.btn_experience);

        btnSport.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Select Sport")
                    .setItems(sports, (dialog, which) -> {
                        selectedSport = sports[which];
                        btnSport.setText(selectedSport);
                        btnPosition.setText("POSITION");
                    })
                    .show();
        });

        btnPosition.setOnClickListener(v -> {
            if (selectedSport.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setMessage("Please select a sport first")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            String[] positions = positionMap.get(selectedSport);
            new AlertDialog.Builder(this)
                    .setTitle("Select Position")
                    .setItems(positions, (dialog, which) -> {
                        btnPosition.setText(positions[which]);
                    })
                    .show();
        });

        btnExperience.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Select Experience")
                    .setItems(experiences, (dialog, which) -> {
                        btnExperience.setText(experiences[which]);
                    })
                    .show();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                return true;
            } else if (id == R.id.nav_workout) {
                startActivity(new Intent(this, WorkoutActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_calculations) {
                startActivity(new Intent(this, CalculationsActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
