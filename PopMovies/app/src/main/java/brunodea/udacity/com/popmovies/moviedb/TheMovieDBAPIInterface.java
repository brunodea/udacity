package brunodea.udacity.com.popmovies.moviedb;

import brunodea.udacity.com.popmovies.model.MovieInfoResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBAPIInterface {
    @GET("movie/{sort_by}/")
    Call<MovieInfoResponseModel> getSortedMovies(@Path("sort_by") String sort_by,
                                                 @Query("page") Integer page,
                                                 @Query("api_key") String api_key);
}
