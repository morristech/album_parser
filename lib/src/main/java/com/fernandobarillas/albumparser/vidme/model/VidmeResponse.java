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

package com.fernandobarillas.albumparser.vidme.model;

import com.fernandobarillas.albumparser.media.BaseApiResponse;
import com.fernandobarillas.albumparser.media.IMedia;
import com.fernandobarillas.albumparser.vidme.api.VidmeApi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.URL;

import javax.annotation.Generated;

/**
 * Created by fb on 5/10/16.
 */
@Generated("org.jsonschema2pojo")
public class VidmeResponse extends BaseApiResponse {
    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("error")
    @Expose
    public String  error;
    @SerializedName("code")
    @Expose
    public String  code;
    @SerializedName("video")
    @Expose
    public Video   video;

    @Override
    public String getApiDomain() {
        return VidmeApi.BASE_DOMAIN;
    }

    @Override
    public String getHash() {
        return (video != null) ? video.url : null;
    }

    @Override
    public String getErrorMessage() {
        return error;
    }

    @Override
    public IMedia getMedia() {
        return video;
    }

    @Override
    public URL getPreviewUrl() {
        return (video != null) ? video.getPreviewUrl() : null;
    }

    @Override
    public boolean isSuccessful() {
        return status;
    }

    @Override
    public String toString() {
        return "VidmeResponse{"
                + "status="
                + status
                + ", error='"
                + error
                + '\''
                + ", code='"
                + code
                + '\''
                + ", video="
                + video
                + '}';
    }
}
