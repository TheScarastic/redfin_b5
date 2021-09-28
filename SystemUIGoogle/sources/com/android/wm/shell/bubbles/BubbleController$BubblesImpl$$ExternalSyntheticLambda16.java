package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda16 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ FileDescriptor f$1;
    public final /* synthetic */ PrintWriter f$2;
    public final /* synthetic */ String[] f$3;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda16(BubbleController.BubblesImpl bubblesImpl, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.f$0 = bubblesImpl;
        this.f$1 = fileDescriptor;
        this.f$2 = printWriter;
        this.f$3 = strArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$dump$25(this.f$1, this.f$2, this.f$3);
    }
}
