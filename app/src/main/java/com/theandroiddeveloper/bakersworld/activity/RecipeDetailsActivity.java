package com.theandroiddeveloper.bakersworld.activity;

import android.content.Intent;
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
import com.theandroiddeveloper.bakersworld.model.RecipeStep;

public class RecipeDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
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

    private void displayRecipeDetails(final Recipe selectedRecipe) {
        getSupportActionBar().setTitle(selectedRecipe.getName());

        FrameLayout flStepsFragment = findViewById(R.id.flStepsFragment);
        FrameLayout flStepDescriptionFragment = findViewById(R.id.flStepDescriptionFragment);

        final boolean isMasterDetailFlow = (flStepDescriptionFragment != null);

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
                    .setIsMasterDetailFlow(true)
                    .setSelectedRecipeStep(selectedRecipe.getSteps().first(), 0,
                            selectedRecipe.getSteps().size());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flStepDescriptionFragment, recipeStepDetailsFragment)
                    .commit();
        }

        final RecipeStepDetailsFragment finalRecipeStepDetailsFragment = recipeStepDetailsFragment;
        recipeStepsFragment.setStepClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeStep selectedRecipeStep = selectedRecipe.getSteps().get(position);
                onRecipeStepSelected(selectedRecipe, selectedRecipeStep, position,
                        isMasterDetailFlow, finalRecipeStepDetailsFragment);
            }
        });
    }

    private void onRecipeStepSelected(Recipe selectedRecipe, RecipeStep selectedRecipeStep,
                                      int stepIndex, boolean isMasterDetailFlow,
                                      RecipeStepDetailsFragment recipeStepDetailsFragment) {
        if (selectedRecipeStep == null) {
            throw new RuntimeException("Step cannot be null.");
        }
        if (isMasterDetailFlow && recipeStepDetailsFragment != null) {
            //update step details fragment
            recipeStepDetailsFragment.setSelectedRecipeStep(selectedRecipeStep, stepIndex,
                    selectedRecipe.getSteps().size());
            //navigation buttons are hidden in master-detail flow.
        } else {
            //start new activity for step details
            Intent intent = new Intent(getApplicationContext(), RecipeStepDetailsActivity.class);
            intent.putExtra(Constant.IntentExtra.RECIPE_ID, selectedRecipe.getId());
            intent.putExtra(Constant.IntentExtra.RECIPE_STEP_INDEX, stepIndex);
            intent.putExtra(Constant.IntentExtra.IS_MASTER_DETAIL_FLOW, isMasterDetailFlow);
            startActivity(intent);
        }
    }
}
