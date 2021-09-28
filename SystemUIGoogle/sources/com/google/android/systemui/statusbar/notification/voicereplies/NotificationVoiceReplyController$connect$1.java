package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.people.Subscription;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.concurrent.CancellationException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ChildHandle;
import kotlinx.coroutines.ChildJob;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.Job;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$connect$1 implements NotificationVoiceReplyManager, Job {
    private final /* synthetic */ Job $$delegate_0;
    final /* synthetic */ NotificationVoiceReplyController.Connection $connection;
    final /* synthetic */ Job $job;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    @Override // kotlinx.coroutines.Job
    public ChildHandle attachChild(ChildJob childJob) {
        Intrinsics.checkNotNullParameter(childJob, "child");
        return this.$$delegate_0.attachChild(childJob);
    }

    @Override // kotlinx.coroutines.Job
    public void cancel(CancellationException cancellationException) {
        this.$$delegate_0.cancel(cancellationException);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(function2, "operation");
        return (R) this.$$delegate_0.fold(r, function2);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return (E) this.$$delegate_0.get(key);
    }

    @Override // kotlinx.coroutines.Job
    public CancellationException getCancellationException() {
        return this.$$delegate_0.getCancellationException();
    }

    @Override // kotlin.coroutines.CoroutineContext.Element
    public CoroutineContext.Key<?> getKey() {
        return this.$$delegate_0.getKey();
    }

    @Override // kotlinx.coroutines.Job
    public DisposableHandle invokeOnCompletion(boolean z, boolean z2, Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "handler");
        return this.$$delegate_0.invokeOnCompletion(z, z2, function1);
    }

    @Override // kotlinx.coroutines.Job
    public boolean isActive() {
        return this.$$delegate_0.isActive();
    }

    @Override // kotlinx.coroutines.Job
    public Object join(Continuation<? super Unit> continuation) {
        return this.$$delegate_0.join(continuation);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public CoroutineContext minusKey(CoroutineContext.Key<?> key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return this.$$delegate_0.minusKey(key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public CoroutineContext plus(CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter(coroutineContext, "context");
        return this.$$delegate_0.plus(coroutineContext);
    }

    @Override // kotlinx.coroutines.Job
    public boolean start() {
        return this.$$delegate_0.start();
    }

    /* access modifiers changed from: package-private */
    public NotificationVoiceReplyController$connect$1(Job job, NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection) {
        this.$job = job;
        this.this$0 = notificationVoiceReplyController;
        this.$connection = connection;
        this.$$delegate_0 = job;
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager
    public VoiceReplySubscription registerHandler(NotificationVoiceReplyHandler notificationVoiceReplyHandler) {
        Intrinsics.checkNotNullParameter(notificationVoiceReplyHandler, "handler");
        ensureConnected();
        return new Object(this.this$0.registerHandler(this.$connection, notificationVoiceReplyHandler), this, this.this$0, this.$connection, notificationVoiceReplyHandler) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$connect$1$registerHandler$1
            private final /* synthetic */ Subscription $$delegate_0;
            final /* synthetic */ NotificationVoiceReplyController.Connection $connection;
            final /* synthetic */ NotificationVoiceReplyHandler $handler;
            final /* synthetic */ Subscription $sub;
            final /* synthetic */ NotificationVoiceReplyController$connect$1 this$0;
            final /* synthetic */ NotificationVoiceReplyController this$1;

            @Override // com.android.systemui.statusbar.notification.people.Subscription
            public void unsubscribe() {
                this.$$delegate_0.unsubscribe();
            }

            /* access modifiers changed from: package-private */
            {
                this.$sub = r1;
                this.this$0 = r2;
                this.this$1 = r3;
                this.$connection = r4;
                this.$handler = r5;
                this.$$delegate_0 = r1;
            }

            @Override // com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplySubscription
            public Object startVoiceReply(int i, String str, Function0<Unit> function0, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super Unit> continuation) {
                this.this$0.ensureConnected();
                Object startVoiceReply = this.this$1.startVoiceReply(this.$connection, this.$handler, i, str, function0, function2, continuation);
                return startVoiceReply == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? startVoiceReply : Unit.INSTANCE;
            }
        };
    }

    /* access modifiers changed from: private */
    public final void ensureConnected() {
        if (!isActive()) {
            throw new IllegalStateException("Manager is no longer connected".toString());
        }
    }
}
