package com.theandroiddeveloper.bakersworld.activity;

import android.os.Bundle;

import com.theandroiddeveloper.bakersworld.Constant;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.fragment.RecipeStepDetailsFragment;
import com.theandroiddeveloper.bakersworld.model.Recipe;
import com.theandroiddeveloper.bakersworld.model.RecipeStep;

public class RecipeStepDetailsActivity extends BaseActivity {
    private int activeStepIndex;
    private Recipe recipe;
    private long playerPosition;
    private RecipeStepDetailsFragment recipeStepDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        if (savedInstanceState != null) {
            activeStepIndex = savedInstanceState.getInt(Constant.AppState.ACTIVE_STEP_INDEX);
            playerPosition = savedInstanceState.getLong(Constant.AppState.VIDEO_PLAYER_POSITION);
        }
        setContentView(R.layout.activity_recipe_step_details);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int recipeId = getIntent().getIntExtra(Constant.IntentExtra.RECIPE_ID, -1);
        if (activeStepIndex == 0) {
            activeStepIndex = getIntent().getIntExtra(Constant.IntentExtra.RECIPE_STEP_INDEX,
                    -1);
        }

        if (recipeId == -1 || activeStepIndex == -1) {
            finish();
            return;
        }

        recipe = getRealmInstance()
                .where(Recipe.class)
                .equalTo(Recipe.MemberKeys.ID, recipeId)
                .findFirst();
        if (recipe == null || recipe.getSteps() == null ||
                activeStepIndex >= recipe.getSteps().size()) {
            finish();
            return;
        }

        displayFragmentForActiveStepIndex();

    }

    private void updateActivityTitle() {
        String activityTitle = "";
        if (activeStepIndex == 0) {
            activityTitle = recipe.getName();
        } else {
            activityTitle = recipe.getName() + " " + getString(R.string.step) + " " +
                    activeStepIndex + " " + getString(R.string.of) + " " +
                    (recipe.getSteps().size() - 1);
        }
        getSupportActionBar().setTitle(activityTitle);
    }

    private void displayFragmentForActiveStepIndex() {
        final RecipeStep recipeStep = recipe.getSteps().get(activeStepIndex);
        if (recipeStep == null) {
            finish();
            return;
        }

        recipeStepDetailsFragment = new RecipeStepDetailsFragment()
                .setPlayerPosition(playerPosition)
                .setIsMasterDetailFlow(getIntent()
                        .getBooleanExtra(Constant.IntentExtra.IS_MASTER_DETAIL_FLOW,
                                false))
                .setSelectedRecipeStep(recipeStep, activeStepIndex, recipe.getSteps().size());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, recipeStepDetailsFragment)
                .commit();

        recipeStepDetailsFragment.setNavButtonClickListener(
                new RecipeStepDetailsFragment.NavButtonClickListener() {
                    @Override
                    public void onPreviousStepClicked() {
                        if (activeStepIndex == 0) {
                            return;
                        }
                        playerPosition = 0;
                        activeStepIndex--;
                        displayFragmentForActiveStepIndex();
                    }

                    @Override
                    public void onNextStepClicked() {
                        if (activeStepIndex == recipe.getSteps().size() - 1) {
                            return;
                        }
                        playerPosition = 0;
                        activeStepIndex++;
                        displayFragmentForActiveStepIndex();
                    }
                });

        updateActivityTitle();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constant.AppState.ACTIVE_STEP_INDEX, activeStepIndex);
        outState.putLong(Constant.AppState.VIDEO_PLAYER_POSITION,
                recipeStepDetailsFragment.getPlayerPosition());
        super.onSaveInstanceState(outState);
    }
}
