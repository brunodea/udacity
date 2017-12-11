package udabake.brunodea.com.udabake.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import udabake.brunodea.com.udabake.model.RecipeModel;

public class RecipeWidgetViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RecipeModel rm = intent.getParcelableExtra(RecipeWidgetConfigActivity.EXTRA_RECIPE_PARCELABLE);
        return new RecipeWidgetViewsFactory(this.getApplicationContext(), rm);
    }
}
