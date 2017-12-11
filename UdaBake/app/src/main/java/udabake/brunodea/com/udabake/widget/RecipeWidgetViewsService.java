package udabake.brunodea.com.udabake.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecipeWidgetViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetViewsFactory(this.getApplicationContext());
    }
}
