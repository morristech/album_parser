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

package com.fernandobarillas.albumparser.imgur.model;

import com.fernandobarillas.albumparser.imgur.api.ImgurApi;
import com.fernandobarillas.albumparser.media.BaseMedia;
import com.fernandobarillas.albumparser.util.ParseUtils;
import com.squareup.moshi.Json;

import java.net.URL;

public class Image extends BaseMedia {
    // https://api.imgur.com/models/image
    public static final String SMALL_SQUARE     = "s";
    public static final String BIG_SQUARE       = "b";
    public static final String SMALL_THUMBNAIL  = "t";
    public static final String MEDIUM_THUMBNAIL = "m";
    public static final String LARGE_THUMBNAIL  = "l"; // ~640px
    public static final String GIANT_THUMBNAIL  = "g"; // ~680px
    public static final String HUGE_THUMBNAIL   = "h"; // ~1024px
    public static final String RETINA_THUMBNAIL = "r"; // ~1360px
    public static final String ORIGINAL         = "";  // Full resolution

    @Json(name = "hash")
    public String  hash;
    @Json(name = "title")
    public String  title;
    @Json(name = "description")
    public String  description;
    @Json(name = "width")
    public Integer width;
    @Json(name = "height")
    public Integer height;
    @Json(name = "size")
    public Integer size;
    @Json(name = "ext")
    public String  ext;
    @Json(name = "animated")
    public Boolean animated;
    @Json(name = "prefer_video")
    public Boolean preferVideo;
    @Json(name = "looping")
    public Boolean looping;
    @Json(name = "datetime")
    public String  datetime;

    private String mLowQuality     = HUGE_THUMBNAIL;
    private String mPreviewQuality = MEDIUM_THUMBNAIL;

    @Override
    public int getByteSize(boolean highQuality) {
        // Imgur only returns size for original quality images
        return (highQuality && !isVideo()) ? defaultSizeIfNull(size) : SIZE_UNAVAILABLE;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getHeight(boolean highQuality) {
        // Imgur only returns height for original quality
        return (highQuality) ? defaultSizeIfNull(height) : SIZE_UNAVAILABLE;
    }

    @Override
    public URL getPreviewUrl() {
        return getImageUrl(mPreviewQuality);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public URL getUrl(boolean highQuality) {
        // Imgur doesn't support low quality animations/video
        if (isVideo()) return highQuality ? getImageUrl(ORIGINAL) : null;
        return (highQuality) ? getImageUrl(ORIGINAL) : getImageUrl(mLowQuality);
    }

    @Override
    public int getWidth(boolean highQuality) {
        // Imgur only returns width for original quality
        return (highQuality) ? defaultSizeIfNull(width) : SIZE_UNAVAILABLE;
    }

    @Override
    public boolean isVideo() {
        return animated != null ? animated : false;
    }

    /**
     * Wrapper for {@link #getImageUrl(String, boolean)} that always gets a GIF/GIFV URL for
     * animations in {@link #ORIGINAL} quality
     *
     * @param quality The quality to use in the returned URL, for example to return the URL for the
     *                small thumbnail image you can pass in {@link #SMALL_THUMBNAIL}
     * @return The URL to the image with the selected quality
     */
    public URL getImageUrl(String quality) {
        return getImageUrl(quality, true);
    }

    /**
     * Gets the url to the image with the passed in quality.
     *
     * @param quality   The quality to use in the returned URL, for example to return the URL for
     *                  the small thumbnail image you can pass in {@link #SMALL_THUMBNAIL}
     * @param preferMp4 True to get an MP4 extension instead of a GIF/GIFV extension, false for
     *                  original extension. This only works when {@link #ORIGINAL} quality is
     *                  requested
     * @return The URL to the image with the selected quality
     */
    public URL getImageUrl(String quality, boolean preferMp4) {
        String newExt = (ext != null) ? ext : "." + EXT_JPG;
        if (!quality.equals(ORIGINAL)) {
            // Imgur returns a JPG image for non-ORIGINAL URLs for animations regardless of the
            // extension in the request. The client shouldn't have to use the response MIME type
            // to figure this out
            newExt = "." + EXT_JPG;
        }

        if (preferMp4 && (newExt.endsWith(EXT_GIF) || newExt.endsWith(EXT_GIFV))) {
            newExt = "." + EXT_MP4;
        }

        String resultUrl = ImgurApi.IMAGE_URL + "/" + hash + quality + newExt;
        return ParseUtils.getUrlObject(resultUrl);
    }

    public void setLowQuality(String lowQuality) {
        if (lowQuality != null) mLowQuality = lowQuality;
    }

    public void setPreviewQuality(String previewQuality) {
        if (previewQuality != null) mPreviewQuality = previewQuality;
    }
}
