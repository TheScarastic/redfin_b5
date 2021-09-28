package com.android.systemui.dump;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: DumpManager.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RegisteredDumpable<T> {
    private final T dumpable;
    private final String name;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegisteredDumpable)) {
            return false;
        }
        RegisteredDumpable registeredDumpable = (RegisteredDumpable) obj;
        return Intrinsics.areEqual(this.name, registeredDumpable.name) && Intrinsics.areEqual(this.dumpable, registeredDumpable.dumpable);
    }

    public int hashCode() {
        int hashCode = this.name.hashCode() * 31;
        T t = this.dumpable;
        return hashCode + (t == null ? 0 : t.hashCode());
    }

    public String toString() {
        return "RegisteredDumpable(name=" + this.name + ", dumpable=" + this.dumpable + ')';
    }

    public RegisteredDumpable(String str, T t) {
        Intrinsics.checkNotNullParameter(str, "name");
        this.name = str;
        this.dumpable = t;
    }

    public final String getName() {
        return this.name;
    }

    public final T getDumpable() {
        return this.dumpable;
    }
}
