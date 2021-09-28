package androidx.core.view.inputmethod;

import android.view.inputmethod.InputContentInfo;
/* loaded from: classes.dex */
public final class InputContentInfoCompat {
    public final InputContentInfoCompatImpl mImpl;

    /* loaded from: classes.dex */
    public static final class InputContentInfoCompatApi25Impl implements InputContentInfoCompatImpl {
        public final InputContentInfo mObject;

        public InputContentInfoCompatApi25Impl(Object obj) {
            this.mObject = (InputContentInfo) obj;
        }

        public void requestPermission() {
            this.mObject.requestPermission();
        }
    }

    /* loaded from: classes.dex */
    public interface InputContentInfoCompatImpl {
    }

    public InputContentInfoCompat(InputContentInfoCompatImpl inputContentInfoCompatImpl) {
        this.mImpl = inputContentInfoCompatImpl;
    }
}
