package com.score.rahasak.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class Annotations {

    @Retention(RetentionPolicy.SOURCE)
    public @interface NumberOfChannels {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SamplingRate {
    }

    Annotations() {
    }
}
