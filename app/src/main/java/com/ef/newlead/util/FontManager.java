package com.ef.newlead.util;

import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seanzhou on 9/1/16.
 * <p>
 * Font manager changes text font on the fly.
 * Code modified from <a href="https://github.com/YesGraph/android-sdk"> github source<href/>
 */
public class FontManager {
    private static FontManager sInstance;
    private Map<String, Typeface> mCache;

    private FontManager() {
        mCache = new HashMap<String, Typeface>();
    }

    public static FontManager getInstance() {
        if (sInstance == null) {
            sInstance = new FontManager();
        }

        return sInstance;
    }

    /***
     * Sets the typeface for {@link TextView}.
     *
     * @param textView {@link TextView} object
     * @param fontName relative path of the font file in the assert fold
     */
    public void setFont(TextView textView, String fontName) {
        Typeface typeface = mCache.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(textView.getContext().getAssets(), fontName);
            mCache.put(fontName, typeface);
        }

        textView.setTypeface(typeface);
    }
}
