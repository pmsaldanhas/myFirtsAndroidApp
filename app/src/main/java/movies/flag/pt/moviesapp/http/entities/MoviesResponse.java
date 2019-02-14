
package movies.flag.pt.moviesapp.http.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesResponse {

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<Movie> movies = new ArrayList<>();

    @SerializedName("total_pages")
    private Integer totalPages;


    public Integer getPage() {
        return page;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public List<Movie> getMovies() {
        return movies;
    }

}
