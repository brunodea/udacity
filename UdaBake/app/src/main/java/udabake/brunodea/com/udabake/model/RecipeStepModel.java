package udabake.brunodea.com.udabake.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RecipeStepModel implements Parcelable {
	public RecipeStepModel(int id, String videoURL, String description, String short_descr) {
		setId(id);
		setVideoURL(videoURL);
		setDescription(description);
		setShortDescription(short_descr);
	}

	public RecipeStepModel(Parcel in) {
	    videoURL = in.readString();
	    description = in.readString();
	    id = in.readInt();
	    shortDescription = in.readString();
	    thumbnailURL = in.readString();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
	    parcel.writeString(videoURL);
	    parcel.writeString(description);
	    parcel.writeInt(id);
	    parcel.writeString(shortDescription);
	    parcel.writeString(thumbnailURL);
	}

	static final Parcelable.Creator<RecipeStepModel> CREATOR
			= new Parcelable.Creator<RecipeStepModel>() {
		@Override
		public RecipeStepModel createFromParcel(Parcel parcel) {
			return new RecipeStepModel(parcel);
		}

		@Override
		public RecipeStepModel[] newArray(int size) {
			return new RecipeStepModel[size];
		}
	};

    @Override
    public int describeContents() {
        return 0;
    }

	@SerializedName("videoURL")
	private String videoURL;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("shortDescription")
	private String shortDescription;

	@SerializedName("thumbnailURL")
	private String thumbnailURL;

	public void setVideoURL(String videoURL){
		this.videoURL = videoURL;
	}

	public String getVideoURL(){
		return videoURL;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShortDescription(String shortDescription){
		this.shortDescription = shortDescription;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public void setThumbnailURL(String thumbnailURL){
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"videoURL = \"" + videoURL + '\"' +
			",description = \"" + description.replace("\"", "\'") + '\"' +
			",id = " + id +
			",shortDescription = \"" + shortDescription.replace("\"", "\'") + '\"' +
			",thumbnailURL = \"" + thumbnailURL + '\"' +
			"}";
		}
}