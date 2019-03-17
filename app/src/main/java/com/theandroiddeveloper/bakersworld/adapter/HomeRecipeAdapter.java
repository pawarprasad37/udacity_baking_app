package com.theandroiddeveloper.bakersworld.adapter;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theandroiddeveloper.bakersworld.AppPreferences;
import com.theandroiddeveloper.bakersworld.AppWidget;
import com.theandroiddeveloper.bakersworld.CommonUtil;
import com.theandroiddeveloper.bakersworld.Constant;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.activity.RecipeDetailsActivity;
import com.theandroiddeveloper.bakersworld.model.Recipe;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HomeRecipeAdapter extends RecyclerView.Adapter {
    private Context context;
    private RealmResults<Recipe> recipeRealmResults;
    private boolean isWidgetRecipeSelectionMode;

    public HomeRecipeAdapter(Context context, RealmResults<Recipe> recipeRealmResults,
                             boolean isWidgetRecipeSelectionMode) {
        this.context = context;
        this.recipeRealmResults = recipeRealmResults;
        this.isWidgetRecipeSelectionMode = isWidgetRecipeSelectionMode;

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

            if (isWidgetRecipeSelectionMode) {
                setWidgetRecipe(context, selectedRecipe);
            } else {
                Intent intent = new Intent(context, RecipeDetailsActivity.class);
                intent.putExtra(Constant.IntentExtra.RECIPE_ID, selectedRecipe.getId());
                context.startActivity(intent);
            }
        }
    }

    private void setWidgetRecipe(final Context context, final Recipe selectedRecipe) {
        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.widget_recipe_select_confirmation))
                .setPositiveButton(context.getString(R.string.select),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                synchronized (HomeRecipeAdapter.this) {
                                    Realm realm = Realm.getDefaultInstance();
                                    Recipe unmanagedRecipe = realm.copyFromRealm(selectedRecipe);
                                    realm.close();
                                    AppPreferences.getInstance(context)
                                            .setWidgetRecipe(unmanagedRecipe);
                                    AppWidget.updateWidget(context);
                                    CommonUtil.showToast(context,
                                            context.getString(R.string.widget_recipe_set_msg),
                                            Toast.LENGTH_SHORT);
                                }
                            }
                        })
                .setNegativeButton(context.getString(R.string.cancel), null)
                .create()
                .show();
    }
}
