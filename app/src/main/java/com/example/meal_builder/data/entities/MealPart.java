package com.example.meal_builder.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}
