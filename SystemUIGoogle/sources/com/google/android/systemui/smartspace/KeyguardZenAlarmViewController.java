package com.google.android.systemui.smartspace;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KFunction;
/* compiled from: KeyguardZenAlarmViewController.kt */
/* loaded from: classes2.dex */
public final class KeyguardZenAlarmViewController {
    public static final Companion Companion = new Companion(null);
    private final Drawable alarmImage;
    private final AlarmManager alarmManager;
    private final Context context;
    private final Handler handler;
    private final NextAlarmController nextAlarmController;
    private final BcSmartspaceDataPlugin plugin;
    private BcSmartspaceDataPlugin.SmartspaceView smartspaceView;
    private final ZenModeController zenModeController;
    private final KFunction<Unit> showNextAlarm = new Function0<Unit>(this) { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$showNextAlarm$1
        @Override // kotlin.jvm.functions.Function0
        public final void invoke() {
            ((KeyguardZenAlarmViewController) this.receiver).showAlarm();
        }
    };
    private final KeyguardZenAlarmViewController$zenModeCallback$1 zenModeCallback = new KeyguardZenAlarmViewController$zenModeCallback$1(this);
    private final NextAlarmController.NextAlarmChangeCallback nextAlarmCallback = new NextAlarmController.NextAlarmChangeCallback(this) { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$nextAlarmCallback$1
        final /* synthetic */ KeyguardZenAlarmViewController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.statusbar.policy.NextAlarmController.NextAlarmChangeCallback
        public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
            this.this$0.updateNextAlarm();
        }
    };
    private final Drawable dndImage = loadDndImage();

    @VisibleForTesting
    public static /* synthetic */ void getSmartspaceView$annotations() {
    }

    public KeyguardZenAlarmViewController(Context context, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, ZenModeController zenModeController, AlarmManager alarmManager, NextAlarmController nextAlarmController, Handler handler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bcSmartspaceDataPlugin, "plugin");
        Intrinsics.checkNotNullParameter(zenModeController, "zenModeController");
        Intrinsics.checkNotNullParameter(alarmManager, "alarmManager");
        Intrinsics.checkNotNullParameter(nextAlarmController, "nextAlarmController");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.context = context;
        this.plugin = bcSmartspaceDataPlugin;
        this.zenModeController = zenModeController;
        this.alarmManager = alarmManager;
        this.nextAlarmController = nextAlarmController;
        this.handler = handler;
        this.alarmImage = context.getResources().getDrawable(R$drawable.ic_access_alarms_big, null);
    }

    public final void setSmartspaceView(BcSmartspaceDataPlugin.SmartspaceView smartspaceView) {
        this.smartspaceView = smartspaceView;
    }

    /* compiled from: KeyguardZenAlarmViewController.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void init() {
        this.plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(this) { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$init$1
            final /* synthetic */ KeyguardZenAlarmViewController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                Intrinsics.checkNotNullParameter(view, "v");
                this.this$0.setSmartspaceView((BcSmartspaceDataPlugin.SmartspaceView) view);
                KeyguardZenAlarmViewController.access$getZenModeController$p(this.this$0).addCallback(KeyguardZenAlarmViewController.access$getZenModeCallback$p(this.this$0));
                KeyguardZenAlarmViewController.access$getNextAlarmController$p(this.this$0).addCallback(KeyguardZenAlarmViewController.access$getNextAlarmCallback$p(this.this$0));
                KeyguardZenAlarmViewController.access$refresh(this.this$0);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                Intrinsics.checkNotNullParameter(view, "v");
                this.this$0.setSmartspaceView(null);
                KeyguardZenAlarmViewController.access$getZenModeController$p(this.this$0).removeCallback(KeyguardZenAlarmViewController.access$getZenModeCallback$p(this.this$0));
                KeyguardZenAlarmViewController.access$getNextAlarmController$p(this.this$0).removeCallback(KeyguardZenAlarmViewController.access$getNextAlarmCallback$p(this.this$0));
            }
        });
        updateNextAlarm();
    }

    public final void refresh() {
        updateDnd();
        updateNextAlarm();
    }

    private final Drawable loadDndImage() {
        Drawable drawable = this.context.getResources().getDrawable(R$drawable.stat_sys_dnd, null);
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.InsetDrawable");
        Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
        Intrinsics.checkNotNullExpressionValue(drawable2, "withInsets.getDrawable()");
        return drawable2;
    }

    @VisibleForTesting
    public final void updateDnd() {
        if (this.zenModeController.getZen() != 0) {
            String string = this.context.getResources().getString(R$string.accessibility_quick_settings_dnd);
            BcSmartspaceDataPlugin.SmartspaceView smartspaceView = this.smartspaceView;
            if (smartspaceView != null) {
                smartspaceView.setDnd(this.dndImage, string);
                return;
            }
            return;
        }
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 = this.smartspaceView;
        if (smartspaceView2 != null) {
            smartspaceView2.setDnd(null, null);
        }
    }

    public final void updateNextAlarm() {
        this.alarmManager.cancel(new AlarmManager.OnAlarmListener((Function0) this.showNextAlarm) { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$sam$android_app_AlarmManager_OnAlarmListener$0
            private final /* synthetic */ Function0 function;

            /* access modifiers changed from: package-private */
            {
                this.function = r1;
            }

            @Override // android.app.AlarmManager.OnAlarmListener
            public final /* synthetic */ void onAlarm() {
                this.function.invoke();
            }
        });
        long nextAlarm = this.zenModeController.getNextAlarm();
        if (nextAlarm > 0) {
            long millis = nextAlarm - TimeUnit.HOURS.toMillis(12);
            if (millis > 0) {
                this.alarmManager.setExact(1, millis, "lock_screen_next_alarm", new AlarmManager.OnAlarmListener((Function0) this.showNextAlarm) { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$sam$android_app_AlarmManager_OnAlarmListener$0
                    private final /* synthetic */ Function0 function;

                    /* access modifiers changed from: package-private */
                    {
                        this.function = r1;
                    }

                    @Override // android.app.AlarmManager.OnAlarmListener
                    public final /* synthetic */ void onAlarm() {
                        this.function.invoke();
                    }
                }, this.handler);
            }
        }
        showAlarm();
    }

    @VisibleForTesting
    public final void showAlarm() {
        long nextAlarm = this.zenModeController.getNextAlarm();
        if (nextAlarm <= 0 || !withinNHours(nextAlarm, 12)) {
            BcSmartspaceDataPlugin.SmartspaceView smartspaceView = this.smartspaceView;
            if (smartspaceView != null) {
                smartspaceView.setNextAlarm(null, null);
                return;
            }
            return;
        }
        String obj = DateFormat.format(DateFormat.is24HourFormat(this.context, ActivityManager.getCurrentUser()) ? "HH:mm" : "h:mm", nextAlarm).toString();
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 = this.smartspaceView;
        if (smartspaceView2 != null) {
            smartspaceView2.setNextAlarm(this.alarmImage, obj);
        }
    }

    private final boolean withinNHours(long j, long j2) {
        return j <= System.currentTimeMillis() + TimeUnit.HOURS.toMillis(j2);
    }
}
