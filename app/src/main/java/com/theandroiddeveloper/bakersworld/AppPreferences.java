package com.theandroiddeveloper.bakersworld;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theandroiddeveloper.bakersworld.model.Recipe;

public class AppPreferences {
    private static AppPreferences INSTANCE;
    private Context context;

    private AppPreferences(Context context) {
        this.context = context;
    }

    public static AppPreferences getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPreferences(context);
        }
        return INSTANCE;
    }

    public Recipe getWidgetRecipe() {
        SharedPreferences preferences = context
                .getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String json = preferences.getString(Constant.AppPreferences.WIDGET_RECIPE, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, new TypeToken<Recipe>() {
        }.getType());
    }

    public void setWidgetRecipe(Recipe recipe) {
        synchronized (this) {
            SharedPreferences preferences = context
                    .getSharedPreferences(context.getString(R.string.app_name),
                            Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String json = new Gson().toJson(recipe, new TypeToken<Recipe>() {
            }.getType());
            editor.putString(Constant.AppPreferences.WIDGET_RECIPE, json);
            editor.commit();
        }
    }
}
