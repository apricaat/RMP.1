package com.example.meal_builder.data.user_sources;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.meal_builder.data.entities.MealPartCrossRef;
import com.example.meal_builder.data.user_sources.UserMeal;

import java.util.List;


public class UserMealWithParts {
    @Embedded public UserMeal userMeal;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "partId",
            associateBy = @Junction(MealPartCrossRef.class)
    ) public List<MealPart> mealParts;

    @Relation(
            entity = MealPartCrossRef.class,
            parentColumn = "mealId",
            entityColumn = "mealId"
    )
    public List<MealPartCrossRef> partsCrossRef;
}
