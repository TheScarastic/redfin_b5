package com.android.systemui.media;

import com.android.systemui.util.animation.MeasurementOutput;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaHostStatesManager.kt */
/* loaded from: classes.dex */
public final class MediaHostStatesManager {
    private final Set<Callback> callbacks = new LinkedHashSet();
    private final Set<MediaViewController> controllers = new LinkedHashSet();
    private final Map<Integer, MeasurementOutput> carouselSizes = new LinkedHashMap();
    private final Map<Integer, MediaHostState> mediaHostStates = new LinkedHashMap();

    /* compiled from: MediaHostStatesManager.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        void onHostStateChanged(int i, MediaHostState mediaHostState);
    }

    public final Map<Integer, MeasurementOutput> getCarouselSizes() {
        return this.carouselSizes;
    }

    public final Map<Integer, MediaHostState> getMediaHostStates() {
        return this.mediaHostStates;
    }

    public final void updateHostState(int i, MediaHostState mediaHostState) {
        Intrinsics.checkNotNullParameter(mediaHostState, "hostState");
        if (!mediaHostState.equals(this.mediaHostStates.get(Integer.valueOf(i)))) {
            MediaHostState copy = mediaHostState.copy();
            this.mediaHostStates.put(Integer.valueOf(i), copy);
            updateCarouselDimensions(i, mediaHostState);
            for (MediaViewController mediaViewController : this.controllers) {
                mediaViewController.getStateCallback().onHostStateChanged(i, copy);
            }
            for (Callback callback : this.callbacks) {
                callback.onHostStateChanged(i, copy);
            }
        }
    }

    public final MeasurementOutput updateCarouselDimensions(int i, MediaHostState mediaHostState) {
        Intrinsics.checkNotNullParameter(mediaHostState, "hostState");
        MeasurementOutput measurementOutput = new MeasurementOutput(0, 0);
        for (MediaViewController mediaViewController : this.controllers) {
            MeasurementOutput measurementsForState = mediaViewController.getMeasurementsForState(mediaHostState);
            if (measurementsForState != null) {
                if (measurementsForState.getMeasuredHeight() > measurementOutput.getMeasuredHeight()) {
                    measurementOutput.setMeasuredHeight(measurementsForState.getMeasuredHeight());
                }
                if (measurementsForState.getMeasuredWidth() > measurementOutput.getMeasuredWidth()) {
                    measurementOutput.setMeasuredWidth(measurementsForState.getMeasuredWidth());
                }
            }
        }
        this.carouselSizes.put(Integer.valueOf(i), measurementOutput);
        return measurementOutput;
    }

    public final void addCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.callbacks.add(callback);
    }

    public final void addController(MediaViewController mediaViewController) {
        Intrinsics.checkNotNullParameter(mediaViewController, "controller");
        this.controllers.add(mediaViewController);
    }

    public final void removeController(MediaViewController mediaViewController) {
        Intrinsics.checkNotNullParameter(mediaViewController, "controller");
        this.controllers.remove(mediaViewController);
    }
}
