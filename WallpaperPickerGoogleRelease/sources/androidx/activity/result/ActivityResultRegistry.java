package androidx.activity.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/* loaded from: classes.dex */
public abstract class ActivityResultRegistry {
    public Random mRandom = new Random();
    public final Map<Integer, String> mRcToKey = new HashMap();
    public final Map<String, Integer> mKeyToRc = new HashMap();
    public final Map<String, LifecycleContainer> mKeyToLifecycleContainers = new HashMap();
    public final transient Map<String, CallbackAndContract<?>> mKeyToCallback = new HashMap();
    public final Map<String, Object> mParsedPendingResults = new HashMap();
    public final Bundle mPendingResults = new Bundle();

    /* renamed from: androidx.activity.result.ActivityResultRegistry$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements LifecycleEventObserver {
        @Override // androidx.lifecycle.LifecycleEventObserver
        public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (Lifecycle.Event.ON_START.equals(event)) {
                throw null;
            } else if (Lifecycle.Event.ON_STOP.equals(event)) {
                throw null;
            } else if (Lifecycle.Event.ON_DESTROY.equals(event)) {
                throw null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class CallbackAndContract<O> {
        public final ActivityResultCallback<O> mCallback;
        public final ActivityResultContract<?, O> mContract;

        public CallbackAndContract(ActivityResultCallback<O> activityResultCallback, ActivityResultContract<?, O> activityResultContract) {
            this.mCallback = activityResultCallback;
            this.mContract = activityResultContract;
        }
    }

    /* loaded from: classes.dex */
    public static class LifecycleContainer {
    }

    public final boolean dispatchResult(int i, int i2, Intent intent) {
        ActivityResultCallback<?> activityResultCallback;
        String str = this.mRcToKey.get(Integer.valueOf(i));
        if (str == null) {
            return false;
        }
        CallbackAndContract<?> callbackAndContract = this.mKeyToCallback.get(str);
        if (callbackAndContract == null || (activityResultCallback = callbackAndContract.mCallback) == null) {
            this.mParsedPendingResults.remove(str);
            this.mPendingResults.putParcelable(str, new ActivityResult(i2, intent));
            return true;
        }
        activityResultCallback.onActivityResult(callbackAndContract.mContract.parseResult(i2, intent));
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r8v0, resolved type: androidx.activity.result.ActivityResultCallback<O> */
    /* JADX WARN: Multi-variable type inference failed */
    public final <I, O> ActivityResultLauncher<I> register(final String str, final ActivityResultContract<I, O> activityResultContract, ActivityResultCallback<O> activityResultCallback) {
        final int i;
        Integer num = this.mKeyToRc.get(str);
        if (num != null) {
            i = num.intValue();
        } else {
            int nextInt = this.mRandom.nextInt(2147418112);
            while (true) {
                i = nextInt + QuickStepContract.SYSUI_STATE_ONE_HANDED_ACTIVE;
                if (!this.mRcToKey.containsKey(Integer.valueOf(i))) {
                    break;
                }
                nextInt = this.mRandom.nextInt(2147418112);
            }
            this.mRcToKey.put(Integer.valueOf(i), str);
            this.mKeyToRc.put(str, Integer.valueOf(i));
        }
        this.mKeyToCallback.put(str, new CallbackAndContract<>(activityResultCallback, activityResultContract));
        if (this.mParsedPendingResults.containsKey(str)) {
            Object obj = this.mParsedPendingResults.get(str);
            this.mParsedPendingResults.remove(str);
            activityResultCallback.onActivityResult(obj);
        }
        ActivityResult activityResult = (ActivityResult) this.mPendingResults.getParcelable(str);
        if (activityResult != null) {
            this.mPendingResults.remove(str);
            activityResultCallback.onActivityResult(activityResultContract.parseResult(activityResult.mResultCode, activityResult.mData));
        }
        return new ActivityResultLauncher<I>() { // from class: androidx.activity.result.ActivityResultRegistry.3
            @Override // androidx.activity.result.ActivityResultLauncher
            public void unregister() {
                ActivityResultRegistry.this.unregister(str);
            }
        };
    }

    public final void unregister(String str) {
        Integer remove = this.mKeyToRc.remove(str);
        if (remove != null) {
            this.mRcToKey.remove(remove);
        }
        this.mKeyToCallback.remove(str);
        if (this.mParsedPendingResults.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.mParsedPendingResults.get(str));
            this.mParsedPendingResults.remove(str);
        }
        if (this.mPendingResults.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.mPendingResults.getParcelable(str));
            this.mPendingResults.remove(str);
        }
        if (this.mKeyToLifecycleContainers.get(str) != null) {
            throw null;
        }
    }
}
