package com.ef.cat;

import com.ran.delta.domain.exception.ErrorMessageFactory;

import java.net.UnknownHostException;

public class ErrorMessageDeterminer implements ErrorMessageFactory {

    @Override
    public String getErrorMessage(Throwable exception) {
        if (exception instanceof UnknownHostException) {
            return "Network Error: Are you connected to the internet?";
        }

        return "An unknown error has occurred";
    }
}

