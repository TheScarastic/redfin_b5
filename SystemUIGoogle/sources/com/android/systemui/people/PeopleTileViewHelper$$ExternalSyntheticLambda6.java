package com.android.systemui.people;

import android.app.people.ConversationStatus;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleTileViewHelper$$ExternalSyntheticLambda6 implements Predicate {
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda6 INSTANCE = new PeopleTileViewHelper$$ExternalSyntheticLambda6();

    private /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda6() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return PeopleTileViewHelper.lambda$getBirthdayStatus$6((ConversationStatus) obj);
    }
}
