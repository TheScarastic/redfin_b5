package com.android.systemui.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.statusbar.phone.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
/* loaded from: classes.dex */
public class FragmentService implements Dumpable {
    private final FragmentCreator mFragmentCreator;
    private final ArrayMap<View, FragmentHostState> mHosts = new ArrayMap<>();
    private final ArrayMap<String, Method> mInjectionMap = new ArrayMap<>();
    private final Handler mHandler = new Handler();
    private ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.fragments.FragmentService.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(Configuration configuration) {
            for (FragmentHostState fragmentHostState : FragmentService.this.mHosts.values()) {
                fragmentHostState.sendConfigurationChange(configuration);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface FragmentCreator {

        /* loaded from: classes.dex */
        public interface Factory {
            FragmentCreator build();
        }

        CollapsedStatusBarFragment createCollapsedStatusBarFragment();

        QSFragment createQSFragment();
    }

    public FragmentService(FragmentCreator.Factory factory, ConfigurationController configurationController) {
        this.mFragmentCreator = factory.build();
        initInjectionMap();
        configurationController.addCallback(this.mConfigurationListener);
    }

    /* access modifiers changed from: package-private */
    public ArrayMap<String, Method> getInjectionMap() {
        return this.mInjectionMap;
    }

    /* access modifiers changed from: package-private */
    public FragmentCreator getFragmentCreator() {
        return this.mFragmentCreator;
    }

    private void initInjectionMap() {
        Method[] declaredMethods = FragmentCreator.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (Fragment.class.isAssignableFrom(method.getReturnType()) && (method.getModifiers() & 1) != 0) {
                this.mInjectionMap.put(method.getReturnType().getName(), method);
            }
        }
    }

    public FragmentHostManager getFragmentHostManager(View view) {
        View rootView = view.getRootView();
        FragmentHostState fragmentHostState = this.mHosts.get(rootView);
        if (fragmentHostState == null) {
            fragmentHostState = new FragmentHostState(rootView);
            this.mHosts.put(rootView, fragmentHostState);
        }
        return fragmentHostState.getFragmentHostManager();
    }

    public void destroyAll() {
        for (FragmentHostState fragmentHostState : this.mHosts.values()) {
            fragmentHostState.mFragmentHostManager.destroy();
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Dumping fragments:");
        for (FragmentHostState fragmentHostState : this.mHosts.values()) {
            fragmentHostState.mFragmentHostManager.getFragmentManager().dump("  ", fileDescriptor, printWriter, strArr);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FragmentHostState {
        private FragmentHostManager mFragmentHostManager;
        private final View mView;

        public FragmentHostState(View view) {
            this.mView = view;
            this.mFragmentHostManager = new FragmentHostManager(FragmentService.this, view);
        }

        public void sendConfigurationChange(Configuration configuration) {
            FragmentService.this.mHandler.post(new FragmentService$FragmentHostState$$ExternalSyntheticLambda0(this, configuration));
        }

        public FragmentHostManager getFragmentHostManager() {
            return this.mFragmentHostManager;
        }

        /* access modifiers changed from: private */
        /* renamed from: handleSendConfigurationChange */
        public void lambda$sendConfigurationChange$0(Configuration configuration) {
            this.mFragmentHostManager.onConfigurationChanged(configuration);
        }
    }
}
