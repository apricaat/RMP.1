package com.example.meal_builder.data.model;

import androidx.annotation.Nullable;

public class ChoosableMealPart {
    @Nullable
    public Integer id;
    public int fats;
    public int protein;
    public int carbohydrates;
    public int calories;
    public String name;
    public String image;

    public ChoosableMealPart(
            @Nullable Integer id, int calories, int fats, int protein, int carbohydrates, String name, String image
    ) {
        this.id = id;
        this.calories = calories;
        this.fats = fats;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.name = name;
        this.image = image;
    }
}
