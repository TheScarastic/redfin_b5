package com.android.wm.shell.pip.phone;
/* loaded from: classes2.dex */
public abstract class PipTouchGesture {
    public abstract void onDown(PipTouchState pipTouchState);

    public abstract boolean onMove(PipTouchState pipTouchState);

    public abstract boolean onUp(PipTouchState pipTouchState);
}
