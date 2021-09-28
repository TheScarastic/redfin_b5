package com.android.wm.shell.common;

import java.lang.reflect.Array;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public interface ShellExecutor extends Executor {
    @Override // java.util.concurrent.Executor
    void execute(Runnable runnable);

    void executeDelayed(Runnable runnable, long j);

    boolean hasCallback(Runnable runnable);

    void removeCallbacks(Runnable runnable);

    default void executeBlocking(Runnable runnable, int i, TimeUnit timeUnit) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        execute(new Runnable(runnable, countDownLatch) { // from class: com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda0
            public final /* synthetic */ Runnable f$0;
            public final /* synthetic */ CountDownLatch f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ShellExecutor.lambda$executeBlocking$0(this.f$0, this.f$1);
            }
        });
        countDownLatch.await((long) i, timeUnit);
    }

    /* access modifiers changed from: private */
    static /* synthetic */ void lambda$executeBlocking$0(Runnable runnable, CountDownLatch countDownLatch) {
        runnable.run();
        countDownLatch.countDown();
    }

    default void executeBlocking(Runnable runnable) throws InterruptedException {
        executeBlocking(runnable, 2, TimeUnit.SECONDS);
    }

    default <T> T executeBlockingForResult(Supplier<T> supplier, Class cls) {
        Object[] objArr = (Object[]) Array.newInstance(cls, 1);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        execute(new Runnable(objArr, supplier, countDownLatch) { // from class: com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda1
            public final /* synthetic */ Object[] f$0;
            public final /* synthetic */ Supplier f$1;
            public final /* synthetic */ CountDownLatch f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ShellExecutor.lambda$executeBlockingForResult$1(this.f$0, this.f$1, this.f$2);
            }
        });
        try {
            countDownLatch.await();
            return (T) objArr[0];
        } catch (InterruptedException unused) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    static /* synthetic */ void lambda$executeBlockingForResult$1(Object[] objArr, Supplier supplier, CountDownLatch countDownLatch) {
        objArr[0] = supplier.get();
        countDownLatch.countDown();
    }
}
