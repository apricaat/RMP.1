package com.example.meal_builder.data.repositories;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.meal_builder.data.DB;
import com.example.meal_builder.data.Mapper;
import com.example.meal_builder.data.data_sources.UserMealDAO;
import com.example.meal_builder.data.entities.UserMealWithParts;
import com.example.meal_builder.data.model.UserMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealsRepository {
    private UserMealDAO userMealDAO;

    private MutableLiveData<List<UserMeal>> userMeals = new MutableLiveData<>(new ArrayList<>());

    public MealsRepository(Application app) {
        DB db = DB.getDatabase(app);
        userMealDAO = db.userMealDAO();
    }

    public MutableLiveData<List<UserMeal>> getMeals() {
        DB.databaseWriteExecutor.execute(() -> {
            List<UserMealWithParts> meals = userMealDAO.getUserMealsWithParts();
            userMeals.postValue(Mapper.mapUserMealToClient(meals));
        });

        return userMeals;
    }

    public void addEmptyMeal() {
        DB.databaseWriteExecutor.execute(() -> {
            UserMealWithParts meal = userMealDAO.insert(new com.example.meal_builder.data.entities.UserMeal());
            userMeals.getValue().add(Mapper.adaptUserMealToClient(meal));
            userMeals.postValue(userMeals.getValue());
        });
    }

    public void updateMeal(UserMeal mealToUpdate) {
        DB.databaseWriteExecutor.execute(() -> {
            UserMealWithParts updatedMeal = userMealDAO.update(Mapper.adaptUserMealToDB(mealToUpdate), mealToUpdate.parts);
            userMeals.getValue().replaceAll(
                    userMeal -> Objects.equals(userMeal.id, Integer.valueOf((int) updatedMeal.userMeal.mealId)) ?
                            Mapper.adaptUserMealToClient(updatedMeal) :
                            userMeal
            );

            userMeals.postValue(userMeals.getValue());
        });
    }
}
