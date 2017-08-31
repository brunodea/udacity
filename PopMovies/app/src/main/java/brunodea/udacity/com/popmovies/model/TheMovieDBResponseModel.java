package brunodea.udacity.com.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TheMovieDBResponseModel implements Parcelable {

	TheMovieDBResponseModel(Parcel in) {
		page = in.readInt();
		totalPages = in.readInt();
		results = new ArrayList<>();
		in.readTypedList(results, TheMovieDBResultModel.CREATOR);
        totalResults = in.readInt();
	}

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(page);
        parcel.writeInt(totalPages);
        parcel.writeTypedList(results);
        parcel.writeInt(totalResults);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    static final Parcelable.Creator<TheMovieDBResponseModel> CREATOR
            = new Parcelable.Creator<TheMovieDBResponseModel>() {
        @Override
        public TheMovieDBResponseModel createFromParcel(Parcel in) {
            return new TheMovieDBResponseModel(in);
        }
        @Override
        public TheMovieDBResponseModel[] newArray(int size) {
            return new TheMovieDBResponseModel[size];
        }
    };

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<TheMovieDBResultModel> results;

	@SerializedName("total_results")
	private int totalResults;

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<TheMovieDBResultModel> results){
		this.results = results;
	}

	public List<TheMovieDBResultModel> getResults(){
		return results;
	}

	public void addAll(List<TheMovieDBResultModel> newResults) {
        this.results.addAll(newResults);
    }

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + results + '\'' + 
			",total_results = '" + totalResults + '\'' + 
			"}";
	}
}