package com.android.systemui.statusbar.phone;

import android.content.Intent;
import android.view.RemoteAnimationAdapter;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1 implements Function1 {
    public final /* synthetic */ StatusBarNotificationActivityStarter.AnonymousClass4 f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1(StatusBarNotificationActivityStarter.AnonymousClass4 r1, Intent intent, int i) {
        this.f$0 = r1;
        this.f$1 = intent;
        this.f$2 = i;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Object obj) {
        return this.f$0.lambda$onDismiss$0(this.f$1, this.f$2, (RemoteAnimationAdapter) obj);
    }
}
