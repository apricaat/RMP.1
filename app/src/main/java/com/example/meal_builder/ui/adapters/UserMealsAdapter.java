package com.example.meal_builder.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.meal_builder.data.model.MealPart;
import com.example.meal_builder.R;
import com.example.meal_builder.data.model.UserMeal;
import com.example.meal_builder.databinding.UserMealCardPartTemplateBinding;
import com.example.meal_builder.ui.viewmodels.MealsViewModel;

import java.util.List;

public class UserMealsAdapter extends RecyclerView.Adapter< UserMealsAdapter.ViewHolder>{
    private final String TAG = this.getClass().getSimpleName();
    private final LayoutInflater inflater;
    private List<UserMeal> items;
    private Context context;

    private MealsViewModel mealsViewModel;
    private Fragment fragment;

    
    public UserMealsAdapter(Context context, MealsViewModel mealsViewModel, Fragment fragment) {
        this.context = context;
        this.items = mealsViewModel.getUserMeals().getValue();
        this.inflater = LayoutInflater.from(context);
        this.mealsViewModel = mealsViewModel;
        this.fragment = fragment;

        mealsViewModel.getUserMeals().observe(
                fragment.getViewLifecycleOwner(),
                (userMeals) -> {
                    items = userMeals;
                    this.notifyDataSetChanged();
                }
        );
    }

    @Override
    public UserMealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meal_card_template, parent, false);
        return new UserMealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserMealsAdapter.ViewHolder holder, int position) {
        UserMeal item = items.get(position);
        holder.name.setText(item.name);

        holder.calories.setText(String.valueOf(item.getTotalCalories()));
        holder.fats.setText(String.valueOf(item.getTotalFats()));
        holder.protein.setText(String.valueOf(item.getTotalProtein()));
        holder.carbohydrates.setText(String.valueOf(item.getTotalCarbohydrates()));

        if (holder.partsContainer.getChildCount() == 0) {
            for (MealPart part : item.parts) {
                UserMealCardPartTemplateBinding cardPartBinding = UserMealCardPartTemplateBinding.inflate(
                        inflater, holder.partsContainer, true
                );
                String uri = "@drawable/" + part.image;
                cardPartBinding.mealCardParts.setImageResource(context.getResources().getIdentifier(uri, null, context.getPackageName()));
            }
        }

        String uri = "@drawable/" + item.image;
        holder.image.setImageResource(context.getResources().getIdentifier(uri, null, context.getPackageName()));

        holder.container.setOnClickListener((layout) -> {
            mealsViewModel.setEditingMealById(item.id);
            Navigation.findNavController(this.fragment.getView()).navigate(R.id.action_meals_to_edit);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView name;
        LinearLayout partsContainer;
        ImageView image;

        TextView calories;
        TextView fats;
        TextView protein;
        TextView carbohydrates;
        ViewHolder(View view){
            super(view);

            container = view.findViewById(R.id.user_meal);
            name = view.findViewById(R.id.meal_card_title);
            partsContainer = view.findViewById(R.id.user_meal_part_container);
            image = view.findViewById(R.id.meal_card_img);

            calories = view.findViewById(R.id.meal_card_calories);
            fats = view.findViewById(R.id.meal_card_fats);
            protein = view.findViewById(R.id.meal_card_protein);
            carbohydrates = view.findViewById(R.id.meal_card_carbonhydrates);
        }
    }
}
