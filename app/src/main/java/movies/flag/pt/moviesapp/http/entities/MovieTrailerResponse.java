
package movies.flag.pt.moviesapp.http.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerResponse {

    @SerializedName("results")
    private List<MovieTrailer> keys = new ArrayList<>();

    public List<MovieTrailer> getKeys() {
        return keys;
    }

}
