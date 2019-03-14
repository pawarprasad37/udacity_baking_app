package com.theandroiddeveloper.bakersworld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theandroiddeveloper.bakersworld.interfaces.BakingAPI;
import com.theandroiddeveloper.bakersworld.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private static NetworkManager INSTANCE;

    private NetworkManager() {

    }

    public static NetworkManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkManager();
        }
        return INSTANCE;
    }

    public void getAllRecipes(Callback<List<Recipe>> callback) {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        BakingAPI bakingAPI = retrofit.create(BakingAPI.class);
        Call<List<Recipe>> call = bakingAPI.getAllRecipes();
        call.enqueue(callback);
    }

}
