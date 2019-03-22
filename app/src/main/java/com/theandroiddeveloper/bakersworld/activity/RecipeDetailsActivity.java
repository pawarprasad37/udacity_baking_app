package com.theandroiddeveloper.bakersworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
    private RecipeStepsFragment recipeStepsFragment;
    private RecipeStepDetailsFragment recipeStepDetailsFragment;
    private int selectedStepIndex;
    private long playerPosition;
    private Parcelable scrollState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.layout_recipe_details_activity);

        if (savedInstanceState != null) {
            scrollState = savedInstanceState
                    .getParcelable(Constant.AppState.STEP_LIST_SCROLL_STATE);
            selectedStepIndex = savedInstanceState
                    .getInt(Constant.AppState.STEP_LIST_SELECTED_INDEX);
            playerPosition = savedInstanceState
                    .getLong(Constant.AppState.VIDEO_PLAYER_POSITION);
        } else {
            boolean isMasterDetailFlow = (findViewById(R.id.flStepDescriptionFragment) != null);
            selectedStepIndex = isMasterDetailFlow ? 0 : -1;
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        scrollState = recipeStepsFragment.getListScrollState();
        playerPosition = recipeStepDetailsFragment != null ?
                recipeStepDetailsFragment.getPlayerPosition() : 0;
    }

    private void displayRecipeDetails(final Recipe selectedRecipe) {
        getSupportActionBar().setTitle(selectedRecipe.getName());

        FrameLayout flStepsFragment = findViewById(R.id.flStepsFragment);
        FrameLayout flStepDescriptionFragment = findViewById(R.id.flStepDescriptionFragment);

        final boolean isMasterDetailFlow = (flStepDescriptionFragment != null);

        recipeStepsFragment = new RecipeStepsFragment()
                .setListScrollState(scrollState)
                .setSelectedStepIndex(selectedStepIndex)
                .setSelectedRecipe(selectedRecipe);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flStepsFragment, recipeStepsFragment)
                .commit();

        recipeStepDetailsFragment = null;
        if (isMasterDetailFlow) {
            recipeStepDetailsFragment = new RecipeStepDetailsFragment()
                    .setIsMasterDetailFlow(true)
                    .setPlayerPosition(playerPosition)
                    .setSelectedRecipeStep(selectedRecipe.getSteps().get(selectedStepIndex),
                            selectedStepIndex,
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
                playerPosition = 0;
                RecipeStep selectedRecipeStep = selectedRecipe.getSteps().get(position);
                onRecipeStepSelected(selectedRecipe, selectedRecipeStep, position,
                        isMasterDetailFlow, finalRecipeStepDetailsFragment);
                recipeStepsFragment.setSelectedStepIndex(position);
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
            recipeStepDetailsFragment
                    .setPlayerPosition(0)
                    .setSelectedRecipeStep(selectedRecipeStep, stepIndex,
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constant.AppState.STEP_LIST_SCROLL_STATE, scrollState);
        outState.putInt(Constant.AppState.STEP_LIST_SELECTED_INDEX,
                recipeStepsFragment.getSelectedIndex());
        outState.putLong(Constant.AppState.VIDEO_PLAYER_POSITION, playerPosition);
        super.onSaveInstanceState(outState);
    }

}
