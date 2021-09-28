package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
/* loaded from: classes2.dex */
public interface Supplier<T> {
    @CanIgnoreReturnValue
    T get();
}
