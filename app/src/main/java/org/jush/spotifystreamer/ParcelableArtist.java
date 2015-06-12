package org.jush.spotifystreamer;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

import auto.parcel.AutoParcel;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import timber.log.Timber;

/**
 * Holds basic information about an artist. Implements {@link Parcelable} so we can transfer it
 * across components.
 */
@AutoParcel
abstract class ParcelableArtist implements Parcelable {
    /**
     * @param artist
     * @return a new {@link ParcelableArtist} with basic information about the given artist.
     */
    static ParcelableArtist create(Artist artist) {
        // Let's figure out if there's a useful image thumbnails for this artist. That is,
        // there's an image which size is between 200-500 pixels.
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
                // If no suitable size is found use the first one
                thumbnailImg = images.get(0);
            }
            thumbnailUrl = thumbnailImg.url;
        }
        return new AutoParcel_ParcelableArtist(thumbnailUrl, artist.name, artist.id);
    }

    /**
     * @return The artist thumbnail URL (Its size is usually between 200px and 500px) or null if
     * there's no picture available
     */
    @Nullable
    abstract String getThumbnailUrl();

    /**
     * @return The artist name
     */
    abstract String getName();

    /**
     * @return The Spotify artist id
     */
    abstract String getId();
}
