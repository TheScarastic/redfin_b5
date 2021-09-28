package androidx.cardview;
/* loaded from: classes.dex */
public class R$dimen {
    public static boolean isOOM(Throwable th) {
        while (th != null) {
            if (th instanceof OutOfMemoryError) {
                return true;
            }
            th = th.getCause();
        }
        return false;
    }
}
