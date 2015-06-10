package org.jush.spotifystreamer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistListAdapter extends BaseAdapter {
    private static final String DATA = "DATA";
    private ArrayList<ParcelableArtist> data = new ArrayList<>(0);

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ParcelableArtist getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().hashCode();
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
        ParcelableArtist artist = getItem(position);
        String thumbnailUrl = artist.getThumbnailUrl();
        Picasso.with(view.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.ic_artist)
                .centerCrop()
                .resizeDimen(R.dimen.artist_thumbnail, R.dimen.artist_thumbnail)
                .into(artistHolder.artistImg);
        artistHolder.artistName.setText(artist.getName());
        return view;
    }

    public void swapData(List<Artist> artists) {
        data = new ArrayList<>(artists.size());
        for (Artist artist : artists) {
            data.add(ParcelableArtist.create(artist));
        }
        notifyDataSetChanged();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(DATA, data);
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
