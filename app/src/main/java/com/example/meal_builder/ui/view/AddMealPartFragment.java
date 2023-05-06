package com.example.meal_builder.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.meal_builder.ui.viewmodels.MealPartsViewModel;
import com.example.meal_builder.R;

public class AddMealPartFragment extends Fragment {

    private MealPartsViewModel mealPartsViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_meal_part, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealPartsViewModel = new ViewModelProvider(getActivity()).get(MealPartsViewModel.class);

        Button cancelBtn = (Button) getView().findViewById(R.id.chooseCancelBtn);
        cancelBtn.setOnClickListener((cancelBtn1) -> {
            Navigation.findNavController(view).popBackStack();
        });

        Button saveBtn = (Button) getView().findViewById(R.id.chooseSaveBtn);
        saveBtn.setOnClickListener((saveBtn1) -> {
            ImageView image = view.findViewById(R.id.new_part_image);
            EditText title = view.findViewById(R.id.new_part_title);
            EditText calories = view.findViewById(R.id.new_part_caloriew);
            EditText fats = view.findViewById(R.id.new_part_fats);
            EditText proteins = view.findViewById(R.id.new_part_proteins);
            EditText carbohydrates = view.findViewById(R.id.new_part_carbohydrates);

            if (title.getEditableText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Пустое название!", Toast.LENGTH_SHORT).show();
                return;
            }

            mealPartsViewModel.addPossibleMealPart(
                    "empty",
                    title.getEditableText().toString(),
                    Integer.parseInt(calories.getEditableText().toString()),
                    Integer.parseInt(fats.getEditableText().toString()),
                    Integer.parseInt(proteins.getEditableText().toString()),
                    Integer.parseInt(carbohydrates.getEditableText().toString())
            );
            Navigation.findNavController(view).popBackStack();
        });
    }
}