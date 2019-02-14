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
import movies.flag.pt.moviesapp.http.entities.Serie;
import movies.flag.pt.moviesapp.screens.DetailPageSerie;

public class SeriesAdapter extends BaseAdapter {

    private ArrayList<Serie> serie;
    private Context context;

    public SeriesAdapter(Context context, ArrayList<Serie> serie) {
        this.context = context;
        this.serie = serie;
    }

    public void addAll(ArrayList<Serie> seriesToAdd){
        serie.addAll(seriesToAdd);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return serie.size();
    }

    @Override
    public Serie getItem(int position) {
        return serie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("SeriesAdapter", String.format("position = %d convertView = %s", position, convertView));

        final Serie item = getItem(position);
        ViewHolder viewHolder;
        View v;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.serie_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleLabel = v.findViewById(R.id.serie_item_title_label);
            v.setTag(viewHolder);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.titleLabel.setText(item.getName());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sVoteAverage = String.valueOf( item.getVoteAverage() );

                Intent intent = new Intent (context, DetailPageSerie.class);
                intent.putExtra(DetailPageSerie.DETAILS_TITLE_SERIE, item.getName());
                intent.putExtra(DetailPageSerie.DETAILS_DESCRIPTION_SERIE, item.getOverview());
                intent.putExtra(DetailPageSerie.DETAILS_RANKING_SERIE, sVoteAverage);
                intent.putExtra(DetailPageSerie.DETAILS_IMAGE_SERIE, item.getPosterPath());
                intent.putExtra(DetailPageSerie.DETAILS_ID_SERIE, String.valueOf(item.getSerieId()));
                context.startActivity(intent);
            }
        });

        return v;
    }

    private class ViewHolder{
        TextView titleLabel;
    }
}
