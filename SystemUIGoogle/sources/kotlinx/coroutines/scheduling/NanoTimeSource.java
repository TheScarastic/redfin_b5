package kotlinx.coroutines.scheduling;
/* compiled from: Tasks.kt */
/* loaded from: classes2.dex */
public final class NanoTimeSource extends TimeSource {
    public static final NanoTimeSource INSTANCE = new NanoTimeSource();

    private NanoTimeSource() {
    }

    @Override // kotlinx.coroutines.scheduling.TimeSource
    public long nanoTime() {
        return System.nanoTime();
    }
}
