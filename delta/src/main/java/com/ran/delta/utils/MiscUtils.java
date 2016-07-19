package com.ran.delta.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.ran.delta.widget.bottomBar.BottomBarTab;

public final class MiscUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static BottomBarTab[] inflateMenuFromResource(Activity activity, @MenuRes int menuRes) {
        PopupMenu popupMenu = new PopupMenu(activity, null);
        Menu menu = popupMenu.getMenu();
        activity.getMenuInflater().inflate(menuRes, menu);

        int menuSize = menu.size();
        BottomBarTab[] tabs = new BottomBarTab[menuSize];

        for (int i = 0; i < menuSize; i++) {
            MenuItem item = menu.getItem(i);
            BottomBarTab tab = new BottomBarTab(item.getIcon(),
                    String.valueOf(item.getTitle()));
            tab.id = item.getItemId();
            tabs[i] = tab;
        }
        return tabs;
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

}
