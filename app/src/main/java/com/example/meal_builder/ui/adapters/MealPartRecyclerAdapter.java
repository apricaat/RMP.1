package com.example.meal_builder.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meal_builder.data.model.ChoosableMealPart;
import com.example.meal_builder.ui.viewmodels.MealPartsViewModel;
import com.example.meal_builder.R;

import java.util.List;

public class MealPartRecyclerAdapter extends RecyclerView.Adapter< MealPartRecyclerAdapter.ViewHolder>{
    private final String TAG = this.getClass().getSimpleName();
    private final LayoutInflater inflater;
    private List<ChoosableMealPart> items;
    private Context context;

    private MealPartsViewModel mealPartsViewModel;


    public MealPartRecyclerAdapter(Context context, MealPartsViewModel mealPartsViewModel, Fragment fragment) {
        this.context = context;
        this.items = mealPartsViewModel.getPossibleMealParts().getValue();
        this.inflater = LayoutInflater.from(context);
        this.mealPartsViewModel = mealPartsViewModel;

        mealPartsViewModel.getPossibleMealParts().observe(fragment.getViewLifecycleOwner(), (possibleMealParts) -> {
            this.items = possibleMealParts;
            this.notifyDataSetChanged();
        });
    }

    @Override
    public MealPartRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meal_part_variant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealPartRecyclerAdapter.ViewHolder holder, int position) {
        ChoosableMealPart item = items.get(position);
        holder.title.setText(item.name);

        holder.calories.setText(String.valueOf(item.calories));
        holder.fats.setText(String.valueOf(item.fats));
        holder.protein.setText(String.valueOf(item.protein));
        holder.carbohydrates.setText(String.valueOf(item.carbohydrates));

        String uri = "@drawable/" + item.image;
        holder.image.setImageResource(context.getResources().getIdentifier(uri, null, context.getPackageName()));

        holder.container.setOnClickListener((layout) -> {
            Log.i(TAG, "ItemClicked!");
            Toast.makeText(context, "ItemClicked!", Toast.LENGTH_SHORT).show();

            mealPartsViewModel.addChosenPartById(item.id);
            holder.container.setBackgroundColor(context.getColor(R.color.main_light));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView title;
        TextView calories;
        TextView fats;
        TextView protein;
        TextView carbohydrates;
        ImageView image;
        ViewHolder(View view){
            super(view);

            container = view.findViewById(R.id.part_variant);
            title = view.findViewById(R.id.part_title);
            calories = view.findViewById(R.id.part_calories);
            fats = view.findViewById(R.id.part_fats);
            protein = view.findViewById(R.id.part_protein);
            carbohydrates = view.findViewById(R.id.part_carbohydrates);
            image = view.findViewById(R.id.part_image);
        }
    }
}

