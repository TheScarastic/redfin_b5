package com.android.systemui.people.widget;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda7 implements Function {
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda7 INSTANCE = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda7();

    private /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda7() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((ConversationChannelWrapper) obj).getShortcutInfo();
    }
}
