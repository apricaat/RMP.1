package com.example.meal_builder.data;

import com.example.meal_builder.data.entities.MealPart;
import com.example.meal_builder.data.entities.MealPartCrossRef;
import com.example.meal_builder.data.entities.UserMealWithParts;
import com.example.meal_builder.data.model.ChoosableMealPart;
import com.example.meal_builder.data.model.UserMeal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Mapper {
    public static ChoosableMealPart adaptPartToChoosable(MealPart o) {
        return new ChoosableMealPart((int) o.partId, o.calories, o.fats, o.proteins, o.carbohydrates, o.name, o.image);
    }

    public static List<ChoosableMealPart> mapMealPartToChoosable(List<MealPart> parts) {
        return parts.stream().map(
                Mapper::adaptPartToChoosable
        ).collect(Collectors.toList());
    }

    public static List<com.example.meal_builder.data.model.MealPart> mapMealPartToClient(List<MealPart> parts, List<MealPartCrossRef> crossRefs) {
        AtomicInteger i = new AtomicInteger();
        return parts.stream().map(mealPart -> {
            com.example.meal_builder.data.model.MealPart newPart =
                    new com.example.meal_builder.data.model.MealPart(adaptPartToChoosable(mealPart), crossRefs.get(i.get()).grams);
            i.getAndIncrement();
            return  newPart;
        }).collect(Collectors.toList());
    }

    public static UserMeal adaptUserMealToClient(UserMealWithParts o) {
        return new UserMeal((int) o.userMeal.mealId, o.userMeal.name, o.userMeal.image, mapMealPartToClient(o.mealParts, o.partsCrossRef));
    }

    public static List<UserMeal> mapUserMealToClient(List<UserMealWithParts> userMealWithPartsList) {
        return userMealWithPartsList.stream().map(Mapper::adaptUserMealToClient).collect(Collectors.toList());
    }

    public static com.example.meal_builder.data.entities.UserMeal adaptUserMealToDB(UserMeal o) {
        return new com.example.meal_builder.data.entities.UserMeal((long) o.id, o.name, o.image);
    }
}
