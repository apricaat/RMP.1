package com.example.meal_builder.ui.view;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;

import android.content.Intent;
import android.os.Bundle;

import com.example.meal_builder.R;


public class MainActivity extends FragmentActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}