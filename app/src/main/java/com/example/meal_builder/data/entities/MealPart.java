package com.example.meal_builder.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import com.example.meal_builder.ChoosableMealPart;
import com.example.meal_builder.MealPart;
import com.example.meal_builder.MealsViewModel;
import com.example.meal_builder.R;
import com.example.meal_builder.UserMeal;
import com.example.meal_builder.UserMealsAdapter;


import java.util.ArrayList;

@Entity(tableName = "meal_parts")
public class MealPart {
    @PrimaryKey(autoGenerate = true)
    public long partId;

    public int calories;

    public int fats;

    public int proteins;

    public int carbohydrates;

    public String name;

    public String image;

    public MealPart(
            int calories, int fats, int proteins, int carbohydrates, String name, String image
    ) {
        this.calories = calories;
        this.fats = fats;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.name = name;
        this.image = image;
    }
    private final String TAG = this.getClass().getSimpleName();

    MealsViewModel mealsViewModel;
    private MealPartVariantAdapter adapter;
    private  UserMeal meal;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    this.showNotification();
                }
            });
    public EditMealFragment() {
        super(R.layout.fragment_edit_meal);
    }

    static ArrayList<MealPart> parts = new ArrayList<MealPart>() {
        {


        }
    };

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
        mealsViewModel.editingMeal.observe(getViewLifecycleOwner(), userMeal -> {
            this.meal = userMeal;
            rerender(view);
        });

        this.meal = mealsViewModel.editingMeal.getValue();


        EditText titleView = (EditText) getView().findViewById(R.id.meal_title);
        titleView.setText(this.meal.name);

        setCalories(view);

        titleView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    meal.name = ((EditText) view).getEditableText().toString();
                    mealsViewModel.changeEditingMeal(meal);
                }
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
        });


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
