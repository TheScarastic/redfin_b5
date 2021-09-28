package com.android.systemui.people.widget;

import android.service.notification.ConversationChannelWrapper;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda12 implements Predicate {
    public static final /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda12 INSTANCE = new PeopleSpaceWidgetManager$$ExternalSyntheticLambda12();

    private /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda12() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return PeopleSpaceWidgetManager.lambda$getPriorityTiles$6((ConversationChannelWrapper) obj);
    }
}
