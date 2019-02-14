
package movies.flag.pt.moviesapp.db;

import com.orm.SugarRecord;

public class SerieDataBase extends SugarRecord {


    private String posterPath;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private String originalLanguage;
    private String name;
    private String backdropPath;
    private Double popularity;
    private Integer voteCount;
    private Double voteAverage;
    private Integer id;


    public SerieDataBase() {}

    public SerieDataBase(
            String posterPath,
            String overview,
            String releaseDate,
            String originalTitle,
            String originalLanguage,
            String name,
            String backdropPath,
            Double popularity,
            Integer voteCount,
            Double voteAverage,
            Integer id )
    {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.name = name;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.id = id;
    }



    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getName() {
        return name;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getSerieId() {
        return id;
    }
}
