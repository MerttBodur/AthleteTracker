package com.example.AthleteTracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AthleteTracker.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddWorkoutActivity extends AppCompatActivity {

    LinearLayout exerciseContainer;
    TextView tvTimer;
    EditText etWorkoutName;
    Button btnBack, btnAddExercise, btnFinish;
    int seconds = 0;
    boolean running = true;
    Handler handler = new Handler();
    ArrayList<JSONObject> workoutExercises = new ArrayList<>();
    ArrayList<String> exerciseNames = new ArrayList<>();
    ArrayList<String> exerciseCategories = new ArrayList<>();
    ArrayList<LinearLayout> setsContainers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        exerciseContainer = findViewById(R.id.exercise_container);
        tvTimer = findViewById(R.id.tv_timer);
        btnBack = findViewById(R.id.btn_back_workout);
        btnAddExercise = findViewById(R.id.btn_add_exercise);
        btnFinish = findViewById(R.id.btn_finish_workout);
        etWorkoutName = findViewById(R.id.et_workout_name);

        startTimer();

        btnBack.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage("Discard workout?")
                    .setPositiveButton("Discard", (d, w) -> finish())
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        btnAddExercise.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseCatalogActivity.class);
            intent.putExtra("selectMode", true);
            startActivityForResult(intent, 100);
        });

        btnFinish.setOnClickListener(v -> finishWorkout());
    }

    private void startTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    int mins = seconds / 60;
                    int secs = seconds % 60;
                    tvTimer.setText(String.format("%02d:%02d", mins, secs));
                    seconds++;
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("exerciseName");
            String category = data.getStringExtra("exerciseCategory");
            String primaryMuscle = data.getStringExtra("exerciseMuscle");
            String cns = data.getStringExtra("exerciseCns");

            addExerciseCard(name, category, primaryMuscle, cns);
        }
    }

    private void addExerciseCard(String name, String category, String primaryMuscle, String cns) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.card_background);
        card.setPadding(40, 30, 40, 30);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 16, 0, 0);
        card.setLayoutParams(cardParams);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(getResources().getColor(R.color.white));
        tvName.setTextSize(18);
        card.addView(tvName);

        TextView tvInfo = new TextView(this);
        tvInfo.setText(primaryMuscle + " • CNS: " + cns);
        tvInfo.setTextColor(getResources().getColor(R.color.light_gray));
        tvInfo.setTextSize(12);
        card.addView(tvInfo);

        LinearLayout setsContainer = new LinearLayout(this);
        setsContainer.setOrientation(LinearLayout.VERTICAL);
        card.addView(setsContainer);

        exerciseNames.add(name);
        exerciseCategories.add(category);
        setsContainers.add(setsContainer);

        if (category.equals("Sprint")) {
            addSprintSet(setsContainer, 1);
        } else if (category.equals("Jump")) {
            addJumpSet(setsContainer, 1);
        } else {
            addWeightSet(setsContainer, 1);
        }

        Button btnRemoveExercise = new Button(this, null, 0, R.style.OutlinedButton);
        btnRemoveExercise.setText("Remove Exercise");
        btnRemoveExercise.setTextSize(12);

        final int exerciseIndex = exerciseNames.size() - 1;
        btnRemoveExercise.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage("Remove " + name + "?")
                    .setPositiveButton("Remove", (d, w) -> {
                        exerciseContainer.removeView(card);
                        int idx = exerciseNames.indexOf(name);
                        if (idx >= 0) {
                            exerciseNames.remove(idx);
                            exerciseCategories.remove(idx);
                            setsContainers.remove(idx);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        card.addView(btnRemoveExercise);

        Button btnAddSet = new Button(this, null, 0, R.style.OutlinedButton);
        btnAddSet.setText("+ Add Set");
        btnAddSet.setTextSize(12);

        final int[] setCount = {1};
        btnAddSet.setOnClickListener(v -> {
            setCount[0]++;
            if (category.equals("Sprint")) {
                addSprintSet(setsContainer, setCount[0]);
            } else if (category.equals("Jump")) {
                addJumpSet(setsContainer, setCount[0]);
            } else {
                addWeightSet(setsContainer, setCount[0]);
            }
        });

        card.addView(btnAddSet);
        exerciseContainer.addView(card);
    }

    private void addWeightSet(LinearLayout container, int setNum) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 10, 0, 10);

        TextView tvSet = new TextView(this);
        tvSet.setText("Set " + setNum);
        tvSet.setTextColor(getResources().getColor(R.color.white));
        tvSet.setTextSize(14);
        tvSet.setWidth(120);
        row.addView(tvSet);

        EditText etReps = new EditText(this);
        etReps.setHint("Reps");
        etReps.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        etReps.setTextColor(getResources().getColor(R.color.white));
        etReps.setTextSize(14);
        etReps.setWidth(200);
        row.addView(etReps);

        EditText etWeight = new EditText(this);
        etWeight.setHint("Kg");
        etWeight.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etWeight.setTextColor(getResources().getColor(R.color.white));
        etWeight.setTextSize(14);
        etWeight.setWidth(200);
        row.addView(etWeight);

        TextView btnRemoveSet = new TextView(this);
        btnRemoveSet.setText("  ✕");
        btnRemoveSet.setTextColor(getResources().getColor(R.color.light_gray));
        btnRemoveSet.setTextSize(18);
        btnRemoveSet.setOnClickListener(v -> container.removeView(row));
        row.addView(btnRemoveSet);

        container.addView(row);
    }

    private void addJumpSet(LinearLayout container, int setNum) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(0, 10, 0, 10);

        TextView btnRemoveSet = new TextView(this);
        btnRemoveSet.setText("  ✕");
        btnRemoveSet.setTextColor(getResources().getColor(R.color.light_gray));
        btnRemoveSet.setTextSize(18);
        btnRemoveSet.setOnClickListener(v -> container.removeView(row));
        row.addView(btnRemoveSet);

        TextView tvSet = new TextView(this);
        tvSet.setText("Set " + setNum);
        tvSet.setTextColor(getResources().getColor(R.color.white));
        tvSet.setTextSize(14);
        row.addView(tvSet);

        LinearLayout inputRow1 = new LinearLayout(this);
        inputRow1.setOrientation(LinearLayout.HORIZONTAL);

        EditText etReps = new EditText(this);
        etReps.setHint("Reps");
        etReps.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        etReps.setTextColor(getResources().getColor(R.color.white));
        etReps.setTextSize(14);
        etReps.setWidth(200);
        inputRow1.addView(etReps);

        EditText etAirTime = new EditText(this);
        etAirTime.setHint("Air Time (s)");
        etAirTime.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAirTime.setTextColor(getResources().getColor(R.color.white));
        etAirTime.setTextSize(14);
        etAirTime.setWidth(250);
        inputRow1.addView(etAirTime);

        row.addView(inputRow1);

        LinearLayout inputRow2 = new LinearLayout(this);
        inputRow2.setOrientation(LinearLayout.HORIZONTAL);

        EditText etJumpHeight = new EditText(this);
        etJumpHeight.setHint("Jump Height (cm)");
        etJumpHeight.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etJumpHeight.setTextColor(getResources().getColor(R.color.white));
        etJumpHeight.setTextSize(14);
        etJumpHeight.setWidth(350);
        inputRow2.addView(etJumpHeight);

        EditText etGct = new EditText(this);
        etGct.setHint("GCT (s)");
        etGct.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etGct.setTextColor(getResources().getColor(R.color.white));
        etGct.setTextSize(14);
        etGct.setWidth(200);
        inputRow2.addView(etGct);

        row.addView(inputRow2);

        etAirTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String airTimeStr = etAirTime.getText().toString();
                if (!airTimeStr.isEmpty()) {
                    double airTime = Double.parseDouble(airTimeStr);
                    double jumpHeight = (9.81 * airTime * airTime) / 2.0 * 100;
                    etJumpHeight.setText(String.format("%.1f", jumpHeight));
                }
            }
        });

        container.addView(row);
    }

    private void addSprintSet(LinearLayout container, int setNum) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 10, 0, 10);

        TextView tvSet = new TextView(this);
        tvSet.setText("Set " + setNum);
        tvSet.setTextColor(getResources().getColor(R.color.white));
        tvSet.setTextSize(14);
        tvSet.setWidth(120);
        row.addView(tvSet);

        EditText etDuration = new EditText(this);
        etDuration.setHint("Duration (s)");
        etDuration.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDuration.setTextColor(getResources().getColor(R.color.white));
        etDuration.setTextSize(14);
        etDuration.setWidth(300);
        row.addView(etDuration);

        TextView btnRemoveSet = new TextView(this);
        btnRemoveSet.setText("  ✕");
        btnRemoveSet.setTextColor(getResources().getColor(R.color.light_gray));
        btnRemoveSet.setTextSize(18);
        btnRemoveSet.setOnClickListener(v -> container.removeView(row));
        row.addView(btnRemoveSet);

        container.addView(row);
    }

    private void finishWorkout() {
        String workoutName = etWorkoutName.getText().toString();

        if (workoutName.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Please enter a workout name")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        running = false;

        SharedPreferences sp = getSharedPreferences("workouts", MODE_PRIVATE);
        String existing = sp.getString("history", "[]");

        try {
            JSONArray history = new JSONArray(existing);
            JSONObject workout = new JSONObject();

            String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());

            workout.put("name", workoutName);
            workout.put("date", date);
            workout.put("duration", String.valueOf(seconds / 60));
            workout.put("notes", exerciseNames.size() + " exercises");

            JSONArray exercisesArray = new JSONArray();

            for (int i = 0; i < exerciseNames.size(); i++) {
                JSONObject exerciseObj = new JSONObject();
                exerciseObj.put("name", exerciseNames.get(i));
                exerciseObj.put("category", exerciseCategories.get(i));

                JSONArray setsArray = new JSONArray();
                LinearLayout container = setsContainers.get(i);

                for (int j = 0; j < container.getChildCount(); j++) {
                    if (container.getChildAt(j) instanceof LinearLayout) {
                        JSONObject setObj = new JSONObject();
                        String category = exerciseCategories.get(i);

                        if (category.equals("Sprint")) {
                            LinearLayout row = (LinearLayout) container.getChildAt(j);
                            EditText etDuration = (EditText) row.getChildAt(1);
                            setObj.put("set", j + 1);
                            setObj.put("duration", etDuration.getText().toString());
                        } else if (category.equals("Jump")) {
                            LinearLayout col = (LinearLayout) container.getChildAt(j);
                            LinearLayout row1 = (LinearLayout) col.getChildAt(1);
                            LinearLayout row2 = (LinearLayout) col.getChildAt(2);

                            EditText etReps = (EditText) row1.getChildAt(0);
                            EditText etAirTime = (EditText) row1.getChildAt(1);
                            EditText etJumpHeight = (EditText) row2.getChildAt(0);
                            EditText etGct = (EditText) row2.getChildAt(1);

                            setObj.put("set", j + 1);
                            setObj.put("reps", etReps.getText().toString());
                            setObj.put("airTime", etAirTime.getText().toString());
                            setObj.put("jumpHeight", etJumpHeight.getText().toString());
                            setObj.put("gct", etGct.getText().toString());
                        } else {
                            LinearLayout row = (LinearLayout) container.getChildAt(j);
                            EditText etReps = (EditText) row.getChildAt(1);
                            EditText etWeight = (EditText) row.getChildAt(2);
                            setObj.put("set", j + 1);
                            setObj.put("reps", etReps.getText().toString());
                            setObj.put("weight", etWeight.getText().toString());
                        }

                        setsArray.put(setObj);
                    }
                }

                exerciseObj.put("sets", setsArray);
                exercisesArray.put(exerciseObj);
            }

            workout.put("exercises", exercisesArray.toString());
            history.put(workout);
            sp.edit().putString("history", history.toString()).apply();

            new AlertDialog.Builder(this)
                    .setMessage("Workout saved!")
                    .setPositiveButton("OK", (d, w) -> finish())
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }
}