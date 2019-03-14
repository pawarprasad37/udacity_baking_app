package com.theandroiddeveloper.bakersworld.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.theandroiddeveloper.bakersworld.CommonUtil;
import com.theandroiddeveloper.bakersworld.Constant;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.fragment.RecipeStepDetailsFragment;
import com.theandroiddeveloper.bakersworld.fragment.RecipeStepsFragment;
import com.theandroiddeveloper.bakersworld.model.Recipe;

public class RecipeDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recipe_details_activity);
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

        FrameLayout flStepsFragment = findViewById(R.id.flStepsFragment);
        FrameLayout flStepDescriptionFragment = findViewById(R.id.flStepDescriptionFragment);

        boolean isMasterDetailFlow = (flStepDescriptionFragment != null);

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment()
                .setSelectedStepIndex(isMasterDetailFlow ? 0 : -1)
                .setSelectedRecipe(selectedRecipe);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flStepsFragment, recipeStepsFragment)
                .commit();

        RecipeStepDetailsFragment recipeStepDetailsFragment = null;
        if (isMasterDetailFlow) {
            recipeStepDetailsFragment = new RecipeStepDetailsFragment()
                    .setSelectedRecipeStep(selectedRecipe.getSteps().first());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flStepDescriptionFragment, recipeStepDetailsFragment)
                    .commit();
        }

        recipeStepsFragment.setStepClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonUtil.showToast(getApplicationContext(), "step clicked",
                        Toast.LENGTH_SHORT);
            }
        });
    }
}
