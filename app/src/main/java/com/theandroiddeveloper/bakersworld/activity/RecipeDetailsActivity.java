package com.theandroiddeveloper.bakersworld.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.theandroiddeveloper.bakersworld.CommonUtil;
import com.theandroiddeveloper.bakersworld.Constant;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.model.Recipe;

public class RecipeDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int selectedRecipeId = getIntent().getIntExtra(Constant.IntentExtra.RECIPE_ID,
                -1);
        Recipe selectedRecipe = getRealmInstance()
                .where(Recipe.class)
                .equalTo(Recipe.MemberKeys.ID, selectedRecipeId)
                .findFirst();
        if (selectedRecipe == null) {
            CommonUtil.showToast(this, getString(R.string.error_invalid_recipe),
                    Toast.LENGTH_LONG);
            finish();
            return;
        }
        displayRecipeDetails(selectedRecipe);
    }

    private void displayRecipeDetails(Recipe selectedRecipe) {
        getSupportActionBar().setTitle(selectedRecipe.getName());
    }
}
