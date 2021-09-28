package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.DisplayCutout;
import android.view.WindowManager;
import android.view.WindowMetrics;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusBarContentInsetsProvider.kt */
/* loaded from: classes.dex */
public final class StatusBarContentInsetsProvider implements CallbackController<StatusBarContentInsetsChangedListener>, ConfigurationController.ConfigurationListener, Dumpable {
    private final ConfigurationController configurationController;
    private final Context context;
    private final DumpManager dumpManager;
    private final Rect[] insetsByCorner = new Rect[4];
    private final Set<StatusBarContentInsetsChangedListener> listeners = new LinkedHashSet();
    private final WindowManager windowManager;

    public StatusBarContentInsetsProvider(Context context, ConfigurationController configurationController, WindowManager windowManager, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(windowManager, "windowManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.context = context;
        this.configurationController = configurationController;
        this.windowManager = windowManager;
        this.dumpManager = dumpManager;
        configurationController.addCallback(this);
        dumpManager.registerDumpable("StatusBarInsetsProvider", this);
    }

    public void addCallback(StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        Intrinsics.checkNotNullParameter(statusBarContentInsetsChangedListener, "listener");
        this.listeners.add(statusBarContentInsetsChangedListener);
    }

    public void removeCallback(StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        Intrinsics.checkNotNullParameter(statusBarContentInsetsChangedListener, "listener");
        this.listeners.remove(statusBarContentInsetsChangedListener);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        clearCachedInsets();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onOverlayChanged() {
        clearCachedInsets();
    }

    private final void clearCachedInsets() {
        Rect[] rectArr = this.insetsByCorner;
        rectArr[0] = null;
        rectArr[1] = null;
        rectArr[2] = null;
        rectArr[3] = null;
        notifyInsetsChanged();
    }

    private final void notifyInsetsChanged() {
        for (StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener : this.listeners) {
            statusBarContentInsetsChangedListener.onStatusBarContentInsetsChanged();
        }
    }

    public final Rect getBoundingRectForPrivacyChipForRotation(int i) {
        Rect rect = this.insetsByCorner[i];
        Resources resourcesForRotation = RotationUtils.getResourcesForRotation(i, this.context);
        if (rect == null) {
            Intrinsics.checkNotNullExpressionValue(resourcesForRotation, "rotatedResources");
            rect = getAndSetInsetsForRotation(i, resourcesForRotation);
        }
        int dimensionPixelSize = resourcesForRotation.getDimensionPixelSize(R$dimen.ongoing_appops_dot_diameter);
        int dimensionPixelSize2 = resourcesForRotation.getDimensionPixelSize(R$dimen.ongoing_appops_chip_max_width);
        boolean z = true;
        if (this.context.getResources().getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        return StatusBarContentInsetsProviderKt.getPrivacyChipBoundingRectForInsets(rect, dimensionPixelSize, dimensionPixelSize2, z);
    }

    public final Rect getStatusBarContentInsetsForRotation(int i) {
        Rect rect = this.insetsByCorner[i];
        if (rect != null) {
            return rect;
        }
        Resources resourcesForRotation = RotationUtils.getResourcesForRotation(i, this.context);
        Intrinsics.checkNotNullExpressionValue(resourcesForRotation, "rotatedResources");
        return getAndSetInsetsForRotation(i, resourcesForRotation);
    }

    private final Rect getAndSetInsetsForRotation(int i, Resources resources) {
        Rect calculatedInsetsForRotation = getCalculatedInsetsForRotation(i, resources);
        this.insetsByCorner[i] = calculatedInsetsForRotation;
        return calculatedInsetsForRotation;
    }

    private final Rect getCalculatedInsetsForRotation(int i, Resources resources) {
        int i2;
        int i3;
        DisplayCutout cutout = this.context.getDisplay().getCutout();
        int exactRotation = RotationUtils.getExactRotation(this.context);
        boolean z = true;
        if (resources.getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.rounded_corner_content_padding);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.ongoing_appops_dot_min_padding);
        if (z) {
            i3 = Math.max(dimensionPixelSize2, dimensionPixelSize);
            i2 = dimensionPixelSize;
        } else {
            i2 = Math.max(dimensionPixelSize2, dimensionPixelSize);
            i3 = dimensionPixelSize;
        }
        WindowMetrics maximumWindowMetrics = this.windowManager.getMaximumWindowMetrics();
        Intrinsics.checkNotNullExpressionValue(maximumWindowMetrics, "windowManager.maximumWindowMetrics");
        return StatusBarContentInsetsProviderKt.calculateInsetsForRotationWithRotatedResources(exactRotation, i, cutout, maximumWindowMetrics, resources.getDimensionPixelSize(R$dimen.status_bar_height), i3, i2);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        int i = 0;
        for (Rect rect : this.insetsByCorner) {
            i++;
            printWriter.println(((Object) RotationUtils.toString(i)) + " -> " + rect);
        }
    }
}
