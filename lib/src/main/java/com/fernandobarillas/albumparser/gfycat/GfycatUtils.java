/*
 * The MIT License (MIT)
 * Copyright (c) 2016 Fernando Barillas (FBis251)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.fernandobarillas.albumparser.gfycat;

import com.fernandobarillas.albumparser.gfycat.api.GfycatApi;
import com.fernandobarillas.albumparser.media.IMedia;
import com.fernandobarillas.albumparser.util.ParseUtils;

import java.net.URL;

/**
 * Created by fb on 5/9/16.
 */
public class GfycatUtils {
    private static final String TYPE_MOBILE = "-mobile";
    private static final String TYPE_POSTER = "-poster";

    /**
     * Attempts to get a Gfycat hash for a passed in URL String
     *
     * @param gfycatUrl The String to attempt to get a hash from
     * @return An Imgur album or URL hash if the passed in URL was a valid Imgur URL, null otherwise
     */
    public static String getHash(String gfycatUrl) {
        URL url = ParseUtils.getUrlObject(gfycatUrl, GfycatApi.BASE_DOMAIN);
        if (url == null) return null; // Passed in String wasn't a valid URL
        String path = url.getPath();

        return ParseUtils.hashRegex(path, "/(\\w+)");
    }

    public static URL getUrlFromHash(String gfyHash) {
        return ParseUtils.getUrlObject(String.format("%s/%s", GfycatApi.API_URL, gfyHash));
    }

    /**
     * @param hash The hash to get the URL for
     * @return The mobile mp4 version URL
     */
    static String getMobileUrl(String hash) {
        return getPosterOrMobileUrl(hash, true);
    }

    /**
     * @param hash The hash to get the URL for
     * @return The JPG preview URL
     */
    static String getPosterUrl(String hash) {
        return getPosterOrMobileUrl(hash, false);
    }

    /**
     * @param hash          The hash to get the URL for
     * @param isMobileVideo True to get the mobile video URL, false to get the poster JPG url
     * @return The URL to the mobile version of the video or to the preview poster URL
     */
    private static String getPosterOrMobileUrl(String hash, boolean isMobileVideo) {
        if (hash == null || hash.trim().length() == 0) return null;
        String ext = (isMobileVideo) ? IMedia.EXT_MP4 : IMedia.EXT_JPG;
        String type = (isMobileVideo) ? TYPE_MOBILE : TYPE_POSTER;
        return String.format("%s/%s%s.%s", GfycatApi.THUMB_URL, hash, type, ext);
    }
}
