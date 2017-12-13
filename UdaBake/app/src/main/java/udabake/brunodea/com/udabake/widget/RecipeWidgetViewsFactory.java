package udabake.brunodea.com.udabake.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeIngredientModel;
import udabake.brunodea.com.udabake.model.RecipeModel;

public class RecipeWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private RecipeModel mRecipeModel;

    public RecipeWidgetViewsFactory(Context appContext, RecipeModel model, int widget_id) {
        mContext = appContext;
        mRecipeModel = model;
        if (mRecipeModel == null) {
            SharedPreferences sp = appContext.getSharedPreferences(RecipeWidgetConfigActivity.WIDGET_SHARED_PREFS,
                    Context.MODE_PRIVATE);
            if (sp != null && sp.contains(RecipeWidgetConfigActivity.WIDGET_RECIPE_MODEL + "_" + widget_id)) {
                String s = sp.getString(RecipeWidgetConfigActivity.WIDGET_RECIPE_MODEL + "_" + widget_id, "");
                Gson gson = new Gson();
                mRecipeModel = gson.fromJson(s, RecipeModel.class);
                RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list);
                views.setTextViewText(R.id.tv_widget_recipe_name, mRecipeModel.getName());
            }
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mRecipeModel == null ? 0 : mRecipeModel.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mRecipeModel == null ||
                mRecipeModel.getIngredients().size() == 0) {
            return null;
        }
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_item);

        RecipeIngredientModel rim = mRecipeModel.getIngredients().get(position);
        if (rim != null) {
            String s = rim.getIngredient();
            if (s != null && !s.isEmpty()) {
                rv.setTextViewText(R.id.tv_ingredient_name, rim.getIngredient());
            }
            rv.setTextViewText(R.id.tv_ingredient_meas, "" + rim.getQuantity() + " " + rim.getMeasure());
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
