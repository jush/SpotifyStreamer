package org.jush.spotifystreamer;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

import auto.parcel.AutoParcel;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import timber.log.Timber;

@AutoParcel
abstract class ParcelableArtist implements Parcelable {
    static ParcelableArtist create(Artist artist) {
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
        return new AutoParcel_ParcelableArtist(thumbnailUrl, artist.name, artist.id);
    }

    @Nullable
    abstract String getThumbnailUrl();

    abstract String getName();

    abstract String getId();
}
