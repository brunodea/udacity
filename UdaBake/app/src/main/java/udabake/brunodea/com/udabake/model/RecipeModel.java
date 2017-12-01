package udabake.brunodea.com.udabake.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RecipeModel implements Parcelable {

    public RecipeModel(Parcel in) {
        image = in.readString();
        servings = in.readInt();
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients, RecipeIngredientModel.CREATOR);
        id = in.readInt();
        steps = new ArrayList<>();
        in.readTypedList(steps, RecipeStepModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeInt(servings);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeInt(id);
        parcel.writeTypedList(steps);
    }

    static final Parcelable.Creator<RecipeModel> CREATOR
            = new Parcelable.Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel parcel) {
            return new RecipeModel(parcel);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

	@SerializedName("image")
	private String image;

	@SerializedName("servings")
	private int servings;

	@SerializedName("name")
	private String name;

	@SerializedName("ingredients")
	private List<RecipeIngredientModel> ingredients;

	@SerializedName("id")
	private int id;

	@SerializedName("steps")
	private List<RecipeStepModel> steps;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setServings(int servings){
		this.servings = servings;
	}

	public int getServings(){
		return servings;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIngredients(List<RecipeIngredientModel> ingredients){
		this.ingredients = ingredients;
	}

	public List<RecipeIngredientModel> getIngredients(){
		return ingredients;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSteps(List<RecipeStepModel> steps){
		this.steps = steps;
	}

	public List<RecipeStepModel> getSteps(){
		return steps;
	}

	@Override
 	public String toString(){
		return 
			"RecipeModel{" +
			"image = '" + image + '\'' + 
			",servings = '" + servings + '\'' + 
			",name = '" + name + '\'' + 
			",ingredients = '" + ingredients + '\'' + 
			",id = '" + id + '\'' + 
			",steps = '" + steps + '\'' + 
			"}";
		}
}