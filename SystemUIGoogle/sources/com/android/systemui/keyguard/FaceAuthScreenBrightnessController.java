package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.MathUtils;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dumpable;
import com.android.systemui.R$drawable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SystemSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FaceAuthScreenBrightnessController.kt */
/* loaded from: classes.dex */
public class FaceAuthScreenBrightnessController implements Dumpable {
    private final long brightnessAnimationDuration;
    private ValueAnimator brightnessAnimator;
    private final DumpManager dumpManager;
    private final boolean enabled;
    private final GlobalSettings globalSettings;
    private final FaceAuthScreenBrightnessController$keyguardUpdateCallback$1 keyguardUpdateCallback;
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final Handler mainHandler;
    private final float maxScreenBrightness;
    private final float maxScrimOpacity;
    private final NotificationShadeWindowController notificationShadeWindowController;
    private boolean overridingBrightness;
    private final Resources resources;
    private final SystemSettings systemSettings;
    private boolean useFaceAuthWallpaper;
    private float userDefinedBrightness = 1.0f;
    private View whiteOverlay;

    @VisibleForTesting
    public static /* synthetic */ void getUseFaceAuthWallpaper$annotations() {
    }

    public FaceAuthScreenBrightnessController(NotificationShadeWindowController notificationShadeWindowController, KeyguardUpdateMonitor keyguardUpdateMonitor, Resources resources, GlobalSettings globalSettings, SystemSettings systemSettings, Handler handler, DumpManager dumpManager, boolean z) {
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(resources, "resources");
        Intrinsics.checkNotNullParameter(globalSettings, "globalSettings");
        Intrinsics.checkNotNullParameter(systemSettings, "systemSettings");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.resources = resources;
        this.globalSettings = globalSettings;
        this.systemSettings = systemSettings;
        this.mainHandler = handler;
        this.dumpManager = dumpManager;
        this.enabled = z;
        this.useFaceAuthWallpaper = globalSettings.getInt("sysui.use_face_auth_wallpaper", FaceAuthScreenBrightnessControllerKt.getDEFAULT_USE_FACE_WALLPAPER() ? 1 : 0) != 1 ? false : true;
        this.brightnessAnimationDuration = globalSettings.getLong("sysui.face_brightness_anim_duration", FaceAuthScreenBrightnessControllerKt.getDEFAULT_ANIMATION_DURATION());
        this.maxScreenBrightness = ((float) globalSettings.getInt("sysui.face_max_brightness", FaceAuthScreenBrightnessControllerKt.getMAX_SCREEN_BRIGHTNESS())) / 100.0f;
        this.maxScrimOpacity = ((float) globalSettings.getInt("sysui.face_max_scrim_opacity", FaceAuthScreenBrightnessControllerKt.getMAX_SCRIM_OPACTY())) / 100.0f;
        this.keyguardUpdateCallback = new FaceAuthScreenBrightnessController$keyguardUpdateCallback$1(this);
    }

    public final boolean getUseFaceAuthWallpaper() {
        return this.useFaceAuthWallpaper;
    }

