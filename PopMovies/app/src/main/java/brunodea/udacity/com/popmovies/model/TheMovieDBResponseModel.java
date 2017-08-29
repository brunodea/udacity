package brunodea.udacity.com.popmovies.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class TheMovieDBResponseModel {
    private int page;
    private List<ResultModel> results;
    private int total_results;
    private int total_pages;

    public TheMovieDBResponseModel() {
        results = new ArrayList<ResultModel>();
    }

    public int getPage() {
        return page;
    }
    public List<ResultModel> getResults() {
        return results;
    }
    public int getTotalResults() {
        return total_results;
    }
    public int getTotalPages(){
        return total_pages;
    }

    public class ResultModel {
        private String poster_path;
        private boolean adult;
        private String overview;
        private String release_date;
        private List<Integer> genre_ids;
        private int id;
        private String original_title;
        private String original_language;
        private String title;
        private String backdrop_path;
        private float popularity;
        private int vote_count;
        private boolean video;
        private float vote_average;

        public ResultModel() {
            genre_ids = new ArrayList<Integer>();
        }

        public String getPosterPath() {
            return poster_path;
        }
        // TODO: add all get methods.
    }
    public static ResultModel parseResultModelJSON(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, ResultModel.class);
    }
    public static TheMovieDBResponseModel parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, TheMovieDBResponseModel.class);
    }
}
