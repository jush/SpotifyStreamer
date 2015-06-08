package org.jush.spotifystreamer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

public class ArtistListAdapter extends BaseAdapter {
    private List<Artist> data = Collections.emptyList();

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Artist getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id.hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(
            int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.artist_row, parent, false);
            view.setTag(new ArtistViewHolder(view));
        } else {
            view = convertView;
        }
        ArtistViewHolder artistHolder = (ArtistViewHolder) view.getTag();
        Artist artist = getItem(position);
        List<Image> images = artist.images;
        if (!images.isEmpty()) {
            Picasso.with(view.getContext())
                    .load(images.get(0).url)
                    .centerCrop()
                    .resizeDimen(R.dimen.artist_thumbnail, R.dimen.artist_thumbnail)
                    .into(artistHolder.artistImg);
        } else {
            artistHolder.artistImg.setImageBitmap(null);
        }
        artistHolder.artistName.setText(artist.name);
        return view;
    }

    public void swapData(List<Artist> artists) {
        data = artists;
        notifyDataSetChanged();
    }

    private static class ArtistViewHolder {
        final ImageView artistImg;
        final TextView artistName;

        public ArtistViewHolder(View view) {
            artistImg = (ImageView) view.findViewById(R.id.artist_image);
            artistName = (TextView) view.findViewById(R.id.artist_name);
        }
    }
}
