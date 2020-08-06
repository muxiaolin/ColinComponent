package com.mgtj.airadio.base.utils.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author : 彭林
 * date   : 2020/7/10
 * desc   :
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
