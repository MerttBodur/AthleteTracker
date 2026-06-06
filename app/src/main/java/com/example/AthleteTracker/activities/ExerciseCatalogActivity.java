package com.example.AthleteTracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AthleteTracker.R;
import com.example.AthleteTracker.utils.FetchJsonTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;

public class ExerciseCatalogActivity extends AppCompatActivity {

    LinearLayout categoryButtons, exerciseList;
    JSONObject allExercises;
    boolean selectMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_catalog);

        categoryButtons = findViewById(R.id.category_buttons);
        exerciseList = findViewById(R.id.exercise_list);
        Button btnBack = findViewById(R.id.btn_back_catalog);

        btnBack.setOnClickListener(v -> finish());

        fetchExercises();
    }

    private void fetchExercises() {
        new FetchJsonTask(new FetchJsonTask.OnJsonFetchedListener() {
            @Override
            public void onJsonFetched(String json) {
                try {
                    allExercises = new JSONObject(json);
                    setupCategoryButtons();
                    showCategory("Push");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFetchError(String error) {
                TextView tv = new TextView(ExerciseCatalogActivity.this);
                tv.setText("Failed to load exercises");
                tv.setTextColor(getResources().getColor(R.color.light_gray));
                exerciseList.addView(tv);
            }
        }).execute("https://raw.githubusercontent.com/MerttBodur/AthleticStandards/refs/heads/main/Exercises.json");

        selectMode = getIntent().getBooleanExtra("selectMode", false);
    }

    private void setupCategoryButtons() {
        Iterator<String> keys = allExercises.keys();

        while (keys.hasNext()) {
            String category = keys.next();

            Button btn = new Button(this, null, 0, R.style.OutlinedButton);
            btn.setText(category);
            btn.setTextSize(12);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 16, 0);
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> showCategory(category));
            categoryButtons.addView(btn);
        }
    }

    private void showCategory(String category) {
        exerciseList.removeAllViews();

        try {
            JSONArray exercises = allExercises.getJSONArray(category);

            for (int i = 0; i < exercises.length(); i++) {
                JSONObject exercise = exercises.getJSONObject(i);

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setBackgroundResource(R.drawable.card_background);
                card.setPadding(40, 30, 40, 30);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 16, 0, 0);
                card.setLayoutParams(params);

                TextView tvName = new TextView(this);
                tvName.setText(exercise.getString("name"));
                tvName.setTextColor(getResources().getColor(R.color.white));
                tvName.setTextSize(16);

                TextView tvMuscle = new TextView(this);
                tvMuscle.setText(exercise.getString("primaryMuscle"));
                tvMuscle.setTextColor(getResources().getColor(R.color.light_gray));
                tvMuscle.setTextSize(12);

                TextView tvCns = new TextView(this);
                tvCns.setText("CNS: " + exercise.getString("cns"));
                tvCns.setTextColor(getResources().getColor(R.color.light_gray));
                tvCns.setTextSize(12);

                card.addView(tvName);
                card.addView(tvMuscle);
                card.addView(tvCns);

                if (selectMode) {
                    card.setOnClickListener(v -> {
                        Intent result = new Intent();
                        try {
                            result.putExtra("exerciseName", exercise.getString("name"));
                            result.putExtra("exerciseCategory", category);
                            result.putExtra("exerciseMuscle", exercise.getString("primaryMuscle"));
                            result.putExtra("exerciseCns", exercise.getString("cns"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        setResult(RESULT_OK, result);
                        finish();
                    });
                }

                exerciseList.addView(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}