    public final void setOverridingBrightness(boolean z) {
        if (this.overridingBrightness != z) {
            this.overridingBrightness = z;
            ValueAnimator valueAnimator = this.brightnessAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (!z) {
                this.notificationShadeWindowController.setFaceAuthDisplayBrightness(-1.0f);
                View view = this.whiteOverlay;
                if (view == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                    throw null;
                } else if (view.getAlpha() > 0.0f) {
                    View view2 = this.whiteOverlay;
                    if (view2 != null) {
                        ValueAnimator createAnimator = createAnimator(view2.getAlpha(), 0.0f);
                        createAnimator.setDuration(200L);
                        createAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$1$1
                            final /* synthetic */ FaceAuthScreenBrightnessController this$0;

                            /* access modifiers changed from: package-private */
                            {
                                this.this$0 = r1;
                            }

                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                View view3 = this.this$0.whiteOverlay;
                                if (view3 != null) {
                                    Object animatedValue = valueAnimator2.getAnimatedValue();
                                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                                    view3.setAlpha(((Float) animatedValue).floatValue());
                                    return;
                                }
                                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                                throw null;
                            }
                        });
                        createAnimator.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$1$2
                            final /* synthetic */ FaceAuthScreenBrightnessController this$0;

                            /* access modifiers changed from: package-private */
                            {
                                this.this$0 = r1;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                View view3 = this.this$0.whiteOverlay;
                                if (view3 != null) {
                                    view3.setVisibility(4);
                                    this.this$0.brightnessAnimator = null;
                                    return;
                                }
                                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                                throw null;
                            }
                        });
                        createAnimator.start();
                        Unit unit = Unit.INSTANCE;
                        this.brightnessAnimator = createAnimator;
                        return;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                    throw null;
                }
            } else {
                float max = Float.max(this.maxScreenBrightness, this.userDefinedBrightness);
                View view3 = this.whiteOverlay;
                if (view3 != null) {
                    view3.setVisibility(0);
                    ValueAnimator createAnimator2 = createAnimator(0.0f, 1.0f);
                    createAnimator2.setDuration(this.brightnessAnimationDuration);
                    createAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, max) { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$2$1
                        final /* synthetic */ float $targetBrightness;
                        final /* synthetic */ FaceAuthScreenBrightnessController this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                            this.$targetBrightness = r2;
                        }

                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            Object animatedValue = valueAnimator2.getAnimatedValue();
                            Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                            float floatValue = ((Float) animatedValue).floatValue();
                            float constrainedMap = MathUtils.constrainedMap(this.this$0.userDefinedBrightness, this.$targetBrightness, 0.0f, 0.5f, floatValue);
                            float constrainedMap2 = MathUtils.constrainedMap(0.0f, this.this$0.maxScrimOpacity, 0.5f, 1.0f, floatValue);
                            this.this$0.notificationShadeWindowController.setFaceAuthDisplayBrightness(constrainedMap);
                            View view4 = this.this$0.whiteOverlay;
                            if (view4 != null) {
                                view4.setAlpha(constrainedMap2);
                            } else {
                                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                                throw null;
                            }
                        }
                    });
                    createAnimator2.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$2$2
                        final /* synthetic */ FaceAuthScreenBrightnessController this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            this.this$0.brightnessAnimator = null;
                        }
                    });
                    createAnimator2.start();
                    Unit unit2 = Unit.INSTANCE;
                    this.brightnessAnimator = createAnimator2;
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                throw null;
            }
        }
    }

    @VisibleForTesting
    public ValueAnimator createAnimator(float f, float f2) {
        return ValueAnimator.ofFloat(f, f2);
    }

    public final Bitmap getFaceAuthWallpaper() {
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        if (!this.useFaceAuthWallpaper || !this.keyguardUpdateMonitor.isFaceAuthEnabledForUser(currentUser)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeResource(this.resources, R$drawable.face_auth_wallpaper, options);
    }

    public final void attach(View view) {
        Intrinsics.checkNotNullParameter(view, "overlayView");
        this.whiteOverlay = view;
        if (view != null) {
            view.setFocusable(8);
            View view2 = this.whiteOverlay;
            if (view2 != null) {
                view2.setBackground(new ColorDrawable(-1));
                View view3 = this.whiteOverlay;
                if (view3 != null) {
                    view3.setEnabled(false);
                    View view4 = this.whiteOverlay;
                    if (view4 != null) {
                        view4.setAlpha(0.0f);
                        View view5 = this.whiteOverlay;
                        if (view5 != null) {
                            view5.setVisibility(4);
                            DumpManager dumpManager = this.dumpManager;
                            String name = FaceAuthScreenBrightnessController.class.getName();
                            Intrinsics.checkNotNullExpressionValue(name, "this.javaClass.name");
                            dumpManager.registerDumpable(name, this);
                            this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateCallback);
                            this.systemSettings.registerContentObserver("screen_brightness_float", new ContentObserver(this, this.mainHandler) { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$attach$1
                                final /* synthetic */ FaceAuthScreenBrightnessController this$0;

                                /* access modifiers changed from: package-private */
                                {
                                    this.this$0 = r1;
                                }

                                @Override // android.database.ContentObserver
                                public void onChange(boolean z) {
                                    FaceAuthScreenBrightnessController faceAuthScreenBrightnessController = this.this$0;
                                    FaceAuthScreenBrightnessController.access$setUserDefinedBrightness$p(faceAuthScreenBrightnessController, FaceAuthScreenBrightnessController.access$getSystemSettings$p(faceAuthScreenBrightnessController).getFloat("screen_brightness_float"));
                                }
                            });
                            this.userDefinedBrightness = this.systemSettings.getFloat("screen_brightness_float", 1.0f);
                            return;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                        throw null;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
        throw null;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println(Intrinsics.stringPlus("overridingBrightness: ", Boolean.valueOf(this.overridingBrightness)));
        printWriter.println(Intrinsics.stringPlus("useFaceAuthWallpaper: ", Boolean.valueOf(getUseFaceAuthWallpaper())));
        printWriter.println(Intrinsics.stringPlus("brightnessAnimator: ", this.brightnessAnimator));
        printWriter.println(Intrinsics.stringPlus("brightnessAnimationDuration: ", Long.valueOf(this.brightnessAnimationDuration)));
        printWriter.println(Intrinsics.stringPlus("maxScreenBrightness: ", Float.valueOf(this.maxScreenBrightness)));
        printWriter.println(Intrinsics.stringPlus("userDefinedBrightness: ", Float.valueOf(this.userDefinedBrightness)));
        printWriter.println(Intrinsics.stringPlus("enabled: ", Boolean.valueOf(this.enabled)));
    }
}
