package com.ef.newlead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

public final class ErrorHandler {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    @NonNull
    private static String getErrorMessage(Throwable exception) {
        if (exception instanceof UnknownHostException) {
            return "An network error has occurred";
        } else if (exception instanceof SocketTimeoutException) {
            return "Time Out";
        } else if (exception instanceof HttpException) {
            HttpException httpException = (HttpException) exception;
            switch (httpException.code()) {
                // confirm with backend

                default:
                    try {
                        return httpException.response().errorBody().string();
                    } catch (IOException e) {
                        return "An unknown error has occurred";
                    }
            }
        } else {
            return exception.getMessage();
        }
    }

    public static void showError(Throwable exception) {
        String errorMessage = getErrorMessage(exception);

        Log.d("exception", errorMessage);
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }
}

