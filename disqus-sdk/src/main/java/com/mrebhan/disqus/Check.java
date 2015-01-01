package com.mrebhan.disqus;

public final class Check {

    public static Object checkNotNull(Object param, String message) {
        if (param == null) {
            throw new NullPointerException(message);
        }
        return param;
    }
}
