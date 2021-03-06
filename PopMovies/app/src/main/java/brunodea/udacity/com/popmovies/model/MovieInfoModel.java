package brunodea.udacity.com.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MovieInfoModel implements Parcelable {
	public MovieInfoModel(Parcel in) {
        overview = in.readString();
        originalTitle = in.readString();
        popularity = in.readDouble();
        voteAverage = in.readDouble();
		posterPath = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
		title = in.readString();
		id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(overview);
        parcel.writeString(originalTitle);
        parcel.writeDouble(popularity);
        parcel.writeDouble(voteAverage);
		parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(releaseDate);
		parcel.writeString(title);
		parcel.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    static final Parcelable.Creator<MovieInfoModel> CREATOR
            = new Parcelable.Creator<MovieInfoModel>() {
        public MovieInfoModel createFromParcel(Parcel in) {
            return new MovieInfoModel(in);
        }
        public MovieInfoModel[] newArray(int size) {
            return new MovieInfoModel[size];
        }
    };

	@SerializedName("overview")
	private String overview;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("original_title")
	private String originalTitle;

	@SerializedName("video")
	private boolean video;

	@SerializedName("title")
	private String title;

	//@SerializedName("genre_ids")
	//private List<Integer> genreIds;

	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("popularity")
	private double popularity;

	@SerializedName("vote_average")
	private double voteAverage;

	@SerializedName("id")
	private int id;

	@SerializedName("adult")
	private boolean adult;

	@SerializedName("vote_count")
	private int voteCount;

	public void setOverview(String overview){
		this.overview = overview;
	}

	public String getOverview(){
		return overview;
	}

	public void setOriginalLanguage(String originalLanguage){
		this.originalLanguage = originalLanguage;
	}

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public void setOriginalTitle(String originalTitle){
		this.originalTitle = originalTitle;
	}

	public String getOriginalTitle(){
		return originalTitle;
	}

	public void setVideo(boolean video){
		this.video = video;
	}

	public boolean isVideo(){
		return video;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	//public void setGenreIds(List<Integer> genreIds){
	//	this.genreIds = genreIds;
	//}

	//public List<Integer> getGenreIds(){
	//	return genreIds;
	//}

	public void setPosterPath(String posterPath){
		this.posterPath = posterPath;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public void setBackdropPath(String backdropPath){
		this.backdropPath = backdropPath;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public void setReleaseDate(String releaseDate){
		this.releaseDate = releaseDate;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public void setPopularity(double popularity){
		this.popularity = popularity;
	}

	public double getPopularity(){
		return popularity;
	}

	public void setVoteAverage(double voteAverage){
		this.voteAverage = voteAverage;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAdult(boolean adult){
		this.adult = adult;
	}

	public boolean isAdult(){
		return adult;
	}

	public void setVoteCount(int voteCount){
		this.voteCount = voteCount;
	}

	public int getVoteCount(){
		return voteCount;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"overview = \"" + overview + '\"' +
			",original_language = \"" + originalLanguage + '\"' +
			",original_title = \"" + originalTitle + '\"' +
			",video = \"" + video + '\"' +
			",title = \"" + title + '\"' +
			//",genre_ids = '" + genreIds + '\"' +
			",poster_path = \"" + posterPath + '\"' +
			",backdrop_path = \"" + backdropPath + '\"' +
			",release_date = \"" + releaseDate + '\"' +
			",popularity = \"" + popularity + '\"' +
			",vote_average = \"" + voteAverage + '\"' +
			",id = \"" + id + '\"' +
			",adult = \"" + adult + '\"' +
			",vote_count = \"" + voteCount + '\"' +
			"}";
	}

}