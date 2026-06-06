package com.example.AthleteTracker.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.AthleteTracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    String[] sports = {"Soccer", "Basketball", "Volleyball", "Tackle Football"};
    String[] experiences = {"0-1 Years", "1-3 Years", "3-5 Years", "5+ Years"};
    HashMap<String, String[]> positionMap = new HashMap<>();
    String selectedSport = "";

    EditText etName, etWeight, etBodyfat, etHeight;
    Button btnSport, btnPosition, btnExperience, btnSave;
    TextView tvWeightValue, tvBodyfatValue, tvFfmiValue;
    ImageView btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.et_name);
        etWeight = findViewById(R.id.et_weight);
        etBodyfat = findViewById(R.id.et_bodyfat);
        etHeight = findViewById(R.id.et_height);
        btnSport = findViewById(R.id.btn_sport);
        btnPosition = findViewById(R.id.btn_position);
        btnExperience = findViewById(R.id.btn_experience);
        btnSave = findViewById(R.id.btn_save);
        btnSettings = findViewById(R.id.btn_settings);
        tvWeightValue = findViewById(R.id.tv_weight_value);
        tvBodyfatValue = findViewById(R.id.tv_bodyfat_value);
        tvFfmiValue = findViewById(R.id.tv_ffmi_value);

        positionMap.put("Soccer", new String[]{"Goalkeeper", "Centre Back", "Left-Right Back", "Central Defensive Midfielder", "Central Midfielder", "Central Attacking Midfielder", "Winger", "Centre Forward"});
        positionMap.put("Basketball", new String[]{"Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center"});
        positionMap.put("Volleyball", new String[]{"Setter", "Hitter", "Libero", "Defensive Specialist"});
        positionMap.put("Tackle Football", new String[]{"Quarterback", "Running Back", "Wide Receiver", "Offensive Linemen", "Tight End", "H-Back", "Defensive Linemen", "Linebacker", "Defensive Back"});

        loadProfile();

        setupSportButton();
        setupPositionButton();
        setupExperienceButton();
        setupSaveButton();
        setupSettingsButton();

        setupBottomNav();
    }

    private void loadProfile() {
        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        etName.setText(sp.getString("name", ""));
        btnSport.setText(sp.getString("sport", "SPORT"));
        btnPosition.setText(sp.getString("position", "POSITION"));
        btnExperience.setText(sp.getString("experience", "EXPERIENCE"));
        etWeight.setText(sp.getString("weight", ""));
        etBodyfat.setText(sp.getString("bodyFat", ""));
        etHeight.setText(sp.getString("height", ""));

        selectedSport = sp.getString("sport", "");

        float savedFfmi = sp.getFloat("ffmi", 0);
        if (savedFfmi > 0) {
            tvWeightValue.setText(sp.getString("weight", "--"));
            tvBodyfatValue.setText(sp.getString("bodyFat", "--"));
            tvFfmiValue.setText(String.format("%.1f", savedFfmi));
        }
    }

    private void setupSportButton() {
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
    }

    private void setupPositionButton() {
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
    }

    private void setupExperienceButton() {
        btnExperience.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Select Experience")
                    .setItems(experiences, (dialog, which) -> {
                        btnExperience.setText(experiences[which]);
                    })
                    .show();
        });
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {
            String weightStr = etWeight.getText().toString();
            String bodyfatStr = etBodyfat.getText().toString();
            String heightStr = etHeight.getText().toString();

            if (weightStr.isEmpty() || bodyfatStr.isEmpty() || heightStr.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setMessage("Please fill all fields")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            double weight = Double.parseDouble(weightStr);
            double bodyFat = Double.parseDouble(bodyfatStr);
            double height = Double.parseDouble(heightStr) / 100.0;

            double fatFreeMass = weight * (1 - bodyFat / 100.0);
            double ffmi = fatFreeMass / (height * height);

            tvWeightValue.setText(String.valueOf(weight));
            tvBodyfatValue.setText(String.valueOf(bodyFat));
            tvFfmiValue.setText(String.format("%.1f", ffmi));

            SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
            sp.edit()
                    .putString("weight", weightStr)
                    .putString("bodyFat", bodyfatStr)
                    .putString("height", heightStr)
                    .putString("name", etName.getText().toString())
                    .putString("sport", btnSport.getText().toString())
                    .putString("position", btnPosition.getText().toString())
                    .putString("experience", btnExperience.getText().toString())
                    .putFloat("ffmi", (float) ffmi)
                    .apply();
        });
    }

    private void setupSettingsButton() {
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    private void setupBottomNav() {
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
