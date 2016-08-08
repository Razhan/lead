package com.ef.newlead;

import java.net.UnknownHostException;

public class ErrorHandler {

    public static String getErrorMessage(Throwable exception) {
        if (exception instanceof UnknownHostException) {
            return "Are you connected to the internet?";
        }

        return "An unknown error has occurred";
    }
}

