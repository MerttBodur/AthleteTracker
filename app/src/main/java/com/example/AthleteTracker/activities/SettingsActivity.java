package com.example.AthleteTracker.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.example.AthleteTracker.R;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnBack = findViewById(R.id.btn_back_settings);
        LinearLayout btnNotification = findViewById(R.id.btn_notification);
        LinearLayout btnShare = findViewById(R.id.btn_share);
        LinearLayout btnReset = findViewById(R.id.btn_reset);
        LinearLayout btnAbout = findViewById(R.id.btn_about);

        btnBack.setOnClickListener(v -> finish());

        btnNotification.setOnClickListener(v -> {
            createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "training")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Athlete Tracker")
                    .setContentText("Time to train! Don't skip your workout today.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.notify(1, builder.build());

            new AlertDialog.Builder(this)
                    .setMessage("Reminder notification sent!")
                    .setPositiveButton("OK", null)
                    .show();
        });

        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Athlete Tracker");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out Athlete Tracker - Your personal fitness companion!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        btnReset.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Reset Profile")
                    .setMessage("Are you sure? All your data will be deleted.")
                    .setPositiveButton("Reset", (dialog, which) -> {
                        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                        sp.edit().clear().apply();
                        new AlertDialog.Builder(this)
                                .setMessage("Profile reset successfully")
                                .setPositiveButton("OK", null)
                                .show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        btnAbout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("Athlete Tracker v1.0\nDeveloped by Mert Bodur\nCMP2004 Project")
                    .setPositiveButton("OK", null)
                    .show();
        });

        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "training",
                    "Training Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Workout reminder notifications");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}