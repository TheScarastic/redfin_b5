package com.android.systemui.people;

import android.app.people.PeopleSpaceTile;
import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda1 implements Comparator {
    public static final /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda1 INSTANCE = new PeopleSpaceUtils$$ExternalSyntheticLambda1();

    private /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return PeopleSpaceUtils.lambda$getSortedTiles$6((PeopleSpaceTile) obj, (PeopleSpaceTile) obj2);
    }
}
