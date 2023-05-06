package com.example.meal_builder.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "user_meals")
public class UserMeal {
    @PrimaryKey(autoGenerate = true)
    public long mealId;

    public String name;

    public String image;

    public UserMeal(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public UserMeal(long mealId, String name, String image) {
        this.mealId = mealId;
        this.name = name;
        this.image = image;
    }

    public UserMeal() {
        this.name = "Новая трапеза";
        this.image = "empty";
    }
}
