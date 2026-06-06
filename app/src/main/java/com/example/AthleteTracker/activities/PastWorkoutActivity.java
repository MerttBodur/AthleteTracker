package com.example.AthleteTracker.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.Date;
import java.util.Locale;

public class PastWorkoutActivity extends AppCompatActivity {

    LinearLayout historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_workouts);

        historyList = findViewById(R.id.workout_history_list);
        Button btnBack = findViewById(R.id.btn_back_history);

        btnBack.setOnClickListener(v -> finish());

        loadHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void showAddDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        EditText etName = new EditText(this);
        etName.setHint("Workout Name");

        EditText etDuration = new EditText(this);
        etDuration.setHint("Duration (min)");
        etDuration.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        EditText etNotes = new EditText(this);
        etNotes.setHint("Notes (optional)");

        layout.addView(etName);
        layout.addView(etDuration);
        layout.addView(etNotes);

        new AlertDialog.Builder(this)
                .setTitle("Log Workout")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String duration = etDuration.getText().toString();
                    String notes = etNotes.getText().toString();

                    if (name.isEmpty() || duration.isEmpty()) {
                        new AlertDialog.Builder(this)
                                .setMessage("Please fill name and duration")
                                .setPositiveButton("OK", null)
                                .show();
                        return;
                    }

                    saveWorkout(name, duration, notes);
                    loadHistory();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveWorkout(String name, String duration, String notes) {
        SharedPreferences sp = getSharedPreferences("workouts", MODE_PRIVATE);
        String existing = sp.getString("history", "[]");

        try {
            JSONArray history = new JSONArray(existing);
            JSONObject workout = new JSONObject();

            String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());

            workout.put("name", name);
            workout.put("duration", duration);
            workout.put("notes", notes);
            workout.put("date", date);

            history.put(workout);

            sp.edit().putString("history", history.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHistory() {
        historyList.removeAllViews();

        SharedPreferences sp = getSharedPreferences("workouts", MODE_PRIVATE);
        String existing = sp.getString("history", "[]");

        try {
            JSONArray history = new JSONArray(existing);

            if (history.length() == 0) {
                TextView empty = new TextView(this);
                empty.setText("No workouts logged yet");
                empty.setTextColor(getResources().getColor(R.color.light_gray));
                empty.setTextSize(14);
                historyList.addView(empty);
                return;
            }

            for (int i = history.length() - 1; i >= 0; i--) {
                JSONObject workout = history.getJSONObject(i);

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setBackgroundResource(R.drawable.card_background);
                card.setPadding(40, 30, 40, 30);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0, 0);
                card.setLayoutParams(params);

                TextView tvName = new TextView(this);
                tvName.setText(workout.getString("name"));
                tvName.setTextColor(getResources().getColor(R.color.white));
                tvName.setTextSize(16);

                TextView tvInfo = new TextView(this);
                tvInfo.setText(workout.getString("date") + " • " + workout.getString("duration") + " min");
                tvInfo.setTextColor(getResources().getColor(R.color.light_gray));
                tvInfo.setTextSize(12);

                card.addView(tvName);
                card.addView(tvInfo);

                String notes = workout.getString("notes");
                if (!notes.isEmpty()) {
                    TextView tvNotes = new TextView(this);
                    tvNotes.setText(notes);
                    tvNotes.setTextColor(getResources().getColor(R.color.light_gray));
                    tvNotes.setTextSize(12);
                    card.addView(tvNotes);
                }

                final JSONObject workoutData = workout;
                card.setOnClickListener(v -> {
                    try {
                        StringBuilder details = new StringBuilder();
                        details.append(workoutData.getString("date"));
                        details.append(" • ").append(workoutData.getString("duration")).append(" min\n\n");

                        if (workoutData.has("exercises")) {
                            JSONArray exercises = new JSONArray(workoutData.getString("exercises"));

                            for (int j = 0; j < exercises.length(); j++) {
                                JSONObject ex = exercises.getJSONObject(j);
                                details.append(ex.getString("name")).append("\n");

                                JSONArray sets = ex.getJSONArray("sets");
                                String category = ex.getString("category");

                                for (int k = 0; k < sets.length(); k++) {
                                    JSONObject set = sets.getJSONObject(k);

                                    if (category.equals("Sprint")) {
                                        details.append("  Set ").append(set.getInt("set"))
                                                .append(": ").append(set.getString("duration")).append("s\n");
                                    } else if (category.equals("Jump")) {
                                        details.append("  Set ").append(set.getInt("set"))
                                                .append(": ").append(set.getString("reps")).append(" reps")
                                                .append(" • ").append(set.getString("jumpHeight")).append("cm\n");
                                    } else {
                                        details.append("  Set ").append(set.getInt("set"))
                                                .append(": ").append(set.getString("reps")).append(" x ")
                                                .append(set.getString("weight")).append("kg\n");
                                    }
                                }
                                details.append("\n");
                            }
                        } else {
                            details.append("No exercise details available");
                        }

                        new AlertDialog.Builder(this)
                                .setTitle(workoutData.getString("name"))
                                .setMessage(details.toString())
                                .setPositiveButton("OK", null)
                                .show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                historyList.addView(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}