package brunodea.udacity.com.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MovieReviewModel implements Parcelable {

	MovieReviewModel(Parcel in) {
		author = in.readString();
		content = in.readString();
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(author);
		parcel.writeString(content);
	}

	static final Parcelable.Creator<MovieReviewModel> CREATOR
			= new Parcelable.Creator<MovieReviewModel>() {
		public MovieReviewModel createFromParcel(Parcel in) {
			return new MovieReviewModel(in);
		}
		public MovieReviewModel[] newArray(int size) {
			return new MovieReviewModel[size];
		}
	};

	@SerializedName("author")
	private String author;

	@SerializedName("id")
	private String id;

	@SerializedName("content")
	private String content;

	@SerializedName("url")
	private String url;

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"MovieReviewModel{" +
			"author = \"" + author + '\"' +
			",id = \"" + id + '\"' +
			",content = \"" + content + '\"' +
			",url = \"" + url + '\"' +
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}
}