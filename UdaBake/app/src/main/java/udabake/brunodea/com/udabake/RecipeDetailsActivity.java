package udabake.brunodea.com.udabake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.model.RecipeStepModel;
import udabake.brunodea.com.udabake.ui.RecipeStepsAdapter;

public class RecipeDetailsActivity extends AppCompatActivity {
    public static final String RECIPE_MODEL_EXTRA = "recipe_model_extra";

    private RecipeModel mRecipeModel;

    @BindView(R.id.bt_goto_ingredients) Button mBTGotoIngredients;
    @BindView(R.id.rv_recipe_steps) RecyclerView mRVSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        // if the intent is null or doesn't have the extra, there was a programmer error, so the
        // app should indeed fail!
        Intent intent = getIntent();
        mRecipeModel = intent.getParcelableExtra(RECIPE_MODEL_EXTRA);
        getSupportActionBar().setTitle(mRecipeModel.getName());
        final RecipeStepsAdapter adapter = new RecipeStepsAdapter(this, mRecipeModel.getSteps(), new RecipeStepsAdapter.OnRecipeStepClickListener() {
            @Override
            public void onStepClick(RecipeStepModel model, int position) {
                Intent intent = new Intent(RecipeDetailsActivity.this, RecipeInfoDetailsActivity.class);
                intent.putExtra(RecipeInfoDetailsActivity.RECIPE_STEP_POS, position);
                intent.putExtra(RecipeInfoDetailsActivity.RECIPE_MODEL_EXTRA, mRecipeModel);
                startActivity(intent);
            }
        });
        mRVSteps.setLayoutManager(new LinearLayoutManager(this));
        mRVSteps.setHasFixedSize(true);
        mRVSteps.setAdapter(adapter);
        mBTGotoIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailsActivity.this, RecipeInfoDetailsActivity.class);
                intent.putExtra(RecipeInfoDetailsActivity.RECIPE_MODEL_EXTRA, mRecipeModel);
                intent.putExtra(RecipeInfoDetailsActivity.RECIPE_IS_INGREDIENTS_EXTRA, true);
                startActivity(intent);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRVSteps.getContext(),
                LinearLayoutManager.VERTICAL);
        mRVSteps.addItemDecoration(dividerItemDecoration);
    }
}
