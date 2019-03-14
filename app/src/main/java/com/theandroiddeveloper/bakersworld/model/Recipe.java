package com.theandroiddeveloper.bakersworld.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Recipe extends RealmObject {

    @SerializedName(MemberKeys.ID)
    private int id;

    @SerializedName(MemberKeys.NAME)
    private String name;

    @SerializedName(MemberKeys.INGREDIENTS)
    private RealmList<RecipeIngredient> ingredients;

    @SerializedName(MemberKeys.STEPS)
    private RealmList<RecipeStep> steps;

    @SerializedName(MemberKeys.SERVINGS)
    private int servings;

    @SerializedName(MemberKeys.IMAGE)
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RealmList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public RealmList<RecipeStep> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public interface MemberKeys {
        String ID = "id";
        String NAME = "name";
        String INGREDIENTS = "ingredients";
        String STEPS = "steps";
        String SERVINGS = "servings";
        String IMAGE = "image";
    }
}
