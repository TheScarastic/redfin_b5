package com.android.systemui.statusbar.phone;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.widget.ImageView;
import androidx.appcompat.R$styleable;
import com.android.systemui.R$color;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class DarkIconDispatcherImpl implements SysuiDarkIconDispatcher, LightBarTransitionsController.DarkIntensityApplier {
    private float mDarkIntensity;
    private int mDarkModeIconColorSingleTone;
    private int mLightModeIconColorSingleTone;
    private final LightBarTransitionsController mTransitionsController;
    private final Rect mTintArea = new Rect();
    private final ArrayMap<Object, DarkIconDispatcher.DarkReceiver> mReceivers = new ArrayMap<>();
    private int mIconTint = -1;

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public int getTintAnimationDuration() {
        return R$styleable.AppCompatTheme_windowFixedHeightMajor;
    }

    public DarkIconDispatcherImpl(Context context, CommandQueue commandQueue) {
        this.mDarkModeIconColorSingleTone = context.getColor(R$color.dark_mode_icon_color_single_tone);
        this.mLightModeIconColorSingleTone = context.getColor(R$color.light_mode_icon_color_single_tone);
        this.mTransitionsController = new LightBarTransitionsController(context, this, commandQueue);
    }

    @Override // com.android.systemui.statusbar.phone.SysuiDarkIconDispatcher
    public LightBarTransitionsController getTransitionsController() {
        return this.mTransitionsController;
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public void addDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.put(darkReceiver, darkReceiver);
        darkReceiver.onDarkChanged(this.mTintArea, this.mDarkIntensity, this.mIconTint);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addDarkReceiver$0(ImageView imageView, Rect rect, float f, int i) {
        imageView.setImageTintList(ColorStateList.valueOf(DarkIconDispatcher.getTint(this.mTintArea, imageView, this.mIconTint)));
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public void addDarkReceiver(ImageView imageView) {
        DarkIconDispatcherImpl$$ExternalSyntheticLambda0 darkIconDispatcherImpl$$ExternalSyntheticLambda0 = new DarkIconDispatcher.DarkReceiver(imageView) { // from class: com.android.systemui.statusbar.phone.DarkIconDispatcherImpl$$ExternalSyntheticLambda0
            public final /* synthetic */ ImageView f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
            public final void onDarkChanged(Rect rect, float f, int i) {
                DarkIconDispatcherImpl.$r8$lambda$JKoHJfV64uCgFa0FIb9JOXZBI20(DarkIconDispatcherImpl.this, this.f$1, rect, f, i);
            }
        };
        this.mReceivers.put(imageView, darkIconDispatcherImpl$$ExternalSyntheticLambda0);
        darkIconDispatcherImpl$$ExternalSyntheticLambda0.onDarkChanged(this.mTintArea, this.mDarkIntensity, this.mIconTint);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public void removeDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.remove(darkReceiver);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public void removeDarkReceiver(ImageView imageView) {
        this.mReceivers.remove(imageView);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public void applyDark(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.get(darkReceiver).onDarkChanged(this.mTintArea, this.mDarkIntensity, this.mIconTint);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public void setIconsDarkArea(Rect rect) {
        if (rect != null || !this.mTintArea.isEmpty()) {
            if (rect == null) {
                this.mTintArea.setEmpty();
            } else {
                this.mTintArea.set(rect);
            }
            applyIconTint();
        }
    }

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public void applyDarkIntensity(float f) {
        this.mDarkIntensity = f;
        this.mIconTint = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightModeIconColorSingleTone), Integer.valueOf(this.mDarkModeIconColorSingleTone))).intValue();
        applyIconTint();
    }

    private void applyIconTint() {
        for (int i = 0; i < this.mReceivers.size(); i++) {
            this.mReceivers.valueAt(i).onDarkChanged(this.mTintArea, this.mDarkIntensity, this.mIconTint);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("DarkIconDispatcher: ");
        printWriter.println("  mIconTint: 0x" + Integer.toHexString(this.mIconTint));
        printWriter.println("  mDarkIntensity: " + this.mDarkIntensity + "f");
        StringBuilder sb = new StringBuilder();
        sb.append("  mTintArea: ");
        sb.append(this.mTintArea);
        printWriter.println(sb.toString());
    }
}
