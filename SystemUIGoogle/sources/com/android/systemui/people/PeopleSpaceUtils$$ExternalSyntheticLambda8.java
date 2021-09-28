package com.android.systemui.people;

import android.content.pm.ShortcutInfo;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda8 implements Predicate {
    public static final /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda8 INSTANCE = new PeopleSpaceUtils$$ExternalSyntheticLambda8();

    private /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda8() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return Objects.nonNull((ShortcutInfo) obj);
    }
}
