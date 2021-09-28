package com.android.keyguard;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardListenQueue.kt */
/* loaded from: classes.dex */
public final class KeyguardListenQueue {
    private final ArrayDeque<KeyguardFaceListenModel> faceQueue;
    private final ArrayDeque<KeyguardFingerprintListenModel> fingerprintQueue;
    private final int sizePerModality;

    public KeyguardListenQueue() {
        this(0, 1, null);
    }

    public final void print(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "writer");
        print$default(this, printWriter, null, 2, null);
    }

    public KeyguardListenQueue(int i) {
        this.sizePerModality = i;
        this.faceQueue = new ArrayDeque<>();
        this.fingerprintQueue = new ArrayDeque<>();
    }

    public /* synthetic */ KeyguardListenQueue(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 20 : i);
    }

    public final List<KeyguardListenModel> getModels() {
        return CollectionsKt___CollectionsKt.plus((Collection) this.faceQueue, (Iterable) this.fingerprintQueue);
    }

    public final void add(KeyguardListenModel keyguardListenModel) {
        ArrayDeque arrayDeque;
        Intrinsics.checkNotNullParameter(keyguardListenModel, "model");
        if (keyguardListenModel instanceof KeyguardFaceListenModel) {
            arrayDeque = this.faceQueue;
            arrayDeque.add(keyguardListenModel);
        } else if (keyguardListenModel instanceof KeyguardFingerprintListenModel) {
            arrayDeque = this.fingerprintQueue;
            arrayDeque.add(keyguardListenModel);
        } else {
            throw new NoWhenBranchMatchedException();
        }
        if (arrayDeque.size() > this.sizePerModality) {
            arrayDeque.removeFirstOrNull();
        }
    }

    public static /* synthetic */ void print$default(KeyguardListenQueue keyguardListenQueue, PrintWriter printWriter, DateFormat dateFormat, int i, Object obj) {
        if ((i & 2) != 0) {
            dateFormat = KeyguardListenQueueKt.DEFAULT_FORMATTING;
        }
        keyguardListenQueue.print(printWriter, dateFormat);
    }

    public final void print(PrintWriter printWriter, DateFormat dateFormat) {
        Intrinsics.checkNotNullParameter(printWriter, "writer");
        Intrinsics.checkNotNullParameter(dateFormat, "dateFormat");
        KeyguardListenQueue$print$stringify$1 keyguardListenQueue$print$stringify$1 = new Function1<KeyguardListenModel, String>(dateFormat) { // from class: com.android.keyguard.KeyguardListenQueue$print$stringify$1
            final /* synthetic */ DateFormat $dateFormat;

            /* access modifiers changed from: package-private */
            {
                this.$dateFormat = r1;
            }

            public final String invoke(KeyguardListenModel keyguardListenModel) {
                Intrinsics.checkNotNullParameter(keyguardListenModel, "model");
                return "    " + ((Object) this.$dateFormat.format(new Date(keyguardListenModel.getTimeMillis()))) + ' ' + keyguardListenModel;
            }
        };
        printWriter.println("  Face listen results (last " + this.faceQueue.size() + " calls):");
        Iterator<KeyguardFaceListenModel> it = this.faceQueue.iterator();
        while (it.hasNext()) {
            printWriter.println(keyguardListenQueue$print$stringify$1.invoke((KeyguardListenQueue$print$stringify$1) it.next()));
        }
        printWriter.println("  Fingerprint listen results (last " + this.fingerprintQueue.size() + " calls):");
        Iterator<KeyguardFingerprintListenModel> it2 = this.fingerprintQueue.iterator();
        while (it2.hasNext()) {
            printWriter.println(keyguardListenQueue$print$stringify$1.invoke((KeyguardListenQueue$print$stringify$1) it2.next()));
        }
    }
}
