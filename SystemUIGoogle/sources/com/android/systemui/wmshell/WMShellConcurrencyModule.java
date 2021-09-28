package com.android.systemui.wmshell;

import android.animation.AnimationHandler;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.R$bool;
import com.android.wm.shell.common.HandlerExecutor;
import com.android.wm.shell.common.ShellExecutor;
/* loaded from: classes2.dex */
public abstract class WMShellConcurrencyModule {
    private static boolean enableShellMainThread(Context context) {
        return context.getResources().getBoolean(R$bool.config_enableShellMainThread);
    }

    public static ShellExecutor provideSysUIMainExecutor(Handler handler) {
        return new HandlerExecutor(handler);
    }

    public static Handler provideShellMainHandler(Context context, Handler handler) {
        if (!enableShellMainThread(context)) {
            return handler;
        }
        HandlerThread handlerThread = new HandlerThread("wmshell.main", -4);
        handlerThread.start();
        if (Build.IS_DEBUGGABLE) {
            handlerThread.getLooper().setTraceTag(32);
            handlerThread.getLooper().setSlowLogThresholdMs(30, 30);
        }
        return Handler.createAsync(handlerThread.getLooper());
    }

    public static ShellExecutor provideShellMainExecutor(Context context, Handler handler, ShellExecutor shellExecutor) {
        return enableShellMainThread(context) ? new HandlerExecutor(handler) : shellExecutor;
    }

    public static ShellExecutor provideShellAnimationExecutor() {
        HandlerThread handlerThread = new HandlerThread("wmshell.anim", -4);
        handlerThread.start();
        if (Build.IS_DEBUGGABLE) {
            handlerThread.getLooper().setTraceTag(32);
            handlerThread.getLooper().setSlowLogThresholdMs(30, 30);
        }
        return new HandlerExecutor(Handler.createAsync(handlerThread.getLooper()));
    }

    public static ShellExecutor provideSplashScreenExecutor() {
        HandlerThread handlerThread = new HandlerThread("wmshell.splashscreen", -10);
        handlerThread.start();
        return new HandlerExecutor(handlerThread.getThreadHandler());
    }

    public static AnimationHandler provideShellMainExecutorSfVsyncAnimationHandler(ShellExecutor shellExecutor) {
        try {
            AnimationHandler animationHandler = new AnimationHandler();
            shellExecutor.executeBlocking(new Runnable(animationHandler) { // from class: com.android.systemui.wmshell.WMShellConcurrencyModule$$ExternalSyntheticLambda0
                public final /* synthetic */ AnimationHandler f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    WMShellConcurrencyModule.m436$r8$lambda$54VPGE3VTZg1u0qIFH7VvqizBY(this.f$0);
                }
            });
            return animationHandler;
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to initialize SfVsync animation handler in 1s", e);
        }
    }

    public static /* synthetic */ void lambda$provideShellMainExecutorSfVsyncAnimationHandler$0(AnimationHandler animationHandler) {
        animationHandler.setProvider(new SfVsyncFrameCallbackProvider());
    }
}
