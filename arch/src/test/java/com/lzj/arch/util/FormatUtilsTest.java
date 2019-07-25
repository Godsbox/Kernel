/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.util;

import org.junit.Test;

import static com.lzj.arch.util.FormatUtils.format;
import static com.lzj.arch.util.FormatUtils.formatSpeed;
import static com.lzj.arch.util.FormatUtils.inTenThousand;
import static com.lzj.arch.util.FormatUtils.toOneDecimal;
import static org.junit.Assert.assertEquals;

/**
 * @author 吴吉林
 */
public class FormatUtilsTest {

    @Test
    public void testFormat() {
        String result = format(230);
        assertEquals("230", result);
        result = format(2230);
        assertEquals("2,230", result);
        result = toOneDecimal(32000 / 10000.0);
        assertEquals("3.2", result);
        result = toOneDecimal(2000 / 10000.0);
        assertEquals("0.2", result);
    }

    @Test
    public void testInTenThousand() {
        int n = 86845;
        assertEquals("8.68w", inTenThousand(n));
        n = 87000;
        assertEquals("8.7w", inTenThousand(n));
    }

    @Test
    public void testFormatSpeed() {
        int speed = 1024;
        assertEquals("1m/s", formatSpeed(speed));
        speed = 1536;
        assertEquals("1.5m/s", formatSpeed(speed));
        speed = 536;
        assertEquals("536k/s", formatSpeed(speed));
    }
}
