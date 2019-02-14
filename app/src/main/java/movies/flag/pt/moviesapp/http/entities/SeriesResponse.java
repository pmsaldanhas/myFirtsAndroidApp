
package movies.flag.pt.moviesapp.http.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SeriesResponse {

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<Serie> series = new ArrayList<>();

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("key")
    private String key;

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public String getKey() {
        return key;
    }
}
