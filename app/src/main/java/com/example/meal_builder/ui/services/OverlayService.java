package com.example.meal_builder.ui.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.meal_builder.R;
import com.example.meal_builder.ui.view.FakeActivity;

public class OverlayService extends Service {
    private WindowManager windowManager;
    private View overlay;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String notSavedParts = intent.getExtras().getString("NotSaved");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        overlay = View.inflate(this, R.layout.not_saved_parts_overlay, null);
        TextView notSavedPartsText = overlay.findViewById(R.id.not_saved_parts);
        notSavedPartsText.setText(notSavedParts);

        overlay.setOnClickListener((view -> {
            Intent intentBack = new Intent(this, FakeActivity.class);
            intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentBack);
        }));

        final WindowManager.LayoutParams params = new
                WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        windowManager.addView(overlay, params);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (overlay != null) windowManager.removeView(overlay);
        super.onDestroy();
    }
}