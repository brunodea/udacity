package udabake.brunodea.com.udabake.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RecipeStepModel {

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
			"RecipeStepModel{" +
			"videoURL = '" + videoURL + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",shortDescription = '" + shortDescription + '\'' + 
			",thumbnailURL = '" + thumbnailURL + '\'' + 
			"}";
		}
}