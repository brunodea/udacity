package udabake.brunodea.com.udabake.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.UdabakeUtil;
import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.model.RecipeStepModel;
import udabake.brunodea.com.udabake.ui.RecipeCardListFragment;
import udabake.brunodea.com.udabake.ui.RecipeItemAdapter;

import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class RecipeWidgetConfigActivity extends AppCompatActivity {
    public static final String WIDGET_RECIPE_MODEL = "recipe_model_widget";
    public static final String WIDGET_SHARED_PREFS = "widget_shared_prefs";
    private int mAppWidgetId;

    @BindView(R.id.rv_widget_recipes) RecyclerView mRVWidgetRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        initializeAppWidget();
    }

    private void initializeAppWidget(){
        mAppWidgetId = INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == INVALID_APPWIDGET_ID) {
            finish();
        } else {
            setContentView(R.layout.activity_recipe_widget_config);
            ButterKnife.bind(this);
            RecipeItemAdapter adapter = new RecipeItemAdapter(this, new RecipeCardListFragment.OnRecipeItemClickListener() {
                @Override
                public void onClickRecipeItem(RecipeModel recipe) {
                    SharedPreferences sp = getSharedPreferences(WIDGET_SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor sp_e = sp.edit();
                    recipe.setSteps(new ArrayList<RecipeStepModel>());
                    sp_e.putString(WIDGET_RECIPE_MODEL + "_" + mAppWidgetId, recipe.toString());
                    sp_e.apply();


                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                    finish();
                }
            });
            adapter.queryRecipes(new RecipeItemAdapter.QueryCallback() {
                @Override
                public void onQueryStarted() {

                }

                @Override
                public void onQueryFailure() {

                }

                @Override
                public void onQuerySuccess() {

                }
            });
            mRVWidgetRecipes.setAdapter(adapter);
            int columns = 1;
            if (UdabakeUtil.isTablet(this)) {
                columns = UdabakeUtil.isLandscape(this) ? 5 : 4;
            }
            if (columns <= 1) {
                mRVWidgetRecipes.setLayoutManager(new LinearLayoutManager(this));
            } else {
                mRVWidgetRecipes.setLayoutManager(new GridLayoutManager(this, columns));
            }
            mRVWidgetRecipes.setHasFixedSize(true);
        }
    }
}
