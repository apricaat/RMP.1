package com.example.meal_builder.data.data_sources;
import androidx.lifecycle.LiveData;

import com.example.meal_builder.data.model.ChoosableMealPart;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
public class PartsSource {
    private static String[] parts = new String[] {"Иванова П.П.", "Лаптева М.Б.", "Салат", "Хлеб", "Творог", "Мюсли", "Банан", "Чай", "Квас", "Помидор"};
    private static String[] images = new String[] {"photo1", "photo2", "photo3", "sandwitch", "tomatoes"};
    public static LiveData<List<ChoosableMealPart>> choosableParts = new LiveData<List<ChoosableMealPart>>(){
        {
            for (int i = 0; i < parts.length; i++) {
                add(new ChoosableMealPart(i, random(0, 674), random(0, 234), random(0, 319), random(0, 199), parts[i], images[random(0, images.length - 1)]));
            }
        }
    };
    static private  int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public LiveData<List<ChoosableMealPart>> get() {
        return new LiveData<List<>(choosableParts);
    }

    public ChoosableMealPart add(ChoosableMealPart toAdd) {
        toAdd.id = choosableParts.size();
        choosableParts.add(toAdd);
        return toAdd;
    }
}