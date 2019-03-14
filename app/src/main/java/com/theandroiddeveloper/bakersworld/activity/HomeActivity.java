package com.theandroiddeveloper.bakersworld.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.theandroiddeveloper.bakersworld.CommonUtil;
import com.theandroiddeveloper.bakersworld.NetworkManager;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements Callback<List<Recipe>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showProgressDialog();
        NetworkManager.getInstance()
                .getAllRecipes(this);
    }

    @Override
    public void onResponse(@NonNull Call<List<Recipe>> call,
                           @NonNull Response<List<Recipe>> response) {
        hideProgressDialog();
        if (!response.isSuccessful()) {
            CommonUtil.showToast(getApplicationContext(),
                    getString(R.string.error_failed_to_fetch_recipes), Toast.LENGTH_SHORT);
            return;
        }
        List<Recipe> recipeList = response.body();
        Log.i("HomeActivity", recipeList.toString());
    }

    @Override
    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
        hideProgressDialog();
        CommonUtil.showToast(getApplicationContext(),
                getString(R.string.error_failed_to_fetch_recipes), Toast.LENGTH_SHORT);
    }
}
