package com.ef.newlead.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.lang.reflect.Method;

public final class MiscUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String getSystemLanguage(Context context) {
        String language = context.getResources().getConfiguration().locale.getLanguage().toLowerCase();
        String country = context.getResources().getConfiguration().locale.getCountry().toLowerCase();

        if (language.equals(country) || language.equals("en")) {
            return language;
        } else {
            return language + "-" + country;
        }
    }

    public static Method find(Object useCase, String useCaseName, Object[] args) {
        return null;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            return "";
        }
    }

    public static SpannableString getSpannableText(String str, String keyword, int color) {
        int start = str.indexOf("%s");
        str = String.format(str, keyword);
        SpannableString styledString = new SpannableString(str);

        if (str.contains(keyword)) {
            styledString.setSpan(new ForegroundColorSpan(color), start, start + keyword.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return styledString;
    }

}
