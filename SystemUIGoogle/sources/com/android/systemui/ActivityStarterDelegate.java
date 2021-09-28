package com.android.systemui;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ActivityStarterDelegate implements ActivityStarter {
    private Optional<Lazy<StatusBar>> mActualStarter;

    public ActivityStarterDelegate(Optional<Lazy<StatusBar>> optional) {
        this.mActualStarter = optional;
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent) {
        this.mActualStarter.ifPresent(new Consumer(pendingIntent) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda0
            public final /* synthetic */ PendingIntent f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$CBqVSpB7Mel0xqOPaF4g4pe1ZNA(this.f$0, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$0(PendingIntent pendingIntent, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable) {
        this.mActualStarter.ifPresent(new Consumer(pendingIntent, runnable) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda3
            public final /* synthetic */ PendingIntent f$0;
            public final /* synthetic */ Runnable f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$FBf0zedluqDHJZZwHcw2AxX0srs(this.f$0, this.f$1, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$1(PendingIntent pendingIntent, Runnable runnable, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent, runnable);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, View view) {
        this.mActualStarter.ifPresent(new Consumer(pendingIntent, runnable, view) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda4
            public final /* synthetic */ PendingIntent f$0;
            public final /* synthetic */ Runnable f$1;
            public final /* synthetic */ View f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.m38$r8$lambda$6tLSiD24UE4d3HlFYy4ZUs6FY(this.f$0, this.f$1, this.f$2, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$2(PendingIntent pendingIntent, Runnable runnable, View view, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent, runnable, view);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer(pendingIntent, runnable, controller) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda5
            public final /* synthetic */ PendingIntent f$0;
            public final /* synthetic */ Runnable f$1;
            public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startPendingIntentDismissingKeyguard$3(this.f$0, this.f$1, this.f$2, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startPendingIntentDismissingKeyguard$3(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).startPendingIntentDismissingKeyguard(pendingIntent, runnable, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, boolean z2, int i) {
        this.mActualStarter.ifPresent(new Consumer(intent, z, z2, i) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda12
            public final /* synthetic */ Intent f$0;
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ boolean f$2;
            public final /* synthetic */ int f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$HpC2bvSf3Xg5JB55PP2XeiJ7XkQ(this.f$0, this.f$1, this.f$2, this.f$3, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startActivity$4(Intent intent, boolean z, boolean z2, int i, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, z2, i);
    }

    public static /* synthetic */ void lambda$startActivity$5(Intent intent, boolean z, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z) {
        this.mActualStarter.ifPresent(new Consumer(intent, z) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda8
            public final /* synthetic */ Intent f$0;
            public final /* synthetic */ boolean f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startActivity$5(this.f$0, this.f$1, (Lazy) obj);
            }
        });
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer(intent, z, controller) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda9
            public final /* synthetic */ Intent f$0;
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$startActivity$6(this.f$0, this.f$1, this.f$2, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startActivity$6(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, boolean z2) {
        this.mActualStarter.ifPresent(new Consumer(intent, z, z2) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda11
            public final /* synthetic */ Intent f$0;
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ boolean f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$TnVER6KGnQoJTf40L_GZQH1lfsM(this.f$0, this.f$1, this.f$2, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startActivity$7(Intent intent, boolean z, boolean z2, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, z2);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void startActivity(Intent intent, boolean z, ActivityStarter.Callback callback) {
        this.mActualStarter.ifPresent(new Consumer(intent, z, callback) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda10
            public final /* synthetic */ Intent f$0;
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ ActivityStarter.Callback f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$KLVw4PbQur0YwmyYTDp4Bw0foyg(this.f$0, this.f$1, this.f$2, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$startActivity$8(Intent intent, boolean z, ActivityStarter.Callback callback, Lazy lazy) {
        ((StatusBar) lazy.get()).startActivity(intent, z, callback);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(Intent intent, int i) {
        this.mActualStarter.ifPresent(new Consumer(intent, i) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda6
            public final /* synthetic */ Intent f$0;
            public final /* synthetic */ int f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$postStartActivityDismissingKeyguard$9(this.f$0, this.f$1, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$9(Intent intent, int i, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(intent, i);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(Intent intent, int i, ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer(intent, i, controller) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda7
            public final /* synthetic */ Intent f$0;
            public final /* synthetic */ int f$1;
            public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.lambda$postStartActivityDismissingKeyguard$10(this.f$0, this.f$1, this.f$2, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$10(Intent intent, int i, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(intent, i, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(PendingIntent pendingIntent) {
        this.mActualStarter.ifPresent(new Consumer(pendingIntent) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda1
            public final /* synthetic */ PendingIntent f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$dmHqE3zn4WygtrRMHKyegXFWE54(this.f$0, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$11(PendingIntent pendingIntent, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(pendingIntent);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postStartActivityDismissingKeyguard(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller) {
        this.mActualStarter.ifPresent(new Consumer(pendingIntent, controller) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda2
            public final /* synthetic */ PendingIntent f$0;
            public final /* synthetic */ ActivityLaunchAnimator.Controller f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.m39$r8$lambda$HJrg9XibaLdwPlCoddPK4isQ(this.f$0, this.f$1, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$postStartActivityDismissingKeyguard$12(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller, Lazy lazy) {
        ((StatusBar) lazy.get()).postStartActivityDismissingKeyguard(pendingIntent, controller);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void postQSRunnableDismissingKeyguard(Runnable runnable) {
        this.mActualStarter.ifPresent(new Consumer(runnable) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda14
            public final /* synthetic */ Runnable f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$GzyBy2b3NSkGSU5PUbMjcd8vV2s(this.f$0, (Lazy) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$postQSRunnableDismissingKeyguard$13(Runnable runnable, Lazy lazy) {
        ((StatusBar) lazy.get()).postQSRunnableDismissingKeyguard(runnable);
    }

    public static /* synthetic */ void lambda$dismissKeyguardThenExecute$14(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, Lazy lazy) {
        ((StatusBar) lazy.get()).dismissKeyguardThenExecute(onDismissAction, runnable, z);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        this.mActualStarter.ifPresent(new Consumer(runnable, z) { // from class: com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda13
            public final /* synthetic */ Runnable f$1;
            public final /* synthetic */ boolean f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityStarterDelegate.$r8$lambda$vjhY4wjH0txfRe7laoKH2eiRkKE(ActivityStarter.OnDismissAction.this, this.f$1, this.f$2, (Lazy) obj);
            }
        });
    }
}
