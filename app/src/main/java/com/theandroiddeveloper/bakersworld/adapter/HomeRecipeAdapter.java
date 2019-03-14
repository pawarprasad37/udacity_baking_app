package com.theandroiddeveloper.bakersworld.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theandroiddeveloper.bakersworld.CommonUtil;
import com.theandroiddeveloper.bakersworld.Constant;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.activity.RecipeDetailsActivity;
import com.theandroiddeveloper.bakersworld.model.Recipe;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HomeRecipeAdapter extends RecyclerView.Adapter {
    private Context context;
    private RealmResults<Recipe> recipeRealmResults;

    public HomeRecipeAdapter(Context context, RealmResults<Recipe> recipeRealmResults) {
        this.context = context;
        this.recipeRealmResults = recipeRealmResults;

        //register update listener
        recipeRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Recipe>>() {
            @Override
            public void onChange(@NonNull RealmResults<Recipe> recipeRealmResults) {
                HomeRecipeAdapter.this.recipeRealmResults = recipeRealmResults;
                HomeRecipeAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.list_item_home_recipes, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Recipe selectedRecipe = recipeRealmResults.get(i);
        ((ViewHolder) viewHolder).textView
                .setText(selectedRecipe.getName());

        Glide.with(context)
                .load(selectedRecipe.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_image_placeholder)
                .into(((ViewHolder) viewHolder).imageView);
    }

    @Override
    public int getItemCount() {
        return recipeRealmResults.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            this.imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe selectedRecipe = recipeRealmResults.get(getAdapterPosition());

            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra(Constant.IntentExtra.RECIPE_ID, selectedRecipe.getId());
            context.startActivity(intent);
        }
    }
}
