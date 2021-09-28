package com.android.systemui.people.widget;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda8 implements Function {
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda8 INSTANCE = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda8();

    private /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda8() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((ConversationChannelWrapper) obj).getShortcutInfo();
    }
}
