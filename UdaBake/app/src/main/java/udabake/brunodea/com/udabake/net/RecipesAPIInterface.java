package udabake.brunodea.com.udabake.net;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import udabake.brunodea.com.udabake.model.RecipeModel;

public interface RecipesAPIInterface {
    @GET("baking.json")
    Call<ArrayList<RecipeModel>> getRecipes();
}
