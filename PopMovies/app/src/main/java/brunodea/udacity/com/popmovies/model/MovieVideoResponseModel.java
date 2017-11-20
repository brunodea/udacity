package brunodea.udacity.com.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MovieVideoResponseModel implements Parcelable {

	MovieVideoResponseModel(Parcel in) {
		id = in.readInt();
		results = new ArrayList<>();
		in.readTypedList(results, MovieVideoModel.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeTypedList(results);
	}

	static final Parcelable.Creator<MovieVideoResponseModel> CREATOR
			= new Parcelable.Creator<MovieVideoResponseModel>() {
		@Override
		public MovieVideoResponseModel createFromParcel(Parcel in) {
			return new MovieVideoResponseModel(in);
		}
		@Override
		public MovieVideoResponseModel[] newArray(int size) {
			return new MovieVideoResponseModel[size];
		}
	};


	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<MovieVideoModel> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<MovieVideoModel> results){
		this.results = results;
	}

	public List<MovieVideoModel> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"MovieVideoResponseModel{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}
}