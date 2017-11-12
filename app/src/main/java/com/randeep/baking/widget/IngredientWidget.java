package com.randeep.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import com.randeep.baking.R;
import com.randeep.baking.ui.RecipeListActivity;
import com.randeep.baking.util.Prefs;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        Prefs prefs = new Prefs(context);

        String recipeName = prefs.getRecipeName();

        if (recipeName.equals("")) {
            views.setTextViewText(R.id.recipe_text,
                    context.getString(R.string.no_recipe));


        } else {
            views.setTextViewText(R.id.recipe_text,recipeName);

            Intent ingredientListIntent = new Intent(context, IngredientWidgetIntentService.class);
            ingredientListIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            ingredientListIntent.setData(Uri.parse(ingredientListIntent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(R.id.ingredient_list, ingredientListIntent);
        }


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

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
}

