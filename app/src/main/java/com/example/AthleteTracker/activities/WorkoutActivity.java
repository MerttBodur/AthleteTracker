package com.example.AthleteTracker.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AthleteTracker.R;
import com.example.AthleteTracker.models.Workout;
import com.example.AthleteTracker.utils.onWorkoutClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WorkoutActivity extends AppCompatActivity implements onWorkoutClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Workout w1 = new Workout("Full Body Foundation 3-Day",
                "Beginner Strength + Athletic Base",
                "3x/week", 60,
                new String[]{"Squat 3x8", "Bench Press 3x8", "Barbell Row 3x8", "Overhead Press 3x8", "Deadlift 3x5"});

        Workout w2 = new Workout("Strength Base 5x5",
                "Foundational Barbell Strength",
                "3x/week", 55,
                new String[]{"Squat 5x5", "Bench Press 5x5", "Barbell Row 5x5", "Overhead Press 5x5", "Deadlift 5x5"});

        Workout w3 = new Workout("5/3/1 Strength 4-Day",
                "Intermediate Sustainable Strength",
                "4x/week", 60,
                new String[]{"Squat 5/3/1", "Bench Press 5/3/1", "Deadlift 5/3/1", "Overhead Press 5/3/1"});

        findViewById(R.id.card_program1).setOnClickListener(v -> onWorkoutClick(w1));
        findViewById(R.id.card_program2).setOnClickListener(v -> onWorkoutClick(w2));
        findViewById(R.id.card_program3).setOnClickListener(v -> onWorkoutClick(w3));

        findViewById(R.id.btn_exercise_catalog).setOnClickListener(v -> {
            startActivity(new Intent(this, ExerciseCatalogActivity.class));
        });

        setupBottomNav();
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

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_workout);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_workout) {
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
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
}