package com.sample;

public class PromoNotApplicableException extends Exception {
    public PromoNotApplicableException() {
    }

    public PromoNotApplicableException(String message) {
        super(message);
    }

    public PromoNotApplicableException(String message, Throwable cause) {
        super(message, cause);
    }

    public PromoNotApplicableException(Throwable cause) {
        super(cause);
    }

    public PromoNotApplicableException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
