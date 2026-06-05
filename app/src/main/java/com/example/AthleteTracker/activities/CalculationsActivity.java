package com.example.AthleteTracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AthleteTracker.R;
import com.example.AthleteTracker.utils.FetchJsonTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalculationsActivity extends AppCompatActivity {

    LinearLayout section1rm, sectionRsi, sectionFfmi, scaleTable;
    EditText et1rmWeight, et1rmReps, et1rmRir;
    EditText etRsiJump, etRsiGct;
    EditText etFfmiWeight, etFfmiHeight, etFfmiBf;
    TextView tvResult, tvResultLabel;
    Button btnTab1rm, btnTabRsi, btnTabFfmi;

    JSONArray ffmiScale;
    JSONArray rsiScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculations);

        section1rm = findViewById(R.id.section_1rm);
        sectionRsi = findViewById(R.id.section_rsi);
        sectionFfmi = findViewById(R.id.section_ffmi);

        et1rmWeight = findViewById(R.id.et_1rm_weight);
        et1rmReps = findViewById(R.id.et_1rm_reps);
        et1rmRir = findViewById(R.id.et_1rm_rir);

        etRsiJump = findViewById(R.id.et_rsi_jump);
        etRsiGct = findViewById(R.id.et_rsi_gct);

        etFfmiWeight = findViewById(R.id.et_ffmi_weight);
        etFfmiHeight = findViewById(R.id.et_ffmi_height);
        etFfmiBf = findViewById(R.id.et_ffmi_bf);
        scaleTable = findViewById(R.id.scale_table);

        tvResult = findViewById(R.id.tv_result);
        tvResultLabel = findViewById(R.id.tv_result_label);

        btnTab1rm = findViewById(R.id.btn_tab_1rm);
        btnTabRsi = findViewById(R.id.btn_tab_rsi);
        btnTabFfmi = findViewById(R.id.btn_tab_ffmi);

        Button btnCalc1rm = findViewById(R.id.btn_calc_1rm);
        Button btnCalcRsi = findViewById(R.id.btn_calc_rsi);
        Button btnCalcFfmi = findViewById(R.id.btn_calc_ffmi);

        btnTab1rm.setOnClickListener(v -> showSection("1rm"));
        btnTabRsi.setOnClickListener(v -> showSection("rsi"));
        btnTabFfmi.setOnClickListener(v -> showSection("ffmi"));

        btnCalc1rm.setOnClickListener(v -> calculate1RM());
        btnCalcRsi.setOnClickListener(v -> calculateRSI());
        btnCalcFfmi.setOnClickListener(v -> calculateFFMI());

        fetchScales();
        setupBottomNav();
    }

    private void fetchScales() {
        new FetchJsonTask(new FetchJsonTask.OnJsonFetchedListener() {
            @Override
            public void onJsonFetched(String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                    String sex = sp.getString("sex", "male").toLowerCase();

                    if (sex.equals("female")) {
                        ffmiScale = obj.getJSONObject("ffmi_scale").getJSONArray("female");
                    } else {
                        ffmiScale = obj.getJSONObject("ffmi_scale").getJSONArray("male");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFetchError(String error) {
                ffmiScale = null;
            }
        }).execute("https://raw.githubusercontent.com/MerttBodur/AthleticStandards/refs/heads/main/FFMI.json");

        new FetchJsonTask(new FetchJsonTask.OnJsonFetchedListener() {
            @Override
            public void onJsonFetched(String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    rsiScale = obj.getJSONArray("rsi_scale");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFetchError(String error) {
                rsiScale = null;
            }
        }).execute("https://raw.githubusercontent.com/MerttBodur/AthleticStandards/refs/heads/main/RSI.json");
    }

    private String getLabel(JSONArray scale, double value) {
        if (scale == null) return "Scale not available";
        try {
            for (int i = 0; i < scale.length(); i++) {
                JSONObject range = scale.getJSONObject(i);
                double min = range.getDouble("min");
                double max = range.getDouble("max");
                if (value >= min && value < max) {
                    return range.getString("label");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private void showSection(String section) {
        section1rm.setVisibility(View.GONE);
        sectionRsi.setVisibility(View.GONE);
        sectionFfmi.setVisibility(View.GONE);
        tvResult.setText("--");
        tvResultLabel.setText("");

        switch (section) {
            case "1rm":
                section1rm.setVisibility(View.VISIBLE);
                break;
            case "rsi":
                sectionRsi.setVisibility(View.VISIBLE);
                break;
            case "ffmi":
                sectionFfmi.setVisibility(View.VISIBLE);
                break;
        }
        scaleTable.removeAllViews();
    }

    private void calculate1RM() {
        String weightStr = et1rmWeight.getText().toString();
        String repsStr = et1rmReps.getText().toString();
        String rirStr = et1rmRir.getText().toString();

        if (weightStr.isEmpty() || repsStr.isEmpty()) {
            showError("Please fill Weight and Reps");
            return;
        }

        double weight = Double.parseDouble(weightStr);
        int reps = Integer.parseInt(repsStr);
        int rir = rirStr.isEmpty() ? 0 : Integer.parseInt(rirStr);
        int effectiveReps = reps + rir;
        double oneRm = weight * (1 + effectiveReps / 30.0);

        tvResult.setText(String.format("%.1f kg", oneRm));
        tvResultLabel.setText("Estimated 1RM");
    }

    private void calculateRSI() {
        String jumpStr = etRsiJump.getText().toString();
        String gctStr = etRsiGct.getText().toString();

        if (jumpStr.isEmpty() || gctStr.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        double jumpHeight = Double.parseDouble(jumpStr) / 100.0;
        double gct = Double.parseDouble(gctStr);
        double rsi = jumpHeight / gct;

        String label = getLabel(rsiScale, rsi);
        tvResult.setText(String.format("%.2f", rsi));
        tvResultLabel.setText(label);
        showScaleTable(rsiScale, rsi);
    }

    private void calculateFFMI() {
        String weightStr = etFfmiWeight.getText().toString();
        String heightStr = etFfmiHeight.getText().toString();
        String bfStr = etFfmiBf.getText().toString();

        if (weightStr.isEmpty() || heightStr.isEmpty() || bfStr.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr) / 100.0;
        double bodyFat = Double.parseDouble(bfStr);
        double fatFreeMass = weight * (1 - bodyFat / 100.0);
        double ffmi = fatFreeMass / (height * height);

        String label = getLabel(ffmiScale, ffmi);
        tvResult.setText(String.format("%.1f", ffmi));
        tvResultLabel.setText(label);
        showScaleTable(ffmiScale, ffmi);
    }

    private void showScaleTable(JSONArray scale, double currentValue) {
        scaleTable.removeAllViews();

        if (scale == null) return;

        try {
            for (int i = 0; i < scale.length(); i++) {
                JSONObject range = scale.getJSONObject(i);
                double min = range.getDouble("min");
                double max = range.getDouble("max");
                String label = range.getString("label");

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(12, 12, 12, 12);

                if (currentValue >= min && currentValue < max) {
                    row.setBackgroundColor(getResources().getColor(R.color.light_purple));
                }

                TextView tvRange = new TextView(this);
                tvRange.setText(String.format("%.1f - %.1f", min, max));
                tvRange.setTextColor(getResources().getColor(R.color.white));
                tvRange.setTextSize(14);
                tvRange.setWidth(300);

                TextView tvLabel = new TextView(this);
                tvLabel.setText(label);
                tvLabel.setTextColor(getResources().getColor(R.color.white));
                tvLabel.setTextSize(14);

                row.addView(tvRange);
                row.addView(tvLabel);
                scaleTable.addView(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_calculations);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_calculations) {
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_workout) {
                startActivity(new Intent(this, WorkoutActivity.class));
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