/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author 吴吉林
 */
public class ParameterizedTypeImpl implements ParameterizedType {

    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type arg) {
        this(raw, new Type[]{ arg });
    }

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Class getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
