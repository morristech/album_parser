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

package com.fernandobarillas.albumparser.vidble.api;

import com.fernandobarillas.albumparser.vidble.model.VidbleResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit Interface for the Vidble API
 */
public interface VidbleApi {
    // No trailing slash!
    String BASE_DOMAIN = "vidble.com";
    String API_URL     = "https://" + BASE_DOMAIN;

    String[]    VALID_DOMAINS     = {
            BASE_DOMAIN, "www." + BASE_DOMAIN
    };
    Set<String> VALID_DOMAINS_SET = new HashSet<>(Arrays.asList(VALID_DOMAINS));

    String IMAGE_URL         = API_URL;
    int    IMAGE_HASH_LENGTH = 10;
    int    ALBUM_HASH_LENGTH = 8;

    @GET("/album/{hash}?json=1")
    Call<VidbleResponse> getAlbumData(@Path("hash") String hash);
}
