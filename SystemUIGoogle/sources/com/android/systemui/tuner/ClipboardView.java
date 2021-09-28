package com.android.systemui.tuner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.R$drawable;
/* loaded from: classes2.dex */
public class ClipboardView extends ImageView implements ClipboardManager.OnPrimaryClipChangedListener {
    private final ClipboardManager mClipboardManager;
    private ClipData mCurrentClip;

    public ClipboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mClipboardManager = (ClipboardManager) context.getSystemService(ClipboardManager.class);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startListening();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopListening();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 && this.mCurrentClip != null) {
            startPocketDrag();
        }
        return super.onTouchEvent(motionEvent);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000f, code lost:
        if (r0 != 6) goto L_0x0023;
     */
    @Override // android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onDragEvent(android.view.DragEvent r4) {
        /*
            r3 = this;
            int r0 = r4.getAction()
            r1 = 3
            r2 = 1
            if (r0 == r1) goto L_0x0016
            r4 = 4
            if (r0 == r4) goto L_0x001f
            r4 = 5
            if (r0 == r4) goto L_0x0012
            r4 = 6
            if (r0 == r4) goto L_0x001f
            goto L_0x0023
        L_0x0012:
            r3.setBackgroundDragTarget(r2)
            goto L_0x0023
        L_0x0016:
            android.content.ClipboardManager r0 = r3.mClipboardManager
            android.content.ClipData r4 = r4.getClipData()
            r0.setPrimaryClip(r4)
        L_0x001f:
            r4 = 0
            r3.setBackgroundDragTarget(r4)
        L_0x0023:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.tuner.ClipboardView.onDragEvent(android.view.DragEvent):boolean");
    }

    private void setBackgroundDragTarget(boolean z) {
        setBackgroundColor(z ? 1308622847 : 0);
    }

    public void startPocketDrag() {
        startDragAndDrop(this.mCurrentClip, new View.DragShadowBuilder(this), null, 256);
    }

    public void startListening() {
        this.mClipboardManager.addPrimaryClipChangedListener(this);
        onPrimaryClipChanged();
    }

    public void stopListening() {
        this.mClipboardManager.removePrimaryClipChangedListener(this);
    }

    @Override // android.content.ClipboardManager.OnPrimaryClipChangedListener
    public void onPrimaryClipChanged() {
        ClipData primaryClip = this.mClipboardManager.getPrimaryClip();
        this.mCurrentClip = primaryClip;
        setImageResource(primaryClip != null ? R$drawable.clipboard_full : R$drawable.clipboard_empty);
    }
}
