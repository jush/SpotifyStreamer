package org.jush.spotifystreamer;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import kaaes.spotify.webapi.android.models.Artist;

/**
 * Holds basic information about an artist. Implements {@link Parcelable} so we can transfer it
 * across components.
 */
@AutoParcel
public abstract class ParcelableArtist implements Parcelable {
    /**
     * @param artist
     * @return a new {@link ParcelableArtist} with basic information about the given artist.
     */
    public static ParcelableArtist create(Artist artist) {
        // Let's figure out if there's a useful image thumbnails for this artist. That is,
        // there's an image which size is between 200-500 pixels.
        String thumbnailUrl = Utils.getSuitableThumbnailUrl(artist.images);
        return new AutoParcel_ParcelableArtist(thumbnailUrl, artist.name, artist.id);
    }

    /**
     * @return The artist thumbnail URL (Its size is usually between 200px and 500px) or null if
     * there's no picture available
     */
    @Nullable
    public abstract String getThumbnailUrl();

    /**
     * @return The artist name
     */
    public abstract String getName();

    /**
     * @return The Spotify artist id
     */
    public abstract String getId();
}
