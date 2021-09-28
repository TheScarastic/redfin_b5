package com.google.android.material.snackbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.Objects;
/* loaded from: classes.dex */
public class SnackbarManager {
    public static SnackbarManager snackbarManager;
    public final Object lock = new Object();
    public final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() { // from class: com.google.android.material.snackbar.SnackbarManager.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what != 0) {
                return false;
            }
            SnackbarManager snackbarManager2 = SnackbarManager.this;
            SnackbarRecord snackbarRecord = (SnackbarRecord) message.obj;
            synchronized (snackbarManager2.lock) {
                if (snackbarRecord == null) {
                    Objects.requireNonNull(snackbarRecord);
                    throw null;
                }
            }
            return true;
        }
    });

    /* loaded from: classes.dex */
    public static class SnackbarRecord {
    }
}
