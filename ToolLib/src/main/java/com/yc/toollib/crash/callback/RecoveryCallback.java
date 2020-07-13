package com.yc.toollib.crash.callback;


public interface RecoveryCallback {

    void stackTrace(String stackTrace);

    void cause(String cause);

    void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber);

    void throwable(Throwable throwable);
}
