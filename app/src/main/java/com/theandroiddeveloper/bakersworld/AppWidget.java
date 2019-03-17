package com.theandroiddeveloper.bakersworld;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.theandroiddeveloper.bakersworld.activity.HomeActivity;
import com.theandroiddeveloper.bakersworld.model.Recipe;
import com.theandroiddeveloper.bakersworld.model.RecipeIngredient;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Recipe widgetRecipe = AppPreferences
                .getInstance(context)
                .getWidgetRecipe();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        if (widgetRecipe == null) {
            views.setTextViewText(R.id.appwidget_title,
                    context.getString(R.string.widget_no_recie_selected));
            views.setViewVisibility(R.id.appwidget_ingredients, View.GONE);
        } else {
            views.setTextViewText(R.id.appwidget_title,
                    widgetRecipe.getName());
            views.setViewVisibility(R.id.appwidget_ingredients, View.VISIBLE);
            views.setTextViewText(R.id.appwidget_ingredients, getIngredientsString(widgetRecipe));
        }
        views.setOnClickPendingIntent(R.id.btChange, getEditClickPendingIntent(context));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent getEditClickPendingIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(Constant.IntentExtra.IS_WIDGET_RECIPE_SELECTION_MODE, true);
        return PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static String getIngredientsString(Recipe widgetRecipe) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < widgetRecipe.getIngredients().size(); i++) {
            RecipeIngredient recipeIngredient = widgetRecipe.getIngredients().get(i);
            String oneIngredient = (i + 1) + "." + recipeIngredient.getName() + " (" +
                    recipeIngredient.getQuantity() + " " + recipeIngredient.getMeasure() + ")";
            stringBuilder.append(oneIngredient);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateWidget(Context context) {
        Intent updateIntent = new Intent(context, AppWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, AppWidget.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(updateIntent);
    }
}

