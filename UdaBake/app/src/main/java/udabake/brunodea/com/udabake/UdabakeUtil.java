package udabake.brunodea.com.udabake;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.ui.RecipeCardListFragment;

public class UdabakeUtil {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.is_tablet);
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static ArrayList<RecipeModel> recipeModelsFromSharedPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences(RecipeCardListFragment.SHARED_PREFS,
                Context.MODE_PRIVATE);
        if (sp != null && sp.contains(RecipeCardListFragment.RECIPE_PREF_JSON)) {
            String recipes_json = sp.getString(RecipeCardListFragment.RECIPE_PREF_JSON, "");
            Type listType = new TypeToken<ArrayList<RecipeModel>>() {
            }.getType();
            Gson gson = new Gson();
            return (ArrayList<RecipeModel>) gson.fromJson(recipes_json, listType);
        }
        return null;
    }
}
