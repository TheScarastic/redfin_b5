package com.android.systemui.statusbar.charging;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: WiredChargingRippleController.kt */
/* loaded from: classes.dex */
public final class WiredChargingRippleController {
    private final Context context;
    private int debounceLevel;
    private Long lastTriggerTime;
    private float normalizedPortPosX;
    private float normalizedPortPosY;
    private Boolean pluggedIn;
    private final boolean rippleEnabled;
    private ChargingRippleView rippleView;
    private final SystemClock systemClock;
    private final UiEventLogger uiEventLogger;
    private final WindowManager.LayoutParams windowLayoutParams;
    private final WindowManager windowManager;

    @VisibleForTesting
    public static /* synthetic */ void getRippleView$annotations() {
    }

    public WiredChargingRippleController(CommandRegistry commandRegistry, BatteryController batteryController, ConfigurationController configurationController, FeatureFlags featureFlags, Context context, WindowManager windowManager, SystemClock systemClock, UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(batteryController, "batteryController");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(windowManager, "windowManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.context = context;
        this.windowManager = windowManager;
        this.systemClock = systemClock;
        this.uiEventLogger = uiEventLogger;
        this.rippleEnabled = featureFlags.isChargingRippleEnabled() && !SystemProperties.getBoolean("persist.debug.suppress-charging-ripple", false);
        this.normalizedPortPosX = context.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_x);
        this.normalizedPortPosY = context.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_y);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.format = -3;
        layoutParams.type = 2006;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("Wired Charging Animation");
        layoutParams.flags = 24;
        layoutParams.setTrustedOverlay();
        Unit unit = Unit.INSTANCE;
        this.windowLayoutParams = layoutParams;
        this.rippleView = new ChargingRippleView(context, null);
        this.pluggedIn = Boolean.valueOf(batteryController.isPluggedIn());
        batteryController.addCallback(new BatteryController.BatteryStateChangeCallback(this, batteryController) { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$batteryStateChangeCallback$1
            final /* synthetic */ BatteryController $batteryController;
            final /* synthetic */ WiredChargingRippleController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$batteryController = r2;
            }

            @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
            public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
                if ((this.this$0.rippleEnabled) && !this.$batteryController.isPluggedInWireless()) {
                    Boolean bool = this.this$0.pluggedIn;
                    this.this$0.pluggedIn = Boolean.valueOf(z);
                    if ((bool == null || !bool.booleanValue()) && z) {
                        this.this$0.startRippleWithDebounce$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                    }
                }
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$configurationChangedListener$1
            final /* synthetic */ WiredChargingRippleController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                this.this$0.updateRippleColor();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onThemeChanged() {
                this.this$0.updateRippleColor();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                this.this$0.updateRippleColor();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                WiredChargingRippleController wiredChargingRippleController = this.this$0;
                wiredChargingRippleController.normalizedPortPosX = wiredChargingRippleController.context.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_x);
                WiredChargingRippleController wiredChargingRippleController2 = this.this$0;
                wiredChargingRippleController2.normalizedPortPosY = wiredChargingRippleController2.context.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_y);
            }
        });
        commandRegistry.registerCommand("charging-ripple", new Function0<Command>(this) { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController.1
            final /* synthetic */ WiredChargingRippleController this$0;

            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final Command invoke() {
                return new ChargingRippleCommand(this.this$0);
            }
        });
        updateRippleColor();
    }

    public final ChargingRippleView getRippleView() {
        return this.rippleView;
    }

    public final void startRippleWithDebounce$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        Long l = this.lastTriggerTime;
        if (l != null) {
            Intrinsics.checkNotNull(l);
            if (((double) (elapsedRealtime - l.longValue())) <= ((double) 2000) * Math.pow(2.0d, (double) this.debounceLevel)) {
                this.debounceLevel = Math.min(3, this.debounceLevel + 1);
                this.lastTriggerTime = Long.valueOf(elapsedRealtime);
            }
        }
        startRipple();
        this.debounceLevel = 0;
        this.lastTriggerTime = Long.valueOf(elapsedRealtime);
    }

    public final void startRipple() {
        if (this.rippleEnabled && !this.rippleView.getRippleInProgress() && this.rippleView.getParent() == null) {
            this.windowLayoutParams.packageName = this.context.getOpPackageName();
            this.rippleView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(this) { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1
                final /* synthetic */ WiredChargingRippleController this$0;

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view) {
                }

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view) {
                    this.this$0.layoutRipple();
                    this.this$0.getRippleView().startRipple(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0012: INVOKE  
                          (wrap: com.android.systemui.statusbar.charging.ChargingRippleView : 0x0007: INVOKE  (r3v3 com.android.systemui.statusbar.charging.ChargingRippleView A[REMOVE]) = 
                          (wrap: com.android.systemui.statusbar.charging.WiredChargingRippleController : 0x0005: IGET  (r3v2 com.android.systemui.statusbar.charging.WiredChargingRippleController A[REMOVE]) = 
                          (r2v0 'this' com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1.this$0 com.android.systemui.statusbar.charging.WiredChargingRippleController)
                         type: VIRTUAL call: com.android.systemui.statusbar.charging.WiredChargingRippleController.getRippleView():com.android.systemui.statusbar.charging.ChargingRippleView)
                          (wrap: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1 : 0x000f: CONSTRUCTOR  (r0v0 com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1 A[REMOVE]) = 
                          (wrap: com.android.systemui.statusbar.charging.WiredChargingRippleController : 0x000d: IGET  (r1v0 com.android.systemui.statusbar.charging.WiredChargingRippleController A[REMOVE]) = 
                          (r2v0 'this' com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1.this$0 com.android.systemui.statusbar.charging.WiredChargingRippleController)
                         call: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1.<init>(com.android.systemui.statusbar.charging.WiredChargingRippleController):void type: CONSTRUCTOR)
                         type: VIRTUAL call: com.android.systemui.statusbar.charging.ChargingRippleView.startRipple(java.lang.Runnable):void in method: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1.onViewAttachedToWindow(android.view.View):void, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1, state: NOT_LOADED
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
                        	... 15 more
                        */
                    /*
                        this = this;
                        com.android.systemui.statusbar.charging.WiredChargingRippleController r3 = r2.this$0
                        com.android.systemui.statusbar.charging.WiredChargingRippleController.access$layoutRipple(r3)
                        com.android.systemui.statusbar.charging.WiredChargingRippleController r3 = r2.this$0
                        com.android.systemui.statusbar.charging.ChargingRippleView r3 = r3.getRippleView()
                        com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1 r0 = new com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1
                        com.android.systemui.statusbar.charging.WiredChargingRippleController r1 = r2.this$0
                        r0.<init>(r1)
                        r3.startRipple(r0)
                        com.android.systemui.statusbar.charging.WiredChargingRippleController r3 = r2.this$0
                        com.android.systemui.statusbar.charging.ChargingRippleView r3 = r3.getRippleView()
                        r3.removeOnAttachStateChangeListener(r2)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1.onViewAttachedToWindow(android.view.View):void");
                }
            });
            this.windowManager.addView(this.rippleView, this.windowLayoutParams);
            this.uiEventLogger.log(WiredChargingRippleEvent.CHARGING_RIPPLE_PLAYED);
        }
    }

    /* access modifiers changed from: private */
    public final void layoutRipple() {
        PointF pointF;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.context.getDisplay().getRealMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        this.rippleView.setRadius((float) Integer.max(i, i2));
        ChargingRippleView chargingRippleView = this.rippleView;
        int rotation = RotationUtils.getRotation(this.context);
        if (rotation == 1) {
            pointF = new PointF(((float) i) * this.normalizedPortPosY, ((float) i2) * (((float) 1) - this.normalizedPortPosX));
        } else if (rotation == 2) {
            float f = (float) 1;
            pointF = new PointF(((float) i) * (f - this.normalizedPortPosX), ((float) i2) * (f - this.normalizedPortPosY));
        } else if (rotation != 3) {
            pointF = new PointF(((float) i) * this.normalizedPortPosX, ((float) i2) * this.normalizedPortPosY);
        } else {
            pointF = new PointF(((float) i) * (((float) 1) - this.normalizedPortPosY), ((float) i2) * this.normalizedPortPosX);
        }
        chargingRippleView.setOrigin(pointF);
    }

    /* access modifiers changed from: private */
    public final void updateRippleColor() {
        this.rippleView.setColor(Utils.getColorAttr(this.context, 16843829).getDefaultColor());
    }

    /* compiled from: WiredChargingRippleController.kt */
    /* loaded from: classes.dex */
    public final class ChargingRippleCommand implements Command {
        final /* synthetic */ WiredChargingRippleController this$0;

        /* JADX WARN: Incorrect args count in method signature: ()V */
        public ChargingRippleCommand(WiredChargingRippleController wiredChargingRippleController) {
            Intrinsics.checkNotNullParameter(wiredChargingRippleController, "this$0");
            this.this$0 = wiredChargingRippleController;
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            this.this$0.startRipple();
        }
    }

    /* compiled from: WiredChargingRippleController.kt */
    /* loaded from: classes.dex */
    public enum WiredChargingRippleEvent implements UiEventLogger.UiEventEnum {
        CHARGING_RIPPLE_PLAYED(829);
        
        private final int _id;

        WiredChargingRippleEvent(int i) {
            this._id = i;
        }

        public int getId() {
            return this._id;
        }
    }
}
