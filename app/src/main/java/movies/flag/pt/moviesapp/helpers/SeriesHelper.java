package movies.flag.pt.moviesapp.helpers;

import java.util.ArrayList;

import movies.flag.pt.moviesapp.db.SerieDataBase;
import movies.flag.pt.moviesapp.http.entities.Serie;

public final class SeriesHelper {

    public static ArrayList<SerieDataBase> createSeriesDb (ArrayList<Serie> series) {
        //seriestodb

        ArrayList<SerieDataBase> seriesDB = new ArrayList<>();

        for (int i = 0; i < series.size(); i++) {

            Serie serie = series.get(i);

            SerieDataBase serieDB = new SerieDataBase(
                    serie.getPosterPath(),
                    serie.getOverview(),
                    serie.getReleaseDate(),
                    serie.getOriginalTitle(),
                    serie.getOriginalLanguage(),
                    serie.getName(),
                    serie.getBackdropPath(),
                    serie.getPopularity(),
                    serie.getVoteCount(),
                    serie.getVoteAverage(),
                    serie.getSerieId()
            );

            seriesDB.add(serieDB);
        }
        return seriesDB;
    }

    public static ArrayList<Serie> fromDb (ArrayList<SerieDataBase> seriesDb) {
        //seriesDBToseries

        ArrayList<Serie> series = new ArrayList<>();

        for (int i = 0; i < seriesDb.size(); i++) {

            SerieDataBase serieDB = seriesDb.get(i);

            Serie serie = new Serie (
                    serieDB.getPosterPath(),
                    serieDB.getOverview(),
                    serieDB.getReleaseDate(),
                    serieDB.getOriginalTitle(),
                    serieDB.getOriginalLanguage(),
                    serieDB.getName(),
                    serieDB.getBackdropPath(),
                    serieDB.getPopularity(),
                    serieDB.getVoteCount(),
                    serieDB.getVoteAverage(),
                    serieDB.getSerieId()
            );

            series.add(serie);
        }
        return series;
    }
}