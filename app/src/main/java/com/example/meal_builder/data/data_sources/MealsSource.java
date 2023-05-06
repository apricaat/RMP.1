package com.example.meal_builder.data.data_sources;
import com.example.meal_builder.data.model.MealPart;
import com.example.meal_builder.data.model.UserMeal;
import java.util.ArrayList;
import java.util.Arrays;

public class MealsSource {
        ArrayList<UserMeal> meals = new ArrayList<UserMeal>(){
            {
                add(new UserMeal(0, "Пациенты", "breakfast1", Arrays.asList(new MealPart(0, 123, 421, 333, 44, "Сыр", "salad", 135))));
                add(new UserMeal(1, "Выбранные пациенты", "salad", Arrays.asList(new MealPart(1, 123, 421, 333, 44, "Колбаса", "tomatoes", 231))));
            }
        };

            public UserMeal add(UserMeal toAdd) {
                toAdd.id = meals.size();
                meals.add(toAdd);
                return toAdd;
            }
        }