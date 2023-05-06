package com.example.meal_builder.ui.view;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.meal_builder.data.model.MealPart;
import com.example.meal_builder.ui.viewmodels.MealsViewModel;
import com.example.meal_builder.R;
import com.example.meal_builder.data.model.UserMeal;
import com.example.meal_builder.ui.adapters.MealPartVariantAdapter;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EditMealFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    MealsViewModel mealsViewModel;
    private MealPartVariantAdapter adapter;
    private  UserMeal meal;

    private final ActivityResultLauncher<String> requestSavePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.CreateDocument("txt/*"), uri -> {
                if (uri != null) {
                    try {
                        ParcelFileDescriptor txt = getActivity().getContentResolver().openFileDescriptor(uri, "w");
                        FileOutputStream fileOutputStream = new FileOutputStream(txt.getFileDescriptor());
                        fileOutputStream.write(new Gson().toJson(mealsViewModel.getUserMeals().getValue()).getBytes());
                        fileOutputStream.close();
                        txt.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    this.showNotification();
                }
            });

    public EditMealFragment() {
        super(R.layout.fragment_edit_meal);
    }

    private void rerender(View view) {
        EditText titleView = (EditText) getView().findViewById(R.id.meal_title);
        titleView.setText(this.meal.name);

        this.adapter.notifyDataSetChanged();
        setCalories(view);
    }

    private void setCalories(View view) {
        TextView calories = view.findViewById(R.id.edit_panel_calories);
        calories.setText(String.valueOf(this.meal.getTotalCalories()));
        TextView fats = view.findViewById(R.id.edit_panel_fats);
        fats.setText(String.valueOf(this.meal.getTotalFats()));
        TextView proteins = view.findViewById(R.id.edit_panel_proteins);
        proteins.setText(String.valueOf(this.meal.getTotalProtein()));
        TextView carbohydrates = view.findViewById(R.id.edit_panel_carbohydrates);
        carbohydrates.setText(String.valueOf(this.meal.getTotalCarbohydrates()));
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mealsViewModel = new ViewModelProvider(getActivity()).get(MealsViewModel.class);
        mealsViewModel.getEditingMeal().observe(getViewLifecycleOwner(), userMeal -> {
            this.meal = userMeal;
            rerender(view);
        });

        this.meal = mealsViewModel.getEditingMeal().getValue();

        EditText titleView = (EditText) getView().findViewById(R.id.meal_title);
        titleView.setText(this.meal.name);

        setCalories(view);

        titleView.setOnFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                saveTitle(view1);
            }
        });

        ListView partsList = getView().findViewById(R.id.parts_container);
        partsList.setItemsCanFocus(true);
        this.adapter = new MealPartVariantAdapter(getContext(), R.layout.meal_part_template, this.meal.parts);
        partsList.setAdapter(this.adapter);

        Button cancelBtn = (Button) getView().findViewById(R.id.editCancelBtn);
        cancelBtn.setOnClickListener((cancelBtn1) -> {
            Navigation.findNavController(view).popBackStack();
        });

        Button saveBtn = (Button) getView().findViewById(R.id.editSaveBtn);
        saveBtn.setOnClickListener((saveBtn1) -> {
            saveTitle(titleView);
            mealsViewModel.saveEditingMeal();
            Navigation.findNavController(view).popBackStack();
        });

        Button chooseBtn = (Button) getView().findViewById(R.id.choosePartsBtn);
        chooseBtn.setOnClickListener((chooseBtn1) -> {
            Navigation.findNavController(view).navigate(R.id.action_edit_to_choose);
        });

        Button planBtn = (Button) getView().findViewById(R.id.editPlanButton);
        planBtn.setOnClickListener((btn) -> {
            this.showNotification();

            requestSavePermissionLauncher.launch("meals.txt");
        });
    }

    private void saveTitle(View view) {
        meal.name = ((EditText) view).getEditableText().toString();
        mealsViewModel.changeEditingMeal(meal);
    }

    private void showNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        if (notificationManager.areNotificationsEnabled()) {

            Intent notificationIntent = new Intent(getContext(), MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "plan_channel")
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.breakfast1))
                    .setContentText((getResources().getString(R.string.plan_notification_text) + " " + this.meal.name))
                    .setContentTitle(getResources().getString(R.string.plan_notification_title) + " " + this.meal.name)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            notificationManager.notify(1, builder.build());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
