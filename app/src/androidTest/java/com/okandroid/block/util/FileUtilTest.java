package com.okandroid.block.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FileUtilTest {
    @Test
    public void getFileExtensionFromUrl() throws Exception {
        assertEquals("jpg", FileUtil.getFileExtensionFromUrl("http://abc/a.jpg"));
        assertEquals("jpg", FileUtil.getFileExtensionFromUrl("a.jpg"));
        assertEquals("jpg", FileUtil.getFileExtensionFromUrl("http://abc/a.jpg/"));
        assertEquals(null, FileUtil.getFileExtensionFromUrl("http://abc/.jpg"));
        assertEquals(null, FileUtil.getFileExtensionFromUrl("http://abc/.jpg."));
    }

    @Test
    public void getFilenameFromUrl() throws Exception {
        assertEquals("a.jpg", FileUtil.getFilenameFromUrl("http://abc/a.jpg"));
        assertEquals("a.jpg", FileUtil.getFilenameFromUrl("http://abc/a.jpg/"));
        assertEquals("a.jpg", FileUtil.getFilenameFromUrl("./../bc/a.jpg/"));
        assertEquals("a", FileUtil.getFilenameFromUrl("./../bc/a/"));
        assertEquals(".jpg", FileUtil.getFilenameFromUrl("./../bc/.jpg/"));
    }

}