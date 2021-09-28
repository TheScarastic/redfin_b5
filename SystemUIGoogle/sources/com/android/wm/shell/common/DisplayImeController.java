package com.android.wm.shell.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IDisplayWindowInsetsController;
import android.view.IWindowManager;
import android.view.InsetsSource;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.view.IInputMethodManager;
import com.android.wm.shell.common.DisplayController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class DisplayImeController implements DisplayController.OnDisplaysChangedListener {
    public static final Interpolator INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    private final DisplayController mDisplayController;
    protected final Executor mMainExecutor;
    private final TransactionPool mTransactionPool;
    protected final IWindowManager mWmService;
    private final SparseArray<PerDisplay> mImePerDisplay = new SparseArray<>();
    private final ArrayList<ImePositionProcessor> mPositionProcessors = new ArrayList<>();

    /* loaded from: classes2.dex */
    public interface ImePositionProcessor {
        default void onImeControlTargetChanged(int i, boolean z) {
        }

        default void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
        }

        default void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
        }

        default int onImeStartPositioning(int i, int i2, int i3, boolean z, boolean z2, SurfaceControl.Transaction transaction) {
            return 0;
        }

        default void onImeVisibilityChanged(int i, boolean z) {
        }
    }

    public DisplayImeController(IWindowManager iWindowManager, DisplayController displayController, Executor executor, TransactionPool transactionPool) {
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        this.mMainExecutor = executor;
        this.mTransactionPool = transactionPool;
    }

    public void startMonitorDisplays() {
        this.mDisplayController.addDisplayWindowListener(this);
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onDisplayAdded(int i) {
        PerDisplay perDisplay = new PerDisplay(i, this.mDisplayController.getDisplayLayout(i).rotation());
        perDisplay.register();
        this.mImePerDisplay.put(i, perDisplay);
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        PerDisplay perDisplay = this.mImePerDisplay.get(i);
        if (perDisplay != null && this.mDisplayController.getDisplayLayout(i).rotation() != perDisplay.mRotation && isImeShowing(i)) {
            perDisplay.startAnimation(true, false);
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onDisplayRemoved(int i) {
        try {
            this.mWmService.setDisplayWindowInsetsController(i, (IDisplayWindowInsetsController) null);
        } catch (RemoteException unused) {
            Slog.w("DisplayImeController", "Unable to remove insets controller on display " + i);
        }
        this.mImePerDisplay.remove(i);
    }

    private boolean isImeShowing(int i) {
        InsetsSource source;
        PerDisplay perDisplay = this.mImePerDisplay.get(i);
        if (perDisplay == null || (source = perDisplay.mInsetsState.getSource(19)) == null || perDisplay.mImeSourceControl == null || !source.isVisible()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void dispatchPositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImePositionChanged(i, i2, transaction);
            }
        }
    }

    /* access modifiers changed from: private */
    public int dispatchStartPositioning(int i, int i2, int i3, boolean z, boolean z2, SurfaceControl.Transaction transaction) {
        int i4;
        synchronized (this.mPositionProcessors) {
            i4 = 0;
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                i4 |= it.next().onImeStartPositioning(i, i2, i3, z, z2, transaction);
            }
        }
        return i4;
    }

    /* access modifiers changed from: private */
    public void dispatchEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImeEndPositioning(i, z, transaction);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchImeControlTargetChanged(int i, boolean z) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImeControlTargetChanged(i, z);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchVisibilityChanged(int i, boolean z) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImeVisibilityChanged(i, z);
            }
        }
    }

    public void addPositionProcessor(ImePositionProcessor imePositionProcessor) {
        synchronized (this.mPositionProcessors) {
            if (!this.mPositionProcessors.contains(imePositionProcessor)) {
                this.mPositionProcessors.add(imePositionProcessor);
            }
        }
    }

    public void removePositionProcessor(ImePositionProcessor imePositionProcessor) {
        synchronized (this.mPositionProcessors) {
            this.mPositionProcessors.remove(imePositionProcessor);
        }
    }

    /* loaded from: classes2.dex */
    public class PerDisplay {
        final int mDisplayId;
        int mRotation;
        final InsetsState mInsetsState = new InsetsState();
        protected final DisplayWindowInsetsControllerImpl mInsetsControllerImpl = new DisplayWindowInsetsControllerImpl();
        InsetsSourceControl mImeSourceControl = null;
        int mAnimationDirection = 0;
        ValueAnimator mAnimation = null;
        boolean mImeShowing = false;
        final Rect mImeFrame = new Rect();
        boolean mAnimateAlpha = true;

        public void topFocusedWindowChanged(String str) {
        }

        public PerDisplay(int i, int i2) {
            this.mRotation = 0;
            this.mDisplayId = i;
            this.mRotation = i2;
        }

        public void register() {
            try {
                DisplayImeController.this.mWmService.setDisplayWindowInsetsController(this.mDisplayId, this.mInsetsControllerImpl);
            } catch (RemoteException unused) {
                Slog.w("DisplayImeController", "Unable to set insets controller on display " + this.mDisplayId);
            }
        }

        protected void insetsChanged(InsetsState insetsState) {
            if (!this.mInsetsState.equals(insetsState)) {
                updateImeVisibility(insetsState.getSourceOrDefaultVisibility(19));
                InsetsSource source = insetsState.getSource(19);
                Rect frame = source.getFrame();
                Rect frame2 = this.mInsetsState.getSource(19).getFrame();
                this.mInsetsState.set(insetsState, true);
                if (this.mImeShowing && !frame.equals(frame2) && source.isVisible()) {
                    startAnimation(this.mImeShowing, true);
                }
            }
        }

        protected void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
            InsetsSourceControl insetsSourceControl;
            insetsChanged(insetsState);
            Point point = null;
            boolean z = false;
            if (insetsSourceControlArr != null) {
                insetsSourceControl = null;
                for (InsetsSourceControl insetsSourceControl2 : insetsSourceControlArr) {
                    if (insetsSourceControl2 != null && insetsSourceControl2.getType() == 19) {
                        insetsSourceControl = insetsSourceControl2;
                    }
                }
            } else {
                insetsSourceControl = null;
            }
            boolean z2 = this.mImeSourceControl != null;
            if (insetsSourceControl != null) {
                z = true;
            }
            if (z2 != z) {
                DisplayImeController.this.dispatchImeControlTargetChanged(this.mDisplayId, z);
            }
            if (z) {
                InsetsSourceControl insetsSourceControl3 = this.mImeSourceControl;
                if (insetsSourceControl3 != null) {
                    point = insetsSourceControl3.getSurfacePosition();
                }
                boolean z3 = !insetsSourceControl.getSurfacePosition().equals(point);
                boolean z4 = !DisplayImeController.haveSameLeash(this.mImeSourceControl, insetsSourceControl);
                if (this.mAnimation == null) {
                    if (z4) {
                        applyVisibilityToLeash(insetsSourceControl);
                    }
                    if (!this.mImeShowing) {
                        DisplayImeController.this.removeImeSurface();
                    }
                } else if (z3) {
                    startAnimation(this.mImeShowing, true);
                }
                InsetsSourceControl insetsSourceControl4 = this.mImeSourceControl;
                if (insetsSourceControl4 != null) {
                    insetsSourceControl4.release(DisplayImeController$PerDisplay$$ExternalSyntheticLambda1.INSTANCE);
                }
                this.mImeSourceControl = insetsSourceControl;
            }
        }

        private void applyVisibilityToLeash(InsetsSourceControl insetsSourceControl) {
            SurfaceControl leash = insetsSourceControl.getLeash();
            if (leash != null) {
                SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
                if (this.mImeShowing) {
                    acquire.show(leash);
                } else {
                    acquire.hide(leash);
                }
                acquire.apply();
                DisplayImeController.this.mTransactionPool.release(acquire);
            }
        }

        protected void showInsets(int i, boolean z) {
            if ((i & WindowInsets.Type.ime()) != 0) {
                startAnimation(true, false);
            }
        }

        protected void hideInsets(int i, boolean z) {
            if ((i & WindowInsets.Type.ime()) != 0) {
                startAnimation(false, false);
            }
        }

        private void setVisibleDirectly(boolean z) {
            this.mInsetsState.getSource(19).setVisible(z);
            try {
                DisplayImeController.this.mWmService.modifyDisplayWindowInsets(this.mDisplayId, this.mInsetsState);
            } catch (RemoteException unused) {
            }
        }

        /* access modifiers changed from: private */
        public int imeTop(float f) {
            return this.mImeFrame.top + ((int) f);
        }

        private boolean calcIsFloating(InsetsSource insetsSource) {
            Rect frame = insetsSource.getFrame();
            if (frame.height() != 0 && frame.height() > DisplayImeController.this.mDisplayController.getDisplayLayout(this.mDisplayId).navBarFrameHeight()) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: private */
        public void startAnimation(boolean z, boolean z2) {
            boolean z3;
            InsetsSource source = this.mInsetsState.getSource(19);
            if (source != null && this.mImeSourceControl != null) {
                Rect frame = source.getFrame();
                final boolean z4 = calcIsFloating(source) && z;
                if (z4) {
                    this.mImeFrame.set(frame);
                    this.mImeFrame.bottom -= (int) (DisplayImeController.this.mDisplayController.getDisplayLayout(this.mDisplayId).density() * -80.0f);
                } else if (frame.height() != 0) {
                    this.mImeFrame.set(frame);
                }
                if (!z2 && this.mAnimationDirection == 1 && z) {
                    return;
                }
                if (this.mAnimationDirection != 2 || z) {
                    float f = 0.0f;
                    ValueAnimator valueAnimator = this.mAnimation;
                    if (valueAnimator != null) {
                        if (valueAnimator.isRunning()) {
                            f = ((Float) this.mAnimation.getAnimatedValue()).floatValue();
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        this.mAnimation.cancel();
                    } else {
                        z3 = false;
                    }
                    final float f2 = (float) this.mImeSourceControl.getSurfacePosition().y;
                    final float f3 = (float) this.mImeSourceControl.getSurfacePosition().x;
                    final float height = f2 + ((float) this.mImeFrame.height());
                    final float f4 = z ? height : f2;
                    final float f5 = z ? f2 : height;
                    if (this.mAnimationDirection == 0 && this.mImeShowing && z) {
                        f = f2;
                        z3 = true;
                    }
                    this.mAnimationDirection = z ? 1 : 2;
                    updateImeVisibility(z);
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(f4, f5);
                    this.mAnimation = ofFloat;
                    ofFloat.setDuration(z ? 275 : 340);
                    if (z3) {
                        this.mAnimation.setCurrentFraction((f - f4) / (f5 - f4));
                    }
                    this.mAnimation.addUpdateListener(new DisplayImeController$PerDisplay$$ExternalSyntheticLambda0(this, f3, z4, height, f2));
                    this.mAnimation.setInterpolator(DisplayImeController.INTERPOLATOR);
                    this.mAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.common.DisplayImeController.PerDisplay.1
                        private boolean mCancelled = false;

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animator) {
                            float f6;
                            SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
                            acquire.setPosition(PerDisplay.this.mImeSourceControl.getLeash(), f3, f4);
                            PerDisplay perDisplay = PerDisplay.this;
                            boolean z5 = false;
                            int dispatchStartPositioning = DisplayImeController.this.dispatchStartPositioning(perDisplay.mDisplayId, perDisplay.imeTop(height), PerDisplay.this.imeTop(f2), PerDisplay.this.mAnimationDirection == 1, z4, acquire);
                            PerDisplay perDisplay2 = PerDisplay.this;
                            if ((dispatchStartPositioning & 1) == 0) {
                                z5 = true;
                            }
                            perDisplay2.mAnimateAlpha = z5;
                            if (z5 || z4) {
                                float f7 = f4;
                                float f8 = height;
                                f6 = (f7 - f8) / (f2 - f8);
                            } else {
                                f6 = 1.0f;
                            }
                            acquire.setAlpha(perDisplay2.mImeSourceControl.getLeash(), f6);
                            PerDisplay perDisplay3 = PerDisplay.this;
                            if (perDisplay3.mAnimationDirection == 1) {
                                acquire.show(perDisplay3.mImeSourceControl.getLeash());
                            }
                            acquire.apply();
                            DisplayImeController.this.mTransactionPool.release(acquire);
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            this.mCancelled = true;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
                            if (!this.mCancelled) {
                                acquire.setPosition(PerDisplay.this.mImeSourceControl.getLeash(), f3, f5);
                                acquire.setAlpha(PerDisplay.this.mImeSourceControl.getLeash(), 1.0f);
                            }
                            PerDisplay perDisplay = PerDisplay.this;
                            DisplayImeController.this.dispatchEndPositioning(perDisplay.mDisplayId, this.mCancelled, acquire);
                            PerDisplay perDisplay2 = PerDisplay.this;
                            if (perDisplay2.mAnimationDirection == 2 && !this.mCancelled) {
                                acquire.hide(perDisplay2.mImeSourceControl.getLeash());
                                DisplayImeController.this.removeImeSurface();
                            }
                            acquire.apply();
                            DisplayImeController.this.mTransactionPool.release(acquire);
                            PerDisplay perDisplay3 = PerDisplay.this;
                            perDisplay3.mAnimationDirection = 0;
                            perDisplay3.mAnimation = null;
                        }
                    });
                    if (!z) {
                        setVisibleDirectly(false);
                    }
                    this.mAnimation.start();
                    if (z) {
                        setVisibleDirectly(true);
                    }
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$startAnimation$0(float f, boolean z, float f2, float f3, ValueAnimator valueAnimator) {
            SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            acquire.setPosition(this.mImeSourceControl.getLeash(), f, floatValue);
            acquire.setAlpha(this.mImeSourceControl.getLeash(), (this.mAnimateAlpha || z) ? (floatValue - f2) / (f3 - f2) : 1.0f);
            DisplayImeController.this.dispatchPositionChanged(this.mDisplayId, imeTop(floatValue), acquire);
            acquire.apply();
            DisplayImeController.this.mTransactionPool.release(acquire);
        }

        private void updateImeVisibility(boolean z) {
            if (this.mImeShowing != z) {
                this.mImeShowing = z;
                DisplayImeController.this.dispatchVisibilityChanged(this.mDisplayId, z);
            }
        }

        /* loaded from: classes2.dex */
        public class DisplayWindowInsetsControllerImpl extends IDisplayWindowInsetsController.Stub {
            public DisplayWindowInsetsControllerImpl() {
            }

            public void topFocusedWindowChanged(String str) throws RemoteException {
                DisplayImeController.this.mMainExecutor.execute(new DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda4(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$topFocusedWindowChanged$0(String str) {
                PerDisplay.this.topFocusedWindowChanged(str);
            }

            public void insetsChanged(InsetsState insetsState) throws RemoteException {
                DisplayImeController.this.mMainExecutor.execute(new DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda2(this, insetsState));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$insetsChanged$1(InsetsState insetsState) {
                PerDisplay.this.insetsChanged(insetsState);
            }

            public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) throws RemoteException {
                DisplayImeController.this.mMainExecutor.execute(new DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda3(this, insetsState, insetsSourceControlArr));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$insetsControlChanged$2(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
                PerDisplay.this.insetsControlChanged(insetsState, insetsSourceControlArr);
            }

            public void showInsets(int i, boolean z) throws RemoteException {
                DisplayImeController.this.mMainExecutor.execute(new DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1(this, i, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$showInsets$3(int i, boolean z) {
                PerDisplay.this.showInsets(i, z);
            }

            public void hideInsets(int i, boolean z) throws RemoteException {
                DisplayImeController.this.mMainExecutor.execute(new DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda0(this, i, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$hideInsets$4(int i, boolean z) {
                PerDisplay.this.hideInsets(i, z);
            }
        }
    }

    void removeImeSurface() {
        IInputMethodManager imms = getImms();
        if (imms != null) {
            try {
                imms.removeImeSurface();
            } catch (RemoteException e) {
                Slog.e("DisplayImeController", "Failed to remove IME surface.", e);
            }
        }
    }

    public IInputMethodManager getImms() {
        return IInputMethodManager.Stub.asInterface(ServiceManager.getService("input_method"));
    }

    /* access modifiers changed from: private */
    public static boolean haveSameLeash(InsetsSourceControl insetsSourceControl, InsetsSourceControl insetsSourceControl2) {
        if (insetsSourceControl == insetsSourceControl2) {
            return true;
        }
        if (!(insetsSourceControl == null || insetsSourceControl2 == null)) {
            if (insetsSourceControl.getLeash() == insetsSourceControl2.getLeash()) {
                return true;
            }
            if (!(insetsSourceControl.getLeash() == null || insetsSourceControl2.getLeash() == null)) {
                return insetsSourceControl.getLeash().isSameSurface(insetsSourceControl2.getLeash());
            }
        }
        return false;
    }
}
