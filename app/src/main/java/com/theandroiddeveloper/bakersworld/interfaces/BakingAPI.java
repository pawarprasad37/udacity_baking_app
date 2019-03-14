package com.theandroiddeveloper.bakersworld.interfaces;

import com.theandroiddeveloper.bakersworld.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingAPI {

    @GET("baking.json")
    Call<List<Recipe>> getAllRecipes();

}
