package com.android.systemui.classifier;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.policy.BatteryController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class FalsingDataProvider {
    private BatteryController mBatteryController;
    private final DockManager mDockManager;
    private MotionEvent mFirstRecentMotionEvent;
    private final int mHeightPixels;
    private boolean mJustUnlockedWithFace;
    private MotionEvent mLastMotionEvent;
    private final int mWidthPixels;
    private final float mXdpi;
    private final float mYdpi;
    private final List<SessionListener> mSessionListeners = new ArrayList();
    private final List<MotionEventListener> mMotionEventListeners = new ArrayList();
    private final List<GestureFinalizedListener> mGestureFinalizedListeners = new ArrayList();
    private TimeLimitedMotionEventBuffer mRecentMotionEvents = new TimeLimitedMotionEventBuffer(1000);
    private List<MotionEvent> mPriorMotionEvents = new ArrayList();
    private boolean mDirty = true;
    private float mAngle = 0.0f;

    /* loaded from: classes.dex */
    public interface GestureFinalizedListener {
        void onGestureFinalized(long j);
    }

    /* loaded from: classes.dex */
    public interface MotionEventListener {
        void onMotionEvent(MotionEvent motionEvent);
    }

    /* loaded from: classes.dex */
    public interface SessionListener {
        void onSessionEnded();

        void onSessionStarted();
    }

    public FalsingDataProvider(DisplayMetrics displayMetrics, BatteryController batteryController, DockManager dockManager) {
        this.mXdpi = displayMetrics.xdpi;
        this.mYdpi = displayMetrics.ydpi;
        this.mWidthPixels = displayMetrics.widthPixels;
        this.mHeightPixels = displayMetrics.heightPixels;
        this.mBatteryController = batteryController;
        this.mDockManager = dockManager;
        FalsingClassifier.logInfo("xdpi, ydpi: " + getXdpi() + ", " + getYdpi());
        FalsingClassifier.logInfo("width, height: " + getWidthPixels() + ", " + getHeightPixels());
    }

    /* access modifiers changed from: package-private */
    public void onMotionEvent(MotionEvent motionEvent) {
        List<MotionEvent> unpackMotionEvent = unpackMotionEvent(motionEvent);
        FalsingClassifier.logDebug("Unpacked into: " + unpackMotionEvent.size());
        if (BrightLineFalsingManager.DEBUG) {
            for (MotionEvent motionEvent2 : unpackMotionEvent) {
                FalsingClassifier.logDebug("x,y,t: " + motionEvent2.getX() + "," + motionEvent2.getY() + "," + motionEvent2.getEventTime());
            }
        }
        if (motionEvent.getActionMasked() == 0) {
            completePriorGesture();
        }
        this.mRecentMotionEvents.addAll(unpackMotionEvent);
        FalsingClassifier.logDebug("Size: " + this.mRecentMotionEvents.size());
        this.mMotionEventListeners.forEach(new Consumer(motionEvent) { // from class: com.android.systemui.classifier.FalsingDataProvider$$ExternalSyntheticLambda0
            public final /* synthetic */ MotionEvent f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FalsingDataProvider.MotionEventListener) obj).onMotionEvent(this.f$0);
            }
        });
        this.mDirty = true;
    }

    /* access modifiers changed from: package-private */
    public void onMotionEventComplete() {
        if (!this.mRecentMotionEvents.isEmpty()) {
            TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer = this.mRecentMotionEvents;
            int actionMasked = timeLimitedMotionEventBuffer.get(timeLimitedMotionEventBuffer.size() - 1).getActionMasked();
            if (actionMasked == 1 || actionMasked == 3) {
                completePriorGesture();
            }
        }
    }

    private void completePriorGesture() {
        if (!this.mRecentMotionEvents.isEmpty()) {
            this.mGestureFinalizedListeners.forEach(new Consumer() { // from class: com.android.systemui.classifier.FalsingDataProvider$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    FalsingDataProvider.this.lambda$completePriorGesture$1((FalsingDataProvider.GestureFinalizedListener) obj);
                }
            });
            this.mPriorMotionEvents = this.mRecentMotionEvents;
            this.mRecentMotionEvents = new TimeLimitedMotionEventBuffer(1000);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$completePriorGesture$1(GestureFinalizedListener gestureFinalizedListener) {
        TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer = this.mRecentMotionEvents;
        gestureFinalizedListener.onGestureFinalized(timeLimitedMotionEventBuffer.get(timeLimitedMotionEventBuffer.size() - 1).getEventTime());
    }

    public int getWidthPixels() {
        return this.mWidthPixels;
    }

    public int getHeightPixels() {
        return this.mHeightPixels;
    }

    public float getXdpi() {
        return this.mXdpi;
    }

    public float getYdpi() {
        return this.mYdpi;
    }

    public List<MotionEvent> getRecentMotionEvents() {
        return this.mRecentMotionEvents;
    }

    public List<MotionEvent> getPriorMotionEvents() {
        return this.mPriorMotionEvents;
    }

    public MotionEvent getFirstRecentMotionEvent() {
        recalculateData();
        return this.mFirstRecentMotionEvent;
    }

    public MotionEvent getLastMotionEvent() {
        recalculateData();
        return this.mLastMotionEvent;
    }

    public float getAngle() {
        recalculateData();
        return this.mAngle;
    }

    public boolean isHorizontal() {
        recalculateData();
        if (!this.mRecentMotionEvents.isEmpty() && Math.abs(this.mFirstRecentMotionEvent.getX() - this.mLastMotionEvent.getX()) > Math.abs(this.mFirstRecentMotionEvent.getY() - this.mLastMotionEvent.getY())) {
            return true;
        }
        return false;
    }

    public boolean isRight() {
        recalculateData();
        if (!this.mRecentMotionEvents.isEmpty() && this.mLastMotionEvent.getX() > this.mFirstRecentMotionEvent.getX()) {
            return true;
        }
        return false;
    }

    public boolean isVertical() {
        return !isHorizontal();
    }

    public boolean isUp() {
        recalculateData();
        if (!this.mRecentMotionEvents.isEmpty() && this.mLastMotionEvent.getY() < this.mFirstRecentMotionEvent.getY()) {
            return true;
        }
        return false;
    }

    private void recalculateData() {
        if (this.mDirty) {
            if (this.mRecentMotionEvents.isEmpty()) {
                this.mFirstRecentMotionEvent = null;
                this.mLastMotionEvent = null;
            } else {
                this.mFirstRecentMotionEvent = this.mRecentMotionEvents.get(0);
                TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer = this.mRecentMotionEvents;
                this.mLastMotionEvent = timeLimitedMotionEventBuffer.get(timeLimitedMotionEventBuffer.size() - 1);
            }
            calculateAngleInternal();
            this.mDirty = false;
        }
    }

    private void calculateAngleInternal() {
        if (this.mRecentMotionEvents.size() < 2) {
            this.mAngle = Float.MAX_VALUE;
            return;
        }
        this.mAngle = (float) Math.atan2((double) (this.mLastMotionEvent.getY() - this.mFirstRecentMotionEvent.getY()), (double) (this.mLastMotionEvent.getX() - this.mFirstRecentMotionEvent.getX()));
        while (true) {
            float f = this.mAngle;
            if (f < 0.0f) {
                this.mAngle = f + 6.2831855f;
            }
        }
        while (true) {
            float f2 = this.mAngle;
            if (f2 > 6.2831855f) {
                this.mAngle = f2 - 6.2831855f;
            } else {
                return;
            }
        }
    }

    private List<MotionEvent> unpackMotionEvent(MotionEvent motionEvent) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int pointerCount = motionEvent.getPointerCount();
        int i = 0;
        for (int i2 = 0; i2 < pointerCount; i2++) {
            MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
            motionEvent.getPointerProperties(i2, pointerProperties);
            arrayList2.add(pointerProperties);
        }
        MotionEvent.PointerProperties[] pointerPropertiesArr = new MotionEvent.PointerProperties[arrayList2.size()];
        arrayList2.toArray(pointerPropertiesArr);
        int historySize = motionEvent.getHistorySize();
        int i3 = 0;
        while (i3 < historySize) {
            ArrayList arrayList3 = new ArrayList();
            for (int i4 = i; i4 < pointerCount; i4++) {
                MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
                motionEvent.getHistoricalPointerCoords(i4, i3, pointerCoords);
                arrayList3.add(pointerCoords);
            }
            arrayList.add(MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getHistoricalEventTime(i3), motionEvent.getAction(), pointerCount, pointerPropertiesArr, (MotionEvent.PointerCoords[]) arrayList3.toArray(new MotionEvent.PointerCoords[i]), motionEvent.getMetaState(), motionEvent.getButtonState(), motionEvent.getXPrecision(), motionEvent.getYPrecision(), motionEvent.getDeviceId(), motionEvent.getEdgeFlags(), motionEvent.getSource(), motionEvent.getFlags()));
            i3++;
            pointerPropertiesArr = pointerPropertiesArr;
            i = i;
            pointerCount = pointerCount;
        }
        arrayList.add(MotionEvent.obtainNoHistory(motionEvent));
        return arrayList;
    }

    public void addSessionListener(SessionListener sessionListener) {
        this.mSessionListeners.add(sessionListener);
    }

    public void removeSessionListener(SessionListener sessionListener) {
        this.mSessionListeners.remove(sessionListener);
    }

    public void addMotionEventListener(MotionEventListener motionEventListener) {
        this.mMotionEventListeners.add(motionEventListener);
    }

    public void removeMotionEventListener(MotionEventListener motionEventListener) {
        this.mMotionEventListeners.remove(motionEventListener);
    }

    public void addGestureCompleteListener(GestureFinalizedListener gestureFinalizedListener) {
        this.mGestureFinalizedListeners.add(gestureFinalizedListener);
    }

    public void removeGestureCompleteListener(GestureFinalizedListener gestureFinalizedListener) {
        this.mGestureFinalizedListeners.remove(gestureFinalizedListener);
    }

    /* access modifiers changed from: package-private */
    public void onSessionStarted() {
        this.mSessionListeners.forEach(FalsingDataProvider$$ExternalSyntheticLambda3.INSTANCE);
    }

    /* access modifiers changed from: package-private */
    public void onSessionEnd() {
        Iterator<MotionEvent> it = this.mRecentMotionEvents.iterator();
        while (it.hasNext()) {
            it.next().recycle();
        }
        this.mRecentMotionEvents.clear();
        this.mDirty = true;
        this.mSessionListeners.forEach(FalsingDataProvider$$ExternalSyntheticLambda2.INSTANCE);
    }

    public boolean isJustUnlockedWithFace() {
        return this.mJustUnlockedWithFace;
    }

    public void setJustUnlockedWithFace(boolean z) {
        this.mJustUnlockedWithFace = z;
    }

    public boolean isDocked() {
        return this.mBatteryController.isWirelessCharging() || this.mDockManager.isDocked();
    }
}
