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

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import timber.log.Timber;

public class TrackListAdapter extends BaseAdapter {
    private List<Track> data = Collections.emptyList();

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Track getItem(int position) {
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
            view = inflater.inflate(R.layout.track_row, parent, false);
            view.setTag(new TrackViewHolder(view));
        } else {
            view = convertView;
        }
        TrackViewHolder trackHolder = (TrackViewHolder) view.getTag();
        Track track = getItem(position);
        List<Image> images = track.album.images;
        String thumbnailUrl = null;
        if (!images.isEmpty()) {
            Image thumbnailImg = null;
            for (Image image : images) {
                if (image.height < 500 && image.height >= 200 && image.width < 500 && image.width
                        >= 200) {
                    thumbnailImg = image;
                    break;
                }
            }
            if (thumbnailImg == null) {
                Timber.w("No suitable thumbnail image found. Using first one.");
                // If no suitable size if found use the first one
                thumbnailImg = images.get(0);
            }
            thumbnailUrl = thumbnailImg.url;
        }
        Picasso.with(view.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.ic_track)
                .centerCrop()
                .resizeDimen(R.dimen.track_thumbnail, R.dimen.track_thumbnail)
                .into(trackHolder.trackImg);
        trackHolder.trackName.setText(track.name);
        trackHolder.trackAlbum.setText(track.album.name);
        return view;
    }

    public void swapData(List<Track> tracks) {
        data = tracks;
        notifyDataSetChanged();
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
