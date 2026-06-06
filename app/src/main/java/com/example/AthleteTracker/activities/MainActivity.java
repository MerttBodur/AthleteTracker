package com.example.AthleteTracker.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AthleteTracker.R;
import com.example.AthleteTracker.models.Workout;
import com.example.AthleteTracker.utils.onWorkoutClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements onWorkoutClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Quick Access butonları
        findViewById(R.id.btn_calculations).setOnClickListener(v -> {
            startActivity(new Intent(this, CalculationsActivity.class));
        });

        findViewById(R.id.btn_past_workouts).setOnClickListener(v -> {
            startActivity(new Intent(this, PastWorkoutActivity.class));
        });

        // Workout kartları
        Workout w1 = new Workout("Full Body Foundation 3-Day",
                "Beginner Strength + Athletic Base",
                "3x/week", 60,
                new String[]{"Box Jump 3x5", "Back Squat 4x5", "Romanian Deadlift 3x6", "Bench Press 3x5-6", "Weighted Pull-Up 3x5-6"});

        Workout w2 = new Workout("Strength Base 5x5",
                "Foundational Barbell Strength",
                "3x/week", 55,
                new String[]{"Squat 5x5", "Bench Press 5x5", "Deadlift 1x5"});

        Workout w3 = new Workout("5/3/1 Strength 4-Day",
                "Intermediate Sustainable Strength",
                "4x/week", 60,
                new String[]{"Squat 5/3/1", "Bench Press 5/3/1", "Deadlift 5/3/1", "Overhead Press 5/3/1"});

        findViewById(R.id.card_workout1).setOnClickListener(v -> onWorkoutClick(w1));
        findViewById(R.id.card_workout2).setOnClickListener(v -> onWorkoutClick(w2));
        findViewById(R.id.card_workout3).setOnClickListener(v -> onWorkoutClick(w3));

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_workout) {
                startActivity(new Intent(this, WorkoutActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_calculations) {
                startActivity(new Intent(this, CalculationsActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onWorkoutClick(Workout workout) {
        Intent intent = new Intent(this, WorkoutDetailActivity.class);
        intent.putExtra("name", workout.getName());
        intent.putExtra("description", workout.getDescription());
        intent.putExtra("frequency", workout.getFrequency());
        intent.putExtra("duration", workout.getDuration());
        intent.putExtra("exercises", workout.getExercises());
        startActivity(intent);
    }
}