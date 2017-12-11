package udabake.brunodea.com.udabake.widget;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeIngredientModel;
import udabake.brunodea.com.udabake.model.RecipeModel;

public class RecipeWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private RecipeModel mRecipeModel;

    public RecipeWidgetViewsFactory(Context appContext, RecipeModel model) {
        mContext = appContext;
        mRecipeModel = model;
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
        if (position == AdapterView.INVALID_POSITION || mRecipeModel.getIngredients().size() == 0) {
            return null;
        }

        RecipeIngredientModel rim = mRecipeModel.getIngredients().get(position);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_item);
        rv.setTextViewText(R.id.tv_ingredient_name, rim.getIngredient());
        rv.setTextViewText(R.id.tv_ingredient_meas, "" + rim.getIngredient() + " " + rim.getMeasure());

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
