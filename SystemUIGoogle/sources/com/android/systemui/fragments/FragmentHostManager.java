package com.android.systemui.fragments;

import android.app.Fragment;
import android.app.FragmentController;
import android.app.FragmentHostCallback;
import android.app.FragmentManager;
import android.app.FragmentManagerNonConfig;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.Dependency;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.util.leak.LeakDetector;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class FragmentHostManager {
    private final InterestingConfigChanges mConfigChanges;
    private final Context mContext;
    private FragmentController mFragments;
    private FragmentManager.FragmentLifecycleCallbacks mLifecycleCallbacks;
    private final FragmentService mManager;
    private final View mRootView;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final HashMap<String, ArrayList<FragmentListener>> mListeners = new HashMap<>();
    private final ExtensionFragmentManager mPlugins = new ExtensionFragmentManager();

    /* loaded from: classes.dex */
    public interface FragmentListener {
        void onFragmentViewCreated(String str, Fragment fragment);

        default void onFragmentViewDestroyed(String str, Fragment fragment) {
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public FragmentHostManager(FragmentService fragmentService, View view) {
        InterestingConfigChanges interestingConfigChanges = new InterestingConfigChanges(-1073741564);
        this.mConfigChanges = interestingConfigChanges;
        Context context = view.getContext();
        this.mContext = context;
        this.mManager = fragmentService;
        this.mRootView = view;
        interestingConfigChanges.applyNewConfig(context.getResources());
        createFragmentHost(null);
    }

    private void createFragmentHost(Parcelable parcelable) {
        FragmentController createController = FragmentController.createController(new HostCallbacks());
        this.mFragments = createController;
        createController.attachHost(null);
        this.mLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() { // from class: com.android.systemui.fragments.FragmentHostManager.1
            @Override // android.app.FragmentManager.FragmentLifecycleCallbacks
            public void onFragmentViewCreated(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
                FragmentHostManager.this.onFragmentViewCreated(fragment);
            }

            @Override // android.app.FragmentManager.FragmentLifecycleCallbacks
            public void onFragmentViewDestroyed(FragmentManager fragmentManager, Fragment fragment) {
                FragmentHostManager.this.onFragmentViewDestroyed(fragment);
            }

            @Override // android.app.FragmentManager.FragmentLifecycleCallbacks
            public void onFragmentDestroyed(FragmentManager fragmentManager, Fragment fragment) {
                ((LeakDetector) Dependency.get(LeakDetector.class)).trackGarbage(fragment);
            }
        };
        this.mFragments.getFragmentManager().registerFragmentLifecycleCallbacks(this.mLifecycleCallbacks, true);
        if (parcelable != null) {
            this.mFragments.restoreAllState(parcelable, (FragmentManagerNonConfig) null);
        }
        this.mFragments.dispatchCreate();
        this.mFragments.dispatchStart();
        this.mFragments.dispatchResume();
    }

    private Parcelable destroyFragmentHost() {
        this.mFragments.dispatchPause();
        Parcelable saveAllState = this.mFragments.saveAllState();
        this.mFragments.dispatchStop();
        this.mFragments.dispatchDestroy();
        this.mFragments.getFragmentManager().unregisterFragmentLifecycleCallbacks(this.mLifecycleCallbacks);
        return saveAllState;
    }

    public FragmentHostManager addTagListener(String str, FragmentListener fragmentListener) {
        ArrayList<FragmentListener> arrayList = this.mListeners.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.mListeners.put(str, arrayList);
        }
        arrayList.add(fragmentListener);
        Fragment findFragmentByTag = getFragmentManager().findFragmentByTag(str);
        if (!(findFragmentByTag == null || findFragmentByTag.getView() == null)) {
            fragmentListener.onFragmentViewCreated(str, findFragmentByTag);
        }
        return this;
    }

    public void removeTagListener(String str, FragmentListener fragmentListener) {
        ArrayList<FragmentListener> arrayList = this.mListeners.get(str);
        if (arrayList != null && arrayList.remove(fragmentListener) && arrayList.size() == 0) {
            this.mListeners.remove(str);
        }
    }

    public void onFragmentViewCreated(Fragment fragment) {
        String tag = fragment.getTag();
        ArrayList<FragmentListener> arrayList = this.mListeners.get(tag);
        if (arrayList != null) {
            arrayList.forEach(new Consumer(tag, fragment) { // from class: com.android.systemui.fragments.FragmentHostManager$$ExternalSyntheticLambda0
                public final /* synthetic */ String f$0;
                public final /* synthetic */ Fragment f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    FragmentHostManager.$r8$lambda$QlJMKD9uPd8hZrV2scpLIysb7yc(this.f$0, this.f$1, (FragmentHostManager.FragmentListener) obj);
                }
            });
        }
    }

    public void onFragmentViewDestroyed(Fragment fragment) {
        String tag = fragment.getTag();
        ArrayList<FragmentListener> arrayList = this.mListeners.get(tag);
        if (arrayList != null) {
            arrayList.forEach(new Consumer(tag, fragment) { // from class: com.android.systemui.fragments.FragmentHostManager$$ExternalSyntheticLambda1
                public final /* synthetic */ String f$0;
                public final /* synthetic */ Fragment f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    FragmentHostManager.$r8$lambda$jqcTN9upx2bwJ3BAaMHXjltVmKA(this.f$0, this.f$1, (FragmentHostManager.FragmentListener) obj);
                }
            });
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mConfigChanges.applyNewConfig(this.mContext.getResources())) {
            reloadFragments();
        } else {
            this.mFragments.dispatchConfigurationChanged(configuration);
        }
    }

    public <T extends View> T findViewById(int i) {
        return (T) this.mRootView.findViewById(i);
    }

    public FragmentManager getFragmentManager() {
        return this.mFragments.getFragmentManager();
    }

    public ExtensionFragmentManager getExtensionManager() {
        return this.mPlugins;
    }

    public void destroy() {
        this.mFragments.dispatchDestroy();
    }

    public <T> T create(Class<T> cls) {
        return (T) this.mPlugins.instantiate(this.mContext, cls.getName(), null);
    }

    public static FragmentHostManager get(View view) {
        try {
            return ((FragmentService) Dependency.get(FragmentService.class)).getFragmentHostManager(view);
        } catch (ClassCastException e) {
            throw e;
        }
    }

    public void reloadFragments() {
        createFragmentHost(destroyFragmentHost());
    }

    /* loaded from: classes.dex */
    public class HostCallbacks extends FragmentHostCallback<FragmentHostManager> {
        @Override // android.app.FragmentHostCallback
        public void onAttachFragment(Fragment fragment) {
        }

        @Override // android.app.FragmentHostCallback
        public int onGetWindowAnimations() {
            return 0;
        }

        @Override // android.app.FragmentHostCallback, android.app.FragmentContainer
        public boolean onHasView() {
            return true;
        }

        @Override // android.app.FragmentHostCallback
        public boolean onHasWindowAnimations() {
            return false;
        }

        @Override // android.app.FragmentHostCallback
        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return true;
        }

        @Override // android.app.FragmentHostCallback
        public boolean onUseFragmentManagerInflaterFactory() {
            return true;
        }

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public HostCallbacks() {
            super(r3.mContext, r3.mHandler, 0);
            FragmentHostManager.this = r3;
        }

        @Override // android.app.FragmentHostCallback
        public FragmentHostManager onGetHost() {
            return FragmentHostManager.this;
        }

        @Override // android.app.FragmentHostCallback
        public void onDump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            FragmentHostManager.this.dump(str, fileDescriptor, printWriter, strArr);
        }

        public Fragment instantiate(Context context, String str, Bundle bundle) {
            return FragmentHostManager.this.mPlugins.instantiate(context, str, bundle);
        }

        @Override // android.app.FragmentHostCallback
        public LayoutInflater onGetLayoutInflater() {
            return LayoutInflater.from(FragmentHostManager.this.mContext);
        }

        @Override // android.app.FragmentHostCallback, android.app.FragmentContainer
        public <T extends View> T onFindViewById(int i) {
            return (T) FragmentHostManager.this.findViewById(i);
        }
    }

    /* loaded from: classes.dex */
    public class ExtensionFragmentManager {
        private final ArrayMap<String, Context> mExtensionLookup = new ArrayMap<>();

        ExtensionFragmentManager() {
            FragmentHostManager.this = r1;
        }

        public void setCurrentExtension(int i, String str, String str2, String str3, Context context) {
            if (str2 != null) {
                this.mExtensionLookup.remove(str2);
            }
            this.mExtensionLookup.put(str3, context);
            FragmentHostManager.this.getFragmentManager().beginTransaction().replace(i, instantiate(context, str3, null), str).commit();
            FragmentHostManager.this.reloadFragments();
        }

        Fragment instantiate(Context context, String str, Bundle bundle) {
            Context context2 = this.mExtensionLookup.get(str);
            if (context2 == null) {
                return instantiateWithInjections(context, str, bundle);
            }
            Fragment instantiateWithInjections = instantiateWithInjections(context2, str, bundle);
            if (instantiateWithInjections instanceof Plugin) {
                ((Plugin) instantiateWithInjections).onCreate(FragmentHostManager.this.mContext, context2);
            }
            return instantiateWithInjections;
        }

        private Fragment instantiateWithInjections(Context context, String str, Bundle bundle) {
            Method method = FragmentHostManager.this.mManager.getInjectionMap().get(str);
            if (method == null) {
                return Fragment.instantiate(context, str, bundle);
            }
            try {
                Fragment fragment = (Fragment) method.invoke(FragmentHostManager.this.mManager.getFragmentCreator(), new Object[0]);
                if (bundle != null) {
                    bundle.setClassLoader(fragment.getClass().getClassLoader());
                    fragment.setArguments(bundle);
                }
                return fragment;
            } catch (IllegalAccessException e) {
                throw new Fragment.InstantiationException("Unable to instantiate " + str, e);
            } catch (InvocationTargetException e2) {
                throw new Fragment.InstantiationException("Unable to instantiate " + str, e2);
            }
        }
    }
}
