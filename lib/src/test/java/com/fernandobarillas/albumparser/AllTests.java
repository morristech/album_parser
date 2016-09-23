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

package com.fernandobarillas.albumparser;

import com.fernandobarillas.albumparser.media.IMedia;
import com.fernandobarillas.albumparser.media.IMediaAlbum;
import com.fernandobarillas.albumparser.parser.IParserResponse;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Runs all the parser tests provided by the library
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ImgurParserTest.class})
public class AllTests {
    static final int API_CALL_TIMEOUT_MS = 10000; // Wait time for HTTP call to finish

    public static void compareAlbum(final URL originalUrl, final IMediaAlbum expectedAlbum,
            final IMediaAlbum mediaAlbum) {
        assertNotNull(originalUrl + " Original URL null", originalUrl);
        assertNotNull(originalUrl + " Expected Album null", expectedAlbum);
        assertNotNull(originalUrl + " Album null", mediaAlbum);

        // Album fields
        assertEquals(originalUrl + " Preview URL", expectedAlbum.getPreviewUrl(),
                mediaAlbum.getPreviewUrl());
        assertEquals(originalUrl + " Count", expectedAlbum.getCount(), mediaAlbum.getCount());
        assertEquals(originalUrl + " Is empty", expectedAlbum.isEmpty(), mediaAlbum.isEmpty());

        // Album media
        List<IMedia> expectedAlbumMedia = expectedAlbum.getAlbumMedia();
        List<IMedia> albumMedia = mediaAlbum.getAlbumMedia();
        assertNotNull(originalUrl + " Expected AlbumMedia null", expectedAlbumMedia);
        assertNotNull(originalUrl + " AlbumMedia null", albumMedia);
        assertEquals(originalUrl + " AlumMedia size", expectedAlbumMedia.size(), albumMedia.size());
        int mediaSize = albumMedia.size();

        for (int i = 0; i < mediaSize; i++) {
            AllTests.compareMedia(originalUrl, expectedAlbumMedia.get(i), albumMedia.get(i));
        }
    }

    public static void compareMedia(final URL originalUrl, final IMedia expectedMedia,
            final IMedia media) {
        assertNotNull(originalUrl + " Original URL null", originalUrl);
        assertNotNull(originalUrl + " Expected Media null", expectedMedia);
        assertNotNull(originalUrl + " Media null", media);

        // Media Fields
        assertEquals(originalUrl + " High quality byte size", expectedMedia.getByteSize(true),
                media.getByteSize(true));
        assertEquals(originalUrl + " Low quality byte size", expectedMedia.getByteSize(false),
                media.getByteSize(false));
        assertEquals(originalUrl + " Description", expectedMedia.getDescription(),
                media.getDescription());
        assertEquals(originalUrl + " Duration", expectedMedia.getDuration(), media.getDuration(),
                0.0);
        assertEquals(originalUrl + " High quality height", expectedMedia.getHeight(true),
                media.getHeight(true));
        assertEquals(originalUrl + " Low quality height", expectedMedia.getHeight(false),
                media.getHeight(false));
        assertEquals(originalUrl + " Preview URL", expectedMedia.getPreviewUrl(),
                media.getPreviewUrl());
        assertEquals(originalUrl + " Title", expectedMedia.getTitle(), media.getTitle());
        assertEquals(originalUrl + " High quality URL", expectedMedia.getUrl(true),
                media.getUrl(true));
        assertEquals(originalUrl + " Low quality URL", expectedMedia.getUrl(false),
                media.getUrl(false));
        assertEquals(originalUrl + " High quality width", expectedMedia.getWidth(true),
                media.getWidth(true));
        assertEquals(originalUrl + " Low quality width", expectedMedia.getWidth(false),
                media.getWidth(false));
        assertEquals(originalUrl + " Is video", expectedMedia.isVideo(), media.isVideo());
    }

    public static void compareParserResponse(final URL originalUrl,
            final IParserResponse expectedParserResponse, final IParserResponse parserResponse) {
        assertNotNull(originalUrl + " Original URL null", originalUrl);
        assertNotNull(originalUrl + " Expected Parser Response null", expectedParserResponse);
        assertNotNull(originalUrl + " Parser Response null", parserResponse);

        assertEquals(originalUrl + " Original URL", expectedParserResponse.getOriginalUrl(),
                parserResponse.getOriginalUrl());
        assertEquals(originalUrl + " Is album", expectedParserResponse.isAlbum(),
                parserResponse.isAlbum());
        assertEquals(originalUrl + " Is single media", expectedParserResponse.isSingleMedia(),
                parserResponse.isSingleMedia());
        assertEquals(originalUrl + " Media", expectedParserResponse.getMedia() == null,
                parserResponse.getMedia() == null);
        assertEquals(originalUrl + " Media Album", expectedParserResponse.getAlbum() == null,
                parserResponse.getAlbum() == null);

        if (expectedParserResponse.isAlbum()) {
            compareAlbum(originalUrl, expectedParserResponse.getAlbum(), parserResponse.getAlbum());
        }

        if (expectedParserResponse.isSingleMedia()) {
            compareMedia(originalUrl, expectedParserResponse.getMedia(), parserResponse.getMedia());
        }
    }
}