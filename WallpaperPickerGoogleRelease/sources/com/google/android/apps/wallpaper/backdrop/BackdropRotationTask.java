package com.google.android.apps.wallpaper.backdrop;

import com.google.android.gms.gcm.GcmTaskService;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public class BackdropRotationTask extends GcmTaskService {
    public static final /* synthetic */ int $r8$clinit = 0;

    /* loaded from: classes.dex */
    public static class TaskResultCallable implements Callable<Integer> {
        public int taskResult;

        public TaskResultCallable(AnonymousClass1 r1) {
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // java.util.concurrent.Callable
        public Integer call() throws Exception {
            return Integer.valueOf(this.taskResult);
        }
    }
}
