package udabake.brunodea.com.udabake.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RecipeIngredientModel implements Parcelable {

    public RecipeIngredientModel(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeFloat(quantity);
		parcel.writeString(measure);
		parcel.writeString(ingredient);
	}

	static final Parcelable.Creator<RecipeIngredientModel> CREATOR
			= new Parcelable.Creator<RecipeIngredientModel>() {
		@Override
		public RecipeIngredientModel createFromParcel(Parcel parcel) {
		    return new RecipeIngredientModel(parcel);
		}

		@Override
		public RecipeIngredientModel[] newArray(int size) {
			return new RecipeIngredientModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@SerializedName("quantity")
	private float quantity;

	@SerializedName("measure")
	private String measure;

	@SerializedName("ingredient")
	private String ingredient;

	public void setQuantity(float quantity){
		this.quantity = quantity;
	}

	public float getQuantity(){
		return quantity;
	}

	public void setMeasure(String measure){
		this.measure = measure;
	}

	public String getMeasure(){
		return measure;
	}

	public void setIngredient(String ingredient){
		this.ingredient = ingredient;
	}

	public String getIngredient(){
		return ingredient;
	}

	@Override
 	public String toString(){
		return 
			"RecipeIngredientModel{" +
			"quantity = '" + quantity + '\'' + 
			",measure = '" + measure + '\'' + 
			",ingredient = '" + ingredient + '\'' + 
			"}";
		}
}