/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 吴吉林
 */
public class CryptoUtilsTest {

    private static final String AES_KEY = "WDF#$H*#SJN*&G$&";
    private static final String AES_IV = "JH&$GF$DR%*K@SC%";

    @Test
    public void testEncryptAes() {
        String source = "0a2f763ccbe9544a2edf11a4d0967f19";
        String cipher = CryptoUtils.encryptAes(AES_KEY, AES_IV, source);
        Assert.assertEquals("Z684w0chSHZEnsjkQcDfyzDbjTy0nhH09kq2XlG1sZg=", cipher);
    }

    @Test
    public void testEncryptAesNumber() {
        String source = "3840";
        String cipher = CryptoUtils.encryptAes(AES_KEY, AES_IV, source);
//        Assert.assertEquals("Z684w0chSHZEnsjkQcDfyzDbjTy0nhH09kq2XlG1sZg=", cipher);
    }
}
