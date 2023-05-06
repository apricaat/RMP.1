package com.example.meal_builder.data.model;

import com.example.meal_builder.data.model.ChoosableMealPart;

public class MealPart extends ChoosableMealPart {
    public static int gramsDefaultCounting = 100;
    public int grams = 100;

    public MealPart(int id, int calories, int fats, int protein, int carbohydrates, String name, String image, int grams) {
        super(id, calories, fats, protein, carbohydrates, name, image);
        this.grams = grams;
    }

    public MealPart(ChoosableMealPart part, int grams) {
        super(part.id, part.calories, part.fats, part.protein, part.carbohydrates, part.name, part.image);
        this.grams = grams;
    }

    public MealPart(ChoosableMealPart part) {
        super(part.id, part.calories, part.fats, part.protein, part.carbohydrates, part.name, part.image);
    }

    public int getTotalCalories() {
        return Math.round((float)(this.calories * this.grams) / gramsDefaultCounting);
    }

    public int getTotalFats() {
        return Math.round((float)(this.fats * this.grams) / gramsDefaultCounting);
    }

    public int getTotalProtein() {
        return Math.round((float)(this.protein * this.grams) / gramsDefaultCounting);
    }

    public int getTotalCarbohydrates() {
        return Math.round((float)(this.carbohydrates * this.grams) / gramsDefaultCounting);
    }
}
