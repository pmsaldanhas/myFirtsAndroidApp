package movies.flag.pt.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import movies.flag.pt.moviesapp.R;
import movies.flag.pt.moviesapp.http.entities.Movie;
import movies.flag.pt.moviesapp.screens.DetailPageMovie;

public class MoviesAdapter extends BaseAdapter {

    private ArrayList<Movie> movie;
    private Context context;

    public MoviesAdapter(Context context, ArrayList<Movie> movie) {
        this.context = context;
        this.movie = movie;
    }

    public void addAll(ArrayList<Movie> moviesToAdd){
        movie.addAll(moviesToAdd);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movie.size();
    }

    @Override
    public Movie getItem(int position) {
        return movie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("MoviesAdapter", String.format("position = %d convertView = %s", position, convertView));

        final Movie item = getItem(position);
        ViewHolder viewHolder;
        View v;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.movie_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleLabel = v.findViewById(R.id.movie_item_title_label);
            v.setTag(viewHolder);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.titleLabel.setText(item.getTitle());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sVoteAverage = String.valueOf( item.getVoteAverage() );

                Intent intent = new Intent (context, DetailPageMovie.class);
                intent.putExtra(DetailPageMovie.DETAILS_TITLE_MOVIE, item.getTitle());
                intent.putExtra(DetailPageMovie.DETAILS_DESCRIPTION_MOVIE, item.getOverview());
                intent.putExtra(DetailPageMovie.DETAILS_RANKING_MOVIE, sVoteAverage);
                intent.putExtra(DetailPageMovie.DETAILS_IMAGE_MOVIE, item.getPosterPath());
                intent.putExtra(DetailPageMovie.DETAILS_ID_MOVIE, String.valueOf(item.getMovieId()));
                context.startActivity(intent);
            }
        });
        return v;
    }

    private class ViewHolder{
        TextView titleLabel;
    }
}
