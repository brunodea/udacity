package udabake.brunodea.com.udabake.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeIngredientModel;
import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.net.RecipesAPI;
import udabake.brunodea.com.udabake.ui.RecipeCardListFragment;

public class RecipeWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<RecipeModel> mRecipeModels;

    public RecipeWidgetViewsFactory(Context appContext) {
        mContext = appContext;
        mRecipeModels = new ArrayList<>();
    }

    private void update_entries() {
        SharedPreferences sp = mContext.getSharedPreferences(RecipeCardListFragment.SHARED_PREFS,
                Context.MODE_PRIVATE);
        if (sp != null && sp.contains(RecipeCardListFragment.RECIPE_PREF_JSON)) {
            String recipes_json = sp.getString(RecipeCardListFragment.RECIPE_PREF_JSON, "");
            Log.d("bruno-test", recipes_json);
            Type listType = new TypeToken<List<RecipeModel>>(){}.getType();
            Gson gson = new Gson();
            List<RecipeModel> list = (List<RecipeModel>) gson.fromJson(recipes_json, listType);
            mRecipeModels.addAll(list);
        } else {
            RecipesAPI.getRecipes(new Callback<ArrayList<RecipeModel>>() {
                @Override
                public void onResponse(Call<ArrayList<RecipeModel>> call, Response<ArrayList<RecipeModel>> response) {
                    if (response.isSuccessful()) {
                        mRecipeModels = response.body();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<RecipeModel>> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onCreate() {
        update_entries();
    }

    @Override
    public void onDataSetChanged() {
        update_entries();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mRecipeModels.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mRecipeModels.size() == 0) {
            return null;
        }

        // TODO: remove hardcoded recipe
        RecipeIngredientModel rim = mRecipeModels.get(0).getIngredients().get(position);

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
