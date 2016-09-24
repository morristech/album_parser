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

package com.fernandobarillas.albumparser.tumblr;

import com.fernandobarillas.albumparser.exception.InvalidApiKeyException;
import com.fernandobarillas.albumparser.exception.InvalidMediaUrlException;
import com.fernandobarillas.albumparser.media.DirectMedia;
import com.fernandobarillas.albumparser.parser.AbstractApiParser;
import com.fernandobarillas.albumparser.parser.ParserResponse;
import com.fernandobarillas.albumparser.tumblr.api.TumblrApi;
import com.fernandobarillas.albumparser.tumblr.model.TumblrResponse;
import com.fernandobarillas.albumparser.util.ParseUtils;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import retrofit2.Response;

/**
 * Parser for Imgur API responses
 */
public class TumblrParser extends AbstractApiParser {
    private String mTumblrApiKey = null;

    public TumblrParser() {
    }

    public TumblrParser(OkHttpClient client, String tumblrApiKey) {
        super(client);
        mTumblrApiKey = tumblrApiKey;
    }

    @Override
    public String getApiUrl() {
        return TumblrApi.API_URL;
    }

    @Override
    public String getBaseDomain() {
        return TumblrApi.BASE_DOMAIN;
    }

    @Override
    public String getHash(URL mediaUrl) throws InvalidMediaUrlException {
        if (mediaUrl == null) throw new InvalidMediaUrlException(mediaUrl);
        String path = mediaUrl.getPath();
        String hash = ParseUtils.hashRegex(path, "/image/(\\d+)");

        if (path.startsWith("/post/")) {
            hash = ParseUtils.hashRegex(path, "/post/(\\d+)");
        }

        if (hash == null) throw new InvalidMediaUrlException(mediaUrl);
        return hash;
    }

    @Override
    public ParserResponse parse(URL mediaUrl) throws IOException, RuntimeException {
        if (mTumblrApiKey == null) {
            // Tumblr requires all API requests to use an API key
            throw new InvalidApiKeyException(mediaUrl, mTumblrApiKey, "Tumblr API key is not set");
        }

        String apiKey = mTumblrApiKey.trim();
        if (apiKey.isEmpty()) {
            throw new InvalidApiKeyException(mediaUrl, apiKey, "Tumblr API key cannot be blank");
        }

        if (ParseUtils.isDirectUrl(mediaUrl)) {
            ParserResponse parserResponse = new ParserResponse(new DirectMedia(mediaUrl));
            parserResponse.setOriginalUrl(mediaUrl);
            return parserResponse;
        }

        String hash = getHash(mediaUrl);
        TumblrApi service = getRetrofit().create(TumblrApi.class);
        Response<TumblrResponse> serviceResponse =
                service.getPost(mediaUrl.getHost(), hash, mTumblrApiKey).execute();
        TumblrResponse apiResponse = serviceResponse.body();
        return getParserResponse(mediaUrl, apiResponse);
    }
}
