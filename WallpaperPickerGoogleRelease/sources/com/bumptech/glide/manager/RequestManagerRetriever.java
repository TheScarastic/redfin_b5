package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class RequestManagerRetriever implements Handler.Callback {
    public static final RequestManagerFactory DEFAULT_FACTORY = new RequestManagerFactory() { // from class: com.bumptech.glide.manager.RequestManagerRetriever.1
    };
    public static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
    public volatile RequestManager applicationManager;
    public final RequestManagerFactory factory;
    public final Handler handler;
    public final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap();
    public final Map<androidx.fragment.app.FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments = new HashMap();

    /* loaded from: classes.dex */
    public interface RequestManagerFactory {
    }

    public RequestManagerRetriever(RequestManagerFactory requestManagerFactory) {
        new Bundle();
        this.factory = requestManagerFactory == null ? DEFAULT_FACTORY : requestManagerFactory;
        this.handler = new Handler(Looper.getMainLooper(), this);
    }

    public RequestManager get(Context context) {
        if (context != null) {
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity) {
                    return get((FragmentActivity) context);
                }
                if (context instanceof Activity) {
                    return get((Activity) context);
                }
                if (context instanceof ContextWrapper) {
                    return get(((ContextWrapper) context).getBaseContext());
                }
            }
            if (this.applicationManager == null) {
                synchronized (this) {
                    if (this.applicationManager == null) {
                        Glide glide = Glide.get(context.getApplicationContext());
                        RequestManagerFactory requestManagerFactory = this.factory;
                        ApplicationLifecycle applicationLifecycle = new ApplicationLifecycle();
                        EmptyRequestManagerTreeNode emptyRequestManagerTreeNode = new EmptyRequestManagerTreeNode();
                        Context applicationContext = context.getApplicationContext();
                        Objects.requireNonNull((AnonymousClass1) requestManagerFactory);
                        this.applicationManager = new RequestManager(glide, applicationLifecycle, emptyRequestManagerTreeNode, applicationContext);
                    }
                }
            }
            return this.applicationManager;
        }
        throw new IllegalArgumentException("You cannot start a load on a null Context");
    }

    public final RequestManagerFragment getRequestManagerFragment(FragmentManager fragmentManager, Fragment fragment, boolean z) {
        RequestManagerFragment requestManagerFragment = (RequestManagerFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (requestManagerFragment == null && (requestManagerFragment = this.pendingRequestManagerFragments.get(fragmentManager)) == null) {
            requestManagerFragment = new RequestManagerFragment(new ActivityFragmentLifecycle());
            requestManagerFragment.parentFragmentHint = fragment;
            if (!(fragment == null || fragment.getActivity() == null)) {
                requestManagerFragment.registerFragmentWithRoot(fragment.getActivity());
            }
            if (z) {
                requestManagerFragment.lifecycle.onStart();
            }
            this.pendingRequestManagerFragments.put(fragmentManager, requestManagerFragment);
            fragmentManager.beginTransaction().add(requestManagerFragment, FRAGMENT_TAG).commitAllowingStateLoss();
            this.handler.obtainMessage(1, fragmentManager).sendToTarget();
        }
        return requestManagerFragment;
    }

    public final SupportRequestManagerFragment getSupportRequestManagerFragment(androidx.fragment.app.FragmentManager fragmentManager, androidx.fragment.app.Fragment fragment, boolean z) {
        SupportRequestManagerFragment supportRequestManagerFragment = (SupportRequestManagerFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (supportRequestManagerFragment == null && (supportRequestManagerFragment = this.pendingSupportRequestManagerFragments.get(fragmentManager)) == null) {
            supportRequestManagerFragment = new SupportRequestManagerFragment();
            supportRequestManagerFragment.parentFragmentHint = fragment;
            if (!(fragment == null || fragment.getActivity() == null)) {
                supportRequestManagerFragment.registerFragmentWithRoot(fragment.getActivity());
            }
            if (z) {
                supportRequestManagerFragment.lifecycle.onStart();
            }
            this.pendingSupportRequestManagerFragments.put(fragmentManager, supportRequestManagerFragment);
            BackStackRecord backStackRecord = new BackStackRecord(fragmentManager);
            backStackRecord.doAddOp(0, supportRequestManagerFragment, FRAGMENT_TAG, 1);
            backStackRecord.commitAllowingStateLoss();
            this.handler.obtainMessage(2, fragmentManager).sendToTarget();
        }
        return supportRequestManagerFragment;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        Object obj;
        Object obj2;
        Object obj3;
        int i = message.what;
        Object obj4 = null;
        boolean z = true;
        if (i == 1) {
            obj3 = (FragmentManager) message.obj;
            obj2 = this.pendingRequestManagerFragments.remove(obj3);
        } else if (i != 2) {
            z = false;
            obj = null;
            if (z && obj4 == null && Log.isLoggable("RMRetriever", 5)) {
                String valueOf = String.valueOf(obj);
                StringBuilder sb = new StringBuilder(valueOf.length() + 61);
                sb.append("Failed to remove expected request manager fragment, manager: ");
                sb.append(valueOf);
                Log.w("RMRetriever", sb.toString());
            }
            return z;
        } else {
            obj3 = (androidx.fragment.app.FragmentManager) message.obj;
            obj2 = this.pendingSupportRequestManagerFragments.remove(obj3);
        }
        obj4 = obj2;
        obj = obj3;
        if (z) {
            String valueOf = String.valueOf(obj);
            StringBuilder sb = new StringBuilder(valueOf.length() + 61);
            sb.append("Failed to remove expected request manager fragment, manager: ");
            sb.append(valueOf);
            Log.w("RMRetriever", sb.toString());
        }
        return z;
    }

    public RequestManager get(FragmentActivity fragmentActivity) {
        if (Util.isOnBackgroundThread()) {
            return get(fragmentActivity.getApplicationContext());
        }
        if (!fragmentActivity.isDestroyed()) {
            SupportRequestManagerFragment supportRequestManagerFragment = getSupportRequestManagerFragment(fragmentActivity.getSupportFragmentManager(), null, !fragmentActivity.isFinishing());
            RequestManager requestManager = supportRequestManagerFragment.requestManager;
            if (requestManager != null) {
                return requestManager;
            }
            Glide glide = Glide.get(fragmentActivity);
            RequestManagerFactory requestManagerFactory = this.factory;
            ActivityFragmentLifecycle activityFragmentLifecycle = supportRequestManagerFragment.lifecycle;
            RequestManagerTreeNode requestManagerTreeNode = supportRequestManagerFragment.requestManagerTreeNode;
            Objects.requireNonNull((AnonymousClass1) requestManagerFactory);
            RequestManager requestManager2 = new RequestManager(glide, activityFragmentLifecycle, requestManagerTreeNode, fragmentActivity);
            supportRequestManagerFragment.requestManager = requestManager2;
            return requestManager2;
        }
        throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
    }

    public RequestManager get(Activity activity) {
        if (Util.isOnBackgroundThread()) {
            return get(activity.getApplicationContext());
        }
        if (!activity.isDestroyed()) {
            RequestManagerFragment requestManagerFragment = getRequestManagerFragment(activity.getFragmentManager(), null, !activity.isFinishing());
            RequestManager requestManager = requestManagerFragment.requestManager;
            if (requestManager != null) {
                return requestManager;
            }
            Glide glide = Glide.get(activity);
            RequestManagerFactory requestManagerFactory = this.factory;
            ActivityFragmentLifecycle activityFragmentLifecycle = requestManagerFragment.lifecycle;
            RequestManagerTreeNode requestManagerTreeNode = requestManagerFragment.requestManagerTreeNode;
            Objects.requireNonNull((AnonymousClass1) requestManagerFactory);
            RequestManager requestManager2 = new RequestManager(glide, activityFragmentLifecycle, requestManagerTreeNode, activity);
            requestManagerFragment.requestManager = requestManager2;
            return requestManager2;
        }
        throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
    }
}
