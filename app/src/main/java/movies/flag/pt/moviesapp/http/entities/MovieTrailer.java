
package movies.flag.pt.moviesapp.http.entities;

import com.google.gson.annotations.SerializedName;

public class MovieTrailer {

    @SerializedName("key")
    private String key;

    public MovieTrailer(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
