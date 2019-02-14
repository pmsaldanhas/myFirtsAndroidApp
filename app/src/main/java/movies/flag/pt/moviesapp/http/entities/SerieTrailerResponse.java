
package movies.flag.pt.moviesapp.http.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SerieTrailerResponse {

    @SerializedName("results")
    private List<SerieTrailer> keys = new ArrayList<>();

    public List<SerieTrailer> getKeys() {
        return keys;
    }

}
