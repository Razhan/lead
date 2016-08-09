package com.ef.newlead;

import android.content.Context;
import android.widget.Toast;

import java.net.UnknownHostException;

public class ErrorHandler {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    private static String getErrorMessage(Throwable exception) {
        if (exception instanceof UnknownHostException) {
            return "An network error has occurred";
        }

        return "An unknown error has occurred";
    }

    public static void showError(Throwable exception) {
        Toast.makeText(mContext, getErrorMessage(exception), Toast.LENGTH_SHORT).show();
    }
}

