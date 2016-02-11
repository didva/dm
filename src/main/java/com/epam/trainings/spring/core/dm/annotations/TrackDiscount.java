package com.epam.trainings.spring.core.dm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TrackDiscount {

    String name();
}
