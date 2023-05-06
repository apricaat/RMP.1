package com.example.meal_builder.data.repositories;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.meal_builder.data.DB;
import com.example.meal_builder.data.Mapper;
import com.example.meal_builder.data.data_sources.MealPartDAO;
import com.example.meal_builder.data.entities.MealPart;
import com.example.meal_builder.data.model.ChoosableMealPart;

import java.util.ArrayList;
import java.util.List;

public class PartsRepository {

    private MealPartDAO mealPartDAO;

    private MutableLiveData<List<ChoosableMealPart>> parts = new MutableLiveData<>(new ArrayList<>());

    public PartsRepository(Application app) {
        DB db = DB.getDatabase(app);
        mealPartDAO = db.mealPartDao();
    }

     public MutableLiveData<List<ChoosableMealPart>> get() {
        DB.databaseWriteExecutor.execute(() -> {
            List<MealPart> newParts = mealPartDAO.getAll();
            parts.postValue(Mapper.mapMealPartToChoosable(newParts));
        });

        return parts;
    }

    public void add(MealPart partToAdd) {
        DB.databaseWriteExecutor.execute(() -> {
            MealPart addedPart = mealPartDAO.insert(partToAdd);
            parts.getValue().add(Mapper.adaptPartToChoosable(addedPart));
            parts.postValue(parts.getValue());
        });
    }
}
