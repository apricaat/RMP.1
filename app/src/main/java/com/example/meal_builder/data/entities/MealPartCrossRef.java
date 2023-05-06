package com.example.meal_builder.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.google.android.material.circularreveal.CircularRevealHelper;

@Entity(
        tableName = "user_meal_parts",
        primaryKeys = {"mealId", "partId"},
        foreignKeys = {
                @ForeignKey(
                        entity = UserMeal.class,
                        parentColumns = "mealId",
                        childColumns = "mealId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = MealPart.class,
                        parentColumns = "partId",
                        childColumns = "partId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        }
)
public class MealPartCrossRef {
    public long mealId;

    @ColumnInfo(index = true)
    public long partId;

    public int grams;

    public MealPartCrossRef(long mealId, long partId, int grams) {
        this.mealId = mealId;
        this.partId = partId;
        this.grams = grams;
    }
}
