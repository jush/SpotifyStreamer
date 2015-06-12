package org.jush.spotifystreamer;

import android.support.annotation.Nullable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import timber.log.Timber;

public class Utils {
    /**
     * Convenient method to find a suitable thumbnail image from a list of images.
     *
     * @param images the list of images from where to find the thumbnail URL
     * @return the thumbnail URL or null if none was found
     */
    @Nullable
    public static String getSuitableThumbnailUrl(List<Image> images) {
        if (images != null && !images.isEmpty()) {
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
            return thumbnailImg.url;
        }
        return null;
    }
}
