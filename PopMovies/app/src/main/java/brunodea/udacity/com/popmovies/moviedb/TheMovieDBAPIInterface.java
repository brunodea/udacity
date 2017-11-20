package brunodea.udacity.com.popmovies.moviedb;

import brunodea.udacity.com.popmovies.model.MovieInfoResponseModel;
import brunodea.udacity.com.popmovies.model.MovieVideoResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBAPIInterface {
    @GET("movie/{sort_by}/")
    Call<MovieInfoResponseModel> getSortedMovies(@Path("sort_by") String sort_by,
                                                 @Query("page") Integer page,
                                                 @Query("api_key") String api_key);
    @GET("movie/{id}/videos/")
    Call<MovieVideoResponseModel> getMovieVideos(@Path("id") String movie_id,
                                                 @Query("api_key") String api_key);
}
