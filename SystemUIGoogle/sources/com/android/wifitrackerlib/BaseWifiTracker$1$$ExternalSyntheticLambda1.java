package com.android.wifitrackerlib;

import android.net.NetworkKey;
import java.util.Set;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class BaseWifiTracker$1$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ BaseWifiTracker$1$$ExternalSyntheticLambda1(Set set) {
        this.f$0 = set;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return this.f$0.add((NetworkKey) obj);
    }
}
