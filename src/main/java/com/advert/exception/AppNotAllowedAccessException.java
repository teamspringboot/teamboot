package com.advert.exception;

public class AppNotAllowedAccessException  extends BaseRuntimeException {

    private static final long serialVersionUID = -927028338979047567L;

    public AppNotAllowedAccessException(String userPin) {
        super("global.error.code.app.not.allowed.access", new String[] {userPin});
    }
}