/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.lzj.arch.util.ObjectUtils.requireNonNull;
import static org.junit.Assert.assertSame;

/**
 * @author 吴吉林
 */
public class ObjectUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testRequireNonNull() {
        Object obj = new Object();
        String msg = "obj must not be null";
        Object result = requireNonNull(obj, msg);
        assertSame(obj, result);
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(msg);
        requireNonNull(null, msg);
    }
}
