package brunodea.udacity.com.popmovies.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class MovieVideoResponseModel{

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
}