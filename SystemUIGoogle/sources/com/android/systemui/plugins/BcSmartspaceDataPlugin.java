package com.android.systemui.plugins;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.plugins.annotations.ProvidesInterface;
import java.util.List;
@ProvidesInterface(action = BcSmartspaceDataPlugin.ACTION, version = 1)
/* loaded from: classes.dex */
public interface BcSmartspaceDataPlugin extends Plugin {
    public static final String ACTION = "com.android.systemui.action.PLUGIN_BC_SMARTSPACE_DATA";
    public static final int VERSION = 1;

    /* loaded from: classes.dex */
    public interface SmartspaceEventNotifier {
        void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent);
    }

    /* loaded from: classes.dex */
    public interface SmartspaceTargetListener {
        void onSmartspaceTargetsUpdated(List<? extends Parcelable> list);
    }

    /* loaded from: classes.dex */
    public interface SmartspaceView {
        void registerDataProvider(BcSmartspaceDataPlugin bcSmartspaceDataPlugin);

        void setDnd(Drawable drawable, String str);

        void setDozeAmount(float f);

        void setFalsingManager(FalsingManager falsingManager);

        void setIntentStarter(IntentStarter intentStarter);

        void setMediaTarget(SmartspaceTarget smartspaceTarget);

        void setNextAlarm(Drawable drawable, String str);

        void setPrimaryTextColor(int i);
    }

    default void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
    }

    default SmartspaceView getView(ViewGroup viewGroup) {
        return null;
    }

    default void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
    }

    void onTargetsAvailable(List<SmartspaceTarget> list);

    void registerListener(SmartspaceTargetListener smartspaceTargetListener);

    default void registerSmartspaceEventNotifier(SmartspaceEventNotifier smartspaceEventNotifier) {
    }

    void unregisterListener(SmartspaceTargetListener smartspaceTargetListener);

    /* loaded from: classes.dex */
    public interface IntentStarter {
        void startIntent(View view, Intent intent);

        void startPendingIntent(PendingIntent pendingIntent);

        default void startFromAction(SmartspaceAction smartspaceAction, View view) {
            if (smartspaceAction.getIntent() != null) {
                startIntent(view, smartspaceAction.getIntent());
            } else if (smartspaceAction.getPendingIntent() != null) {
                startPendingIntent(smartspaceAction.getPendingIntent());
            }
        }
    }
}
