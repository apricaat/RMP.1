package com.example.meal_builder.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meal_builder.ui.viewmodels.MealsViewModel;
import com.example.meal_builder.R;
import com.example.meal_builder.ui.adapters.UserMealsAdapter;
import com.example.meal_builder.ui.viewmodels.LoginViewModel;

public class MealsFragment extends Fragment {
    public MealsFragment() {
        super(R.layout.fragment_meals);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MealsViewModel mealsViewModel = new ViewModelProvider(getActivity()).get(MealsViewModel.class);
        LoginViewModel loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

        RecyclerView mealsList = getView().findViewById(R.id.user_meals_container);
        UserMealsAdapter adapter = new UserMealsAdapter(getContext(), mealsViewModel, this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mealsList.setLayoutManager(manager);
        mealsList.setAdapter(adapter);

        Button addButton = getView().findViewById(R.id.add_meal_btn);
        addButton.setOnClickListener(view1 -> {
            mealsViewModel.addNewMeal();
        });

        Button logoutBtn = getView().findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(view1 -> {
            loginViewModel.logout();
            Navigation.findNavController(getView()).navigate(
                    R.id.action_meals_to_login,
                    null,
                    new NavOptions.Builder().setPopUpTo(R.id.mealsFragment, true).build()
            );
        });
    }
}