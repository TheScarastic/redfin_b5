package com.android.wm.shell.animation;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
/* synthetic */ class PhysicsAnimator$Companion$instanceConstructor$1 extends FunctionReferenceImpl implements Function1<Object, PhysicsAnimator<Object>> {
    public static final PhysicsAnimator$Companion$instanceConstructor$1 INSTANCE = new PhysicsAnimator$Companion$instanceConstructor$1();

    PhysicsAnimator$Companion$instanceConstructor$1() {
        super(1, PhysicsAnimator.class, "<init>", "<init>(Ljava/lang/Object;)V", 0);
    }

    /* Return type fixed from 'com.android.wm.shell.animation.PhysicsAnimator<T>' to match base method */
    @Override // kotlin.jvm.functions.Function1
    public final PhysicsAnimator<Object> invoke(Object obj) {
        Intrinsics.checkNotNullParameter(obj, "p0");
        return new PhysicsAnimator<>(obj, null);
    }
}
