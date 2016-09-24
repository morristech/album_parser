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

import com.fernandobarillas.albumparser.AllTests;
import com.fernandobarillas.albumparser.imgur.ImgurParser;
import com.fernandobarillas.albumparser.media.IApiResponse;
import com.fernandobarillas.albumparser.media.IMedia;
import com.fernandobarillas.albumparser.media.IMediaAlbum;
import com.fernandobarillas.albumparser.util.ParseUtils;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

import static com.fernandobarillas.albumparser.AllTests.API_CALL_TIMEOUT_MS;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for the Imgur API parser
 */
public class ImgurParserTest {
    private OkHttpClient mOkHttpClient;
    private ImgurParser  mImgurParser;
    private String mImgurApiKey = ""; // TODO: Set your Imgur API key

    public ImgurParserTest() {
        mOkHttpClient = new OkHttpClient();
        mImgurParser = new ImgurParser(mOkHttpClient, mImgurApiKey);
    }

    @Test
    public void testCanParseAndGetHash() throws Exception {
        Map<String, String> validHashes = new HashMap<>();

        // Albums
        validHashes.put("VhGBD", "http://imgur.com/r/motivation/VhGBD"); // Album with /r/ prefix
        validHashes.put("WKauF", "https://imgur.com/gallery/WKauF"); // Album with gallery URL
        validHashes.put("cvehZ", "http://imgur.com/a/cvehZ"); // /a/ prefix album

        // Images
        validHashes.put("awsGf9p", "http://imgur.com/awsGf9p"); // No prefix image URL
        validHashes.put("sCjRLQG", "http://i.imgur.com/sCjRLQG.jpg?1"); // Direct url with param
        validHashes.put("PBTrqAA", "https://imgur.com/gallery/PBTrqAA"); // gallery prefix
        validHashes.put("mhcWa37", "http://imgur.com/gallery/mhcWa37/new"); // Extra path after hash
        validHashes.put("SWSteYm", "http://imgur.com/r/google/SWSteYm"); // /r/ prefix

        AllTests.validateCanParseAndHashes(mImgurParser, validHashes);
    }

    // Tests a standard album URL
    @Test(timeout = API_CALL_TIMEOUT_MS)
    public void testAlbum() throws IOException, RuntimeException {
        URL albumUrl = ParseUtils.getUrlObject("https://imgur.com/a/cU6rs");
        String albumHash = "cU6rs";
        String previewHash = "zzaVA8m";
        testAlbumWithApiCall(albumUrl, albumHash, previewHash, 12);
    }

    // Album URL returns 404 by API
    @Test(expected = RuntimeException.class, timeout = API_CALL_TIMEOUT_MS)
    public void testAlbumWith404Error() throws IOException, RuntimeException {
        URL albumUrl = ParseUtils.getUrlObject("https://imgur.com/a/a8sxH");
        assertNotNull(albumUrl);
        mImgurParser.parse(albumUrl);
    }

    // Tests an album with a /gallery URL
    @Test(timeout = API_CALL_TIMEOUT_MS)
    public void testAlbumWithGalleryUrl() throws IOException, RuntimeException {
        URL albumUrl = ParseUtils.getUrlObject("https://imgur.com/gallery/WKauF");
        String albumHash = "WKauF";
        String previewHash = "FJRVge0";
        testAlbumWithApiCall(albumUrl, albumHash, previewHash, 8);
    }

    // Tests a direct GIF URL with no API call
    @Test
    public void testGifWithNoApiCall() throws IOException {
        URL imgurUrl = ParseUtils.getUrlObject("http://i.imgur.com/FJRVge0.gif");
        testSingleMediaWithNoApiCall(imgurUrl, "FJRVge0", true);
    }

    // Tests a direct GIF URL with an uppercase extension with no API call
    @Test
    public void testGifWithUppercaseExtension() throws IOException {
        URL imgurUrl = ParseUtils.getUrlObject("https://i.imgur.com/M1ZXzzn.GIF");
        testSingleMediaWithNoApiCall(imgurUrl, "M1ZXzzn", true);
    }

    // Tests a direct GIFV URL with no API call
    @Test
    public void testGifvWithNoApiCall() throws IOException {
        URL imgurUrl = ParseUtils.getUrlObject("http://i.imgur.com/aRadjBe.gifv");
        testSingleMediaWithNoApiCall(imgurUrl, "aRadjBe", true);
    }

    // Tests a direct JPG thumbnail URL with no API call
    @Test
    public void testJpgThumbnailWithNoApiCall() throws IOException {
        URL imgurUrl = ParseUtils.getUrlObject("https://i.imgur.com/jIg2N6qb.jpg");
        testSingleMediaWithNoApiCall(imgurUrl, "jIg2N6q", false);
    }

    // Tests a direct JPG URL with no API call
    @Test
    public void testJpgWithNoApiCall() throws IOException {
        URL imgurUrl = ParseUtils.getUrlObject("https://i.imgur.com/zzaVA8m.jpg");
        testSingleMediaWithNoApiCall(imgurUrl, "zzaVA8m", false);
    }

    private void testAlbumWithApiCall(final URL albumUrl, final String albumHash,
            final String previewHash, final int albumCount) throws IOException {
        assertNotNull(albumUrl);

        ParserResponse result = mImgurParser.parse(albumUrl);
        assertFalse(albumHash + " isSingleMedia", result.isSingleMedia());
        assertTrue(albumHash + " isAlbum", result.isAlbum());

        assertNotNull(albumHash + " getApiResponse", result.getApiResponse());
        IApiResponse apiResponse = result.getApiResponse();
        assertTrue(albumHash + " API Response Successful", apiResponse.isSuccessful());
        assertNull(albumHash + " Album should return null media", apiResponse.getMedia());
        assertNull(albumHash + " No error message on success", apiResponse.getErrorMessage());

        assertEquals(albumHash + " preview URL valid",
                "https://i.imgur.com/" + previewHash + "m.jpg",
                apiResponse.getPreviewUrl().toString());
        IMediaAlbum album = apiResponse.getAlbum();
        assertNotNull(albumHash + " Album not null", album);
        assertEquals(albumHash + " Album contains 8 images", albumCount, album.getCount());

    }

    private void testSingleMediaWithNoApiCall(final URL imgurUrl, final String hash,
            final boolean isAnimated) throws IOException {
        assertNotNull(imgurUrl);
        ParserResponse result = new ImgurParser(mOkHttpClient).parse(imgurUrl);
        IMedia media = result.getMedia();
        assertNotNull(hash + " result media not null", media);
        String expectedExtension = isAnimated ? ".mp4" : ".jpg";
        assertTrue(hash + " isSingleMedia", result.isSingleMedia());
        assertEquals(hash + " isVideo", isAnimated, media.isVideo());
        assertEquals(hash + " preview URL", "https://i.imgur.com/" + hash + "m.jpg",
                media.getPreviewUrl().toString());
        assertEquals(hash + " high quality URL", "https://i.imgur.com/" + hash + expectedExtension,
                media.getUrl(true).toString());
        URL expectedLowQualityUrl = ParseUtils.getUrlObject(
                isAnimated ? null : "https://i.imgur.com/" + hash + "h.jpg");
        assertEquals(hash + " low quality URL", expectedLowQualityUrl, media.getUrl(false));
    }
}