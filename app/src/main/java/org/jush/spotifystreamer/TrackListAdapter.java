package org.jush.spotifystreamer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class TrackListAdapter extends BaseAdapter {
    private static final String DATA = "org.jush.spotifystreamer.TrackListAdapter.DATA";
    private ArrayList<ParcelableTrack> data;

    /**
     * Creates a new adapter able to display information about tracks.
     *
     * @param savedInstanceState If not null then it will be used to initialize the list of tracks.
     */

    public TrackListAdapter(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(DATA)) {
            this.data = savedInstanceState.getParcelableArrayList(DATA);
        } else {
            this.data = new ArrayList<>(0);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ParcelableTrack getItem(int position) {
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
            view = inflater.inflate(R.layout.track_row, parent, false);
            view.setTag(new TrackViewHolder(view));
        } else {
            view = convertView;
        }
        TrackViewHolder trackHolder = (TrackViewHolder) view.getTag();
        ParcelableTrack track = getItem(position);
        String thumbnailUrl = track.getThumbnailUrl();
        Picasso.with(view.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.ic_track)
                .centerCrop()
                .resizeDimen(R.dimen.track_thumbnail, R.dimen.track_thumbnail)
                .into(trackHolder.trackImg);
        trackHolder.trackName.setText(track.getName());
        trackHolder.trackAlbum.setText(track.getAlbumName());
        return view;
    }

    public void swapData(List<Track> tracks) {
        // Transform the list of tracks to ParcelableTracks
        data = new ArrayList<>(tracks.size());
        for (Track track : tracks) {
            data.add(ParcelableTrack.create(track));
        }
        notifyDataSetChanged();
    }

    /**
     * Convenient method to store the current list of artists in this adapter.
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(DATA, data);
    }

    private static class TrackViewHolder {
        final ImageView trackImg;
        final TextView trackName;
        final TextView trackAlbum;

        public TrackViewHolder(View view) {
            trackImg = (ImageView) view.findViewById(R.id.track_image);
            trackName = (TextView) view.findViewById(R.id.track_name);
            trackAlbum = (TextView) view.findViewById(R.id.track_album);
        }
    }
}
