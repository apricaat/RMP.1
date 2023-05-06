package com.example.meal_builder.data.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

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
