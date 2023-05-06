package com.example.meal_builder.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meal_builder.ui.viewmodels.MealPartsViewModel;
import com.example.meal_builder.ui.viewmodels.MealsViewModel;
import com.example.meal_builder.R;
import com.example.meal_builder.data.model.ChoosableMealPart;
import com.example.meal_builder.ui.adapters.MealPartRecyclerAdapter;
import com.example.meal_builder.ui.services.OverlayService;

public class ChoosePartsFragment extends Fragment implements DefaultLifecycleObserver {

    private final String TAG = this.getClass().getSimpleName();
    MealPartsViewModel mealPartsViewModel;
    MealsViewModel mealsViewModel;

    public ChoosePartsFragment() {
        super(R.layout.fragment_choose_parts);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mealPartsViewModel = new ViewModelProvider(getActivity()).get(MealPartsViewModel.class);
        mealsViewModel = new ViewModelProvider(getActivity()).get(MealsViewModel.class);

        RecyclerView partsList = getView().findViewById(R.id.part_variants_container);
        MealPartRecyclerAdapter adapter = new MealPartRecyclerAdapter(getContext(), mealPartsViewModel, this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        partsList.setLayoutManager(manager);
        partsList.setAdapter(adapter);

        Button cancelBtn = (Button) getView().findViewById(R.id.chooseCancelBtn);
        cancelBtn.setOnClickListener((cancelBtn1) -> {
            Navigation.findNavController(view).popBackStack();
        });

        Button saveBtn = (Button) getView().findViewById(R.id.chooseSaveBtn);
        saveBtn.setOnClickListener((saveBtn1) -> {
            mealsViewModel.addPartsToEditingMeal(mealPartsViewModel.getChosenMealParts().getValue());
            Navigation.findNavController(view).popBackStack();
        });

        Button addPartBtn = getView().findViewById(R.id.add_part_btn);
        addPartBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_choose_to_add_parts);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
        Toast.makeText(context, "onAttach", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        Log.i(TAG, "onCreate");
        Toast.makeText(getContext(), "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (isVisible() && mealPartsViewModel.getChosenMealParts().getValue().size() > 0) {
            Intent intent = new Intent(getContext(), OverlayService.class);
            StringBuilder notSavedParts = new StringBuilder();
            for (ChoosableMealPart part : mealPartsViewModel.getChosenMealParts().getValue()) {
                notSavedParts.append(part.name).append(" ");
            }
            intent.putExtra("NotSaved", notSavedParts.toString());
            getContext().startService(intent);
        }
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        if (isVisible() && mealPartsViewModel.getChosenMealParts().getValue().size() > 0) {
            getContext().stopService(new Intent(getContext(), OverlayService.class));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        Toast.makeText(getContext(), "onCreateView", Toast.LENGTH_SHORT).show();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mealPartsViewModel.clearChosenParts();
    }
}
