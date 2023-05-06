package com.example.meal_builder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.meal_builder.data.data_sources.MealPartDAO;
import com.example.meal_builder.data.data_sources.UserMealDAO;
import com.example.meal_builder.data.entities.MealPart;
import com.example.meal_builder.data.entities.MealPartCrossRef;
import com.example.meal_builder.data.entities.UserMeal;
import com.example.meal_builder.data.entities.UserMealWithParts;
import com.example.meal_builder.data.model.ChoosableMealPart;
import com.example.meal_builder.domain.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Database(entities = {MealPart.class, MealPartCrossRef.class, UserMeal.class}, version = 3, exportSchema = false)
public abstract class DB extends RoomDatabase {

    public abstract MealPartDAO mealPartDao();
    public  abstract UserMealDAO userMealDAO();

    private static volatile DB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback DBPopulationCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                MealPartDAO partsDAO = INSTANCE.mealPartDao();
                UserMealDAO userMealDAO = INSTANCE.userMealDAO();
                partsDAO.deleteAll();
                userMealDAO.deleteAll();

                String[] parts = new String[] {"Сыр", "Колбаса", "Салат", "Хлеб", "Творог", "Мюсли", "Банан", "Чай", "Квас", "Помидор"};
                String[] images = new String[] {"breakfast1", "fried_eggs", "salad", "sandwitch", "tomatoes"};
                ArrayList<Long> partIds = new ArrayList<Long>();

                for (String part : parts) {
                    long id = partsDAO.insert(new MealPart(Util.random(0, 674), Util.random(0, 234), Util.random(0, 319), Util.random(0, 199), part, images[Util.random(0, images.length - 1)])).partId;
                    partIds.add(id);
                }

                UserMeal[] meals = new UserMeal[] {new UserMeal( "Мой завтрак", "breakfast1"), new UserMeal( "Мой перекус", "salad")};
                ArrayList<Long> mealIds = new ArrayList<>();

                for (UserMeal userMeal : meals) {
                    long id = userMealDAO.insertUserMeal(userMeal);
                    mealIds.add(id);
                }

                int basePartsNumber = 2;

                for (Long mealId : mealIds) {
                    for (int i = 0; i < basePartsNumber; i ++) {
                        userMealDAO.assignMealPartToUserMeal(new MealPartCrossRef(mealId, partIds.get(Util.random(0, partIds.size() - 1)), Util.random(10, 1000)));
                    }
                }
            });
        }
    };

    public static DB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DB.class, "meals_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(DBPopulationCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}