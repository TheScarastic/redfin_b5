package com.android.systemui.people;

import android.app.people.ConversationStatus;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleTileViewHelper$$ExternalSyntheticLambda4 implements Predicate {
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda4 INSTANCE = new PeopleTileViewHelper$$ExternalSyntheticLambda4();

    private /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return PeopleTileViewHelper.lambda$getHasNewStory$4((ConversationStatus) obj);
    }
}
