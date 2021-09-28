package com.android.systemui.shared.system;

import android.annotation.NonNull;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.PictureInPictureSurfaceTransaction;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
import com.android.systemui.shared.recents.model.ThumbnailData;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class RemoteTransitionCompat implements Parcelable {
    public static final Parcelable.Creator<RemoteTransitionCompat> CREATOR = new Parcelable.Creator<RemoteTransitionCompat>() { // from class: com.android.systemui.shared.system.RemoteTransitionCompat.3
        @Override // android.os.Parcelable.Creator
        public RemoteTransitionCompat createFromParcel(Parcel parcel) {
            return new RemoteTransitionCompat(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public RemoteTransitionCompat[] newArray(int i) {
            return new RemoteTransitionCompat[i];
        }
    };
    private static final String TAG;
    public TransitionFilter mFilter;
    public final IRemoteTransition mTransition;

    /* loaded from: classes.dex */
    public static class Builder {
        private long mBuilderFieldsSet = 0;
        private TransitionFilter mFilter;
        private IRemoteTransition mTransition;

        public Builder(IRemoteTransition iRemoteTransition) {
            this.mTransition = iRemoteTransition;
            AnnotationValidations.validate(NonNull.class, (NonNull) null, iRemoteTransition);
        }

        private void checkNotUsed() {
            if ((this.mBuilderFieldsSet & 4) != 0) {
                throw new IllegalStateException("This Builder should not be reused. Use a new Builder instance instead");
            }
        }

        public RemoteTransitionCompat build() {
            checkNotUsed();
            long j = this.mBuilderFieldsSet | 4;
            this.mBuilderFieldsSet = j;
            if ((j & 2) == 0) {
                this.mFilter = null;
            }
            RemoteTransitionCompat remoteTransitionCompat = new RemoteTransitionCompat(this.mTransition);
            remoteTransitionCompat.mFilter = this.mFilter;
            return remoteTransitionCompat;
        }

        public Builder setFilter(TransitionFilter transitionFilter) {
            checkNotUsed();
            this.mBuilderFieldsSet |= 2;
            this.mFilter = transitionFilter;
            return this;
        }

        public Builder setTransition(IRemoteTransition iRemoteTransition) {
            checkNotUsed();
            this.mBuilderFieldsSet |= 1;
            this.mTransition = iRemoteTransition;
            return this;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class RecentsControllerWrap extends RecentsAnimationControllerCompat {
        private RecentsAnimationControllerCompat mWrapped = null;
        private IRemoteTransitionFinishedCallback mFinishCB = null;
        private WindowContainerToken mPausingTask = null;
        private TransitionInfo mInfo = null;
        private SurfaceControl mOpeningLeash = null;
        private ArrayMap<SurfaceControl, SurfaceControl> mLeashMap = null;

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public void cleanupScreenshot() {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                recentsAnimationControllerCompat.cleanupScreenshot();
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:25:0x0067  */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0093 A[LOOP:1: B:30:0x0087->B:32:0x0093, LOOP_END] */
        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        @android.annotation.SuppressLint({"NewApi"})
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void finish(boolean r5, boolean r6) {
            /*
                r4 = this;
                android.window.IRemoteTransitionFinishedCallback r0 = r4.mFinishCB
                java.lang.String r1 = "RemoteTransitionCompat"
                if (r0 != 0) goto L_0x0011
                java.lang.RuntimeException r4 = new java.lang.RuntimeException
                r4.<init>()
                java.lang.String r5 = "Duplicate call to finish"
                android.util.Log.e(r1, r5, r4)
                return
            L_0x0011:
                com.android.systemui.shared.system.RecentsAnimationControllerCompat r0 = r4.mWrapped
                if (r0 == 0) goto L_0x0018
                r0.finish(r5, r6)
            L_0x0018:
                r6 = 0
                if (r5 != 0) goto L_0x0034
                android.window.WindowContainerToken r5 = r4.mPausingTask     // Catch: RemoteException -> 0x0052
                if (r5 == 0) goto L_0x0034
                android.view.SurfaceControl r5 = r4.mOpeningLeash     // Catch: RemoteException -> 0x0052
                if (r5 != 0) goto L_0x0034
                android.window.WindowContainerTransaction r5 = new android.window.WindowContainerTransaction     // Catch: RemoteException -> 0x0052
                r5.<init>()     // Catch: RemoteException -> 0x0052
                android.window.WindowContainerToken r0 = r4.mPausingTask     // Catch: RemoteException -> 0x0052
                r2 = 1
                r5.reorder(r0, r2)     // Catch: RemoteException -> 0x0052
                android.window.IRemoteTransitionFinishedCallback r0 = r4.mFinishCB     // Catch: RemoteException -> 0x0052
                r0.onTransitionFinished(r5)     // Catch: RemoteException -> 0x0052
                goto L_0x0058
            L_0x0034:
                android.view.SurfaceControl r5 = r4.mOpeningLeash     // Catch: RemoteException -> 0x0052
                if (r5 == 0) goto L_0x004c
                android.view.SurfaceControl$Transaction r5 = new android.view.SurfaceControl$Transaction     // Catch: RemoteException -> 0x0052
                r5.<init>()     // Catch: RemoteException -> 0x0052
                android.view.SurfaceControl r0 = r4.mOpeningLeash     // Catch: RemoteException -> 0x0052
                r5.show(r0)     // Catch: RemoteException -> 0x0052
                android.view.SurfaceControl r0 = r4.mOpeningLeash     // Catch: RemoteException -> 0x0052
                r2 = 1065353216(0x3f800000, float:1.0)
                r5.setAlpha(r0, r2)     // Catch: RemoteException -> 0x0052
                r5.apply()     // Catch: RemoteException -> 0x0052
            L_0x004c:
                android.window.IRemoteTransitionFinishedCallback r5 = r4.mFinishCB     // Catch: RemoteException -> 0x0052
                r5.onTransitionFinished(r6)     // Catch: RemoteException -> 0x0052
                goto L_0x0058
            L_0x0052:
                r5 = move-exception
                java.lang.String r0 = "Failed to call animation finish callback"
                android.util.Log.e(r1, r0, r5)
            L_0x0058:
                android.view.SurfaceControl$Transaction r5 = new android.view.SurfaceControl$Transaction
                r5.<init>()
                r0 = 0
                r1 = r0
            L_0x005f:
                android.util.ArrayMap<android.view.SurfaceControl, android.view.SurfaceControl> r2 = r4.mLeashMap
                int r2 = r2.size()
                if (r1 >= r2) goto L_0x0084
                android.util.ArrayMap<android.view.SurfaceControl, android.view.SurfaceControl> r2 = r4.mLeashMap
                java.lang.Object r2 = r2.keyAt(r1)
                android.util.ArrayMap<android.view.SurfaceControl, android.view.SurfaceControl> r3 = r4.mLeashMap
                java.lang.Object r3 = r3.valueAt(r1)
                if (r2 != r3) goto L_0x0076
                goto L_0x0081
            L_0x0076:
                android.util.ArrayMap<android.view.SurfaceControl, android.view.SurfaceControl> r2 = r4.mLeashMap
                java.lang.Object r2 = r2.valueAt(r1)
                android.view.SurfaceControl r2 = (android.view.SurfaceControl) r2
                r5.remove(r2)
            L_0x0081:
                int r1 = r1 + 1
                goto L_0x005f
            L_0x0084:
                r5.apply()
            L_0x0087:
                android.window.TransitionInfo r5 = r4.mInfo
                java.util.List r5 = r5.getChanges()
                int r5 = r5.size()
                if (r0 >= r5) goto L_0x00a9
                android.window.TransitionInfo r5 = r4.mInfo
                java.util.List r5 = r5.getChanges()
                java.lang.Object r5 = r5.get(r0)
                android.window.TransitionInfo$Change r5 = (android.window.TransitionInfo.Change) r5
                android.view.SurfaceControl r5 = r5.getLeash()
                r5.release()
                int r0 = r0 + 1
                goto L_0x0087
            L_0x00a9:
                r4.mWrapped = r6
                r4.mFinishCB = r6
                r4.mPausingTask = r6
                r4.mInfo = r6
                r4.mOpeningLeash = r6
                r4.mLeashMap = r6
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.shared.system.RemoteTransitionCompat.RecentsControllerWrap.finish(boolean, boolean):void");
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public void hideCurrentInputMethod() {
            this.mWrapped.hideCurrentInputMethod();
        }

        @SuppressLint({"NewApi"})
        public boolean merge(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, RecentsAnimationListener recentsAnimationListener) {
            TransitionInfo.Change change = null;
            for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
                TransitionInfo.Change change2 = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
                if ((change2.getMode() == 1 || change2.getMode() == 3) && change2.getTaskInfo() != null) {
                    if (change != null) {
                        Log.w(RemoteTransitionCompat.TAG, " Expecting to merge a task-open, but got >1 opening tasks");
                    }
                    change = change2;
                }
            }
            if (change == null) {
                return false;
            }
            this.mOpeningLeash = change.getLeash();
            if (change.getContainer().equals(this.mPausingTask)) {
                return true;
            }
            int size2 = this.mInfo.getChanges().size() * 3;
            RemoteAnimationTargetCompat remoteAnimationTargetCompat = new RemoteAnimationTargetCompat(change, size2, this.mInfo, transaction);
            this.mLeashMap.put(this.mOpeningLeash, remoteAnimationTargetCompat.leash.mSurfaceControl);
            transaction.reparent(remoteAnimationTargetCompat.leash.mSurfaceControl, this.mInfo.getRootLeash());
            transaction.setLayer(remoteAnimationTargetCompat.leash.mSurfaceControl, size2);
            transaction.hide(remoteAnimationTargetCompat.leash.mSurfaceControl);
            transaction.apply();
            recentsAnimationListener.onTaskAppeared(remoteAnimationTargetCompat);
            return true;
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public boolean removeTask(int i) {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                return recentsAnimationControllerCompat.removeTask(i);
            }
            return false;
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public ThumbnailData screenshotTask(int i) {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                return recentsAnimationControllerCompat.screenshotTask(i);
            }
            return null;
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public void setAnimationTargetsBehindSystemBars(boolean z) {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                recentsAnimationControllerCompat.setAnimationTargetsBehindSystemBars(z);
            }
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public void setDeferCancelUntilNextTransition(boolean z, boolean z2) {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                recentsAnimationControllerCompat.setDeferCancelUntilNextTransition(z, z2);
            }
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public void setFinishTaskTransaction(int i, PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction, SurfaceControl surfaceControl) {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                recentsAnimationControllerCompat.setFinishTaskTransaction(i, pictureInPictureSurfaceTransaction, surfaceControl);
            }
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public void setInputConsumerEnabled(boolean z) {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                recentsAnimationControllerCompat.setInputConsumerEnabled(z);
            }
        }

        @Override // com.android.systemui.shared.system.RecentsAnimationControllerCompat
        public void setWillFinishToHome(boolean z) {
            RecentsAnimationControllerCompat recentsAnimationControllerCompat = this.mWrapped;
            if (recentsAnimationControllerCompat != null) {
                recentsAnimationControllerCompat.setWillFinishToHome(z);
            }
        }

        public void setup(RecentsAnimationControllerCompat recentsAnimationControllerCompat, TransitionInfo transitionInfo, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback, WindowContainerToken windowContainerToken, ArrayMap<SurfaceControl, SurfaceControl> arrayMap) {
            if (this.mInfo == null) {
                this.mWrapped = recentsAnimationControllerCompat;
                this.mInfo = transitionInfo;
                this.mFinishCB = iRemoteTransitionFinishedCallback;
                this.mPausingTask = windowContainerToken;
                this.mLeashMap = arrayMap;
                return;
            }
            throw new IllegalStateException("Trying to run a new recents animation while recents is already active.");
        }
    }

    public RemoteTransitionCompat(IRemoteTransition iRemoteTransition) {
        this.mFilter = null;
        this.mTransition = iRemoteTransition;
    }

    @Deprecated
    private void __metadata() {
    }

    public void addHomeOpenCheck() {
        if (this.mFilter == null) {
            this.mFilter = new TransitionFilter();
        }
        this.mFilter.mRequirements = new TransitionFilter.Requirement[]{new TransitionFilter.Requirement()};
        TransitionFilter.Requirement[] requirementArr = this.mFilter.mRequirements;
        requirementArr[0].mActivityType = 2;
        requirementArr[0].mModes = new int[]{1, 3};
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public TransitionFilter getFilter() {
        return this.mFilter;
    }

    public IRemoteTransition getTransition() {
        return this.mTransition;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mFilter != null ? (byte) 2 : 0);
        parcel.writeStrongInterface(this.mTransition);
        TransitionFilter transitionFilter = this.mFilter;
        if (transitionFilter != null) {
            parcel.writeTypedObject(transitionFilter, i);
        }
    }

    public RemoteTransitionCompat(final RemoteTransitionRunner remoteTransitionRunner, final Executor executor) {
        this.mFilter = null;
        this.mTransition = new IRemoteTransition.Stub() { // from class: com.android.systemui.shared.system.RemoteTransitionCompat.1
            public static /* synthetic */ void lambda$mergeAnimation$2(IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
                try {
                    iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null);
                } catch (RemoteException e) {
                    Log.e(RemoteTransitionCompat.TAG, "Failed to call transition finished callback", e);
                }
            }

            public static /* synthetic */ void lambda$startAnimation$0(IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
                try {
                    iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null);
                } catch (RemoteException e) {
                    Log.e(RemoteTransitionCompat.TAG, "Failed to call transition finished callback", e);
                }
            }

            public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
                executor.execute(new RemoteTransitionCompat$1$$ExternalSyntheticLambda1(remoteTransitionRunner, iBinder, transitionInfo, transaction, iBinder2, new RemoteTransitionCompat$1$$ExternalSyntheticLambda0(iRemoteTransitionFinishedCallback, 0)));
            }

            public void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
                executor.execute(new RemoteTransitionCompat$1$$ExternalSyntheticLambda2(remoteTransitionRunner, iBinder, transitionInfo, transaction, new RemoteTransitionCompat$1$$ExternalSyntheticLambda0(iRemoteTransitionFinishedCallback, 1)));
            }
        };
    }

    public RemoteTransitionCompat(final RecentsAnimationListener recentsAnimationListener, final RecentsAnimationControllerCompat recentsAnimationControllerCompat) {
        this.mFilter = null;
        this.mTransition = new IRemoteTransition.Stub() { // from class: com.android.systemui.shared.system.RemoteTransitionCompat.2
            public final RecentsControllerWrap mRecentsSession = new RecentsControllerWrap();
            public IBinder mToken = null;

            public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
                if (iBinder2.equals(this.mToken) && this.mRecentsSession.merge(transitionInfo, transaction, recentsAnimationListener)) {
                    try {
                        iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null);
                    } catch (RemoteException e) {
                        Log.e(RemoteTransitionCompat.TAG, "Error merging transition.", e);
                    }
                }
            }

            public void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
                ArrayMap<SurfaceControl, SurfaceControl> arrayMap = new ArrayMap<>();
                RemoteAnimationTargetCompat[] wrap = RemoteAnimationTargetCompat.wrap(transitionInfo, false, transaction, arrayMap);
                RemoteAnimationTargetCompat[] wrap2 = RemoteAnimationTargetCompat.wrap(transitionInfo, true, transaction, arrayMap);
                this.mToken = iBinder;
                WindowContainerToken windowContainerToken = null;
                for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
                    TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
                    if (change.getMode() == 2 || change.getMode() == 4) {
                        transaction.setLayer(arrayMap.get(change.getLeash()), (transitionInfo.getChanges().size() * 3) - size);
                        if (change.getTaskInfo() != null) {
                            windowContainerToken = change.getTaskInfo().token;
                        }
                    }
                }
                for (int length = wrap2.length - 1; length >= 0; length--) {
                    transaction.setAlpha(wrap2[length].leash.mSurfaceControl, 1.0f);
                }
                transaction.apply();
                this.mRecentsSession.setup(recentsAnimationControllerCompat, transitionInfo, iRemoteTransitionFinishedCallback, windowContainerToken, arrayMap);
                recentsAnimationListener.onAnimationStart(this.mRecentsSession, wrap, wrap2, new Rect(0, 0, 0, 0), new Rect());
            }
        };
    }

    public RemoteTransitionCompat(Parcel parcel) {
        TransitionFilter transitionFilter;
        this.mFilter = null;
        byte readByte = parcel.readByte();
        IRemoteTransition asInterface = IRemoteTransition.Stub.asInterface(parcel.readStrongBinder());
        if ((readByte & 2) == 0) {
            transitionFilter = null;
        } else {
            transitionFilter = (TransitionFilter) parcel.readTypedObject(TransitionFilter.CREATOR);
        }
        this.mTransition = asInterface;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, asInterface);
        this.mFilter = transitionFilter;
    }
}
