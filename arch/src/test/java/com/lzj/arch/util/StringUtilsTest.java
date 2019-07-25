/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.util;

import android.support.v4.util.ArrayMap;

import org.junit.Assert;
import org.junit.Test;

import static com.lzj.arch.util.StringUtils.isEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author 吴吉林
 */
public class StringUtilsTest {
    @Test
    public void testIsEmpty() {
        assertFalse(isEmpty("not empty"));
        assertTrue(isEmpty(""));
        assertTrue(isEmpty(null));

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("ddd", "");
        Assert.assertEquals(map.get("ddd"), "");
    }
}
