package androidx.fragment.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.SpecialEffectsController;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistryController;
import com.android.systemui.shared.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class FragmentStateManager {
    public final FragmentLifecycleCallbacksDispatcher mDispatcher;
    public final Fragment mFragment;
    public final FragmentStore mFragmentStore;
    public boolean mMovingToState = false;
    public int mFragmentManagerState = -1;

    public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, Fragment fragment) {
        this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
        this.mFragmentStore = fragmentStore;
        this.mFragment = fragment;
    }

    public void activityCreated() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("moveto ACTIVITY_CREATED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        Bundle bundle = fragment.mSavedFragmentState;
        fragment.mChildFragmentManager.noteStateNotSaved();
        fragment.mState = 3;
        fragment.mCalled = false;
        fragment.mCalled = true;
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d("FragmentManager", "moveto RESTORE_VIEW_STATE: " + fragment);
        }
        View view = fragment.mView;
        if (view != null) {
            Bundle bundle2 = fragment.mSavedFragmentState;
            SparseArray<Parcelable> sparseArray = fragment.mSavedViewState;
            if (sparseArray != null) {
                view.restoreHierarchyState(sparseArray);
                fragment.mSavedViewState = null;
            }
            if (fragment.mView != null) {
                FragmentViewLifecycleOwner fragmentViewLifecycleOwner = fragment.mViewLifecycleOwner;
                fragmentViewLifecycleOwner.mSavedStateRegistryController.performRestore(fragment.mSavedViewRegistryState);
                fragment.mSavedViewRegistryState = null;
            }
            fragment.mCalled = false;
            fragment.onViewStateRestored(bundle2);
            if (!fragment.mCalled) {
                throw new SuperNotCalledException(Fragment$$ExternalSyntheticOutline0.m("Fragment ", fragment, " did not call through to super.onViewStateRestored()"));
            } else if (fragment.mView != null) {
                fragment.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            }
        }
        fragment.mSavedFragmentState = null;
        FragmentManager fragmentManager = fragment.mChildFragmentManager;
        fragmentManager.mStateSaved = false;
        fragmentManager.mStopped = false;
        fragmentManager.mNonConfig.mIsStateSaved = false;
        fragmentManager.dispatchStateChange(4);
        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
        Fragment fragment2 = this.mFragment;
        fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated(fragment2, fragment2.mSavedFragmentState, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0039, code lost:
        if (r1 >= r0.mAdded.size()) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003b, code lost:
        r4 = r0.mAdded.get(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0045, code lost:
        if (r4.mContainer != r2) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        r4 = r4.mView;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0049, code lost:
        if (r4 == null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        r3 = r2.indexOfChild(r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addViewToContainer() {
        /*
            r7 = this;
            androidx.fragment.app.FragmentStore r0 = r7.mFragmentStore
            androidx.fragment.app.Fragment r1 = r7.mFragment
            java.util.Objects.requireNonNull(r0)
            android.view.ViewGroup r2 = r1.mContainer
            r3 = -1
            if (r2 != 0) goto L_0x000d
            goto L_0x004f
        L_0x000d:
            java.util.ArrayList<androidx.fragment.app.Fragment> r4 = r0.mAdded
            int r1 = r4.indexOf(r1)
            int r4 = r1 + -1
        L_0x0015:
            if (r4 < 0) goto L_0x0031
            java.util.ArrayList<androidx.fragment.app.Fragment> r5 = r0.mAdded
            java.lang.Object r5 = r5.get(r4)
            androidx.fragment.app.Fragment r5 = (androidx.fragment.app.Fragment) r5
            android.view.ViewGroup r6 = r5.mContainer
            if (r6 != r2) goto L_0x002e
            android.view.View r5 = r5.mView
            if (r5 == 0) goto L_0x002e
            int r0 = r2.indexOfChild(r5)
            int r3 = r0 + 1
            goto L_0x004f
        L_0x002e:
            int r4 = r4 + -1
            goto L_0x0015
        L_0x0031:
            int r1 = r1 + 1
            java.util.ArrayList<androidx.fragment.app.Fragment> r4 = r0.mAdded
            int r4 = r4.size()
            if (r1 >= r4) goto L_0x004f
            java.util.ArrayList<androidx.fragment.app.Fragment> r4 = r0.mAdded
            java.lang.Object r4 = r4.get(r1)
            androidx.fragment.app.Fragment r4 = (androidx.fragment.app.Fragment) r4
            android.view.ViewGroup r5 = r4.mContainer
            if (r5 != r2) goto L_0x0031
            android.view.View r4 = r4.mView
            if (r4 == 0) goto L_0x0031
            int r3 = r2.indexOfChild(r4)
        L_0x004f:
            androidx.fragment.app.Fragment r7 = r7.mFragment
            android.view.ViewGroup r0 = r7.mContainer
            android.view.View r7 = r7.mView
            r0.addView(r7, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.addViewToContainer():void");
    }

    public void attach() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("moveto ATTACHED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        Fragment fragment2 = fragment.mTarget;
        FragmentStateManager fragmentStateManager = null;
        if (fragment2 != null) {
            FragmentStateManager fragmentStateManager2 = this.mFragmentStore.getFragmentStateManager(fragment2.mWho);
            if (fragmentStateManager2 != null) {
                Fragment fragment3 = this.mFragment;
                fragment3.mTargetWho = fragment3.mTarget.mWho;
                fragment3.mTarget = null;
                fragmentStateManager = fragmentStateManager2;
            } else {
                StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Fragment ");
                m2.append(this.mFragment);
                m2.append(" declared target fragment ");
                m2.append(this.mFragment.mTarget);
                m2.append(" that does not belong to this FragmentManager!");
                throw new IllegalStateException(m2.toString());
            }
        } else {
            String str = fragment.mTargetWho;
            if (str != null && (fragmentStateManager = this.mFragmentStore.getFragmentStateManager(str)) == null) {
                StringBuilder m3 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Fragment ");
                m3.append(this.mFragment);
                m3.append(" declared target fragment ");
                throw new IllegalStateException(FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m3, this.mFragment.mTargetWho, " that does not belong to this FragmentManager!"));
            }
        }
        if (fragmentStateManager != null) {
            fragmentStateManager.moveToExpectedState();
        }
        Fragment fragment4 = this.mFragment;
        FragmentManager fragmentManager = fragment4.mFragmentManager;
        fragment4.mHost = fragmentManager.mHost;
        fragment4.mParentFragment = fragmentManager.mParent;
        this.mDispatcher.dispatchOnFragmentPreAttached(fragment4, false);
        Fragment fragment5 = this.mFragment;
        Iterator<Fragment.OnPreAttachedListener> it = fragment5.mOnPreAttachedListeners.iterator();
        while (it.hasNext()) {
            it.next().onPreAttached();
        }
        fragment5.mOnPreAttachedListeners.clear();
        fragment5.mChildFragmentManager.attachController(fragment5.mHost, fragment5.createFragmentContainer(), fragment5);
        fragment5.mState = 0;
        fragment5.mCalled = false;
        fragment5.onAttach(fragment5.mHost.mContext);
        if (fragment5.mCalled) {
            FragmentManager fragmentManager2 = fragment5.mFragmentManager;
            Iterator<FragmentOnAttachListener> it2 = fragmentManager2.mOnAttachListeners.iterator();
            while (it2.hasNext()) {
                it2.next().onAttachFragment(fragmentManager2, fragment5);
            }
            FragmentManager fragmentManager3 = fragment5.mChildFragmentManager;
            fragmentManager3.mStateSaved = false;
            fragmentManager3.mStopped = false;
            fragmentManager3.mNonConfig.mIsStateSaved = false;
            fragmentManager3.dispatchStateChange(0);
            this.mDispatcher.dispatchOnFragmentAttached(this.mFragment, false);
            return;
        }
        throw new SuperNotCalledException(Fragment$$ExternalSyntheticOutline0.m("Fragment ", fragment5, " did not call through to super.onAttach()"));
    }

    public int computeExpectedState() {
        SpecialEffectsController.Operation operation;
        SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact;
        Fragment fragment = this.mFragment;
        if (fragment.mFragmentManager == null) {
            return fragment.mState;
        }
        int i = this.mFragmentManagerState;
        int ordinal = fragment.mMaxState.ordinal();
        if (ordinal == 1) {
            i = Math.min(i, 0);
        } else if (ordinal == 2) {
            i = Math.min(i, 1);
        } else if (ordinal == 3) {
            i = Math.min(i, 5);
        } else if (ordinal != 4) {
            i = Math.min(i, -1);
        }
        Fragment fragment2 = this.mFragment;
        if (fragment2.mFromLayout) {
            if (fragment2.mInLayout) {
                i = Math.max(this.mFragmentManagerState, 2);
                View view = this.mFragment.mView;
                if (view != null && view.getParent() == null) {
                    i = Math.min(i, 2);
                }
            } else {
                i = this.mFragmentManagerState < 4 ? Math.min(i, fragment2.mState) : Math.min(i, 1);
            }
        }
        if (!this.mFragment.mAdded) {
            i = Math.min(i, 1);
        }
        Fragment fragment3 = this.mFragment;
        ViewGroup viewGroup = fragment3.mContainer;
        SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact2 = null;
        if (viewGroup != null) {
            SpecialEffectsController orCreateController = SpecialEffectsController.getOrCreateController(viewGroup, fragment3.getParentFragmentManager().getSpecialEffectsControllerFactory());
            Objects.requireNonNull(orCreateController);
            SpecialEffectsController.Operation findPendingOperation = orCreateController.findPendingOperation(this.mFragment);
            if (findPendingOperation != null) {
                lifecycleImpact = findPendingOperation.mLifecycleImpact;
            } else {
                Fragment fragment4 = this.mFragment;
                Iterator<SpecialEffectsController.Operation> it = orCreateController.mRunningOperations.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        operation = null;
                        break;
                    }
                    operation = it.next();
                    if (operation.mFragment.equals(fragment4) && !operation.mIsCanceled) {
                        break;
                    }
                }
                if (operation != null) {
                    lifecycleImpact = operation.mLifecycleImpact;
                }
            }
            lifecycleImpact2 = lifecycleImpact;
        }
        if (lifecycleImpact2 == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
            i = Math.min(i, 6);
        } else if (lifecycleImpact2 == SpecialEffectsController.Operation.LifecycleImpact.REMOVING) {
            i = Math.max(i, 3);
        } else {
            Fragment fragment5 = this.mFragment;
            if (fragment5.mRemoving) {
                if (fragment5.isInBackStack()) {
                    i = Math.min(i, 1);
                } else {
                    i = Math.min(i, -1);
                }
            }
        }
        Fragment fragment6 = this.mFragment;
        if (fragment6.mDeferStart && fragment6.mState < 5) {
            i = Math.min(i, 4);
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "computeExpectedState() of " + i + " for " + this.mFragment);
        }
        return i;
    }

    public void create() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("moveto CREATED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        if (!fragment.mIsCreated) {
            this.mDispatcher.dispatchOnFragmentPreCreated(fragment, fragment.mSavedFragmentState, false);
            Fragment fragment2 = this.mFragment;
            Bundle bundle = fragment2.mSavedFragmentState;
            fragment2.mChildFragmentManager.noteStateNotSaved();
            fragment2.mState = 1;
            fragment2.mCalled = false;
            fragment2.mLifecycleRegistry.addObserver(
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x003e: INVOKE  
                  (wrap: androidx.lifecycle.LifecycleRegistry : 0x0037: IGET  (r3v2 androidx.lifecycle.LifecycleRegistry A[REMOVE]) = (r0v3 'fragment2' androidx.fragment.app.Fragment) androidx.fragment.app.Fragment.mLifecycleRegistry androidx.lifecycle.LifecycleRegistry)
                  (wrap: androidx.fragment.app.Fragment$5 : 0x003b: CONSTRUCTOR  (r5v0 androidx.fragment.app.Fragment$5 A[REMOVE]) = (r0v3 'fragment2' androidx.fragment.app.Fragment) call: androidx.fragment.app.Fragment.5.<init>(androidx.fragment.app.Fragment):void type: CONSTRUCTOR)
                 type: VIRTUAL call: androidx.lifecycle.LifecycleRegistry.addObserver(androidx.lifecycle.LifecycleObserver):void in method: androidx.fragment.app.FragmentStateManager.create():void, file: classes.dex
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: androidx.fragment.app.Fragment, state: GENERATED_AND_UNLOADED
                	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                	... 23 more
                */
            /*
                this = this;
                r0 = 3
                boolean r0 = androidx.fragment.app.FragmentManager.isLoggingEnabled(r0)
                if (r0 == 0) goto L_0x001b
                java.lang.String r0 = "moveto CREATED: "
                java.lang.StringBuilder r0 = android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m(r0)
                androidx.fragment.app.Fragment r1 = r6.mFragment
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "FragmentManager"
                android.util.Log.d(r1, r0)
            L_0x001b:
                androidx.fragment.app.Fragment r0 = r6.mFragment
                boolean r1 = r0.mIsCreated
                r2 = 1
                if (r1 != 0) goto L_0x006e
                androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r1 = r6.mDispatcher
                android.os.Bundle r3 = r0.mSavedFragmentState
                r4 = 0
                r1.dispatchOnFragmentPreCreated(r0, r3, r4)
                androidx.fragment.app.Fragment r0 = r6.mFragment
                android.os.Bundle r1 = r0.mSavedFragmentState
                androidx.fragment.app.FragmentManager r3 = r0.mChildFragmentManager
                r3.noteStateNotSaved()
                r0.mState = r2
                r0.mCalled = r4
                androidx.lifecycle.LifecycleRegistry r3 = r0.mLifecycleRegistry
                androidx.fragment.app.Fragment$5 r5 = new androidx.fragment.app.Fragment$5
                r5.<init>()
                r3.addObserver(r5)
                androidx.savedstate.SavedStateRegistryController r3 = r0.mSavedStateRegistryController
                r3.performRestore(r1)
                r0.onCreate(r1)
                r0.mIsCreated = r2
                boolean r1 = r0.mCalled
                if (r1 == 0) goto L_0x0060
                androidx.lifecycle.LifecycleRegistry r0 = r0.mLifecycleRegistry
                androidx.lifecycle.Lifecycle$Event r1 = androidx.lifecycle.Lifecycle.Event.ON_CREATE
                r0.handleLifecycleEvent(r1)
                androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r6.mDispatcher
                androidx.fragment.app.Fragment r6 = r6.mFragment
                android.os.Bundle r1 = r6.mSavedFragmentState
                r0.dispatchOnFragmentCreated(r6, r1, r4)
                goto L_0x0077
            L_0x0060:
                androidx.fragment.app.SuperNotCalledException r6 = new androidx.fragment.app.SuperNotCalledException
                java.lang.String r1 = "Fragment "
                java.lang.String r2 = " did not call through to super.onCreate()"
                java.lang.String r0 = androidx.fragment.app.Fragment$$ExternalSyntheticOutline0.m(r1, r0, r2)
                r6.<init>(r0)
                throw r6
            L_0x006e:
                android.os.Bundle r1 = r0.mSavedFragmentState
                r0.restoreChildFragmentState(r1)
                androidx.fragment.app.Fragment r6 = r6.mFragment
                r6.mState = r2
            L_0x0077:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.create():void");
        }

        public void createView() {
            String str;
            if (!this.mFragment.mFromLayout) {
                if (FragmentManager.isLoggingEnabled(3)) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("moveto CREATE_VIEW: ");
                    m.append(this.mFragment);
                    Log.d("FragmentManager", m.toString());
                }
                Fragment fragment = this.mFragment;
                LayoutInflater onGetLayoutInflater = fragment.onGetLayoutInflater(fragment.mSavedFragmentState);
                ViewGroup viewGroup = null;
                Fragment fragment2 = this.mFragment;
                ViewGroup viewGroup2 = fragment2.mContainer;
                if (viewGroup2 != null) {
                    viewGroup = viewGroup2;
                } else {
                    int i = fragment2.mContainerId;
                    if (i != 0) {
                        if (i != -1) {
                            viewGroup = (ViewGroup) fragment2.mFragmentManager.mContainer.onFindViewById(i);
                            if (viewGroup == null) {
                                Fragment fragment3 = this.mFragment;
                                if (!fragment3.mRestored) {
                                    try {
                                        str = fragment3.getResources().getResourceName(this.mFragment.mContainerId);
                                    } catch (Resources.NotFoundException unused) {
                                        str = "unknown";
                                    }
                                    StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("No view found for id 0x");
                                    m2.append(Integer.toHexString(this.mFragment.mContainerId));
                                    m2.append(" (");
                                    m2.append(str);
                                    m2.append(") for fragment ");
                                    m2.append(this.mFragment);
                                    throw new IllegalArgumentException(m2.toString());
                                }
                            }
                        } else {
                            StringBuilder m3 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Cannot create fragment ");
                            m3.append(this.mFragment);
                            m3.append(" for a container view with no id");
                            throw new IllegalArgumentException(m3.toString());
                        }
                    }
                }
                Fragment fragment4 = this.mFragment;
                fragment4.mContainer = viewGroup;
                fragment4.performCreateView(onGetLayoutInflater, viewGroup, fragment4.mSavedFragmentState);
                View view = this.mFragment.mView;
                if (view != null) {
                    view.setSaveFromParentEnabled(false);
                    Fragment fragment5 = this.mFragment;
                    fragment5.mView.setTag(R.id.fragment_container_view_tag, fragment5);
                    if (viewGroup != null) {
                        addViewToContainer();
                    }
                    Fragment fragment6 = this.mFragment;
                    if (fragment6.mHidden) {
                        fragment6.mView.setVisibility(8);
                    }
                    View view2 = this.mFragment.mView;
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                    if (view2.isAttachedToWindow()) {
                        this.mFragment.mView.requestApplyInsets();
                    } else {
                        final View view3 = this.mFragment.mView;
                        view3.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(this) { // from class: androidx.fragment.app.FragmentStateManager.1
                            @Override // android.view.View.OnAttachStateChangeListener
                            public void onViewAttachedToWindow(View view4) {
                                view3.removeOnAttachStateChangeListener(this);
                                View view5 = view3;
                                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                                view5.requestApplyInsets();
                            }

                            @Override // android.view.View.OnAttachStateChangeListener
                            public void onViewDetachedFromWindow(View view4) {
                            }
                        });
                    }
                    Fragment fragment7 = this.mFragment;
                    fragment7.onViewCreated(fragment7.mView, fragment7.mSavedFragmentState);
                    fragment7.mChildFragmentManager.dispatchStateChange(2);
                    FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
                    Fragment fragment8 = this.mFragment;
                    fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(fragment8, fragment8.mView, fragment8.mSavedFragmentState, false);
                    int visibility = this.mFragment.mView.getVisibility();
                    this.mFragment.ensureAnimationInfo().mPostOnViewCreatedAlpha = this.mFragment.mView.getAlpha();
                    Fragment fragment9 = this.mFragment;
                    if (fragment9.mContainer != null && visibility == 0) {
                        View findFocus = fragment9.mView.findFocus();
                        if (findFocus != null) {
                            this.mFragment.ensureAnimationInfo().mFocusedView = findFocus;
                            if (FragmentManager.isLoggingEnabled(2)) {
                                Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + this.mFragment);
                            }
                        }
                        this.mFragment.mView.setAlpha(0.0f);
                    }
                }
                this.mFragment.mState = 2;
            }
        }

        public void destroy() {
            Fragment findActiveFragment;
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("movefrom CREATED: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment = this.mFragment;
            boolean z = true;
            boolean z2 = fragment.mRemoving && !fragment.isInBackStack();
            if (z2 || this.mFragmentStore.mNonConfig.shouldDestroy(this.mFragment)) {
                FragmentHostCallback<?> fragmentHostCallback = this.mFragment.mHost;
                if (fragmentHostCallback instanceof ViewModelStoreOwner) {
                    z = this.mFragmentStore.mNonConfig.mHasBeenCleared;
                } else {
                    Context context = fragmentHostCallback.mContext;
                    if (context instanceof Activity) {
                        z = true ^ ((Activity) context).isChangingConfigurations();
                    }
                }
                if (z2 || z) {
                    FragmentManagerViewModel fragmentManagerViewModel = this.mFragmentStore.mNonConfig;
                    Fragment fragment2 = this.mFragment;
                    Objects.requireNonNull(fragmentManagerViewModel);
                    if (FragmentManager.isLoggingEnabled(3)) {
                        Log.d("FragmentManager", "Clearing non-config state for " + fragment2);
                    }
                    FragmentManagerViewModel fragmentManagerViewModel2 = fragmentManagerViewModel.mChildNonConfigs.get(fragment2.mWho);
                    if (fragmentManagerViewModel2 != null) {
                        fragmentManagerViewModel2.onCleared();
                        fragmentManagerViewModel.mChildNonConfigs.remove(fragment2.mWho);
                    }
                    ViewModelStore viewModelStore = fragmentManagerViewModel.mViewModelStores.get(fragment2.mWho);
                    if (viewModelStore != null) {
                        viewModelStore.clear();
                        fragmentManagerViewModel.mViewModelStores.remove(fragment2.mWho);
                    }
                }
                Fragment fragment3 = this.mFragment;
                fragment3.mChildFragmentManager.dispatchDestroy();
                fragment3.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
                fragment3.mState = 0;
                fragment3.mCalled = false;
                fragment3.mIsCreated = false;
                fragment3.onDestroy();
                if (fragment3.mCalled) {
                    this.mDispatcher.dispatchOnFragmentDestroyed(this.mFragment, false);
                    Iterator it = ((ArrayList) this.mFragmentStore.getActiveFragmentStateManagers()).iterator();
                    while (it.hasNext()) {
                        FragmentStateManager fragmentStateManager = (FragmentStateManager) it.next();
                        if (fragmentStateManager != null) {
                            Fragment fragment4 = fragmentStateManager.mFragment;
                            if (this.mFragment.mWho.equals(fragment4.mTargetWho)) {
                                fragment4.mTarget = this.mFragment;
                                fragment4.mTargetWho = null;
                            }
                        }
                    }
                    Fragment fragment5 = this.mFragment;
                    String str = fragment5.mTargetWho;
                    if (str != null) {
                        fragment5.mTarget = this.mFragmentStore.findActiveFragment(str);
                    }
                    this.mFragmentStore.makeInactive(this);
                    return;
                }
                throw new SuperNotCalledException(Fragment$$ExternalSyntheticOutline0.m("Fragment ", fragment3, " did not call through to super.onDestroy()"));
            }
            String str2 = this.mFragment.mTargetWho;
            if (!(str2 == null || (findActiveFragment = this.mFragmentStore.findActiveFragment(str2)) == null || !findActiveFragment.mRetainInstance)) {
                this.mFragment.mTarget = findActiveFragment;
            }
            this.mFragment.mState = 0;
        }

        public void destroyFragmentView() {
            View view;
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("movefrom CREATE_VIEW: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment = this.mFragment;
            ViewGroup viewGroup = fragment.mContainer;
            if (!(viewGroup == null || (view = fragment.mView) == null)) {
                viewGroup.removeView(view);
            }
            this.mFragment.performDestroyView();
            this.mDispatcher.dispatchOnFragmentViewDestroyed(this.mFragment, false);
            Fragment fragment2 = this.mFragment;
            fragment2.mContainer = null;
            fragment2.mView = null;
            fragment2.mViewLifecycleOwner = null;
            fragment2.mViewLifecycleOwnerLiveData.setValue(null);
            this.mFragment.mInLayout = false;
        }

        public void detach() {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("movefrom ATTACHED: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment = this.mFragment;
            fragment.mState = -1;
            fragment.mCalled = false;
            fragment.onDetach();
            if (fragment.mCalled) {
                FragmentManager fragmentManager = fragment.mChildFragmentManager;
                if (!fragmentManager.mDestroyed) {
                    fragmentManager.dispatchDestroy();
                    fragment.mChildFragmentManager = new FragmentManagerImpl();
                }
                this.mDispatcher.dispatchOnFragmentDetached(this.mFragment, false);
                Fragment fragment2 = this.mFragment;
                fragment2.mState = -1;
                fragment2.mHost = null;
                fragment2.mParentFragment = null;
                fragment2.mFragmentManager = null;
                if ((fragment2.mRemoving && !fragment2.isInBackStack()) || this.mFragmentStore.mNonConfig.shouldDestroy(this.mFragment)) {
                    if (FragmentManager.isLoggingEnabled(3)) {
                        StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("initState called for fragment: ");
                        m2.append(this.mFragment);
                        Log.d("FragmentManager", m2.toString());
                    }
                    Fragment fragment3 = this.mFragment;
                    Objects.requireNonNull(fragment3);
                    fragment3.mLifecycleRegistry = new LifecycleRegistry(fragment3, true);
                    fragment3.mSavedStateRegistryController = new SavedStateRegistryController(fragment3);
                    fragment3.mDefaultFactory = null;
                    fragment3.mWho = UUID.randomUUID().toString();
                    fragment3.mAdded = false;
                    fragment3.mRemoving = false;
                    fragment3.mFromLayout = false;
                    fragment3.mInLayout = false;
                    fragment3.mRestored = false;
                    fragment3.mBackStackNesting = 0;
                    fragment3.mFragmentManager = null;
                    fragment3.mChildFragmentManager = new FragmentManagerImpl();
                    fragment3.mHost = null;
                    fragment3.mFragmentId = 0;
                    fragment3.mContainerId = 0;
                    fragment3.mTag = null;
                    fragment3.mHidden = false;
                    fragment3.mDetached = false;
                    return;
                }
                return;
            }
            throw new SuperNotCalledException(Fragment$$ExternalSyntheticOutline0.m("Fragment ", fragment, " did not call through to super.onDetach()"));
        }

        public void ensureInflatedView() {
            Fragment fragment = this.mFragment;
            if (fragment.mFromLayout && fragment.mInLayout && !fragment.mPerformedCreateView) {
                if (FragmentManager.isLoggingEnabled(3)) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("moveto CREATE_VIEW: ");
                    m.append(this.mFragment);
                    Log.d("FragmentManager", m.toString());
                }
                Fragment fragment2 = this.mFragment;
                fragment2.performCreateView(fragment2.onGetLayoutInflater(fragment2.mSavedFragmentState), null, this.mFragment.mSavedFragmentState);
                View view = this.mFragment.mView;
                if (view != null) {
                    view.setSaveFromParentEnabled(false);
                    Fragment fragment3 = this.mFragment;
                    fragment3.mView.setTag(R.id.fragment_container_view_tag, fragment3);
                    Fragment fragment4 = this.mFragment;
                    if (fragment4.mHidden) {
                        fragment4.mView.setVisibility(8);
                    }
                    Fragment fragment5 = this.mFragment;
                    fragment5.onViewCreated(fragment5.mView, fragment5.mSavedFragmentState);
                    fragment5.mChildFragmentManager.dispatchStateChange(2);
                    FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
                    Fragment fragment6 = this.mFragment;
                    fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(fragment6, fragment6.mView, fragment6.mSavedFragmentState, false);
                    this.mFragment.mState = 2;
                }
            }
        }

        public void moveToExpectedState() {
            ViewGroup viewGroup;
            ViewGroup viewGroup2;
            ViewGroup viewGroup3;
            SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact = SpecialEffectsController.Operation.LifecycleImpact.NONE;
            if (!this.mMovingToState) {
                try {
                    this.mMovingToState = true;
                    while (true) {
                        int computeExpectedState = computeExpectedState();
                        Fragment fragment = this.mFragment;
                        int i = fragment.mState;
                        if (computeExpectedState == i) {
                            if (fragment.mHiddenChanged) {
                                if (!(fragment.mView == null || (viewGroup = fragment.mContainer) == null)) {
                                    SpecialEffectsController orCreateController = SpecialEffectsController.getOrCreateController(viewGroup, fragment.getParentFragmentManager().getSpecialEffectsControllerFactory());
                                    if (this.mFragment.mHidden) {
                                        Objects.requireNonNull(orCreateController);
                                        if (FragmentManager.isLoggingEnabled(2)) {
                                            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing hide operation for fragment " + this.mFragment);
                                        }
                                        orCreateController.enqueue(SpecialEffectsController.Operation.State.GONE, lifecycleImpact, this);
                                    } else {
                                        Objects.requireNonNull(orCreateController);
                                        if (FragmentManager.isLoggingEnabled(2)) {
                                            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing show operation for fragment " + this.mFragment);
                                        }
                                        orCreateController.enqueue(SpecialEffectsController.Operation.State.VISIBLE, lifecycleImpact, this);
                                    }
                                }
                                Fragment fragment2 = this.mFragment;
                                FragmentManager fragmentManager = fragment2.mFragmentManager;
                                if (fragmentManager != null && fragment2.mAdded && fragmentManager.isMenuAvailable(fragment2)) {
                                    fragmentManager.mNeedMenuInvalidate = true;
                                }
                                this.mFragment.mHiddenChanged = false;
                            }
                            return;
                        } else if (computeExpectedState > i) {
                            switch (i + 1) {
                                case 0:
                                    attach();
                                    continue;
                                case 1:
                                    create();
                                    continue;
                                case 2:
                                    ensureInflatedView();
                                    createView();
                                    continue;
                                case 3:
                                    activityCreated();
                                    continue;
                                case 4:
                                    if (!(fragment.mView == null || (viewGroup2 = fragment.mContainer) == null)) {
                                        SpecialEffectsController orCreateController2 = SpecialEffectsController.getOrCreateController(viewGroup2, fragment.getParentFragmentManager().getSpecialEffectsControllerFactory());
                                        SpecialEffectsController.Operation.State from = SpecialEffectsController.Operation.State.from(this.mFragment.mView.getVisibility());
                                        Objects.requireNonNull(orCreateController2);
                                        if (FragmentManager.isLoggingEnabled(2)) {
                                            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing add operation for fragment " + this.mFragment);
                                        }
                                        orCreateController2.enqueue(from, SpecialEffectsController.Operation.LifecycleImpact.ADDING, this);
                                    }
                                    this.mFragment.mState = 4;
                                    continue;
                                case 5:
                                    start();
                                    continue;
                                case 6:
                                    fragment.mState = 6;
                                    continue;
                                case 7:
                                    resume();
                                    continue;
                                default:
                                    continue;
                            }
                        } else {
                            switch (i - 1) {
                                case -1:
                                    detach();
                                    continue;
                                case 0:
                                    destroy();
                                    continue;
                                case 1:
                                    destroyFragmentView();
                                    this.mFragment.mState = 1;
                                    continue;
                                case 2:
                                    fragment.mInLayout = false;
                                    fragment.mState = 2;
                                    continue;
                                case 3:
                                    if (FragmentManager.isLoggingEnabled(3)) {
                                        Log.d("FragmentManager", "movefrom ACTIVITY_CREATED: " + this.mFragment);
                                    }
                                    Fragment fragment3 = this.mFragment;
                                    if (fragment3.mView != null && fragment3.mSavedViewState == null) {
                                        saveViewState();
                                    }
                                    Fragment fragment4 = this.mFragment;
                                    if (!(fragment4.mView == null || (viewGroup3 = fragment4.mContainer) == null)) {
                                        SpecialEffectsController orCreateController3 = SpecialEffectsController.getOrCreateController(viewGroup3, fragment4.getParentFragmentManager().getSpecialEffectsControllerFactory());
                                        Objects.requireNonNull(orCreateController3);
                                        if (FragmentManager.isLoggingEnabled(2)) {
                                            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing remove operation for fragment " + this.mFragment);
                                        }
                                        orCreateController3.enqueue(SpecialEffectsController.Operation.State.REMOVED, SpecialEffectsController.Operation.LifecycleImpact.REMOVING, this);
                                    }
                                    this.mFragment.mState = 3;
                                    continue;
                                case 4:
                                    stop();
                                    continue;
                                case 5:
                                    fragment.mState = 5;
                                    continue;
                                case 6:
                                    pause();
                                    continue;
                                default:
                                    continue;
                            }
                        }
                    }
                } finally {
                    this.mMovingToState = false;
                }
            } else if (FragmentManager.isLoggingEnabled(2)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Ignoring re-entrant call to moveToExpectedState() for ");
                m.append(this.mFragment);
                Log.v("FragmentManager", m.toString());
            }
        }

        public void pause() {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("movefrom RESUMED: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment = this.mFragment;
            fragment.mChildFragmentManager.dispatchStateChange(5);
            if (fragment.mView != null) {
                fragment.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
            }
            fragment.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
            fragment.mState = 6;
            fragment.mCalled = false;
            fragment.onPause();
            if (fragment.mCalled) {
                this.mDispatcher.dispatchOnFragmentPaused(this.mFragment, false);
                return;
            }
            throw new SuperNotCalledException(Fragment$$ExternalSyntheticOutline0.m("Fragment ", fragment, " did not call through to super.onPause()"));
        }

        public void restoreState(ClassLoader classLoader) {
            Bundle bundle = this.mFragment.mSavedFragmentState;
            if (bundle != null) {
                bundle.setClassLoader(classLoader);
                Fragment fragment = this.mFragment;
                fragment.mSavedViewState = fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                Fragment fragment2 = this.mFragment;
                fragment2.mSavedViewRegistryState = fragment2.mSavedFragmentState.getBundle("android:view_registry_state");
                Fragment fragment3 = this.mFragment;
                fragment3.mTargetWho = fragment3.mSavedFragmentState.getString("android:target_state");
                Fragment fragment4 = this.mFragment;
                if (fragment4.mTargetWho != null) {
                    fragment4.mTargetRequestCode = fragment4.mSavedFragmentState.getInt("android:target_req_state", 0);
                }
                Fragment fragment5 = this.mFragment;
                Objects.requireNonNull(fragment5);
                fragment5.mUserVisibleHint = fragment5.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                Fragment fragment6 = this.mFragment;
                if (!fragment6.mUserVisibleHint) {
                    fragment6.mDeferStart = true;
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:21:0x0045  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void resume() {
            /*
            // Method dump skipped, instructions count: 229
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.resume():void");
        }

        public void saveViewState() {
            if (this.mFragment.mView != null) {
                SparseArray<Parcelable> sparseArray = new SparseArray<>();
                this.mFragment.mView.saveHierarchyState(sparseArray);
                if (sparseArray.size() > 0) {
                    this.mFragment.mSavedViewState = sparseArray;
                }
                Bundle bundle = new Bundle();
                this.mFragment.mViewLifecycleOwner.mSavedStateRegistryController.performSave(bundle);
                if (!bundle.isEmpty()) {
                    this.mFragment.mSavedViewRegistryState = bundle;
                }
            }
        }

        public void start() {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("moveto STARTED: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment = this.mFragment;
            fragment.mChildFragmentManager.noteStateNotSaved();
            fragment.mChildFragmentManager.execPendingActions(true);
            fragment.mState = 5;
            fragment.mCalled = false;
            fragment.onStart();
            if (fragment.mCalled) {
                LifecycleRegistry lifecycleRegistry = fragment.mLifecycleRegistry;
                Lifecycle.Event event = Lifecycle.Event.ON_START;
                lifecycleRegistry.handleLifecycleEvent(event);
                if (fragment.mView != null) {
                    fragment.mViewLifecycleOwner.handleLifecycleEvent(event);
                }
                FragmentManager fragmentManager = fragment.mChildFragmentManager;
                fragmentManager.mStateSaved = false;
                fragmentManager.mStopped = false;
                fragmentManager.mNonConfig.mIsStateSaved = false;
                fragmentManager.dispatchStateChange(5);
                this.mDispatcher.dispatchOnFragmentStarted(this.mFragment, false);
                return;
            }
            throw new SuperNotCalledException(Fragment$$ExternalSyntheticOutline0.m("Fragment ", fragment, " did not call through to super.onStart()"));
        }

        public void stop() {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("movefrom STARTED: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment = this.mFragment;
            FragmentManager fragmentManager = fragment.mChildFragmentManager;
            fragmentManager.mStopped = true;
            fragmentManager.mNonConfig.mIsStateSaved = true;
            fragmentManager.dispatchStateChange(4);
            if (fragment.mView != null) {
                fragment.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            }
            fragment.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            fragment.mState = 4;
            fragment.mCalled = false;
            fragment.onStop();
            if (fragment.mCalled) {
                this.mDispatcher.dispatchOnFragmentStopped(this.mFragment, false);
                return;
            }
            throw new SuperNotCalledException(Fragment$$ExternalSyntheticOutline0.m("Fragment ", fragment, " did not call through to super.onStop()"));
        }

        public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, ClassLoader classLoader, FragmentFactory fragmentFactory, FragmentState fragmentState) {
            this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
            this.mFragmentStore = fragmentStore;
            Fragment instantiate = fragmentFactory.instantiate(classLoader, fragmentState.mClassName);
            Bundle bundle = fragmentState.mArguments;
            if (bundle != null) {
                bundle.setClassLoader(classLoader);
            }
            instantiate.setArguments(fragmentState.mArguments);
            instantiate.mWho = fragmentState.mWho;
            instantiate.mFromLayout = fragmentState.mFromLayout;
            instantiate.mRestored = true;
            instantiate.mFragmentId = fragmentState.mFragmentId;
            instantiate.mContainerId = fragmentState.mContainerId;
            instantiate.mTag = fragmentState.mTag;
            instantiate.mRetainInstance = fragmentState.mRetainInstance;
            instantiate.mRemoving = fragmentState.mRemoving;
            instantiate.mDetached = fragmentState.mDetached;
            instantiate.mHidden = fragmentState.mHidden;
            instantiate.mMaxState = Lifecycle.State.values()[fragmentState.mMaxLifecycleState];
            Bundle bundle2 = fragmentState.mSavedFragmentState;
            if (bundle2 != null) {
                instantiate.mSavedFragmentState = bundle2;
            } else {
                instantiate.mSavedFragmentState = new Bundle();
            }
            this.mFragment = instantiate;
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Instantiated fragment " + instantiate);
            }
        }

        public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, Fragment fragment, FragmentState fragmentState) {
            this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
            this.mFragmentStore = fragmentStore;
            this.mFragment = fragment;
            fragment.mSavedViewState = null;
            fragment.mSavedViewRegistryState = null;
            fragment.mBackStackNesting = 0;
            fragment.mInLayout = false;
            fragment.mAdded = false;
            Fragment fragment2 = fragment.mTarget;
            fragment.mTargetWho = fragment2 != null ? fragment2.mWho : null;
            fragment.mTarget = null;
            Bundle bundle = fragmentState.mSavedFragmentState;
            if (bundle != null) {
                fragment.mSavedFragmentState = bundle;
            } else {
                fragment.mSavedFragmentState = new Bundle();
            }
        }
    }
