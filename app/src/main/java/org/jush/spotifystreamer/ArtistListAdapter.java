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
import timber.log.Timber;

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
                .placeholder(R.drawable.ic_artist)
                .centerCrop()
                .resizeDimen(R.dimen.artist_thumbnail, R.dimen.artist_thumbnail)
                .into(artistHolder.artistImg);
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
