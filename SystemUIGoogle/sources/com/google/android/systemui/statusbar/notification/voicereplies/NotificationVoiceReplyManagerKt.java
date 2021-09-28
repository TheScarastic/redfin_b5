package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.people.Subscription;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerKt {
    public static final Object awaitHunStateChange(HeadsUpManager headsUpManager, Continuation<? super Pair<NotificationEntry, Boolean>> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
        NotificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1 notificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1 = new NotificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1(AtomicLatch(), headsUpManager, cancellableContinuationImpl);
        headsUpManager.addListener(notificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1);
        cancellableContinuationImpl.invokeOnCancellation(new Function1<Throwable, Unit>(headsUpManager, notificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$awaitHunStateChange$2$1
            final /* synthetic */ NotificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1 $listener;
            final /* synthetic */ HeadsUpManager $this_awaitHunStateChange;

            /* access modifiers changed from: package-private */
            {
                this.$this_awaitHunStateChange = r1;
                this.$listener = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke(th);
                return Unit.INSTANCE;
            }

            public final void invoke(Throwable th) {
                this.$this_awaitHunStateChange.removeListener(this.$listener);
            }
        });
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public static final Object awaitStateChange(NotificationShadeWindowController notificationShadeWindowController, Continuation<? super StatusBarWindowState> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
        NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1 notificationVoiceReplyManagerKt$awaitStateChange$2$cb$1 = new NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1(AtomicLatch(), notificationShadeWindowController, cancellableContinuationImpl);
        notificationShadeWindowController.registerCallback(notificationVoiceReplyManagerKt$awaitStateChange$2$cb$1);
        cancellableContinuationImpl.invokeOnCancellation(new Function1<Throwable, Unit>(notificationShadeWindowController, notificationVoiceReplyManagerKt$awaitStateChange$2$cb$1) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$awaitStateChange$2$1
            final /* synthetic */ NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1 $cb;
            final /* synthetic */ NotificationShadeWindowController $this_awaitStateChange;

            /* access modifiers changed from: package-private */
            {
                this.$this_awaitStateChange = r1;
                this.$cb = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke(th);
                return Unit.INSTANCE;
            }

            public final void invoke(Throwable th) {
                this.$this_awaitStateChange.unregisterCallback(this.$cb);
            }
        });
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0055 A[EDGE_INSN: B:33:0x0055->B:26:0x0055 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final android.widget.Button getReplyButton(android.view.View r4, android.app.RemoteInput r5) {
        /*
            r0 = 16908722(0x10201b2, float:2.3878445E-38)
            android.view.View r4 = r4.findViewById(r0)
            android.view.ViewGroup r4 = (android.view.ViewGroup) r4
            r0 = 0
            if (r4 != 0) goto L_0x000e
        L_0x000c:
            r4 = r0
            goto L_0x001b
        L_0x000e:
            kotlin.sequences.Sequence r4 = com.android.systemui.util.ConvenienceExtensionsKt.getChildren(r4)
            if (r4 != 0) goto L_0x0015
            goto L_0x000c
        L_0x0015:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getReplyButton$1 r1 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getReplyButton$1.INSTANCE
            kotlin.sequences.Sequence r4 = kotlin.sequences.SequencesKt.filter(r4, r1)
        L_0x001b:
            if (r4 != 0) goto L_0x001e
            goto L_0x0061
        L_0x001e:
            java.util.Iterator r4 = r4.iterator()
        L_0x0022:
            boolean r1 = r4.hasNext()
            if (r1 == 0) goto L_0x0054
            java.lang.Object r1 = r4.next()
            r2 = r1
            android.view.View r2 = (android.view.View) r2
            r3 = 16909350(0x1020426, float:2.3880205E-38)
            java.lang.Object r2 = r2.getTag(r3)
            if (r2 != 0) goto L_0x003a
        L_0x0038:
            r2 = r0
            goto L_0x0049
        L_0x003a:
            boolean r3 = r2 instanceof java.lang.Object[]
            if (r3 == 0) goto L_0x0041
            java.lang.Object[] r2 = (java.lang.Object[]) r2
            goto L_0x0042
        L_0x0041:
            r2 = r0
        L_0x0042:
            if (r2 != 0) goto L_0x0045
            goto L_0x0038
        L_0x0045:
            kotlin.sequences.Sequence r2 = kotlin.collections.ArraysKt.asSequence(r2)
        L_0x0049:
            if (r2 != 0) goto L_0x004d
            r2 = 0
            goto L_0x0051
        L_0x004d:
            boolean r2 = kotlin.sequences.SequencesKt.contains(r2, r5)
        L_0x0051:
            if (r2 == 0) goto L_0x0022
            goto L_0x0055
        L_0x0054:
            r1 = r0
        L_0x0055:
            android.view.View r1 = (android.view.View) r1
            if (r1 != 0) goto L_0x005a
            goto L_0x0061
        L_0x005a:
            boolean r4 = r1 instanceof android.widget.Button
            if (r4 == 0) goto L_0x0061
            android.widget.Button r1 = (android.widget.Button) r1
            r0 = r1
        L_0x0061:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.getReplyButton(android.view.View, android.app.RemoteInput):android.widget.Button");
    }

    public static final Subscription Subscription(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "block");
        return new SafeSubscription(function0);
    }

    /* access modifiers changed from: private */
    public static final <K, V> V getOrPut(Map<K, V> map, K k, Function0<? extends V> function0) {
        V compute = map.compute(k, new BiFunction<K, V, V>(function0) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getOrPut$1
            final /* synthetic */ Function0<V> $default;

            /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlin.jvm.functions.Function0<? extends V> */
            /* JADX WARN: Multi-variable type inference failed */
            /* access modifiers changed from: package-private */
            {
                this.$default = r1;
            }

            /* JADX WARN: Type inference failed for: r2v1, types: [V, java.lang.Object] */
            @Override // java.util.function.BiFunction
            public final V apply(K k2, V v) {
                return v == 0 ? this.$default.invoke() : v;
            }
        });
        Intrinsics.checkNotNull(compute);
        return compute;
    }

    public static final <T> Flow<T> distinctUntilChanged(Flow<? extends T> flow, Function2<? super T, ? super T, Boolean> function2) {
        Intrinsics.checkNotNullParameter(flow, "<this>");
        Intrinsics.checkNotNullParameter(function2, "areEqual");
        return FlowKt.flow(new NotificationVoiceReplyManagerKt$distinctUntilChanged$2(flow, function2, null));
    }

    public static final <T> Object collectLatest(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super Unit> continuation) {
        Object coroutineScope = CoroutineScopeKt.coroutineScope(new NotificationVoiceReplyManagerKt$collectLatest$2(flow, function2, null), continuation);
        return coroutineScope == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? coroutineScope : Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    public static final AtomicBoolean AtomicLatch() {
        return new AtomicBoolean(true);
    }
}
