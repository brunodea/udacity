package udabake.brunodea.com.udabake.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import udabake.brunodea.com.udabake.model.RecipeModel;

public class RecipeWidgetViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RecipeModel rm = intent.getParcelableExtra(RecipeWidget.WIDGET_RECIPE_MODEL);
        int widget_id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new RecipeWidgetViewsFactory(this.getApplicationContext(), rm, widget_id);
    }
}
