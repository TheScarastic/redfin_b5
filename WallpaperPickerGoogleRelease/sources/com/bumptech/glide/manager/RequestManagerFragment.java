package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1;
import com.bumptech.glide.RequestManager;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Deprecated
/* loaded from: classes.dex */
public class RequestManagerFragment extends Fragment {
    public final Set<RequestManagerFragment> childRequestManagerFragments;
    public final ActivityFragmentLifecycle lifecycle;
    public Fragment parentFragmentHint;
    public RequestManager requestManager;
    public final RequestManagerTreeNode requestManagerTreeNode;
    public RequestManagerFragment rootRequestManagerFragment;

    /* loaded from: classes.dex */
    public class FragmentRequestManagerTreeNode implements RequestManagerTreeNode {
        public FragmentRequestManagerTreeNode() {
        }

        public String toString() {
            String obj = super.toString();
            String valueOf = String.valueOf(RequestManagerFragment.this);
            return Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(valueOf.length() + XMPPathFactory$$ExternalSyntheticOutline0.m(obj, 11), obj, "{fragment=", valueOf, "}");
        }
    }

    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            registerFragmentWithRoot(activity);
        } catch (IllegalStateException e) {
            if (Log.isLoggable("RMFragment", 5)) {
                Log.w("RMFragment", "Unable to register fragment with root", e);
            }
        }
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.lifecycle.onDestroy();
        unregisterFragmentWithRoot();
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        unregisterFragmentWithRoot();
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        this.lifecycle.onStart();
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        this.lifecycle.onStop();
    }

    public final void registerFragmentWithRoot(Activity activity) {
        unregisterFragmentWithRoot();
        RequestManagerRetriever requestManagerRetriever = Glide.get(activity).requestManagerRetriever;
        Objects.requireNonNull(requestManagerRetriever);
        RequestManagerFragment requestManagerFragment = requestManagerRetriever.getRequestManagerFragment(activity.getFragmentManager(), null, !activity.isFinishing());
        this.rootRequestManagerFragment = requestManagerFragment;
        if (!equals(requestManagerFragment)) {
            this.rootRequestManagerFragment.childRequestManagerFragments.add(this);
        }
    }

    @Override // android.app.Fragment, java.lang.Object
    public String toString() {
        String fragment = super.toString();
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            parentFragment = this.parentFragmentHint;
        }
        String valueOf = String.valueOf(parentFragment);
        return Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(valueOf.length() + XMPPathFactory$$ExternalSyntheticOutline0.m(fragment, 9), fragment, "{parent=", valueOf, "}");
    }

    public final void unregisterFragmentWithRoot() {
        RequestManagerFragment requestManagerFragment = this.rootRequestManagerFragment;
        if (requestManagerFragment != null) {
            requestManagerFragment.childRequestManagerFragments.remove(this);
            this.rootRequestManagerFragment = null;
        }
    }

    @SuppressLint({"ValidFragment"})
    public RequestManagerFragment(ActivityFragmentLifecycle activityFragmentLifecycle) {
        this.requestManagerTreeNode = new FragmentRequestManagerTreeNode();
        this.childRequestManagerFragments = new HashSet();
        this.lifecycle = activityFragmentLifecycle;
    }
}
