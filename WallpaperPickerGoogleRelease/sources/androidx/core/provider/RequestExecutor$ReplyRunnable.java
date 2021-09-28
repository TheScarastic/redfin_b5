package androidx.core.provider;

import android.os.Handler;
import androidx.core.util.Consumer;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public class RequestExecutor$ReplyRunnable<T> implements Runnable {
    public Callable<T> mCallable;
    public Consumer<T> mConsumer;
    public Handler mHandler;

    public RequestExecutor$ReplyRunnable(Handler handler, Callable<T> callable, Consumer<T> consumer) {
        this.mCallable = callable;
        this.mConsumer = consumer;
        this.mHandler = handler;
    }

    @Override // java.lang.Runnable
    public void run() {
        final T t;
        try {
            t = this.mCallable.call();
        } catch (Exception unused) {
            t = null;
        }
        final Consumer<T> consumer = this.mConsumer;
        this.mHandler.post(new Runnable(this) { // from class: androidx.core.provider.RequestExecutor$ReplyRunnable.1
            /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: androidx.core.util.Consumer */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                consumer.accept(t);
            }
        });
    }
}
