package com.google.android.systemui.assist.uihints.edgelights;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.metrics.LogMaker;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$color;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PathSpecCornerPathRenderer;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.google.android.systemui.assist.uihints.DisplayUtils;
import com.google.android.systemui.assist.uihints.edgelights.mode.Gone;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class EdgeLightsView extends View {
    private List<EdgeLight> mAssistInvocationLights;
    private EdgeLight[] mAssistLights;
    private Set<EdgeLightsListener> mListeners;
    private Mode mMode;
    private final Paint mPaint;
    private final Path mPath;
    private final PerimeterPathGuide mPerimeterPathGuide;
    private int[] mScreenLocation;

    /* loaded from: classes2.dex */
    public interface Mode {
        int getSubType();

        default void onAudioLevelUpdate(float f, float f2) {
        }

        default void onConfigurationChanged() {
        }

        void onNewModeRequest(EdgeLightsView edgeLightsView, Mode mode);

        default boolean preventsInvocations() {
            return false;
        }

        void start(EdgeLightsView edgeLightsView, PerimeterPathGuide perimeterPathGuide, Mode mode);

        default void logState() {
            MetricsLogger.action(new LogMaker(1716).setType(6).setSubtype(getSubType()));
        }
    }

    public EdgeLightsView(Context context) {
        this(context, null);
    }

    public EdgeLightsView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public EdgeLightsView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public EdgeLightsView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Paint paint = new Paint();
        this.mPaint = paint;
        this.mAssistLights = new EdgeLight[0];
        this.mAssistInvocationLights = new ArrayList();
        this.mPath = new Path();
        this.mListeners = new HashSet();
        this.mScreenLocation = new int[2];
        int convertDpToPx = DisplayUtils.convertDpToPx(3.0f, context);
        paint.setStrokeWidth((float) convertDpToPx);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setAntiAlias(true);
        this.mPerimeterPathGuide = new PerimeterPathGuide(context, new PathSpecCornerPathRenderer(context), convertDpToPx / 2, DisplayUtils.getWidth(context), DisplayUtils.getHeight(context));
        Gone gone = new Gone();
        this.mMode = gone;
        commitModeTransition(gone);
        Resources resources = getResources();
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(R$color.edge_light_blue), 0.0f, 0.0f));
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(R$color.edge_light_red), 0.0f, 0.0f));
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(R$color.edge_light_yellow), 0.0f, 0.0f));
        this.mAssistInvocationLights.add(new EdgeLight(resources.getColor(R$color.edge_light_green), 0.0f, 0.0f));
    }

    /* access modifiers changed from: protected */
    public void addListener(EdgeLightsListener edgeLightsListener) {
        this.mListeners.add(edgeLightsListener);
    }

    public EdgeLight[] getAssistLights() {
        if (Looper.getMainLooper().isCurrentThread()) {
            return this.mAssistLights;
        }
        throw new IllegalStateException("Must be called from main thread");
    }

    public List<EdgeLight> getAssistInvocationLights() {
        return this.mAssistInvocationLights;
    }

    public void setAssistLights(EdgeLight[] edgeLightArr) {
        post(new Runnable(edgeLightArr) { // from class: com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView$$ExternalSyntheticLambda0
            public final /* synthetic */ EdgeLight[] f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                EdgeLightsView.this.lambda$setAssistLights$1(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAssistLights$1(EdgeLight[] edgeLightArr) {
        this.mAssistLights = EdgeLight.copy(edgeLightArr);
        this.mListeners.forEach(new Consumer() { // from class: com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                EdgeLightsView.this.lambda$setAssistLights$0((EdgeLightsListener) obj);
            }
        });
        invalidate();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAssistLights$0(EdgeLightsListener edgeLightsListener) {
        edgeLightsListener.onAssistLightsUpdated(this.mMode, this.mAssistLights);
    }

    public Mode getMode() {
        return this.mMode;
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        int visibility = getVisibility();
        super.setVisibility(i);
        if (visibility == 8) {
            updateRotation();
        }
    }

    public void commitModeTransition(Mode mode) {
        mode.start(this, this.mPerimeterPathGuide, this.mMode);
        this.mMode = mode;
        this.mListeners.forEach(new Consumer() { // from class: com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                EdgeLightsView.this.lambda$commitModeTransition$2((EdgeLightsListener) obj);
            }
        });
        this.mAssistInvocationLights.forEach(EdgeLightsView$$ExternalSyntheticLambda3.INSTANCE);
        invalidate();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$commitModeTransition$2(EdgeLightsListener edgeLightsListener) {
        edgeLightsListener.onModeStarted(this.mMode);
    }

    public void onAudioLevelUpdate(float f, float f2) {
        this.mMode.onAudioLevelUpdate(f, f2);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateRotation();
        this.mMode.onConfigurationChanged();
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateRotation();
    }

    private void updateRotation() {
        this.mPerimeterPathGuide.setRotation(getContext().getDisplay().getRotation());
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        getLocationOnScreen(this.mScreenLocation);
        int[] iArr = this.mScreenLocation;
        canvas.translate((float) (-iArr[0]), (float) (-iArr[1]));
        renderLights(canvas, this.mAssistLights);
        renderLights(canvas, this.mAssistInvocationLights);
    }

    private void renderLights(Canvas canvas, List<EdgeLight> list) {
        if (!list.isEmpty()) {
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            renderLight(canvas, list.get(0));
            if (list.size() > 1) {
                renderLight(canvas, list.get(list.size() - 1));
            }
            if (list.size() > 2) {
                this.mPaint.setStrokeCap(Paint.Cap.BUTT);
                for (EdgeLight edgeLight : list.subList(1, list.size() - 1)) {
                    renderLight(canvas, edgeLight);
                }
            }
        }
    }

    private void renderLights(Canvas canvas, EdgeLight[] edgeLightArr) {
        if (edgeLightArr.length != 0) {
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            renderLight(canvas, edgeLightArr[0]);
            if (edgeLightArr.length > 1) {
                renderLight(canvas, edgeLightArr[edgeLightArr.length - 1]);
            }
            if (edgeLightArr.length > 2) {
                this.mPaint.setStrokeCap(Paint.Cap.BUTT);
                for (int i = 1; i < edgeLightArr.length - 1; i++) {
                    renderLight(canvas, edgeLightArr[i]);
                }
            }
        }
    }

    private void renderLight(Canvas canvas, EdgeLight edgeLight) {
        this.mPerimeterPathGuide.strokeSegment(this.mPath, edgeLight.getStart(), edgeLight.getStart() + edgeLight.getLength());
        this.mPaint.setColor(edgeLight.getColor());
        canvas.drawPath(this.mPath, this.mPaint);
    }
}
