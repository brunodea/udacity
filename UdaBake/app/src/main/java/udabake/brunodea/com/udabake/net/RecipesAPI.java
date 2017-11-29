package udabake.brunodea.com.udabake.net;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udabake.brunodea.com.udabake.model.RecipeModel;

public class RecipesAPI {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public static void getRecipes(final Callback<ArrayList<RecipeModel>> callback) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RecipesAPIInterface api = retrofit.create(RecipesAPIInterface.class);
        Call<ArrayList<RecipeModel>> call = api.getRecipes();
        call.enqueue(callback);
    }

    public static void downloadImageToView(Context context, ImageView imageView, String imagePath) {
        if (!imagePath.isEmpty()) {
            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(new ColorDrawable(context.getResources()
                            .getColor(android.R.color.white, context.getTheme())))
                    // TODO
                    //.error(R.mipmap.broken_image)
                    .into(imageView);
        }
    }
}
