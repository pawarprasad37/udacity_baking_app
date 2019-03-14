package com.theandroiddeveloper.bakersworld.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.model.RecipeStep;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailsFragment extends Fragment {
    private RecipeStep selectedRecipeStep;

    public RecipeStepDetailsFragment() {
        // Required empty public constructor
    }

    public RecipeStepDetailsFragment setSelectedRecipeStep(RecipeStep selectedRecipeStep) {
        this.selectedRecipeStep = selectedRecipeStep;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_step_details, container, false);
    }

}
