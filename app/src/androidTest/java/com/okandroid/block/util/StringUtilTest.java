package com.okandroid.block.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StringUtilTest {

    @Test
    public void trim() throws Exception {
        assertEquals("abc", StringUtil.trim("abc ", null));
        assertEquals("abc", StringUtil.trim(" abc ", null));
        assertEquals("abc", StringUtil.trim("abc*\\", "*\\"));
        assertEquals("abc", StringUtil.trim("/ abc*\\", "*\\/"));
        assertEquals("abc", StringUtil.trim("./ abc.//*\\", "*\\/."));
    }

}