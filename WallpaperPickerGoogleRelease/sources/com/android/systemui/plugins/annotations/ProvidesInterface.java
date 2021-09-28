package com.android.systemui.plugins.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes.dex */
public @interface ProvidesInterface {
    String action() default "";

    int version();
}
