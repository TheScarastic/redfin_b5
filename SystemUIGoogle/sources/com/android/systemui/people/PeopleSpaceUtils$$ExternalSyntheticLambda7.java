package com.android.systemui.people;

import android.app.people.PeopleSpaceTile;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda7 implements Predicate {
    public static final /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda7 INSTANCE = new PeopleSpaceUtils$$ExternalSyntheticLambda7();

    private /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda7() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return PeopleSpaceUtils.shouldKeepConversation((PeopleSpaceTile) obj);
    }
}
