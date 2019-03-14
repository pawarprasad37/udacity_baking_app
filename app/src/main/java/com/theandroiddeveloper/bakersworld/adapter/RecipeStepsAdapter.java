package com.theandroiddeveloper.bakersworld.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.model.RecipeStep;

import io.realm.RealmList;

public class RecipeStepsAdapter extends BaseAdapter {
    private Context context;
    private RealmList<RecipeStep> recipeStepRealmList;
    private int selectedPosition;

    public RecipeStepsAdapter(Context context, RealmList<RecipeStep> recipeStepRealmList,
                              int selectedStepIndex) {
        this.context = context;
        this.recipeStepRealmList = recipeStepRealmList;
        this.selectedPosition = selectedStepIndex;
    }

    @Override
    public int getCount() {
        return recipeStepRealmList.size();
    }

    @Override
    public RecipeStep getItem(int position) {
        return recipeStepRealmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipeStepRealmList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) convertView;
        if (textView == null) {
            textView = getTextViewInstance();
        }
        if (selectedPosition == position) {
            textView.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.colorPrimaryDarkTransparent));
        }
        textView.setText(getItem(position).getShortDescription());
        return textView;
    }

    private TextView getTextViewInstance() {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = context.getResources().getDimensionPixelOffset(R.dimen.margin_padding_medium);
        textView.setPadding(padding, padding, padding, padding);
        textView.setMinHeight((int) (context.getResources()
                .getDimensionPixelOffset(R.dimen.minimum_button_heght) * 1.5));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        return textView;
    }
}
