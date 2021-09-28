package com.android.systemui.shared.system;

import android.graphics.Rect;
import android.graphics.Region;
import android.view.ViewTreeObserver;
import com.android.systemui.shared.system.ViewTreeObserverWrapper;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ViewTreeObserverWrapper {
    private static final HashMap<OnComputeInsetsListener, ViewTreeObserver> sListenerObserverMap = new HashMap<>();
    private static final HashMap<OnComputeInsetsListener, ViewTreeObserver.OnComputeInternalInsetsListener> sListenerInternalListenerMap = new HashMap<>();

    /* loaded from: classes.dex */
    public static final class InsetsInfo {
        public static final int TOUCHABLE_INSETS_CONTENT = 1;
        public static final int TOUCHABLE_INSETS_FRAME = 0;
        public static final int TOUCHABLE_INSETS_REGION = 3;
        public static final int TOUCHABLE_INSETS_VISIBLE = 2;
        public int mTouchableInsets;
        public final Rect contentInsets = new Rect();
        public final Rect visibleInsets = new Rect();
        public final Region touchableRegion = new Region();

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || InsetsInfo.class != obj.getClass()) {
                return false;
            }
            InsetsInfo insetsInfo = (InsetsInfo) obj;
            return this.mTouchableInsets == insetsInfo.mTouchableInsets && this.contentInsets.equals(insetsInfo.contentInsets) && this.visibleInsets.equals(insetsInfo.visibleInsets) && this.touchableRegion.equals(insetsInfo.touchableRegion);
        }

        public int hashCode() {
            int hashCode = this.visibleInsets.hashCode();
            return ((this.touchableRegion.hashCode() + ((hashCode + (this.contentInsets.hashCode() * 31)) * 31)) * 31) + this.mTouchableInsets;
        }

        public void setTouchableInsets(int i) {
            this.mTouchableInsets = i;
        }
    }

    /* loaded from: classes.dex */
    public interface OnComputeInsetsListener {
        void onComputeInsets(InsetsInfo insetsInfo);
    }

    public static void addOnComputeInsetsListener(ViewTreeObserver viewTreeObserver, OnComputeInsetsListener onComputeInsetsListener) {
        ViewTreeObserver.OnComputeInternalInsetsListener viewTreeObserverWrapper$$ExternalSyntheticLambda0 = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.shared.system.ViewTreeObserverWrapper$$ExternalSyntheticLambda0
            public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
                ViewTreeObserverWrapper.$r8$lambda$Fc87AITf2d5DHsK1sT4dqB8PMy4(ViewTreeObserverWrapper.OnComputeInsetsListener.this, internalInsetsInfo);
            }
        };
        sListenerObserverMap.put(onComputeInsetsListener, viewTreeObserver);
        sListenerInternalListenerMap.put(onComputeInsetsListener, viewTreeObserverWrapper$$ExternalSyntheticLambda0);
        viewTreeObserver.addOnComputeInternalInsetsListener(viewTreeObserverWrapper$$ExternalSyntheticLambda0);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$addOnComputeInsetsListener$0(OnComputeInsetsListener onComputeInsetsListener, ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        InsetsInfo insetsInfo = new InsetsInfo();
        insetsInfo.contentInsets.set(internalInsetsInfo.contentInsets);
        insetsInfo.visibleInsets.set(internalInsetsInfo.visibleInsets);
        insetsInfo.touchableRegion.set(internalInsetsInfo.touchableRegion);
        onComputeInsetsListener.onComputeInsets(insetsInfo);
        internalInsetsInfo.contentInsets.set(insetsInfo.contentInsets);
        internalInsetsInfo.visibleInsets.set(insetsInfo.visibleInsets);
        internalInsetsInfo.touchableRegion.set(insetsInfo.touchableRegion);
        internalInsetsInfo.setTouchableInsets(insetsInfo.mTouchableInsets);
    }

    public static void removeOnComputeInsetsListener(OnComputeInsetsListener onComputeInsetsListener) {
        HashMap<OnComputeInsetsListener, ViewTreeObserver> hashMap = sListenerObserverMap;
        ViewTreeObserver viewTreeObserver = hashMap.get(onComputeInsetsListener);
        HashMap<OnComputeInsetsListener, ViewTreeObserver.OnComputeInternalInsetsListener> hashMap2 = sListenerInternalListenerMap;
        ViewTreeObserver.OnComputeInternalInsetsListener onComputeInternalInsetsListener = hashMap2.get(onComputeInsetsListener);
        if (!(viewTreeObserver == null || onComputeInternalInsetsListener == null)) {
            viewTreeObserver.removeOnComputeInternalInsetsListener(onComputeInternalInsetsListener);
        }
        hashMap.remove(onComputeInsetsListener);
        hashMap2.remove(onComputeInsetsListener);
    }
}
