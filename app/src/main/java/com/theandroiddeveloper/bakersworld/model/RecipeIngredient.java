package com.theandroiddeveloper.bakersworld.model;

import com.google.gson.annotations.SerializedName;

public class RecipeIngredient {

    @SerializedName("ingredient")
    private String name;

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("measure")
    private String measure;

    public String getName() {
        return name;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }
}
