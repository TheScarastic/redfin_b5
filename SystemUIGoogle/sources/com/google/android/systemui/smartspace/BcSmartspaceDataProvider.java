package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.bcsmartspace.R$layout;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class BcSmartspaceDataProvider implements BcSmartspaceDataPlugin {
    private final List<BcSmartspaceDataPlugin.SmartspaceTargetListener> mSmartspaceTargetListeners = new ArrayList();
    private final List<SmartspaceTarget> mSmartspaceTargets = new ArrayList();
    private BcSmartspaceDataPlugin.SmartspaceView mView = null;
    private Set<View.OnAttachStateChangeListener> mAttachListeners = new HashSet();
    private BcSmartspaceDataPlugin.SmartspaceEventNotifier mEventNotifier = null;

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void registerListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.mSmartspaceTargetListeners.add(smartspaceTargetListener);
        smartspaceTargetListener.onSmartspaceTargetsUpdated(this.mSmartspaceTargets);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void unregisterListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.mSmartspaceTargetListeners.remove(smartspaceTargetListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void registerSmartspaceEventNotifier(BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier) {
        this.mEventNotifier = smartspaceEventNotifier;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
        BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier = this.mEventNotifier;
        if (smartspaceEventNotifier != null) {
            smartspaceEventNotifier.notifySmartspaceEvent(smartspaceTargetEvent);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public BcSmartspaceDataPlugin.SmartspaceView getView(ViewGroup viewGroup) {
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView = this.mView;
        if (smartspaceView == null) {
            this.mView = (BcSmartspaceDataPlugin.SmartspaceView) LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.smartspace_enhanced, viewGroup, false);
            addListeners();
        } else {
            View view = (View) smartspaceView;
            if (view.getParent() != viewGroup) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        return this.mView;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        this.mAttachListeners.add(onAttachStateChangeListener);
        addListeners();
    }

    private void addListeners() {
        if (this.mView != null) {
            for (View.OnAttachStateChangeListener onAttachStateChangeListener : this.mAttachListeners) {
                ((View) this.mView).addOnAttachStateChangeListener(onAttachStateChangeListener);
            }
            this.mAttachListeners.clear();
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void onTargetsAvailable(List<SmartspaceTarget> list) {
        this.mSmartspaceTargets.clear();
        for (SmartspaceTarget smartspaceTarget : list) {
            if (smartspaceTarget.getFeatureType() != 15) {
                this.mSmartspaceTargets.add(smartspaceTarget);
            }
        }
        this.mSmartspaceTargetListeners.forEach(new Consumer() { // from class: com.google.android.systemui.smartspace.BcSmartspaceDataProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BcSmartspaceDataProvider.this.lambda$onTargetsAvailable$0((BcSmartspaceDataPlugin.SmartspaceTargetListener) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTargetsAvailable$0(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        smartspaceTargetListener.onSmartspaceTargetsUpdated(this.mSmartspaceTargets);
    }
}
