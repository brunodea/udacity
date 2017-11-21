package brunodea.udacity.com.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MovieReviewResponseModel implements Parcelable {

	MovieReviewResponseModel(Parcel in) {
		id = in.readInt();
		page = in.readInt();
		totalPages = in.readInt();
		results = new ArrayList<>();
		in.readTypedList(results, MovieReviewModel.CREATOR);
		totalResults = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeInt(page);
		parcel.writeInt(totalPages);
		parcel.writeTypedList(results);
		parcel.writeInt(totalResults);
	}

	static final Parcelable.Creator<MovieReviewResponseModel> CREATOR
			= new Parcelable.Creator<MovieReviewResponseModel>() {
		@Override
		public MovieReviewResponseModel createFromParcel(Parcel in) {
			return new MovieReviewResponseModel(in);
		}
		@Override
		public MovieReviewResponseModel[] newArray(int size) {
			return new MovieReviewResponseModel[size];
		}
	};

	@SerializedName("id")
	private int id;

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<MovieReviewModel> results;

	@SerializedName("total_results")
	private int totalResults;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

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

	public void setResults(List<MovieReviewModel> results){
		this.results = results;
	}

	public List<MovieReviewModel> getResults(){
		return results;
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
			"MovieReviewResponseModel{" + 
			"id = \"" + id + '\"' +
			",page = \"" + page + '\"' +
			",total_pages = \"" + totalPages + '\"' +
			",results = \"" + results + '\"' +
			",total_results = \"" + totalResults + '\"' +
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}
}