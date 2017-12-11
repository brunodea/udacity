package udabake.brunodea.com.udabake.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeModel;

public class RecipeWidget extends AppWidgetProvider {
    private RecipeModel mRecipeModel;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences sp = context.getSharedPreferences(RecipeWidgetConfigActivity.WIDGET_SHARED_PREFS,
                Context.MODE_PRIVATE);
        if (sp != null && sp.contains(RecipeWidgetConfigActivity.WIDGET_RECIPE_MODEL + "_" + appWidgetId)) {
            String s = sp.getString(RecipeWidgetConfigActivity.WIDGET_RECIPE_MODEL + "_" + appWidgetId, "");
            Gson gson = new Gson();
            RecipeModel rm = gson.fromJson(s, RecipeModel.class);

            Intent intent = new Intent(context, RecipeWidgetViewsService.class);
            intent.putExtra(RecipeWidgetConfigActivity.WIDGET_RECIPE_MODEL, rm);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list);
            views.setTextViewText(R.id.tv_widget_recipe_name, rm.getName());
            views.setRemoteAdapter(R.id.lv_widget_ingredients, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
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
}

