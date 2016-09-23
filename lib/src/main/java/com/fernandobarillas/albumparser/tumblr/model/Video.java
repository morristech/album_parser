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

package com.fernandobarillas.albumparser.tumblr.model;

import com.fernandobarillas.albumparser.media.BaseMedia;
import com.fernandobarillas.albumparser.util.ParseUtils;

import java.net.URL;

public class Video extends BaseMedia {

    private URL    mUrl;
    private URL    mPreviewUrl;
    private double mDuration;
    private String mDescription;

    public Video(String videoUrl, String previewUrl, String description, double duration) {
        mUrl = ParseUtils.getUrlObject(videoUrl);
        mPreviewUrl = ParseUtils.getUrlObject(previewUrl);
        mDescription = description;
        mDuration = duration;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public double getDuration() {
        return mDuration;
    }

    @Override
    public URL getPreviewUrl() {
        return mPreviewUrl;
    }

    @Override
    public URL getUrl(boolean highQuality) {
        return highQuality ? mUrl : null;
    }

    @Override
    public boolean isVideo() {
        return true;
    }

    @Override
    public String toString() {
        return "Video{" + "mUrl=" + mUrl + '}';
    }
}