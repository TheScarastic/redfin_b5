package com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils;

import android.support.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
/* loaded from: classes2.dex */
public final class Utils {
    private static final Random randomScreenIdGenerator = new Random();

    public static <T> T checkNotNull(@Nullable T t) {
        Objects.requireNonNull(t);
        return t;
    }
}
