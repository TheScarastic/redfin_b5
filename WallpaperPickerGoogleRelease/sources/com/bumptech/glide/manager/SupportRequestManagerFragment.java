package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1;
import com.bumptech.glide.RequestManager;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class SupportRequestManagerFragment extends Fragment {
    public final Set<SupportRequestManagerFragment> childRequestManagerFragments;
    public final ActivityFragmentLifecycle lifecycle;
    public Fragment parentFragmentHint;
    public RequestManager requestManager;
    public final RequestManagerTreeNode requestManagerTreeNode;
    public SupportRequestManagerFragment rootRequestManagerFragment;

    /* loaded from: classes.dex */
    public class SupportFragmentRequestManagerTreeNode implements RequestManagerTreeNode {
        public SupportFragmentRequestManagerTreeNode() {
        }

        public String toString() {
            String obj = super.toString();
            String valueOf = String.valueOf(SupportRequestManagerFragment.this);
            return Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(valueOf.length() + XMPPathFactory$$ExternalSyntheticOutline0.m(obj, 11), obj, "{fragment=", valueOf, "}");
        }
    }

    public SupportRequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            registerFragmentWithRoot(getActivity());
        } catch (IllegalStateException e) {
            if (Log.isLoggable("SupportRMFragment", 5)) {
                Log.w("SupportRMFragment", "Unable to register fragment with root", e);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mCalled = true;
        this.lifecycle.onDestroy();
        unregisterFragmentWithRoot();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        this.mCalled = true;
        this.parentFragmentHint = null;
        unregisterFragmentWithRoot();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        this.mCalled = true;
        this.lifecycle.onStart();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        this.mCalled = true;
        this.lifecycle.onStop();
    }

    public final void registerFragmentWithRoot(FragmentActivity fragmentActivity) {
        unregisterFragmentWithRoot();
        RequestManagerRetriever requestManagerRetriever = Glide.get(fragmentActivity).requestManagerRetriever;
        Objects.requireNonNull(requestManagerRetriever);
        SupportRequestManagerFragment supportRequestManagerFragment = requestManagerRetriever.getSupportRequestManagerFragment(fragmentActivity.getSupportFragmentManager(), null, !fragmentActivity.isFinishing());
        this.rootRequestManagerFragment = supportRequestManagerFragment;
        if (!equals(supportRequestManagerFragment)) {
            this.rootRequestManagerFragment.childRequestManagerFragments.add(this);
        }
    }

    @Override // androidx.fragment.app.Fragment, java.lang.Object
    public String toString() {
        String fragment = super.toString();
        Fragment fragment2 = this.mParentFragment;
        if (fragment2 == null) {
            fragment2 = this.parentFragmentHint;
        }
        String valueOf = String.valueOf(fragment2);
        return Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(valueOf.length() + XMPPathFactory$$ExternalSyntheticOutline0.m(fragment, 9), fragment, "{parent=", valueOf, "}");
    }

    public final void unregisterFragmentWithRoot() {
        SupportRequestManagerFragment supportRequestManagerFragment = this.rootRequestManagerFragment;
        if (supportRequestManagerFragment != null) {
            supportRequestManagerFragment.childRequestManagerFragments.remove(this);
            this.rootRequestManagerFragment = null;
        }
    }

    @SuppressLint({"ValidFragment"})
    public SupportRequestManagerFragment(ActivityFragmentLifecycle activityFragmentLifecycle) {
        this.requestManagerTreeNode = new SupportFragmentRequestManagerTreeNode();
        this.childRequestManagerFragments = new HashSet();
        this.lifecycle = activityFragmentLifecycle;
    }
}
