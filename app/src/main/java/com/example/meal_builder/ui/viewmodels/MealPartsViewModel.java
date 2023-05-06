package com.example.meal_builder.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meal_builder.data.entities.MealPart;
import com.example.meal_builder.data.model.ChoosableMealPart;
import com.example.meal_builder.data.repositories.PartsRepository;

import java.util.ArrayList;
import java.util.List;

public class MealPartsViewModel extends AndroidViewModel {
    PartsRepository partsRepository;

    public MutableLiveData<List<ChoosableMealPart>> possibleMealParts;
    public MutableLiveData<List<ChoosableMealPart>> chosenMealParts = new MutableLiveData<>(new ArrayList<>());

    public MealPartsViewModel(Application app) {
        super(app);
        partsRepository = new PartsRepository(app);
        possibleMealParts = partsRepository.get();
    }

    public LiveData<List<ChoosableMealPart>> getPossibleMealParts() {
        return possibleMealParts;
    }

    public LiveData<List<ChoosableMealPart>> getChosenMealParts() {
        return chosenMealParts;
    }


    public ChoosableMealPart getMealPartById(int id) {
        return possibleMealParts.getValue().stream().filter(mealPart -> mealPart.id == id).findFirst().orElse(null);
    }

    public void addChosenPartById(int id) {
        chosenMealParts.getValue().add(getMealPartById(id));
    }

    public void clearChosenParts() {
        chosenMealParts.getValue().clear();
    }

    public void addPossibleMealPart(String image, String title, int calories, int fats, int proteins, int carbohydrates) {
        partsRepository.add(new MealPart(calories, fats, proteins, carbohydrates, title, image));
    }
}
