package com.android.systemui.people.widget;

import android.content.Intent;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PeopleSpaceWidgetManager.AnonymousClass2 f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ PeopleSpaceWidgetManager$2$$ExternalSyntheticLambda0(PeopleSpaceWidgetManager.AnonymousClass2 r1, Intent intent) {
        this.f$0 = r1;
        this.f$1 = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onReceive$0(this.f$1);
    }
}
