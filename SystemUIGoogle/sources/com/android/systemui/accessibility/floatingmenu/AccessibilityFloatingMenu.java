package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.accessibility.dialog.AccessibilityTargetHelper;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Prefs;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
/* loaded from: classes.dex */
public class AccessibilityFloatingMenu implements IAccessibilityFloatingMenu {
    private final ContentObserver mContentObserver;
    private final Context mContext;
    private final DockTooltipView mDockTooltipView;
    private final ContentObserver mEnabledA11yServicesContentObserver;
    private final ContentObserver mFadeOutContentObserver;
    private final Handler mHandler;
    private final AccessibilityFloatingMenuView mMenuView;
    private final MigrationTooltipView mMigrationTooltipView;
    private final ContentObserver mSizeContentObserver;

    public AccessibilityFloatingMenu(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(AccessibilityFloatingMenu.this.mContext, 0));
            }
        };
        this.mSizeContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.setSizeType(AccessibilityFloatingMenu.getSizeType(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mFadeOutContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.updateOpacityWith(AccessibilityFloatingMenu.isFadeEffectEnabled(AccessibilityFloatingMenu.this.mContext), AccessibilityFloatingMenu.getOpacityValue(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mEnabledA11yServicesContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onEnabledFeaturesChanged();
            }
        };
        this.mContext = context;
        AccessibilityFloatingMenuView accessibilityFloatingMenuView = new AccessibilityFloatingMenuView(context, getPosition(context));
        this.mMenuView = accessibilityFloatingMenuView;
        this.mMigrationTooltipView = new MigrationTooltipView(context, accessibilityFloatingMenuView);
        this.mDockTooltipView = new DockTooltipView(context, accessibilityFloatingMenuView);
    }

    @VisibleForTesting
    AccessibilityFloatingMenu(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(AccessibilityFloatingMenu.this.mContext, 0));
            }
        };
        this.mSizeContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.setSizeType(AccessibilityFloatingMenu.getSizeType(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mFadeOutContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.updateOpacityWith(AccessibilityFloatingMenu.isFadeEffectEnabled(AccessibilityFloatingMenu.this.mContext), AccessibilityFloatingMenu.getOpacityValue(AccessibilityFloatingMenu.this.mContext));
            }
        };
        this.mEnabledA11yServicesContentObserver = new ContentObserver(handler) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AccessibilityFloatingMenu.this.mMenuView.onEnabledFeaturesChanged();
            }
        };
        this.mContext = context;
        this.mMenuView = accessibilityFloatingMenuView;
        this.mMigrationTooltipView = new MigrationTooltipView(context, accessibilityFloatingMenuView);
        this.mDockTooltipView = new DockTooltipView(context, accessibilityFloatingMenuView);
    }

    public boolean isShowing() {
        return this.mMenuView.isShowing();
    }

    @Override // com.android.systemui.accessibility.floatingmenu.IAccessibilityFloatingMenu
    public void show() {
        if (!isShowing()) {
            this.mMenuView.show();
            this.mMenuView.onTargetsChanged(AccessibilityTargetHelper.getTargets(this.mContext, 0));
            this.mMenuView.updateOpacityWith(isFadeEffectEnabled(this.mContext), getOpacityValue(this.mContext));
            this.mMenuView.setSizeType(getSizeType(this.mContext));
            this.mMenuView.setShapeType(getShapeType(this.mContext));
            this.mMenuView.setOnDragEndListener(new AccessibilityFloatingMenuView.OnDragEndListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenu$$ExternalSyntheticLambda0
                @Override // com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.OnDragEndListener
                public final void onDragEnd(Position position) {
                    AccessibilityFloatingMenu.$r8$lambda$mAj3e5DgO1QYDpyreIc1gwo3q6s(AccessibilityFloatingMenu.this, position);
                }
            });
            showMigrationTooltipIfNecessary();
            registerContentObservers();
        }
    }

    @Override // com.android.systemui.accessibility.floatingmenu.IAccessibilityFloatingMenu
    public void hide() {
        if (isShowing()) {
            this.mMenuView.hide();
            this.mMenuView.setOnDragEndListener(null);
            this.mMigrationTooltipView.hide();
            this.mDockTooltipView.hide();
            unregisterContentObservers();
        }
    }

    private Position getPosition(Context context) {
        String string = Prefs.getString(context, "AccessibilityFloatingMenuPosition", null);
        if (TextUtils.isEmpty(string)) {
            return new Position(1.0f, 0.9f);
        }
        return Position.fromString(string);
    }

    private void showMigrationTooltipIfNecessary() {
        if (isMigrationTooltipPromptEnabled(this.mContext)) {
            this.mMigrationTooltipView.show();
            Settings.Secure.putInt(this.mContext.getContentResolver(), "accessibility_floating_menu_migration_tooltip_prompt", 0);
        }
    }

    private static boolean isMigrationTooltipPromptEnabled(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_migration_tooltip_prompt", 0) == 1) {
            return true;
        }
        return false;
    }

    public void onDragEnd(Position position) {
        savePosition(this.mContext, position);
        showDockTooltipIfNecessary(this.mContext);
    }

    private void savePosition(Context context, Position position) {
        Prefs.putString(context, "AccessibilityFloatingMenuPosition", position.toString());
    }

    private void showDockTooltipIfNecessary(Context context) {
        if (!Prefs.get(context).getBoolean("HasSeenAccessibilityFloatingMenuDockTooltip", false)) {
            if (this.mMenuView.isOvalShape()) {
                this.mDockTooltipView.show();
            }
            Prefs.putBoolean(context, "HasSeenAccessibilityFloatingMenuDockTooltip", true);
        }
    }

    public static boolean isFadeEffectEnabled(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_fade_enabled", 1) == 1) {
            return true;
        }
        return false;
    }

    public static float getOpacityValue(Context context) {
        return Settings.Secure.getFloat(context.getContentResolver(), "accessibility_floating_menu_opacity", 0.55f);
    }

    public static int getSizeType(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_size", 0);
    }

    private static int getShapeType(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_floating_menu_icon_type", 0);
    }

    private void registerContentObservers() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_button_targets"), false, this.mContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_size"), false, this.mSizeContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_fade_enabled"), false, this.mFadeOutContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_floating_menu_opacity"), false, this.mFadeOutContentObserver, -2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("enabled_accessibility_services"), false, this.mEnabledA11yServicesContentObserver, -2);
    }

    private void unregisterContentObservers() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mSizeContentObserver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mFadeOutContentObserver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mEnabledA11yServicesContentObserver);
    }
}
