package com.android.systemui.qs.external;

import android.content.ComponentName;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CustomTileStatePersister.kt */
/* loaded from: classes.dex */
public final class TileServiceKey {
    private final ComponentName componentName;
    private final String string;
    private final int user;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TileServiceKey)) {
            return false;
        }
        TileServiceKey tileServiceKey = (TileServiceKey) obj;
        return Intrinsics.areEqual(this.componentName, tileServiceKey.componentName) && this.user == tileServiceKey.user;
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + Integer.hashCode(this.user);
    }

    /* JADX DEBUG: TODO: convert one arg to string using `String.valueOf()`, args: [(wrap: java.lang.Object : ?: CAST (java.lang.Object) (wrap: java.lang.String : 0x0011: INVOKE  (r2v1 java.lang.String A[REMOVE]) = (r2v0 android.content.ComponentName) type: VIRTUAL call: android.content.ComponentName.flattenToString():java.lang.String)), (':' char), (r3v0 int)] */
    public TileServiceKey(ComponentName componentName, int i) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.componentName = componentName;
        this.user = i;
        StringBuilder sb = new StringBuilder();
        sb.append((Object) componentName.flattenToString());
        sb.append(':');
        sb.append(i);
        this.string = sb.toString();
    }

    public String toString() {
        return this.string;
    }
}
