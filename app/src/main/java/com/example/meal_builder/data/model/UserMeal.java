package com.example.meal_builder.data.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserMeal {
    @Nullable
    public Integer id;
    public String name;
    public String image;
    public List<MealPart> parts;

    public UserMeal(int id, String name, String image, List<MealPart> parts) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.parts= parts;
    }

    public UserMeal(UserMeal meal) {
        this.id = meal.id;
        this.name = meal.name;
        this.image = meal.image;
        this.parts = new ArrayList<>(meal.parts);
    }

    public UserMeal() {
        this.id = null;
        this.name = "Новая трапеза";
        this.image = "empty";
        this.parts = new ArrayList<>();
    }

    public int getTotalCalories() {
        int total = 0;
        for (MealPart part : this.parts) total += part.getTotalCalories();
        return total;
    }

    public int getTotalFats() {
        int total = 0;
        for (MealPart part : this.parts) total += part.getTotalFats();
        return total;
    }

    public int getTotalProtein() {
        int total = 0;
        for (MealPart part : this.parts) total += part.getTotalProtein();
        return total;
    }

    public int getTotalCarbohydrates() {
        int total = 0;
        for (MealPart part : this.parts) total += part.getTotalCarbohydrates();
        return total;
    }
}
