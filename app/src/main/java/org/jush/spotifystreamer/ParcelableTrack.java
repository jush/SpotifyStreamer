package org.jush.spotifystreamer;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import kaaes.spotify.webapi.android.models.Track;

@AutoParcel
public abstract class ParcelableTrack implements Parcelable {

    public static ParcelableTrack create(Track track) {
        // Let's figure out if there's a useful image thumbnails for this track. That is,
        // there's an image which size is between 200-500 pixels.
        String thumbnailUrl = Utils.getSuitableThumbnailUrl(track.album.images);
        return new AutoParcel_ParcelableTrack(thumbnailUrl, track.name, track.album.name, track.id);
    }

    /**
     * @return The track album's thumbnail URL (Its size is usually between 200px and 500px) or
     * null if there's no picture available
     */
    @Nullable
    public abstract String getThumbnailUrl();

    /**
     * @return The track name
     */
    public abstract String getName();

    /**
     * @return The track's album name
     */
    public abstract String getAlbumName();

    /**
     * @return The Spotify track id
     */
    public abstract String getId();
}
