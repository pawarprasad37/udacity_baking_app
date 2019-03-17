package com.theandroiddeveloper.bakersworld.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.theandroiddeveloper.bakersworld.AppWidget;
import com.theandroiddeveloper.bakersworld.CommonUtil;
import com.theandroiddeveloper.bakersworld.Constant;
import com.theandroiddeveloper.bakersworld.NetworkManager;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.adapter.HomeRecipeAdapter;
import com.theandroiddeveloper.bakersworld.model.Recipe;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements Callback<List<Recipe>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getIntent().getBooleanExtra(Constant.IntentExtra.IS_WIDGET_RECIPE_SELECTION_MODE,
                false)) {
            getSupportActionBar().setTitle(getString(R.string.activity_title_select_widget_recipe));
        }

        AppWidget.updateWidget(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        RealmResults<Recipe> recipeRealmResults = getRealmInstance()
                .where(Recipe.class)
                .findAll();
        displayRecipes(recipeRealmResults);
        if (recipeRealmResults.isEmpty()) {
            if (!CommonUtil.hasNetworkConnectivity(this)) {
                CommonUtil.showToast(this, getString(R.string.error_no_internet),
                        Toast.LENGTH_LONG);
                return;
            }
            showProgressDialog();
            NetworkManager.getInstance()
                    .getAllRecipes(this);
        }
    }

    private void displayRecipes(RealmResults<Recipe> recipeRealmResults) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        View tabIdentifier = findViewById(R.id.tabIdentifier);
        boolean isTabletDevice = (tabIdentifier != null);
        if (isTabletDevice) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),
                    3));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        HomeRecipeAdapter adapter = new HomeRecipeAdapter(this, recipeRealmResults,
                getIntent().getBooleanExtra(Constant.IntentExtra.IS_WIDGET_RECIPE_SELECTION_MODE,
                        false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponse(@NonNull Call<List<Recipe>> call,
                           @NonNull Response<List<Recipe>> response) {
        if (isFinishing()) {
            return;
        }
        hideProgressDialog();
        if (!response.isSuccessful()) {
            CommonUtil.showToast(getApplicationContext(),
                    getString(R.string.error_failed_to_fetch_recipes), Toast.LENGTH_SHORT);
            return;
        }
        final List<Recipe> recipeList = response.body();
        getRealmInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealm(recipeList);
            }
        });
    }

    @Override
    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
        if (isFinishing()) {
            return;
        }
        hideProgressDialog();
        CommonUtil.showToast(getApplicationContext(),
                getString(R.string.error_failed_to_fetch_recipes), Toast.LENGTH_SHORT);
    }
}
