package com.android.systemui.volume;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.media.AudioSystem;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;
import com.android.settingslib.Utils;
import com.android.settingslib.volume.Util;
import com.android.systemui.Dependency;
import com.android.systemui.Prefs;
import com.android.systemui.R$bool;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.AlphaTintDrawableWrapper;
import com.android.systemui.util.RoundedCornerProgressDrawable;
import com.android.systemui.volume.CaptionsToggleImageButton;
import com.android.systemui.volume.VolumeDialogImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class VolumeDialogImpl implements VolumeDialog, ConfigurationController.ConfigurationListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    private static final String TAG = Util.logTag(VolumeDialogImpl.class);
    private int mActiveStream;
    private final ActivityManager mActivityManager;
    private final boolean mChangeVolumeRowTintWhenInactive;
    private ConfigurableTexts mConfigurableTexts;
    private final Context mContext;
    private Consumer<Boolean> mCrossWindowBlurEnabledListener;
    private CustomDialog mDialog;
    private int mDialogCornerRadius;
    private final int mDialogHideAnimationDurationMs;
    private ViewGroup mDialogRowsView;
    private BackgroundBlurDrawable mDialogRowsViewBackground;
    private ViewGroup mDialogRowsViewContainer;
    private final int mDialogShowAnimationDurationMs;
    private ViewGroup mDialogView;
    private int mDialogWidth;
    private boolean mHasSeenODICaptionsTooltip;
    private final KeyguardManager mKeyguard;
    private CaptionsToggleImageButton mODICaptionsIcon;
    private ViewStub mODICaptionsTooltipViewStub;
    private ViewGroup mODICaptionsView;
    private int mPrevActiveStream;
    private ViewGroup mRinger;
    private View mRingerAndDrawerContainer;
    private Drawable mRingerAndDrawerContainerBackground;
    private int mRingerCount;
    private ViewGroup mRingerDrawerContainer;
    private ImageView mRingerDrawerIconAnimatingDeselected;
    private ImageView mRingerDrawerIconAnimatingSelected;
    private int mRingerDrawerItemSize;
    private ViewGroup mRingerDrawerMute;
    private ImageView mRingerDrawerMuteIcon;
    private ViewGroup mRingerDrawerNewSelectionBg;
    private ViewGroup mRingerDrawerNormal;
    private ImageView mRingerDrawerNormalIcon;
    private ViewGroup mRingerDrawerVibrate;
    private ImageView mRingerDrawerVibrateIcon;
    private ImageButton mRingerIcon;
    private int mRingerRowsPadding;
    private SafetyWarningDialog mSafetyWarning;
    private ViewGroup mSelectedRingerContainer;
    private ImageView mSelectedRingerIcon;
    private ImageButton mSettingsIcon;
    private View mSettingsView;
    private boolean mShowA11yStream;
    private final boolean mShowLowMediaVolumeIcon;
    private boolean mShowVibrate;
    private boolean mShowing;
    private VolumeDialogController.State mState;
    private View mTopContainer;
    private final boolean mUseBackgroundBlur;
    private Window mWindow;
    private FrameLayout mZenIcon;
    private final H mHandler = new H();
    private final Region mTouchableRegion = new Region();
    private final ValueAnimator mRingerDrawerIconColorAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
    private final ValueAnimator mAnimateUpBackgroundToMatchDrawer = ValueAnimator.ofFloat(1.0f, 0.0f);
    private boolean mIsRingerDrawerOpen = false;
    private float mRingerDrawerClosedAmount = 1.0f;
    private final List<VolumeRow> mRows = new ArrayList();
    private final SparseBooleanArray mDynamic = new SparseBooleanArray();
    private final Object mSafetyWarningLock = new Object();
    private final Accessibility mAccessibility = new Accessibility();
    private boolean mAutomute = true;
    private boolean mSilentMode = true;
    private boolean mHovering = false;
    private boolean mConfigChanged = false;
    private boolean mIsAnimatingDismiss = false;
    private View mODICaptionsTooltipView = null;
    private final VolumeDialogController.Callbacks mControllerCallbackH = new VolumeDialogController.Callbacks() { // from class: com.android.systemui.volume.VolumeDialogImpl.6
        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onShowRequested(int i) {
            VolumeDialogImpl.this.showH(i);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onDismissRequested(int i) {
            VolumeDialogImpl.this.dismissH(i);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onScreenOff() {
            VolumeDialogImpl.this.dismissH(4);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onStateChanged(VolumeDialogController.State state) {
            VolumeDialogImpl.this.onStateChangedH(state);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onLayoutDirectionChanged(int i) {
            VolumeDialogImpl.this.mDialogView.setLayoutDirection(i);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onConfigurationChanged() {
            VolumeDialogImpl.this.mDialog.dismiss();
            VolumeDialogImpl.this.mConfigChanged = true;
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onShowVibrateHint() {
            if (VolumeDialogImpl.this.mSilentMode) {
                VolumeDialogImpl.this.mController.setRingerMode(0, false);
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onShowSilentHint() {
            if (VolumeDialogImpl.this.mSilentMode) {
                VolumeDialogImpl.this.mController.setRingerMode(2, false);
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onShowSafetyWarning(int i) {
            VolumeDialogImpl.this.showSafetyWarningH(i);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onAccessibilityModeChanged(Boolean bool) {
            VolumeDialogImpl.this.mShowA11yStream = bool == null ? false : bool.booleanValue();
            VolumeRow activeRow = VolumeDialogImpl.this.getActiveRow();
            if (VolumeDialogImpl.this.mShowA11yStream || 10 != activeRow.stream) {
                VolumeDialogImpl.this.updateRowsH(activeRow);
            } else {
                VolumeDialogImpl.this.dismissH(7);
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public void onCaptionComponentStateChanged(Boolean bool, Boolean bool2) {
            VolumeDialogImpl.this.updateODICaptionsH(bool.booleanValue(), bool2.booleanValue());
        }
    };
    private final VolumeDialogController mController = (VolumeDialogController) Dependency.get(VolumeDialogController.class);
    private final AccessibilityManagerWrapper mAccessibilityMgr = (AccessibilityManagerWrapper) Dependency.get(AccessibilityManagerWrapper.class);
    private final DeviceProvisionedController mDeviceProvisionedController = (DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class);
    private boolean mShowActiveStreamOnly = showActiveStreamOnly();

    public VolumeDialogImpl(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R$style.volume_dialog_theme);
        this.mContext = contextThemeWrapper;
        this.mKeyguard = (KeyguardManager) contextThemeWrapper.getSystemService("keyguard");
        this.mActivityManager = (ActivityManager) contextThemeWrapper.getSystemService("activity");
        this.mHasSeenODICaptionsTooltip = Prefs.getBoolean(context, "HasSeenODICaptionsTooltip", false);
        this.mShowLowMediaVolumeIcon = contextThemeWrapper.getResources().getBoolean(R$bool.config_showLowMediaVolumeIcon);
        this.mChangeVolumeRowTintWhenInactive = contextThemeWrapper.getResources().getBoolean(R$bool.config_changeVolumeRowTintWhenInactive);
        this.mDialogShowAnimationDurationMs = contextThemeWrapper.getResources().getInteger(R$integer.config_dialogShowAnimationDurationMs);
        this.mDialogHideAnimationDurationMs = contextThemeWrapper.getResources().getInteger(R$integer.config_dialogHideAnimationDurationMs);
        boolean z = contextThemeWrapper.getResources().getBoolean(R$bool.config_volumeDialogUseBackgroundBlur);
        this.mUseBackgroundBlur = z;
        if (z) {
            this.mCrossWindowBlurEnabledListener = new Consumer(contextThemeWrapper.getColor(R$color.volume_dialog_background_color_above_blur), contextThemeWrapper.getColor(R$color.volume_dialog_background_color)) { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda23
                public final /* synthetic */ int f$1;
                public final /* synthetic */ int f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    VolumeDialogImpl.$r8$lambda$HyW6tDngDyRrnxkCTf2DikvsSeQ(VolumeDialogImpl.this, this.f$1, this.f$2, (Boolean) obj);
                }
            };
        }
        initDimens();
    }

    public /* synthetic */ void lambda$new$0(int i, int i2, Boolean bool) {
        BackgroundBlurDrawable backgroundBlurDrawable = this.mDialogRowsViewBackground;
        if (!bool.booleanValue()) {
            i = i2;
        }
        backgroundBlurDrawable.setColor(i);
        this.mDialogRowsView.invalidate();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onUiModeChanged() {
        this.mContext.getTheme().applyStyle(this.mContext.getThemeResId(), true);
    }

    @Override // com.android.systemui.plugins.VolumeDialog
    public void init(int i, VolumeDialog.Callback callback) {
        initDialog();
        this.mAccessibility.init();
        this.mController.addCallback(this.mControllerCallbackH, this.mHandler);
        this.mController.getState();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
    }

    @Override // com.android.systemui.plugins.VolumeDialog
    public void destroy() {
        this.mController.removeCallback(this.mControllerCallbackH);
        this.mHandler.removeCallbacksAndMessages(null);
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).removeCallback(this);
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        this.mTouchableRegion.setEmpty();
        for (int i = 0; i < this.mDialogView.getChildCount(); i++) {
            unionViewBoundstoTouchableRegion(this.mDialogView.getChildAt(i));
        }
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            unionViewBoundstoTouchableRegion(this.mODICaptionsTooltipView);
        }
        internalInsetsInfo.touchableRegion.set(this.mTouchableRegion);
    }

    private void unionViewBoundstoTouchableRegion(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        float f = (float) iArr[0];
        float f2 = (float) iArr[1];
        if (view == this.mTopContainer && !this.mIsRingerDrawerOpen) {
            if (!isLandscape()) {
                f2 += (float) getRingerDrawerOpenExtraSize();
            } else {
                f += (float) getRingerDrawerOpenExtraSize();
            }
        }
        this.mTouchableRegion.op((int) f, (int) f2, iArr[0] + view.getWidth(), iArr[1] + view.getHeight(), Region.Op.UNION);
    }

    private void initDialog() {
        this.mDialog = new CustomDialog(this.mContext);
        initDimens();
        this.mConfigurableTexts = new ConfigurableTexts(this.mContext);
        this.mHovering = false;
        this.mShowing = false;
        Window window = this.mDialog.getWindow();
        this.mWindow = window;
        window.requestFeature(1);
        this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mWindow.clearFlags(65538);
        this.mWindow.addFlags(17563688);
        this.mWindow.addPrivateFlags(536870912);
        this.mWindow.setType(2020);
        this.mWindow.setWindowAnimations(16973828);
        WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
        attributes.format = -3;
        attributes.setTitle(VolumeDialogImpl.class.getSimpleName());
        attributes.windowAnimations = -1;
        attributes.gravity = this.mContext.getResources().getInteger(R$integer.volume_dialog_gravity);
        this.mWindow.setAttributes(attributes);
        this.mWindow.setLayout(-2, -2);
        this.mDialog.setContentView(R$layout.volume_dialog);
        ViewGroup viewGroup = (ViewGroup) this.mDialog.findViewById(R$id.volume_dialog);
        this.mDialogView = viewGroup;
        viewGroup.setAlpha(0.0f);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                VolumeDialogImpl.$r8$lambda$MT7i0PacQs98wqOZ6XfY5A8Vbt0(VolumeDialogImpl.this, dialogInterface);
            }
        });
        this.mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                VolumeDialogImpl.$r8$lambda$DyaX8F4M8jXEGpG9YWlR2LDbwRY(VolumeDialogImpl.this, dialogInterface);
            }
        });
        this.mDialogView.setOnHoverListener(new View.OnHoverListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda9
            @Override // android.view.View.OnHoverListener
            public final boolean onHover(View view, MotionEvent motionEvent) {
                return VolumeDialogImpl.$r8$lambda$Rva1HQIzAGbdWvqSNkfUnj9EowY(VolumeDialogImpl.this, view, motionEvent);
            }
        });
        this.mDialogRowsView = (ViewGroup) this.mDialog.findViewById(R$id.volume_dialog_rows);
        if (this.mUseBackgroundBlur) {
            this.mDialogView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.volume.VolumeDialogImpl.1
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().addCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                    VolumeDialogImpl.this.mDialogRowsViewBackground = view.getViewRootImpl().createBackgroundBlurDrawable();
                    Resources resources = VolumeDialogImpl.this.mContext.getResources();
                    VolumeDialogImpl.this.mDialogRowsViewBackground.setCornerRadius((float) VolumeDialogImpl.this.mContext.getResources().getDimensionPixelSize(Utils.getThemeAttr(VolumeDialogImpl.this.mContext, 16844145)));
                    VolumeDialogImpl.this.mDialogRowsViewBackground.setBlurRadius(resources.getDimensionPixelSize(R$dimen.volume_dialog_background_blur_radius));
                    VolumeDialogImpl.this.mDialogRowsView.setBackground(VolumeDialogImpl.this.mDialogRowsViewBackground);
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().removeCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                }
            });
        }
        this.mDialogRowsViewContainer = (ViewGroup) this.mDialogView.findViewById(R$id.volume_dialog_rows_container);
        this.mTopContainer = this.mDialogView.findViewById(R$id.volume_dialog_top_container);
        View findViewById = this.mDialogView.findViewById(R$id.volume_ringer_and_drawer_container);
        this.mRingerAndDrawerContainer = findViewById;
        if (findViewById != null) {
            if (isLandscape()) {
                View view = this.mRingerAndDrawerContainer;
                view.setPadding(view.getPaddingLeft(), this.mRingerAndDrawerContainer.getPaddingTop(), this.mRingerAndDrawerContainer.getPaddingRight(), this.mRingerRowsPadding);
                this.mRingerAndDrawerContainer.setBackgroundDrawable(this.mContext.getDrawable(R$drawable.volume_background_top_rounded));
            }
            this.mRingerAndDrawerContainer.post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl.m409$r8$lambda$DfKK2jL2sUGMWDNCeYERB7B1xw(VolumeDialogImpl.this);
                }
            });
        }
        ViewGroup viewGroup2 = (ViewGroup) this.mDialog.findViewById(R$id.ringer);
        this.mRinger = viewGroup2;
        if (viewGroup2 != null) {
            this.mRingerIcon = (ImageButton) viewGroup2.findViewById(R$id.ringer_icon);
            this.mZenIcon = (FrameLayout) this.mRinger.findViewById(R$id.dnd_icon);
        }
        this.mSelectedRingerIcon = (ImageView) this.mDialog.findViewById(R$id.volume_new_ringer_active_icon);
        this.mSelectedRingerContainer = (ViewGroup) this.mDialog.findViewById(R$id.volume_new_ringer_active_icon_container);
        this.mRingerDrawerMute = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_mute);
        this.mRingerDrawerNormal = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_normal);
        this.mRingerDrawerVibrate = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_vibrate);
        this.mRingerDrawerMuteIcon = (ImageView) this.mDialog.findViewById(R$id.volume_drawer_mute_icon);
        this.mRingerDrawerVibrateIcon = (ImageView) this.mDialog.findViewById(R$id.volume_drawer_vibrate_icon);
        this.mRingerDrawerNormalIcon = (ImageView) this.mDialog.findViewById(R$id.volume_drawer_normal_icon);
        this.mRingerDrawerNewSelectionBg = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_selection_background);
        setupRingerDrawer();
        ViewGroup viewGroup3 = (ViewGroup) this.mDialog.findViewById(R$id.odi_captions);
        this.mODICaptionsView = viewGroup3;
        if (viewGroup3 != null) {
            this.mODICaptionsIcon = (CaptionsToggleImageButton) viewGroup3.findViewById(R$id.odi_captions_icon);
        }
        ViewStub viewStub = (ViewStub) this.mDialog.findViewById(R$id.odi_captions_tooltip_stub);
        this.mODICaptionsTooltipViewStub = viewStub;
        if (this.mHasSeenODICaptionsTooltip && viewStub != null) {
            this.mDialogView.removeView(viewStub);
            this.mODICaptionsTooltipViewStub = null;
        }
        this.mSettingsView = this.mDialog.findViewById(R$id.settings_container);
        this.mSettingsIcon = (ImageButton) this.mDialog.findViewById(R$id.settings);
        if (this.mRows.isEmpty()) {
            if (!AudioSystem.isSingleVolume(this.mContext)) {
                int i = R$drawable.ic_volume_accessibility;
                addRow(10, i, i, true, false);
            }
            addRow(3, R$drawable.ic_volume_media, R$drawable.ic_volume_media_mute, true, true);
            if (!AudioSystem.isSingleVolume(this.mContext)) {
                addRow(2, R$drawable.ic_volume_ringer, R$drawable.ic_volume_ringer_mute, true, false);
                addRow(4, R$drawable.ic_alarm, R$drawable.ic_volume_alarm_mute, true, false);
                addRow(0, 17302805, 17302805, false, false);
                int i2 = R$drawable.ic_volume_bt_sco;
                addRow(6, i2, i2, false, false);
                addRow(1, R$drawable.ic_volume_system, R$drawable.ic_volume_system_mute, false, false);
            }
        } else {
            addExistingRows();
        }
        updateRowsH(getActiveRow());
        initRingerH();
        initSettingsH();
        initODICaptionsH();
    }

    public /* synthetic */ void lambda$initDialog$2(DialogInterface dialogInterface) {
        this.mDialogView.getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        if (!isLandscape()) {
            ViewGroup viewGroup = this.mDialogView;
            viewGroup.setTranslationX(((float) viewGroup.getWidth()) / 2.0f);
        }
        this.mDialogView.setAlpha(0.0f);
        this.mDialogView.animate().alpha(1.0f).translationX(0.0f).setDuration((long) this.mDialogShowAnimationDurationMs).setInterpolator(new SystemUIInterpolators$LogDecelerateInterpolator()).withEndAction(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                VolumeDialogImpl.$r8$lambda$VLEq7abVqw87obgALDHObBKHVAo(VolumeDialogImpl.this);
            }
        }).start();
    }

    public /* synthetic */ void lambda$initDialog$1() {
        ImageButton imageButton;
        if (!Prefs.getBoolean(this.mContext, "TouchedRingerToggle", false) && (imageButton = this.mRingerIcon) != null) {
            imageButton.postOnAnimationDelayed(getSinglePressFor(imageButton), 1500);
        }
    }

    public /* synthetic */ void lambda$initDialog$3(DialogInterface dialogInterface) {
        this.mDialogView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    public /* synthetic */ boolean lambda$initDialog$4(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        this.mHovering = actionMasked == 9 || actionMasked == 7;
        rescheduleTimeoutH();
        return true;
    }

    public /* synthetic */ void lambda$initDialog$5() {
        LayerDrawable layerDrawable = (LayerDrawable) this.mRingerAndDrawerContainer.getBackground();
        if (layerDrawable != null && layerDrawable.getNumberOfLayers() > 0) {
            this.mRingerAndDrawerContainerBackground = layerDrawable.getDrawable(0);
            updateBackgroundForDrawerClosedAmount();
            setTopContainerBackgroundDrawable();
        }
    }

    private void initDimens() {
        this.mDialogWidth = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_dialog_panel_width);
        this.mDialogCornerRadius = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_dialog_panel_width_half);
        this.mRingerDrawerItemSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_ringer_drawer_item_size);
        this.mRingerRowsPadding = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_dialog_ringer_rows_padding);
        boolean hasVibrator = this.mController.hasVibrator();
        this.mShowVibrate = hasVibrator;
        this.mRingerCount = hasVibrator ? 3 : 2;
    }

    private int getAlphaAttr(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{i});
        float f = obtainStyledAttributes.getFloat(0, 0.0f);
        obtainStyledAttributes.recycle();
        return (int) (f * 255.0f);
    }

    public boolean isLandscape() {
        return this.mContext.getResources().getConfiguration().orientation == 2;
    }

    private boolean isRtl() {
        return this.mContext.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public void setStreamImportant(int i, boolean z) {
        this.mHandler.obtainMessage(5, i, z ? 1 : 0).sendToTarget();
    }

    public void setAutomute(boolean z) {
        if (this.mAutomute != z) {
            this.mAutomute = z;
            this.mHandler.sendEmptyMessage(4);
        }
    }

    public void setSilentMode(boolean z) {
        if (this.mSilentMode != z) {
            this.mSilentMode = z;
            this.mHandler.sendEmptyMessage(4);
        }
    }

    private void addRow(int i, int i2, int i3, boolean z, boolean z2) {
        addRow(i, i2, i3, z, z2, false);
    }

    private void addRow(int i, int i2, int i3, boolean z, boolean z2, boolean z3) {
        if (D.BUG) {
            String str = TAG;
            Slog.d(str, "Adding row for stream " + i);
        }
        VolumeRow volumeRow = new VolumeRow();
        initRow(volumeRow, i, i2, i3, z, z2);
        this.mDialogRowsView.addView(volumeRow.view);
        this.mRows.add(volumeRow);
    }

    private void addExistingRows() {
        int size = this.mRows.size();
        for (int i = 0; i < size; i++) {
            VolumeRow volumeRow = this.mRows.get(i);
            initRow(volumeRow, volumeRow.stream, volumeRow.iconRes, volumeRow.iconMuteRes, volumeRow.important, volumeRow.defaultStream);
            this.mDialogRowsView.addView(volumeRow.view);
            updateVolumeRowH(volumeRow);
        }
    }

    public VolumeRow getActiveRow() {
        for (VolumeRow volumeRow : this.mRows) {
            if (volumeRow.stream == this.mActiveStream) {
                return volumeRow;
            }
        }
        for (VolumeRow volumeRow2 : this.mRows) {
            if (volumeRow2.stream == 3) {
                return volumeRow2;
            }
        }
        return this.mRows.get(0);
    }

    private VolumeRow findRow(int i) {
        for (VolumeRow volumeRow : this.mRows) {
            if (volumeRow.stream == i) {
                return volumeRow;
            }
        }
        return null;
    }

    public static int getImpliedLevel(SeekBar seekBar, int i) {
        int max = seekBar.getMax();
        int i2 = max / 100;
        int i3 = i2 - 1;
        if (i == 0) {
            return 0;
        }
        return i == max ? i2 : ((int) ((((float) i) / ((float) max)) * ((float) i3))) + 1;
    }

    @SuppressLint({"InflateParams"})
    private void initRow(VolumeRow volumeRow, int i, int i2, int i3, boolean z, boolean z2) {
        volumeRow.stream = i;
        volumeRow.iconRes = i2;
        volumeRow.iconMuteRes = i3;
        volumeRow.important = z;
        volumeRow.defaultStream = z2;
        AlphaTintDrawableWrapper alphaTintDrawableWrapper = null;
        volumeRow.view = this.mDialog.getLayoutInflater().inflate(R$layout.volume_dialog_row, (ViewGroup) null);
        volumeRow.view.setId(volumeRow.stream);
        volumeRow.view.setTag(volumeRow);
        volumeRow.header = (TextView) volumeRow.view.findViewById(R$id.volume_row_header);
        volumeRow.header.setId(volumeRow.stream * 20);
        if (i == 10) {
            volumeRow.header.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        }
        volumeRow.dndIcon = (FrameLayout) volumeRow.view.findViewById(R$id.dnd_icon);
        volumeRow.slider = (SeekBar) volumeRow.view.findViewById(R$id.volume_row_slider);
        volumeRow.slider.setOnSeekBarChangeListener(new VolumeSeekBarChangeListener(volumeRow));
        volumeRow.number = (TextView) volumeRow.view.findViewById(R$id.volume_number);
        volumeRow.anim = null;
        LayerDrawable layerDrawable = (LayerDrawable) this.mContext.getDrawable(R$drawable.volume_row_seekbar);
        LayerDrawable layerDrawable2 = (LayerDrawable) ((RoundedCornerProgressDrawable) layerDrawable.findDrawableByLayerId(16908301)).getDrawable();
        volumeRow.sliderProgressSolid = layerDrawable2.findDrawableByLayerId(R$id.volume_seekbar_progress_solid);
        Drawable findDrawableByLayerId = layerDrawable2.findDrawableByLayerId(R$id.volume_seekbar_progress_icon);
        if (findDrawableByLayerId != null) {
            alphaTintDrawableWrapper = (AlphaTintDrawableWrapper) ((RotateDrawable) findDrawableByLayerId).getDrawable();
        }
        volumeRow.sliderProgressIcon = alphaTintDrawableWrapper;
        volumeRow.slider.setProgressDrawable(layerDrawable);
        volumeRow.icon = (ImageButton) volumeRow.view.findViewById(R$id.volume_row_icon);
        volumeRow.setIcon(i2, this.mContext.getTheme());
        if (volumeRow.icon == null) {
            return;
        }
        if (volumeRow.stream != 10) {
            volumeRow.icon.setOnClickListener(new View.OnClickListener(volumeRow, i) { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda8
                public final /* synthetic */ VolumeDialogImpl.VolumeRow f$1;
                public final /* synthetic */ int f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VolumeDialogImpl.m415$r8$lambda$zFqww50WyVlvM05DITBmr0y_eI(VolumeDialogImpl.this, this.f$1, this.f$2, view);
                }
            });
        } else {
            volumeRow.icon.setImportantForAccessibility(2);
        }
    }

    public /* synthetic */ void lambda$initRow$6(VolumeRow volumeRow, int i, View view) {
        int i2;
        int i3 = 0;
        boolean z = true;
        Events.writeEvent(7, Integer.valueOf(volumeRow.stream), Integer.valueOf(volumeRow.iconState));
        this.mController.setActiveStream(volumeRow.stream);
        if (volumeRow.stream == 2) {
            boolean hasVibrator = this.mController.hasVibrator();
            if (this.mState.ringerModeInternal != 2) {
                this.mController.setRingerMode(2, false);
                if (volumeRow.ss.level == 0) {
                    this.mController.setStreamVolume(i, 1);
                }
            } else if (hasVibrator) {
                this.mController.setRingerMode(1, false);
            } else {
                if (volumeRow.ss.level != 0) {
                    z = false;
                }
                VolumeDialogController volumeDialogController = this.mController;
                if (z) {
                    i3 = volumeRow.lastAudibleLevel;
                }
                volumeDialogController.setStreamVolume(i, i3);
            }
        } else {
            if (volumeRow.ss.level == volumeRow.ss.levelMin) {
                i3 = 1;
            }
            VolumeDialogController volumeDialogController2 = this.mController;
            if (i3 != 0) {
                i2 = volumeRow.lastAudibleLevel;
            } else {
                i2 = volumeRow.ss.levelMin;
            }
            volumeDialogController2.setStreamVolume(i, i2);
        }
        volumeRow.userAttempt = 0;
    }

    public void setRingerMode(int i) {
        Events.writeEvent(18, Integer.valueOf(i));
        incrementManualToggleCount();
        updateRingerH();
        provideTouchFeedbackH(i);
        this.mController.setRingerMode(i, false);
        maybeShowToastH(i);
    }

    private void setupRingerDrawer() {
        ViewGroup viewGroup = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_container);
        this.mRingerDrawerContainer = viewGroup;
        if (viewGroup != null) {
            if (!this.mShowVibrate) {
                this.mRingerDrawerVibrate.setVisibility(8);
            }
            if (!isLandscape()) {
                ViewGroup viewGroup2 = this.mDialogView;
                viewGroup2.setPadding(viewGroup2.getPaddingLeft(), this.mDialogView.getPaddingTop(), this.mDialogView.getPaddingRight(), this.mDialogView.getPaddingBottom() + getRingerDrawerOpenExtraSize());
            } else {
                ViewGroup viewGroup3 = this.mDialogView;
                viewGroup3.setPadding(viewGroup3.getPaddingLeft() + getRingerDrawerOpenExtraSize(), this.mDialogView.getPaddingTop(), this.mDialogView.getPaddingRight(), this.mDialogView.getPaddingBottom());
            }
            ((LinearLayout) this.mRingerDrawerContainer.findViewById(R$id.volume_drawer_options)).setOrientation(!isLandscape());
            this.mSelectedRingerContainer.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VolumeDialogImpl.m413$r8$lambda$w_utYPPWwXqrSoO_rh01H8Vk3k(VolumeDialogImpl.this, view);
                }
            });
            this.mRingerDrawerVibrate.setOnClickListener(new RingerDrawerItemClickListener(1));
            this.mRingerDrawerMute.setOnClickListener(new RingerDrawerItemClickListener(0));
            this.mRingerDrawerNormal.setOnClickListener(new RingerDrawerItemClickListener(2));
            int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(this.mContext);
            this.mRingerDrawerIconColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(Utils.getColorAttrDefaultColor(this.mContext, 16844002), colorAccentDefaultColor) { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda1
                public final /* synthetic */ int f$1;
                public final /* synthetic */ int f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    VolumeDialogImpl.m412$r8$lambda$kKbGZepMRKoIuscA_XyzYIKj3g(VolumeDialogImpl.this, this.f$1, this.f$2, valueAnimator);
                }
            });
            this.mRingerDrawerIconColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.volume.VolumeDialogImpl.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingDeselected.clearColorFilter();
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingSelected.clearColorFilter();
                }
            });
            this.mRingerDrawerIconColorAnimator.setDuration(175L);
            this.mAnimateUpBackgroundToMatchDrawer.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    VolumeDialogImpl.$r8$lambda$uLxAk3kuBuRojTOjuEenIywoA7c(VolumeDialogImpl.this, valueAnimator);
                }
            });
        }
    }

    public /* synthetic */ void lambda$setupRingerDrawer$7(View view) {
        if (this.mIsRingerDrawerOpen) {
            hideRingerDrawer();
        } else {
            showRingerDrawer();
        }
    }

    public /* synthetic */ void lambda$setupRingerDrawer$8(int i, int i2, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
        int intValue2 = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i2), Integer.valueOf(i))).intValue();
        this.mRingerDrawerIconAnimatingDeselected.setColorFilter(intValue);
        this.mRingerDrawerIconAnimatingSelected.setColorFilter(intValue2);
    }

    public /* synthetic */ void lambda$setupRingerDrawer$9(ValueAnimator valueAnimator) {
        this.mRingerDrawerClosedAmount = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateBackgroundForDrawerClosedAmount();
    }

    public ImageView getDrawerIconViewForMode(int i) {
        if (i == 1) {
            return this.mRingerDrawerVibrateIcon;
        }
        if (i == 0) {
            return this.mRingerDrawerMuteIcon;
        }
        return this.mRingerDrawerNormalIcon;
    }

    public float getTranslationInDrawerForRingerMode(int i) {
        int i2;
        if (i == 1) {
            i2 = (-this.mRingerDrawerItemSize) * 2;
        } else if (i != 0) {
            return 0.0f;
        } else {
            i2 = -this.mRingerDrawerItemSize;
        }
        return (float) i2;
    }

    private void showRingerDrawer() {
        if (!this.mIsRingerDrawerOpen) {
            int i = 4;
            this.mRingerDrawerVibrateIcon.setVisibility(this.mState.ringerModeInternal == 1 ? 4 : 0);
            this.mRingerDrawerMuteIcon.setVisibility(this.mState.ringerModeInternal == 0 ? 4 : 0);
            ImageView imageView = this.mRingerDrawerNormalIcon;
            if (this.mState.ringerModeInternal != 2) {
                i = 0;
            }
            imageView.setVisibility(i);
            this.mRingerDrawerNewSelectionBg.setAlpha(0.0f);
            if (!isLandscape()) {
                this.mRingerDrawerNewSelectionBg.setTranslationY(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal));
            } else {
                this.mRingerDrawerNewSelectionBg.setTranslationX(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal));
            }
            if (!isLandscape()) {
                this.mRingerDrawerContainer.setTranslationY((float) (this.mRingerDrawerItemSize * (this.mRingerCount - 1)));
            } else {
                this.mRingerDrawerContainer.setTranslationX((float) (this.mRingerDrawerItemSize * (this.mRingerCount - 1)));
            }
            this.mRingerDrawerContainer.setAlpha(0.0f);
            this.mRingerDrawerContainer.setVisibility(0);
            int i2 = this.mState.ringerModeInternal == 1 ? 175 : 250;
            ViewPropertyAnimator animate = this.mRingerDrawerContainer.animate();
            Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
            long j = (long) i2;
            animate.setInterpolator(interpolator).setDuration(j).setStartDelay(this.mState.ringerModeInternal == 1 ? 75 : 0).alpha(1.0f).translationX(0.0f).translationY(0.0f).start();
            this.mSelectedRingerContainer.animate().setInterpolator(interpolator).setDuration(250).withEndAction(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl.$r8$lambda$65qX4hUSfWCslUVDsb2at6Pd8x0(VolumeDialogImpl.this);
                }
            });
            this.mAnimateUpBackgroundToMatchDrawer.setDuration(j);
            this.mAnimateUpBackgroundToMatchDrawer.setInterpolator(interpolator);
            this.mAnimateUpBackgroundToMatchDrawer.start();
            if (!isLandscape()) {
                this.mSelectedRingerContainer.animate().translationY(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal)).start();
            } else {
                this.mSelectedRingerContainer.animate().translationX(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal)).start();
            }
            this.mSelectedRingerContainer.setContentDescription(this.mContext.getString(getStringDescriptionResourceForRingerMode(this.mState.ringerModeInternal)));
            this.mIsRingerDrawerOpen = true;
        }
    }

    public /* synthetic */ void lambda$showRingerDrawer$10() {
        getDrawerIconViewForMode(this.mState.ringerModeInternal).setVisibility(0);
    }

    public void hideRingerDrawer() {
        if (this.mRingerDrawerContainer != null && this.mIsRingerDrawerOpen) {
            getDrawerIconViewForMode(this.mState.ringerModeInternal).setVisibility(4);
            this.mRingerDrawerContainer.animate().alpha(0.0f).setDuration(250).setStartDelay(0).withEndAction(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl.m414$r8$lambda$xrvBLM3Cxgthva1VPX_RpKFexo(VolumeDialogImpl.this);
                }
            });
            if (!isLandscape()) {
                this.mRingerDrawerContainer.animate().translationY((float) (this.mRingerDrawerItemSize * 2)).start();
            } else {
                this.mRingerDrawerContainer.animate().translationX((float) (this.mRingerDrawerItemSize * 2)).start();
            }
            this.mAnimateUpBackgroundToMatchDrawer.setDuration(250L);
            this.mAnimateUpBackgroundToMatchDrawer.setInterpolator(Interpolators.FAST_OUT_SLOW_IN_REVERSE);
            this.mAnimateUpBackgroundToMatchDrawer.reverse();
            this.mSelectedRingerContainer.animate().translationX(0.0f).translationY(0.0f).start();
            this.mSelectedRingerContainer.setContentDescription(this.mContext.getString(R$string.volume_ringer_change));
            this.mIsRingerDrawerOpen = false;
        }
    }

    public /* synthetic */ void lambda$hideRingerDrawer$11() {
        this.mRingerDrawerContainer.setVisibility(4);
    }

    public void initSettingsH() {
        View view = this.mSettingsView;
        if (view != null) {
            view.setVisibility((!this.mDeviceProvisionedController.isCurrentUserSetup() || this.mActivityManager.getLockTaskModeState() != 0) ? 8 : 0);
        }
        ImageButton imageButton = this.mSettingsIcon;
        if (imageButton != null) {
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    VolumeDialogImpl.$r8$lambda$OAcEdErP9Giyy2okZIuK7beoZ28(VolumeDialogImpl.this, view2);
                }
            });
        }
    }

    public /* synthetic */ void lambda$initSettingsH$12(View view) {
        Events.writeEvent(8, new Object[0]);
        Intent intent = new Intent("android.settings.panel.action.VOLUME");
        dismissH(5);
        ((MediaOutputDialogFactory) Dependency.get(MediaOutputDialogFactory.class)).dismiss();
        ((ActivityStarter) Dependency.get(ActivityStarter.class)).startActivity(intent, true);
    }

    public void initRingerH() {
        ImageButton imageButton = this.mRingerIcon;
        if (imageButton != null) {
            imageButton.setAccessibilityLiveRegion(1);
            this.mRingerIcon.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VolumeDialogImpl.m410$r8$lambda$HkX5nasoBh2AJxzMUPz19gEhfw(VolumeDialogImpl.this, view);
                }
            });
        }
        updateRingerH();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0023, code lost:
        if (r2 != false) goto L_0x0034;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void lambda$initRingerH$13(android.view.View r6) {
        /*
            r5 = this;
            android.content.Context r6 = r5.mContext
            java.lang.String r0 = "TouchedRingerToggle"
            r1 = 1
            com.android.systemui.Prefs.putBoolean(r6, r0, r1)
            com.android.systemui.plugins.VolumeDialogController$State r6 = r5.mState
            android.util.SparseArray<com.android.systemui.plugins.VolumeDialogController$StreamState> r6 = r6.states
            r0 = 2
            java.lang.Object r6 = r6.get(r0)
            com.android.systemui.plugins.VolumeDialogController$StreamState r6 = (com.android.systemui.plugins.VolumeDialogController.StreamState) r6
            if (r6 != 0) goto L_0x0016
            return
        L_0x0016:
            com.android.systemui.plugins.VolumeDialogController r2 = r5.mController
            boolean r2 = r2.hasVibrator()
            com.android.systemui.plugins.VolumeDialogController$State r3 = r5.mState
            int r3 = r3.ringerModeInternal
            r4 = 0
            if (r3 != r0) goto L_0x0026
            if (r2 == 0) goto L_0x0028
            goto L_0x0034
        L_0x0026:
            if (r3 != r1) goto L_0x002a
        L_0x0028:
            r1 = r4
            goto L_0x0034
        L_0x002a:
            int r6 = r6.level
            if (r6 != 0) goto L_0x0033
            com.android.systemui.plugins.VolumeDialogController r6 = r5.mController
            r6.setStreamVolume(r0, r1)
        L_0x0033:
            r1 = r0
        L_0x0034:
            r5.setRingerMode(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.lambda$initRingerH$13(android.view.View):void");
    }

    private void initODICaptionsH() {
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        if (captionsToggleImageButton != null) {
            captionsToggleImageButton.setOnConfirmedTapListener(new CaptionsToggleImageButton.ConfirmedTapListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10
                @Override // com.android.systemui.volume.CaptionsToggleImageButton.ConfirmedTapListener
                public final void onConfirmedTap() {
                    VolumeDialogImpl.$r8$lambda$hHuqrsYEk7uOeCMf3L3BqhDDIT0(VolumeDialogImpl.this);
                }
            }, this.mHandler);
        }
        this.mController.getCaptionsComponentState(false);
    }

    public /* synthetic */ void lambda$initODICaptionsH$14() {
        onCaptionIconClicked();
        Events.writeEvent(21, new Object[0]);
    }

    private void checkODICaptionsTooltip(boolean z) {
        boolean z2 = this.mHasSeenODICaptionsTooltip;
        if (!z2 && !z && this.mODICaptionsTooltipViewStub != null) {
            this.mController.getCaptionsComponentState(true);
        } else if (z2 && z && this.mODICaptionsTooltipView != null) {
            hideCaptionsTooltip();
        }
    }

    protected void showCaptionsTooltip() {
        ViewStub viewStub;
        if (!this.mHasSeenODICaptionsTooltip && (viewStub = this.mODICaptionsTooltipViewStub) != null) {
            View inflate = viewStub.inflate();
            this.mODICaptionsTooltipView = inflate;
            inflate.findViewById(R$id.dismiss).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VolumeDialogImpl.$r8$lambda$FTnmISwX7H3BRqLO5RzXTva5EMc(VolumeDialogImpl.this, view);
                }
            });
            this.mODICaptionsTooltipViewStub = null;
            rescheduleTimeoutH();
        }
        View view = this.mODICaptionsTooltipView;
        if (view != null) {
            view.setAlpha(0.0f);
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl.$r8$lambda$egcbtUIQapypE_Ls3CiEeOILEVo(VolumeDialogImpl.this);
                }
            });
        }
    }

    public /* synthetic */ void lambda$showCaptionsTooltip$15(View view) {
        hideCaptionsTooltip();
        Events.writeEvent(22, new Object[0]);
    }

    public /* synthetic */ void lambda$showCaptionsTooltip$17() {
        int[] locationOnScreen = this.mODICaptionsTooltipView.getLocationOnScreen();
        int[] locationOnScreen2 = this.mODICaptionsIcon.getLocationOnScreen();
        this.mODICaptionsTooltipView.setTranslationY(((float) (locationOnScreen2[1] - locationOnScreen[1])) - (((float) (this.mODICaptionsTooltipView.getHeight() - this.mODICaptionsIcon.getHeight())) / 2.0f));
        this.mODICaptionsTooltipView.animate().alpha(1.0f).setStartDelay((long) this.mDialogShowAnimationDurationMs).withEndAction(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                VolumeDialogImpl.$r8$lambda$NZzdhvG_oDn2MMd6OTVkXKJRTZg(VolumeDialogImpl.this);
            }
        }).start();
    }

    public /* synthetic */ void lambda$showCaptionsTooltip$16() {
        if (D.BUG) {
            Log.d(TAG, "tool:checkODICaptionsTooltip() putBoolean true");
        }
        Prefs.putBoolean(this.mContext, "HasSeenODICaptionsTooltip", true);
        this.mHasSeenODICaptionsTooltip = true;
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        if (captionsToggleImageButton != null) {
            captionsToggleImageButton.postOnAnimation(getSinglePressFor(captionsToggleImageButton));
        }
    }

    private void hideCaptionsTooltip() {
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            this.mODICaptionsTooltipView.animate().cancel();
            this.mODICaptionsTooltipView.setAlpha(1.0f);
            this.mODICaptionsTooltipView.animate().alpha(0.0f).setStartDelay(0).setDuration((long) this.mDialogHideAnimationDurationMs).withEndAction(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl.$r8$lambda$O4mOgi6JfrYnhcId9yF0YNBzDTw(VolumeDialogImpl.this);
                }
            }).start();
        }
    }

    public /* synthetic */ void lambda$hideCaptionsTooltip$18() {
        View view = this.mODICaptionsTooltipView;
        if (view != null) {
            view.setVisibility(4);
        }
    }

    protected void tryToRemoveCaptionsTooltip() {
        if (this.mHasSeenODICaptionsTooltip && this.mODICaptionsTooltipView != null) {
            ((ViewGroup) this.mDialog.findViewById(R$id.volume_dialog_container)).removeView(this.mODICaptionsTooltipView);
            this.mODICaptionsTooltipView = null;
        }
    }

    public void updateODICaptionsH(boolean z, boolean z2) {
        ViewGroup viewGroup = this.mODICaptionsView;
        if (viewGroup != null) {
            viewGroup.setVisibility(z ? 0 : 8);
        }
        if (z) {
            updateCaptionsIcon();
            if (z2) {
                showCaptionsTooltip();
            }
        }
    }

    private void updateCaptionsIcon() {
        boolean areCaptionsEnabled = this.mController.areCaptionsEnabled();
        if (this.mODICaptionsIcon.getCaptionsEnabled() != areCaptionsEnabled) {
            this.mHandler.post(this.mODICaptionsIcon.setCaptionsEnabled(areCaptionsEnabled));
        }
        boolean isCaptionStreamOptedOut = this.mController.isCaptionStreamOptedOut();
        if (this.mODICaptionsIcon.getOptedOut() != isCaptionStreamOptedOut) {
            this.mHandler.post(new Runnable(isCaptionStreamOptedOut) { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda22
                public final /* synthetic */ boolean f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl.$r8$lambda$SMLEg0boZiPQYkzBy1PpLMhJycQ(VolumeDialogImpl.this, this.f$1);
                }
            });
        }
    }

    public /* synthetic */ void lambda$updateCaptionsIcon$19(boolean z) {
        this.mODICaptionsIcon.setOptedOut(z);
    }

    private void onCaptionIconClicked() {
        this.mController.setCaptionsEnabled(!this.mController.areCaptionsEnabled());
        updateCaptionsIcon();
    }

    private void incrementManualToggleCount() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Settings.Secure.putInt(contentResolver, "manual_ringer_toggle_count", Settings.Secure.getInt(contentResolver, "manual_ringer_toggle_count", 0) + 1);
    }

    private void provideTouchFeedbackH(int i) {
        VibrationEffect vibrationEffect;
        if (i == 0) {
            vibrationEffect = VibrationEffect.get(0);
        } else if (i != 2) {
            vibrationEffect = VibrationEffect.get(1);
        } else {
            this.mController.scheduleTouchFeedback();
            vibrationEffect = null;
        }
        if (vibrationEffect != null) {
            this.mController.vibrate(vibrationEffect);
        }
    }

    private void maybeShowToastH(int i) {
        int i2 = Prefs.getInt(this.mContext, "RingerGuidanceCount", 0);
        if (i2 <= 12) {
            String str = null;
            if (i == 0) {
                str = this.mContext.getString(17041563);
            } else if (i != 2) {
                str = this.mContext.getString(17041564);
            } else {
                VolumeDialogController.StreamState streamState = this.mState.states.get(2);
                if (streamState != null) {
                    str = this.mContext.getString(R$string.volume_dialog_ringer_guidance_ring, Utils.formatPercentage((long) streamState.level, (long) streamState.levelMax));
                }
            }
            Toast.makeText(this.mContext, str, 0).show();
            Prefs.putInt(this.mContext, "RingerGuidanceCount", i2 + 1);
        }
    }

    public void showH(int i) {
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "showH r=" + Events.SHOW_REASONS[i]);
        }
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        rescheduleTimeoutH();
        if (this.mConfigChanged) {
            initDialog();
            this.mConfigurableTexts.update();
            this.mConfigChanged = false;
        }
        initSettingsH();
        this.mShowing = true;
        this.mIsAnimatingDismiss = false;
        this.mDialog.show();
        Events.writeEvent(0, Integer.valueOf(i), Boolean.valueOf(this.mKeyguard.isKeyguardLocked()));
        this.mController.notifyVisible(true);
        this.mController.getCaptionsComponentState(false);
        checkODICaptionsTooltip(false);
        updateBackgroundForDrawerClosedAmount();
    }

    protected void rescheduleTimeoutH() {
        this.mHandler.removeMessages(2);
        int computeTimeoutH = computeTimeoutH();
        H h = this.mHandler;
        h.sendMessageDelayed(h.obtainMessage(2, 3, 0), (long) computeTimeoutH);
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "rescheduleTimeout " + computeTimeoutH + " " + Debug.getCaller());
        }
        this.mController.userActivity();
    }

    private int computeTimeoutH() {
        if (this.mHovering) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(16000, 4);
        }
        if (this.mSafetyWarning != null) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(5000, 6);
        }
        if (this.mHasSeenODICaptionsTooltip || this.mODICaptionsTooltipView == null) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(3000, 4);
        }
        return this.mAccessibilityMgr.getRecommendedTimeoutMillis(5000, 6);
    }

    protected void dismissH(int i) {
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "mDialog.dismiss() reason: " + Events.DISMISS_REASONS[i] + " from: " + Debug.getCaller());
        }
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(1);
        if (!this.mIsAnimatingDismiss) {
            this.mIsAnimatingDismiss = true;
            this.mDialogView.animate().cancel();
            if (this.mShowing) {
                this.mShowing = false;
                Events.writeEvent(1, Integer.valueOf(i));
            }
            this.mDialogView.setTranslationX(0.0f);
            this.mDialogView.setAlpha(1.0f);
            ViewPropertyAnimator withEndAction = this.mDialogView.animate().alpha(0.0f).setDuration((long) this.mDialogHideAnimationDurationMs).setInterpolator(new SystemUIInterpolators$LogAccelerateInterpolator()).withEndAction(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl.$r8$lambda$lm4kwPNjrCYEr2R_hDlpFQygHOw(VolumeDialogImpl.this);
                }
            });
            if (!isLandscape()) {
                withEndAction.translationX(((float) this.mDialogView.getWidth()) / 2.0f);
            }
            withEndAction.start();
            checkODICaptionsTooltip(true);
            this.mController.notifyVisible(false);
            synchronized (this.mSafetyWarningLock) {
                if (this.mSafetyWarning != null) {
                    if (D.BUG) {
                        Log.d(TAG, "SafetyWarning dismissed");
                    }
                    this.mSafetyWarning.dismiss();
                }
            }
        }
    }

    public /* synthetic */ void lambda$dismissH$21() {
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                VolumeDialogImpl.m411$r8$lambda$_awe_YKppyJm4lKMkqKjvWLFsA(VolumeDialogImpl.this);
            }
        }, 50);
    }

    public /* synthetic */ void lambda$dismissH$20() {
        this.mDialog.dismiss();
        tryToRemoveCaptionsTooltip();
        this.mIsAnimatingDismiss = false;
        hideRingerDrawer();
    }

    private boolean showActiveStreamOnly() {
        return this.mContext.getPackageManager().hasSystemFeature("android.software.leanback") || this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.television");
    }

    private boolean shouldBeVisibleH(VolumeRow volumeRow, VolumeRow volumeRow2) {
        if (volumeRow.stream == volumeRow2.stream) {
            return true;
        }
        if (this.mShowActiveStreamOnly) {
            return false;
        }
        if (volumeRow.stream == 10) {
            return this.mShowA11yStream;
        }
        if (volumeRow2.stream == 10 && volumeRow.stream == this.mPrevActiveStream) {
            return true;
        }
        if (volumeRow.defaultStream) {
            return volumeRow2.stream == 2 || volumeRow2.stream == 4 || volumeRow2.stream == 0 || volumeRow2.stream == 10 || this.mDynamic.get(volumeRow2.stream);
        }
        return false;
    }

    public void updateRowsH(VolumeRow volumeRow) {
        if (D.BUG) {
            Log.d(TAG, "updateRowsH");
        }
        if (!this.mShowing) {
            trimObsoleteH();
        }
        int i = !isRtl() ? -1 : 32767;
        Iterator<VolumeRow> it = this.mRows.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            VolumeRow next = it.next();
            if (next == volumeRow) {
                z = true;
            }
            boolean shouldBeVisibleH = shouldBeVisibleH(next, volumeRow);
            Util.setVisOrGone(next.view, shouldBeVisibleH);
            if (shouldBeVisibleH && this.mRingerAndDrawerContainerBackground != null) {
                if (!isRtl()) {
                    i = Math.max(i, this.mDialogRowsView.indexOfChild(next.view));
                } else {
                    i = Math.min(i, this.mDialogRowsView.indexOfChild(next.view));
                }
                ViewGroup.LayoutParams layoutParams = next.view.getLayoutParams();
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                    if (!isRtl()) {
                        layoutParams2.setMarginEnd(this.mRingerRowsPadding);
                    } else {
                        layoutParams2.setMarginStart(this.mRingerRowsPadding);
                    }
                }
                next.view.setBackgroundDrawable(this.mContext.getDrawable(R$drawable.volume_row_rounded_background));
            }
            if (next.view.isShown()) {
                updateVolumeRowTintH(next, z);
            }
        }
        if (i > -1 && i < 32767) {
            View childAt = this.mDialogRowsView.getChildAt(i);
            ViewGroup.LayoutParams layoutParams3 = childAt.getLayoutParams();
            if (layoutParams3 instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
                layoutParams4.setMarginStart(0);
                layoutParams4.setMarginEnd(0);
                childAt.setBackgroundColor(0);
            }
        }
        updateBackgroundForDrawerClosedAmount();
    }

    protected void updateRingerH() {
        VolumeDialogController.State state;
        VolumeDialogController.StreamState streamState;
        if (this.mRinger != null && (state = this.mState) != null && (streamState = state.states.get(2)) != null) {
            VolumeDialogController.State state2 = this.mState;
            int i = state2.zenMode;
            boolean z = false;
            boolean z2 = i == 3 || i == 2 || (i == 1 && state2.disallowRinger);
            enableRingerViewsH(!z2);
            int i2 = this.mState.ringerModeInternal;
            if (i2 == 0) {
                ImageButton imageButton = this.mRingerIcon;
                int i3 = R$drawable.ic_volume_ringer_mute;
                imageButton.setImageResource(i3);
                this.mSelectedRingerIcon.setImageResource(i3);
                this.mRingerIcon.setTag(2);
                addAccessibilityDescription(this.mRingerIcon, 0, this.mContext.getString(R$string.volume_ringer_hint_unmute));
            } else if (i2 != 1) {
                if ((this.mAutomute && streamState.level == 0) || streamState.muted) {
                    z = true;
                }
                if (z2 || !z) {
                    ImageButton imageButton2 = this.mRingerIcon;
                    int i4 = R$drawable.ic_volume_ringer;
                    imageButton2.setImageResource(i4);
                    this.mSelectedRingerIcon.setImageResource(i4);
                    if (this.mController.hasVibrator()) {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(R$string.volume_ringer_hint_vibrate));
                    } else {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(R$string.volume_ringer_hint_mute));
                    }
                    this.mRingerIcon.setTag(1);
                    return;
                }
                ImageButton imageButton3 = this.mRingerIcon;
                int i5 = R$drawable.ic_volume_ringer_mute;
                imageButton3.setImageResource(i5);
                this.mSelectedRingerIcon.setImageResource(i5);
                addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(R$string.volume_ringer_hint_unmute));
                this.mRingerIcon.setTag(2);
            } else {
                ImageButton imageButton4 = this.mRingerIcon;
                int i6 = R$drawable.ic_volume_ringer_vibrate;
                imageButton4.setImageResource(i6);
                this.mSelectedRingerIcon.setImageResource(i6);
                addAccessibilityDescription(this.mRingerIcon, 1, this.mContext.getString(R$string.volume_ringer_hint_mute));
                this.mRingerIcon.setTag(3);
            }
        }
    }

    private void addAccessibilityDescription(View view, int i, final String str) {
        view.setContentDescription(this.mContext.getString(getStringDescriptionResourceForRingerMode(i)));
        view.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.systemui.volume.VolumeDialogImpl.3
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view2, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, str));
            }
        });
    }

    private int getStringDescriptionResourceForRingerMode(int i) {
        if (i == 0) {
            return R$string.volume_ringer_status_silent;
        }
        if (i != 1) {
            return R$string.volume_ringer_status_normal;
        }
        return R$string.volume_ringer_status_vibrate;
    }

    private void enableVolumeRowViewsH(VolumeRow volumeRow, boolean z) {
        volumeRow.dndIcon.setVisibility(z ^ true ? 0 : 8);
    }

    private void enableRingerViewsH(boolean z) {
        ImageButton imageButton = this.mRingerIcon;
        if (imageButton != null) {
            imageButton.setEnabled(z);
        }
        FrameLayout frameLayout = this.mZenIcon;
        if (frameLayout != null) {
            frameLayout.setVisibility(z ? 8 : 0);
        }
    }

    private void trimObsoleteH() {
        if (D.BUG) {
            Log.d(TAG, "trimObsoleteH");
        }
        for (int size = this.mRows.size() - 1; size >= 0; size--) {
            VolumeRow volumeRow = this.mRows.get(size);
            if (volumeRow.ss != null && volumeRow.ss.dynamic && !this.mDynamic.get(volumeRow.stream)) {
                this.mRows.remove(size);
                this.mDialogRowsView.removeView(volumeRow.view);
                this.mConfigurableTexts.remove(volumeRow.header);
            }
        }
    }

    protected void onStateChangedH(VolumeDialogController.State state) {
        int i;
        int i2;
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "onStateChangedH() state: " + state.toString());
        }
        VolumeDialogController.State state2 = this.mState;
        if (!(state2 == null || state == null || (i = state2.ringerModeInternal) == -1 || i == (i2 = state.ringerModeInternal) || i2 != 1)) {
            this.mController.vibrate(VibrationEffect.get(5));
        }
        this.mState = state;
        this.mDynamic.clear();
        for (int i3 = 0; i3 < state.states.size(); i3++) {
            int keyAt = state.states.keyAt(i3);
            if (state.states.valueAt(i3).dynamic) {
                this.mDynamic.put(keyAt, true);
                if (findRow(keyAt) == null) {
                    addRow(keyAt, R$drawable.ic_volume_remote, R$drawable.ic_volume_remote_mute, true, false, true);
                }
            }
        }
        int i4 = this.mActiveStream;
        int i5 = state.activeStream;
        if (i4 != i5) {
            this.mPrevActiveStream = i4;
            this.mActiveStream = i5;
            updateRowsH(getActiveRow());
            if (this.mShowing) {
                rescheduleTimeoutH();
            }
        }
        for (VolumeRow volumeRow : this.mRows) {
            updateVolumeRowH(volumeRow);
        }
        updateRingerH();
        this.mWindow.setTitle(composeWindowTitle());
    }

    CharSequence composeWindowTitle() {
        return this.mContext.getString(R$string.volume_dialog_title, getStreamLabelH(getActiveRow().ss));
    }

    private void updateVolumeRowH(VolumeRow volumeRow) {
        VolumeDialogController.StreamState streamState;
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        if (D.BUG) {
            Log.i(TAG, "updateVolumeRowH s=" + volumeRow.stream);
        }
        VolumeDialogController.State state = this.mState;
        if (state != null && (streamState = state.states.get(volumeRow.stream)) != null) {
            volumeRow.ss = streamState;
            int i5 = streamState.level;
            if (i5 > 0) {
                volumeRow.lastAudibleLevel = i5;
            }
            if (streamState.level == volumeRow.requestedLevel) {
                volumeRow.requestedLevel = -1;
            }
            int i6 = 0;
            boolean z2 = volumeRow.stream == 10;
            int i7 = 2;
            boolean z3 = volumeRow.stream == 2;
            boolean z4 = volumeRow.stream == 1;
            boolean z5 = volumeRow.stream == 4;
            boolean z6 = volumeRow.stream == 3;
            boolean z7 = z3 && this.mState.ringerModeInternal == 1;
            boolean z8 = z3 && this.mState.ringerModeInternal == 0;
            VolumeDialogController.State state2 = this.mState;
            int i8 = state2.zenMode;
            boolean z9 = i8 == 1;
            boolean z10 = i8 == 3;
            boolean z11 = i8 == 2;
            if (!z10 ? !z11 ? !z9 || ((!z5 || !state2.disallowAlarms) && ((!z6 || !state2.disallowMedia) && ((!z3 || !state2.disallowRinger) && (!z4 || !state2.disallowSystem)))) : !z3 && !z4 && !z5 && !z6 : !z3 && !z4) {
                z = false;
            } else {
                z = true;
            }
            int i9 = streamState.levelMax * 100;
            if (i9 != volumeRow.slider.getMax()) {
                volumeRow.slider.setMax(i9);
            }
            int i10 = streamState.levelMin * 100;
            if (i10 != volumeRow.slider.getMin()) {
                volumeRow.slider.setMin(i10);
            }
            Util.setText(volumeRow.header, getStreamLabelH(streamState));
            volumeRow.slider.setContentDescription(volumeRow.header.getText());
            this.mConfigurableTexts.add(volumeRow.header, streamState.name);
            boolean z12 = (this.mAutomute || streamState.muteSupported) && !z;
            if (z7) {
                i = R$drawable.ic_volume_ringer_vibrate;
            } else if (z8 || z) {
                i = volumeRow.iconMuteRes;
            } else if (streamState.routedToBluetooth) {
                i = isStreamMuted(streamState) ? R$drawable.ic_volume_media_bt_mute : R$drawable.ic_volume_media_bt;
            } else {
                i = isStreamMuted(streamState) ? streamState.muted ? R$drawable.ic_volume_media_off : volumeRow.iconMuteRes : (!this.mShowLowMediaVolumeIcon || streamState.level * 2 >= streamState.levelMax + streamState.levelMin) ? volumeRow.iconRes : R$drawable.ic_volume_media_low;
            }
            volumeRow.setIcon(i, this.mContext.getTheme());
            if (i == R$drawable.ic_volume_ringer_vibrate) {
                i7 = 3;
            } else if (!(i == R$drawable.ic_volume_media_bt_mute || i == volumeRow.iconMuteRes)) {
                i7 = (i == R$drawable.ic_volume_media_bt || i == volumeRow.iconRes || i == R$drawable.ic_volume_media_low) ? 1 : 0;
            }
            volumeRow.iconState = i7;
            if (volumeRow.icon != null) {
                if (!z12) {
                    volumeRow.icon.setContentDescription(getStreamLabelH(streamState));
                } else if (z3) {
                    if (z7) {
                        volumeRow.icon.setContentDescription(this.mContext.getString(R$string.volume_stream_content_description_unmute, getStreamLabelH(streamState)));
                    } else if (this.mController.hasVibrator()) {
                        ImageButton imageButton = volumeRow.icon;
                        Context context = this.mContext;
                        if (this.mShowA11yStream) {
                            i4 = R$string.volume_stream_content_description_vibrate_a11y;
                        } else {
                            i4 = R$string.volume_stream_content_description_vibrate;
                        }
                        imageButton.setContentDescription(context.getString(i4, getStreamLabelH(streamState)));
                    } else {
                        ImageButton imageButton2 = volumeRow.icon;
                        Context context2 = this.mContext;
                        if (this.mShowA11yStream) {
                            i3 = R$string.volume_stream_content_description_mute_a11y;
                        } else {
                            i3 = R$string.volume_stream_content_description_mute;
                        }
                        imageButton2.setContentDescription(context2.getString(i3, getStreamLabelH(streamState)));
                    }
                } else if (z2) {
                    volumeRow.icon.setContentDescription(getStreamLabelH(streamState));
                } else if (streamState.muted || (this.mAutomute && streamState.level == 0)) {
                    volumeRow.icon.setContentDescription(this.mContext.getString(R$string.volume_stream_content_description_unmute, getStreamLabelH(streamState)));
                } else {
                    ImageButton imageButton3 = volumeRow.icon;
                    Context context3 = this.mContext;
                    if (this.mShowA11yStream) {
                        i2 = R$string.volume_stream_content_description_mute_a11y;
                    } else {
                        i2 = R$string.volume_stream_content_description_mute;
                    }
                    imageButton3.setContentDescription(context3.getString(i2, getStreamLabelH(streamState)));
                }
            }
            if (z) {
                volumeRow.tracking = false;
            }
            enableVolumeRowViewsH(volumeRow, !z);
            boolean z13 = !z;
            if (!volumeRow.ss.muted || z3 || z) {
                i6 = volumeRow.ss.level;
            }
            updateVolumeRowSliderH(volumeRow, z13, i6);
            if (volumeRow.number != null) {
                volumeRow.number.setText(Integer.toString(i6));
            }
        }
    }

    private boolean isStreamMuted(VolumeDialogController.StreamState streamState) {
        return (this.mAutomute && streamState.level == 0) || streamState.muted;
    }

    private void updateVolumeRowTintH(VolumeRow volumeRow, boolean z) {
        ColorStateList colorStateList;
        int i;
        if (z) {
            volumeRow.slider.requestFocus();
        }
        boolean z2 = z && volumeRow.slider.isEnabled();
        if (z2 || this.mChangeVolumeRowTintWhenInactive) {
            if (z2) {
                colorStateList = Utils.getColorAccent(this.mContext);
            } else {
                colorStateList = Utils.getColorAttr(this.mContext, 17956902);
            }
            if (z2) {
                i = Color.alpha(colorStateList.getDefaultColor());
            } else {
                i = getAlphaAttr(16844115);
            }
            ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 16844002);
            ColorStateList colorAttr2 = Utils.getColorAttr(this.mContext, 17957102);
            volumeRow.sliderProgressSolid.setTintList(colorStateList);
            if (volumeRow.sliderBgIcon != null) {
                volumeRow.sliderBgIcon.setTintList(colorStateList);
            }
            if (volumeRow.sliderBgSolid != null) {
                volumeRow.sliderBgSolid.setTintList(colorAttr);
            }
            if (volumeRow.sliderProgressIcon != null) {
                volumeRow.sliderProgressIcon.setTintList(colorAttr);
            }
            if (volumeRow.icon != null) {
                volumeRow.icon.setImageTintList(colorAttr2);
                volumeRow.icon.setImageAlpha(i);
            }
            if (volumeRow.number != null) {
                volumeRow.number.setTextColor(colorStateList);
                volumeRow.number.setAlpha((float) i);
            }
        }
    }

    private void updateVolumeRowSliderH(VolumeRow volumeRow, boolean z, int i) {
        int i2;
        volumeRow.slider.setEnabled(z);
        updateVolumeRowTintH(volumeRow, volumeRow.stream == this.mActiveStream);
        if (!volumeRow.tracking) {
            int progress = volumeRow.slider.getProgress();
            int impliedLevel = getImpliedLevel(volumeRow.slider, progress);
            boolean z2 = volumeRow.view.getVisibility() == 0;
            boolean z3 = SystemClock.uptimeMillis() - volumeRow.userAttempt < 1000;
            this.mHandler.removeMessages(3, volumeRow);
            boolean z4 = this.mShowing;
            if (z4 && z2 && z3) {
                if (D.BUG) {
                    Log.d(TAG, "inGracePeriod");
                }
                H h = this.mHandler;
                h.sendMessageAtTime(h.obtainMessage(3, volumeRow), volumeRow.userAttempt + 1000);
            } else if ((i == impliedLevel && z4 && z2) || progress == (i2 = i * 100)) {
            } else {
                if (!z4 || !z2) {
                    if (volumeRow.anim != null) {
                        volumeRow.anim.cancel();
                    }
                    volumeRow.slider.setProgress(i2, true);
                } else if (volumeRow.anim == null || !volumeRow.anim.isRunning() || volumeRow.animTargetProgress != i2) {
                    if (volumeRow.anim == null) {
                        volumeRow.anim = ObjectAnimator.ofInt(volumeRow.slider, "progress", progress, i2);
                        volumeRow.anim.setInterpolator(new DecelerateInterpolator());
                    } else {
                        volumeRow.anim.cancel();
                        volumeRow.anim.setIntValues(progress, i2);
                    }
                    volumeRow.animTargetProgress = i2;
                    volumeRow.anim.setDuration(80L);
                    volumeRow.anim.start();
                }
            }
        }
    }

    public void recheckH(VolumeRow volumeRow) {
        if (volumeRow == null) {
            if (D.BUG) {
                Log.d(TAG, "recheckH ALL");
            }
            trimObsoleteH();
            for (VolumeRow volumeRow2 : this.mRows) {
                updateVolumeRowH(volumeRow2);
            }
            return;
        }
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "recheckH " + volumeRow.stream);
        }
        updateVolumeRowH(volumeRow);
    }

    public void setStreamImportantH(int i, boolean z) {
        for (VolumeRow volumeRow : this.mRows) {
            if (volumeRow.stream == i) {
                volumeRow.important = z;
                return;
            }
        }
    }

    public void showSafetyWarningH(int i) {
        if ((i & 1025) != 0 || this.mShowing) {
            synchronized (this.mSafetyWarningLock) {
                if (this.mSafetyWarning == null) {
                    AnonymousClass4 r0 = new SafetyWarningDialog(this.mContext, this.mController.getAudioManager()) { // from class: com.android.systemui.volume.VolumeDialogImpl.4
                        @Override // com.android.systemui.volume.SafetyWarningDialog
                        protected void cleanUp() {
                            synchronized (VolumeDialogImpl.this.mSafetyWarningLock) {
                                VolumeDialogImpl.this.mSafetyWarning = null;
                            }
                            VolumeDialogImpl.this.recheckH(null);
                        }
                    };
                    this.mSafetyWarning = r0;
                    r0.show();
                    recheckH(null);
                } else {
                    return;
                }
            }
        }
        rescheduleTimeoutH();
    }

    private String getStreamLabelH(VolumeDialogController.StreamState streamState) {
        if (streamState == null) {
            return "";
        }
        String str = streamState.remoteLabel;
        if (str != null) {
            return str;
        }
        try {
            return this.mContext.getResources().getString(streamState.name);
        } catch (Resources.NotFoundException unused) {
            String str2 = TAG;
            Slog.e(str2, "Can't find translation for stream " + streamState);
            return "";
        }
    }

    private Runnable getSinglePressFor(ImageButton imageButton) {
        return new Runnable(imageButton) { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda21
            public final /* synthetic */ ImageButton f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                VolumeDialogImpl.m408$r8$lambda$3g6GYO3DdPA7AVLEDeqtMjDRLM(VolumeDialogImpl.this, this.f$1);
            }
        };
    }

    public /* synthetic */ void lambda$getSinglePressFor$22(ImageButton imageButton) {
        if (imageButton != null) {
            imageButton.setPressed(true);
            imageButton.postOnAnimationDelayed(getSingleUnpressFor(imageButton), 200);
        }
    }

    private Runnable getSingleUnpressFor(ImageButton imageButton) {
        return new Runnable(imageButton) { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda11
            public final /* synthetic */ ImageButton f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                VolumeDialogImpl.$r8$lambda$P10l4iHWcjx53YWqOyVeewnhz74(this.f$0);
            }
        };
    }

    public static /* synthetic */ void lambda$getSingleUnpressFor$23(ImageButton imageButton) {
        if (imageButton != null) {
            imageButton.setPressed(false);
        }
    }

    private int getRingerDrawerOpenExtraSize() {
        return (this.mRingerCount - 1) * this.mRingerDrawerItemSize;
    }

    private void updateBackgroundForDrawerClosedAmount() {
        Drawable drawable = this.mRingerAndDrawerContainerBackground;
        if (drawable != null) {
            Rect copyBounds = drawable.copyBounds();
            if (!isLandscape()) {
                copyBounds.top = (int) (this.mRingerDrawerClosedAmount * ((float) getRingerDrawerOpenExtraSize()));
            } else {
                copyBounds.left = (int) (this.mRingerDrawerClosedAmount * ((float) getRingerDrawerOpenExtraSize()));
            }
            this.mRingerAndDrawerContainerBackground.setBounds(copyBounds);
        }
    }

    private void setTopContainerBackgroundDrawable() {
        int i;
        int i2;
        if (this.mTopContainer != null) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{new ColorDrawable(Utils.getColorAttrDefaultColor(this.mContext, 17956909))});
            int i3 = this.mDialogWidth;
            if (!isLandscape()) {
                i = this.mDialogRowsView.getHeight();
            } else {
                i = this.mDialogRowsView.getHeight() + this.mDialogCornerRadius;
            }
            layerDrawable.setLayerSize(0, i3, i);
            if (!isLandscape()) {
                i2 = this.mDialogRowsViewContainer.getTop();
            } else {
                i2 = this.mDialogRowsViewContainer.getTop() - this.mDialogCornerRadius;
            }
            layerDrawable.setLayerInsetTop(0, i2);
            layerDrawable.setLayerGravity(0, 53);
            if (isLandscape()) {
                this.mRingerAndDrawerContainer.setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.systemui.volume.VolumeDialogImpl.5
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) VolumeDialogImpl.this.mDialogCornerRadius);
                    }
                });
                this.mRingerAndDrawerContainer.setClipToOutline(true);
            }
            this.mTopContainer.setBackground(layerDrawable);
        }
    }

    /* loaded from: classes2.dex */
    public final class H extends Handler {
        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public H() {
            super(Looper.getMainLooper());
            VolumeDialogImpl.this = r1;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    VolumeDialogImpl.this.showH(message.arg1);
                    return;
                case 2:
                    VolumeDialogImpl.this.dismissH(message.arg1);
                    return;
                case 3:
                    VolumeDialogImpl.this.recheckH((VolumeRow) message.obj);
                    return;
                case 4:
                    VolumeDialogImpl.this.recheckH(null);
                    return;
                case 5:
                    VolumeDialogImpl.this.setStreamImportantH(message.arg1, message.arg2 != 0);
                    return;
                case 6:
                    VolumeDialogImpl.this.rescheduleTimeoutH();
                    return;
                case 7:
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    volumeDialogImpl.onStateChangedH(volumeDialogImpl.mState);
                    return;
                default:
                    return;
            }
        }
    }

    /* loaded from: classes2.dex */
    public final class CustomDialog extends Dialog {
        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public CustomDialog(Context context) {
            super(context, R$style.volume_dialog_theme);
            VolumeDialogImpl.this = r1;
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // android.app.Dialog
        protected void onStart() {
            super.setCanceledOnTouchOutside(true);
            super.onStart();
        }

        @Override // android.app.Dialog
        protected void onStop() {
            super.onStop();
            VolumeDialogImpl.this.mHandler.sendEmptyMessage(4);
        }

        @Override // android.app.Dialog
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!VolumeDialogImpl.this.mShowing || motionEvent.getAction() != 4) {
                return false;
            }
            VolumeDialogImpl.this.dismissH(1);
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public final class VolumeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private final VolumeRow mRow;

        private VolumeSeekBarChangeListener(VolumeRow volumeRow) {
            VolumeDialogImpl.this = r1;
            this.mRow = volumeRow;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int i2;
            if (this.mRow.ss != null) {
                if (D.BUG) {
                    String str = VolumeDialogImpl.TAG;
                    Log.d(str, AudioSystem.streamToString(this.mRow.stream) + " onProgressChanged " + i + " fromUser=" + z);
                }
                if (z) {
                    if (this.mRow.ss.levelMin > 0 && i < (i2 = this.mRow.ss.levelMin * 100)) {
                        seekBar.setProgress(i2);
                        i = i2;
                    }
                    int impliedLevel = VolumeDialogImpl.getImpliedLevel(seekBar, i);
                    if (this.mRow.ss.level != impliedLevel || (this.mRow.ss.muted && impliedLevel > 0)) {
                        this.mRow.userAttempt = SystemClock.uptimeMillis();
                        if (this.mRow.requestedLevel != impliedLevel) {
                            VolumeDialogImpl.this.mController.setActiveStream(this.mRow.stream);
                            VolumeDialogImpl.this.mController.setStreamVolume(this.mRow.stream, impliedLevel);
                            this.mRow.requestedLevel = impliedLevel;
                            Events.writeEvent(9, Integer.valueOf(this.mRow.stream), Integer.valueOf(impliedLevel));
                        }
                    }
                }
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (D.BUG) {
                String str = VolumeDialogImpl.TAG;
                Log.d(str, "onStartTrackingTouch " + this.mRow.stream);
            }
            VolumeDialogImpl.this.mController.setActiveStream(this.mRow.stream);
            this.mRow.tracking = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (D.BUG) {
                String str = VolumeDialogImpl.TAG;
                Log.d(str, "onStopTrackingTouch " + this.mRow.stream);
            }
            this.mRow.tracking = false;
            this.mRow.userAttempt = SystemClock.uptimeMillis();
            int impliedLevel = VolumeDialogImpl.getImpliedLevel(seekBar, seekBar.getProgress());
            Events.writeEvent(16, Integer.valueOf(this.mRow.stream), Integer.valueOf(impliedLevel));
            if (this.mRow.ss.level != impliedLevel) {
                VolumeDialogImpl.this.mHandler.sendMessageDelayed(VolumeDialogImpl.this.mHandler.obtainMessage(3, this.mRow), 1000);
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class Accessibility extends View.AccessibilityDelegate {
        private Accessibility() {
            VolumeDialogImpl.this = r1;
        }

        public void init() {
            VolumeDialogImpl.this.mDialogView.setAccessibilityDelegate(this);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.getText().add(VolumeDialogImpl.this.composeWindowTitle());
            return true;
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    /* loaded from: classes2.dex */
    public static class VolumeRow {
        private ObjectAnimator anim;
        private int animTargetProgress;
        private boolean defaultStream;
        private FrameLayout dndIcon;
        private TextView header;
        private ImageButton icon;
        private int iconMuteRes;
        private int iconRes;
        private int iconState;
        private boolean important;
        private int lastAudibleLevel;
        private TextView number;
        private int requestedLevel;
        private SeekBar slider;
        private AlphaTintDrawableWrapper sliderBgIcon;
        private Drawable sliderBgSolid;
        private AlphaTintDrawableWrapper sliderProgressIcon;
        private Drawable sliderProgressSolid;
        private VolumeDialogController.StreamState ss;
        private int stream;
        private boolean tracking;
        private long userAttempt;
        private View view;

        private VolumeRow() {
            this.requestedLevel = -1;
            this.lastAudibleLevel = 1;
        }

        void setIcon(int i, Resources.Theme theme) {
            ImageButton imageButton = this.icon;
            if (imageButton != null) {
                imageButton.setImageResource(i);
            }
            AlphaTintDrawableWrapper alphaTintDrawableWrapper = this.sliderProgressIcon;
            if (alphaTintDrawableWrapper != null) {
                alphaTintDrawableWrapper.setDrawable(this.view.getResources().getDrawable(i, theme));
            }
            AlphaTintDrawableWrapper alphaTintDrawableWrapper2 = this.sliderBgIcon;
            if (alphaTintDrawableWrapper2 != null) {
                alphaTintDrawableWrapper2.setDrawable(this.view.getResources().getDrawable(i, theme));
            }
        }
    }

    /* loaded from: classes2.dex */
    public class RingerDrawerItemClickListener implements View.OnClickListener {
        private final int mClickedRingerMode;

        RingerDrawerItemClickListener(int i) {
            VolumeDialogImpl.this = r1;
            this.mClickedRingerMode = i;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (VolumeDialogImpl.this.mIsRingerDrawerOpen) {
                VolumeDialogImpl.this.setRingerMode(this.mClickedRingerMode);
                VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                volumeDialogImpl.mRingerDrawerIconAnimatingSelected = volumeDialogImpl.getDrawerIconViewForMode(this.mClickedRingerMode);
                VolumeDialogImpl volumeDialogImpl2 = VolumeDialogImpl.this;
                volumeDialogImpl2.mRingerDrawerIconAnimatingDeselected = volumeDialogImpl2.getDrawerIconViewForMode(volumeDialogImpl2.mState.ringerModeInternal);
                VolumeDialogImpl.this.mRingerDrawerIconColorAnimator.start();
                VolumeDialogImpl.this.mSelectedRingerContainer.setVisibility(4);
                VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.setAlpha(1.0f);
                VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().setInterpolator(Interpolators.ACCELERATE_DECELERATE).setDuration(175).withEndAction(new VolumeDialogImpl$RingerDrawerItemClickListener$$ExternalSyntheticLambda0(this));
                if (!VolumeDialogImpl.this.isLandscape()) {
                    VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().translationY(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode)).start();
                } else {
                    VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().translationX(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode)).start();
                }
            }
        }

        public /* synthetic */ void lambda$onClick$0() {
            VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.setAlpha(0.0f);
            if (!VolumeDialogImpl.this.isLandscape()) {
                VolumeDialogImpl.this.mSelectedRingerContainer.setTranslationY(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode));
            } else {
                VolumeDialogImpl.this.mSelectedRingerContainer.setTranslationX(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode));
            }
            VolumeDialogImpl.this.mSelectedRingerContainer.setVisibility(0);
            VolumeDialogImpl.this.hideRingerDrawer();
        }
    }
}
