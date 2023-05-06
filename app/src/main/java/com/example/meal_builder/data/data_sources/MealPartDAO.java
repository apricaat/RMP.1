package com.example.meal_builder.data.data_sources;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.meal_builder.data.entities.MealPart;

import java.util.List;

@Dao
public abstract class MealPartDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertPart(MealPart mealPart);

    @Transaction
    public MealPart insert(MealPart mealPart) {
        long id = insertPart(mealPart);
        return getById(id);
    }

    @Query("DELETE FROM meal_parts")
    public abstract void deleteAll();

    @Query("SELECT * FROM meal_parts")
    public abstract List<MealPart> getAll();

    @Query("SELECT * FROM meal_parts WHERE partId = :id LIMIT 1")
    public abstract MealPart getById(long id);
}
