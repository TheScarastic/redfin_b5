package androidx.slice;
/* loaded from: classes.dex */
public class SystemClock implements Clock {
    @Override // androidx.slice.Clock
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
