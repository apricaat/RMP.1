package com.example.meal_builder.ui.view;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.meal_builder.R;
import com.example.meal_builder.data.DB;
import com.google.gson.Gson;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CharSequence name = getString(R.string.plan_channel);
        String description = getString(R.string.plan_channel_description);
        NotificationChannel channel = new NotificationChannel("plan_channel", name, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        DB db = DB.getDatabase(this);
        DB.databaseWriteExecutor.execute(() -> {
            db.mealPartDao().getAll();
        });
    }
}
