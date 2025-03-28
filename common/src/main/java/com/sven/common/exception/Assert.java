package com.sven.common.exception;

public interface Assert {

    BaseException newException(Object... args);

    BaseException newException(Throwable t, Object... args);

    default void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }
    
    default void assertNotTrue(Boolean obj, Object... args) {
        if (obj) {
            throw newException(args);
        }
    }

    default void assertNotFalse(Boolean obj, Object... args) {
        if (!obj) {
            throw newException(args);
        }
    }
}
