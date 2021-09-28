package com.android.systemui.controls;

import com.android.internal.logging.InstanceIdSequence;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.ui.ControlViewHolder;
import com.android.systemui.shared.system.SysUiStatsLog;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: ControlsMetricsLoggerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsMetricsLoggerImpl implements ControlsMetricsLogger {
    public static final Companion Companion = new Companion(null);
    private int instanceId;
    private final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(8192);

    @Override // com.android.systemui.controls.ControlsMetricsLogger
    public void drag(ControlViewHolder controlViewHolder, boolean z) {
        ControlsMetricsLogger.DefaultImpls.drag(this, controlViewHolder, z);
    }

    @Override // com.android.systemui.controls.ControlsMetricsLogger
    public void longPress(ControlViewHolder controlViewHolder, boolean z) {
        ControlsMetricsLogger.DefaultImpls.longPress(this, controlViewHolder, z);
    }

    @Override // com.android.systemui.controls.ControlsMetricsLogger
    public void refreshBegin(int i, boolean z) {
        ControlsMetricsLogger.DefaultImpls.refreshBegin(this, i, z);
    }

    @Override // com.android.systemui.controls.ControlsMetricsLogger
    public void refreshEnd(ControlViewHolder controlViewHolder, boolean z) {
        ControlsMetricsLogger.DefaultImpls.refreshEnd(this, controlViewHolder, z);
    }

    @Override // com.android.systemui.controls.ControlsMetricsLogger
    public void touch(ControlViewHolder controlViewHolder, boolean z) {
        ControlsMetricsLogger.DefaultImpls.touch(this, controlViewHolder, z);
    }

    /* compiled from: ControlsMetricsLoggerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.controls.ControlsMetricsLogger
    public void assignInstanceId() {
        this.instanceId = this.instanceIdSequence.newInstanceId().getId();
    }

    @Override // com.android.systemui.controls.ControlsMetricsLogger
    public void log(int i, int i2, int i3, boolean z) {
        SysUiStatsLog.write(349, i, this.instanceId, i2, i3, z);
    }
}
