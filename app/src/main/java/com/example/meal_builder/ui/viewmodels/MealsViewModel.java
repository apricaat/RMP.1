package com.example.meal_builder.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meal_builder.data.model.ChoosableMealPart;
import com.example.meal_builder.data.model.MealPart;
import com.example.meal_builder.data.model.UserMeal;
import com.example.meal_builder.data.repositories.MealsRepository;

import java.util.List;
import java.util.Objects;

public class MealsViewModel extends AndroidViewModel {
    MealsRepository mRepository;
    MutableLiveData<List<UserMeal>> userMeals;
    MutableLiveData<UserMeal> editingMeal = new MutableLiveData<>(null);

    public MealsViewModel(Application app) {
        super(app);
        mRepository = new MealsRepository(app);
        userMeals = mRepository.getMeals();
    }

    public LiveData<List<UserMeal>> getUserMeals() { return userMeals;}

    public LiveData<UserMeal> getEditingMeal() {return editingMeal;}

    public void addNewMeal() {mRepository.addEmptyMeal();}

    public UserMeal getMealById(int id) {
        return userMeals.getValue().stream().filter(userMeal -> userMeal.id == id).findFirst().orElse(null);
    }

    public void setEditingMealById(int id) {
        editingMeal.setValue(new UserMeal(getMealById(id)));
    }

    public void saveEditingMeal() {
        mRepository.updateMeal(editingMeal.getValue());
    }

    public void changeEditingMeal(UserMeal meal) {
        editingMeal.setValue(meal);
    }

    public void addPartsToEditingMeal(List<ChoosableMealPart> parts) {
        UserMeal meal = editingMeal.getValue();
        for (ChoosableMealPart part : parts) {
            meal.parts.add(new MealPart(part));
        }
    }
}
