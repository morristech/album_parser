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

package com.fernandobarillas.albumparser.parser;

import com.fernandobarillas.albumparser.media.IApiResponse;
import com.fernandobarillas.albumparser.media.IMedia;
import com.fernandobarillas.albumparser.media.IMediaAlbum;

import java.net.URL;

/**
 * Class that facilitates getting API responses, regardless of which API returned them. This class
 * will always try to return either a full API response when a call was made to the API, or it will
 * return an IMedia Object with a URL that links directly to video or an image
 */
public interface IParserResponse<T extends IMedia> {

    /**
     * @return The IMediaAlbum returned by the parser
     */
    IMediaAlbum<T> getAlbum();

    /**
     * @return The name of the API provider that was used to create this ParseResponse
     */
    String getApiProviderName();

    /**
     * @return The IApiResponse returned by the parser
     */
    IApiResponse<T> getApiResponse();

    /**
     * @return The hash that was used when making the API request that created this ParseResponse
     */
    String getHash();

    /**
     * @return The IMedia returned by the parser
     */
    T getMedia();

    /**
     * @return The original URL used by the parser before the request was made to the API. For
     * example, an Imgur original URL might be: https://imgur.com/gallery/PBTrqAA
     */
    URL getOriginalUrl();

    /**
     * @return True when the response is an album, false otherwise
     */
    boolean isAlbum();

    /**
     * @return True when there is only a single media, false when there is a full response from the
     * API available
     */
    boolean isSingleMedia();
}
