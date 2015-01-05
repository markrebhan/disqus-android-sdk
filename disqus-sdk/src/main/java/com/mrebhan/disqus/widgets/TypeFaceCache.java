package com.mrebhan.disqus.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;
import java.util.WeakHashMap;

/**
 * Typefaces are not cached by default, we must cache the .ttf files for better performance
 */
public class TypeFaceCache {

    private static final WeakHashMap<String, Typeface> cache = new WeakHashMap<>();

    public static Typeface getTypeface(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), assetPath);
                    cache.put(assetPath, typeface);
                } catch (Exception e) {
                    Log.e("TypeFaceCache", "Unable to get typeface", e);
                }
            }

            return cache.get(assetPath);
        }
    }

}
