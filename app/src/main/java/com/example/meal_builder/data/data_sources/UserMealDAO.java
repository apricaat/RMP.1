package com.example.meal_builder.data.data_sources;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.meal_builder.data.entities.MealPartCrossRef;
import com.example.meal_builder.data.entities.UserMeal;
import com.example.meal_builder.data.entities.UserMealWithParts;
import com.example.meal_builder.data.model.MealPart;

import java.util.List;

@Dao
public abstract class UserMealDAO {
    @Transaction
    @Query("SELECT * FROM user_meals")
    abstract public List<UserMealWithParts> getUserMealsWithParts();

    @Transaction
    @Query("SELECT * FROM user_meals WHERE mealId = :id")
    abstract public UserMealWithParts getUserMealsWithPartsById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public long insertUserMeal(UserMeal userMeal);

    @Transaction
    public UserMealWithParts insert(UserMeal userMeal) {
        long id = insertUserMeal(userMeal);
        return getUserMealsWithPartsById(id);
    };

    @Update
    abstract public int updateUserMeal(UserMeal userMeal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void assignMealPartToUserMeal(MealPartCrossRef mealPartCrossRef);

    @Transaction
    public UserMealWithParts update(UserMeal userMeal, List<MealPart> mealParts) {
        updateUserMeal(userMeal);

        for (MealPart mealPart : mealParts) {
            MealPartCrossRef ref = new MealPartCrossRef(userMeal.mealId, (long) mealPart.id, mealPart.grams);
            assignMealPartToUserMeal(ref);
        }

        return getUserMealsWithPartsById(userMeal.mealId);
    }

    @Query("DELETE FROM user_meals")
    abstract public void deleteAll();
}
