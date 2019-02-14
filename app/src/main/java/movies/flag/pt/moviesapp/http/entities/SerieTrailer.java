
package movies.flag.pt.moviesapp.http.entities;

import com.google.gson.annotations.SerializedName;

public class SerieTrailer {

    @SerializedName("key")
    private String key;

    public SerieTrailer(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
