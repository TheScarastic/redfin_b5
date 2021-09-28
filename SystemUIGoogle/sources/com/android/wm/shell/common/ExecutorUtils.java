package com.android.wm.shell.common;

import android.util.Slog;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class ExecutorUtils {
    public static <T> void executeRemoteCallWithTaskPermission(RemoteCallable<T> remoteCallable, String str, Consumer<T> consumer) {
        executeRemoteCallWithTaskPermission(remoteCallable, str, consumer, false);
    }

    public static <T> void executeRemoteCallWithTaskPermission(RemoteCallable<T> remoteCallable, String str, Consumer<T> consumer, boolean z) {
        if (remoteCallable != null) {
            remoteCallable.getContext().enforceCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", str);
            if (z) {
                try {
                    remoteCallable.getRemoteCallExecutor().executeBlocking(new Runnable(consumer, remoteCallable) { // from class: com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda0
                        public final /* synthetic */ Consumer f$0;
                        public final /* synthetic */ RemoteCallable f$1;

                        {
                            this.f$0 = r1;
                            this.f$1 = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            ExecutorUtils.m518$r8$lambda$ie9__9r8EAIXQD67CKIgDXd9z8(this.f$0, this.f$1);
                        }
                    });
                } catch (InterruptedException e) {
                    Slog.e("ExecutorUtils", "Remote call failed", e);
                }
            } else {
                remoteCallable.getRemoteCallExecutor().execute(new Runnable(consumer, remoteCallable) { // from class: com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1
                    public final /* synthetic */ Consumer f$0;
                    public final /* synthetic */ RemoteCallable f$1;

                    {
                        this.f$0 = r1;
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        ExecutorUtils.m519$r8$lambda$s8eUOdyrqpqzzyFwAMGxOMaCg4(this.f$0, this.f$1);
                    }
                });
            }
        }
    }
}
