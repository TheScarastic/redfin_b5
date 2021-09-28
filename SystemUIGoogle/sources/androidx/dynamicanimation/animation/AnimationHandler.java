package androidx.dynamicanimation.animation;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class AnimationHandler {
    private static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal<>();
    private FrameCallbackScheduler mScheduler;
    private final SimpleArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new SimpleArrayMap<>();
    final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList<>();
    private final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    private final Runnable mRunnable = new Runnable() { // from class: androidx.dynamicanimation.animation.AnimationHandler$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            AnimationHandler.$r8$lambda$YiBk3K09ifLdAPQDzrOxNk7Tzy0(AnimationHandler.this);
        }
    };
    long mCurrentFrameTime = 0;
    private boolean mListDirty = false;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    /* loaded from: classes.dex */
    public interface FrameCallbackScheduler {
        boolean isCurrentThread();

        void postFrameCallback(Runnable runnable);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AnimationCallbackDispatcher {
        private AnimationCallbackDispatcher() {
        }

        void dispatchAnimationFrame() {
            AnimationHandler.this.mCurrentFrameTime = SystemClock.uptimeMillis();
            AnimationHandler animationHandler = AnimationHandler.this;
            animationHandler.doAnimationFrame(animationHandler.mCurrentFrameTime);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                AnimationHandler.this.mScheduler.postFrameCallback(AnimationHandler.this.mRunnable);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mCallbackDispatcher.dispatchAnimationFrame();
    }

    /* access modifiers changed from: package-private */
    public static AnimationHandler getInstance() {
        FrameCallbackScheduler frameCallbackScheduler;
        ThreadLocal<AnimationHandler> threadLocal = sAnimatorHandler;
        if (threadLocal.get() == null) {
            if (Build.VERSION.SDK_INT >= 16) {
                frameCallbackScheduler = new FrameCallbackScheduler16();
            } else {
                frameCallbackScheduler = new FrameCallbackScheduler14();
            }
            threadLocal.set(new AnimationHandler(frameCallbackScheduler));
        }
        return threadLocal.get();
    }

    public AnimationHandler(FrameCallbackScheduler frameCallbackScheduler) {
        this.mScheduler = frameCallbackScheduler;
    }

    /* access modifiers changed from: package-private */
    public void addAnimationFrameCallback(AnimationFrameCallback animationFrameCallback, long j) {
        if (this.mAnimationCallbacks.size() == 0) {
            this.mScheduler.postFrameCallback(this.mRunnable);
        }
        if (!this.mAnimationCallbacks.contains(animationFrameCallback)) {
            this.mAnimationCallbacks.add(animationFrameCallback);
        }
        if (j > 0) {
            this.mDelayedCallbackStartTime.put(animationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + j));
        }
    }

    /* access modifiers changed from: package-private */
    public void removeCallback(AnimationFrameCallback animationFrameCallback) {
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        int indexOf = this.mAnimationCallbacks.indexOf(animationFrameCallback);
        if (indexOf >= 0) {
            this.mAnimationCallbacks.set(indexOf, null);
            this.mListDirty = true;
        }
    }

    void doAnimationFrame(long j) {
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i = 0; i < this.mAnimationCallbacks.size(); i++) {
            AnimationFrameCallback animationFrameCallback = this.mAnimationCallbacks.get(i);
            if (animationFrameCallback != null && isCallbackDue(animationFrameCallback, uptimeMillis)) {
                animationFrameCallback.doAnimationFrame(j);
            }
        }
        cleanUpList();
    }

    /* access modifiers changed from: package-private */
    public boolean isCurrentThread() {
        return this.mScheduler.isCurrentThread();
    }

    private boolean isCallbackDue(AnimationFrameCallback animationFrameCallback, long j) {
        Long l = this.mDelayedCallbackStartTime.get(animationFrameCallback);
        if (l == null) {
            return true;
        }
        if (l.longValue() >= j) {
            return false;
        }
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        return true;
    }

    private void cleanUpList() {
        if (this.mListDirty) {
            for (int size = this.mAnimationCallbacks.size() - 1; size >= 0; size--) {
                if (this.mAnimationCallbacks.get(size) == null) {
                    this.mAnimationCallbacks.remove(size);
                }
            }
            this.mListDirty = false;
        }
    }

    public void setScheduler(FrameCallbackScheduler frameCallbackScheduler) {
        this.mScheduler = frameCallbackScheduler;
    }

    public FrameCallbackScheduler getScheduler() {
        return this.mScheduler;
    }

    /* loaded from: classes.dex */
    static final class FrameCallbackScheduler16 implements FrameCallbackScheduler {
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private final Looper mLooper = Looper.myLooper();

        FrameCallbackScheduler16() {
        }

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public void postFrameCallback(Runnable runnable) {
            this.mChoreographer.postFrameCallback(new AnimationHandler$FrameCallbackScheduler16$$ExternalSyntheticLambda0(runnable));
        }

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public boolean isCurrentThread() {
            return Thread.currentThread() == this.mLooper.getThread();
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FrameCallbackScheduler14 implements FrameCallbackScheduler {
        private final Handler mHandler = new Handler(Looper.myLooper());
        private long mLastFrameTime;

        FrameCallbackScheduler14() {
        }

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public void postFrameCallback(Runnable runnable) {
            this.mHandler.postDelayed(new AnimationHandler$FrameCallbackScheduler14$$ExternalSyntheticLambda0(this, runnable), Math.max(10 - (SystemClock.uptimeMillis() - this.mLastFrameTime), 0L));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$postFrameCallback$0(Runnable runnable) {
            this.mLastFrameTime = SystemClock.uptimeMillis();
            runnable.run();
        }

        @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
        public boolean isCurrentThread() {
            return Thread.currentThread() == this.mHandler.getLooper().getThread();
        }
    }
}
