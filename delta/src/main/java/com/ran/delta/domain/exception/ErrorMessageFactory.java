package com.ran.delta.domain.exception;

public interface ErrorMessageFactory {

    String getErrorMessage(Throwable exception);
}
