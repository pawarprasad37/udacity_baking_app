package com.theandroiddeveloper.bakersworld.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.adapter.RecipeStepsAdapter;
import com.theandroiddeveloper.bakersworld.model.Recipe;

/**
 * Created by prasad on 15/03/2019.
 */
public class RecipeStepsFragment extends Fragment {
    private Recipe selectedRecipe;
    private ListView listView;
    private AdapterView.OnItemClickListener onStepClickListener;
    private int selectedStepIndex;
    private Parcelable scrollState;

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    public RecipeStepsFragment setSelectedRecipe(@NonNull Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
        return this;
    }

    public RecipeStepsFragment setSelectedStepIndex(int selectedStepIndex) {
        this.selectedStepIndex = selectedStepIndex;
        updateListSelection();
        return this;
    }

    public RecipeStepsFragment setStepClickListener(@NonNull AdapterView.OnItemClickListener
                                                            onStepClickListener) {
        this.onStepClickListener = onStepClickListener;
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container,
                false);
        listView = rootView.findViewById(R.id.listView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        renderStepsList();
    }

    private void renderStepsList() {
        RecipeStepsAdapter adapter = new RecipeStepsAdapter(getContext(),
                selectedRecipe.getSteps(), selectedStepIndex);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onStepClickListener);
        if (scrollState != null) {
            listView.onRestoreInstanceState(scrollState);
        }
    }

    private void updateListSelection() {
        if (!isAdded() || listView == null || listView.getAdapter() == null) {
            return;
        }
        ((RecipeStepsAdapter) listView.getAdapter())
                .setSelectedIndex(selectedStepIndex);
    }

    public int getSelectedIndex() {
        return selectedStepIndex;
    }

    public Parcelable getListScrollState() {
        if (listView == null) return null;
        return listView.onSaveInstanceState();
    }

    public RecipeStepsFragment setListScrollState(Parcelable scrollState) {
        this.scrollState = scrollState;
        return this;
    }
}
