package com.example.AthleteTracker.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AthleteTracker.R;

public class WorkoutDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String frequency = getIntent().getStringExtra("frequency");
        int duration = getIntent().getIntExtra("duration", 0);
        String[] exercises = getIntent().getStringArrayExtra("exercises");

        TextView tvName = findViewById(R.id.tv_workout_name);
        TextView tvDesc = findViewById(R.id.tv_workout_desc);
        TextView tvInfo = findViewById(R.id.tv_workout_info);
        LinearLayout exerciseList = findViewById(R.id.exercise_list);
        Button btnBack = findViewById(R.id.btn_back);

        tvName.setText(name);
        tvDesc.setText(description);
        tvInfo.setText(frequency + " • " + duration + " min");

        for (String exercise : exercises) {
            TextView tv = new TextView(this);
            tv.setText("• " + exercise);
            tv.setTextSize(16);
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setPadding(0, 8, 0, 8);
            exerciseList.addView(tv);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}